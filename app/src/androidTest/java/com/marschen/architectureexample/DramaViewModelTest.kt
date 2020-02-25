package com.marschen.architectureexample

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.gson.Gson
import com.marschen.architectureexample.api.DramaApi.ListingResponse
import com.marschen.architectureexample.db.DramaDao
import com.marschen.architectureexample.db.DramaRoomDatabase
import com.marschen.architectureexample.viewmodel.DramaViewModel
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class DramaViewModelTest {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var mDramaDao: DramaDao
    private lateinit var mDb: DramaRoomDatabase
    private lateinit var mViewModel: DramaViewModel
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mDb = Room.inMemoryDatabaseBuilder(context, DramaRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        mDramaDao = mDb.dramaDao()
        val localJson = Gson().fromJson(
            readFromFile("/drama.json"),
            ListingResponse::class.java
        )

        mDramaDao.insert(localJson.data)
        mViewModel = DramaViewModel(DataRepository(mDb))
        //init searchQuery
        mViewModel.searchDB("")
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        mDb.close()
    }

    @Test
    @Throws(Exception::class)
    fun getDramasForAdapterTest() {
        val allDramas =
            LiveDataTestUtil.getValue(
                mViewModel.getDramasForAdapter()
            )
        Assert.assertEquals(allDramas.size, 6)
    }

    @Test
    @Throws(Exception::class)
    fun searchTest() {
        mViewModel.searchDB("好")
        val resultDramas =
            LiveDataTestUtil.getValue(
                mViewModel.getDramasForAdapter()
            )
        Assert.assertEquals(resultDramas.size, 2)
    }

    @Test
    @Throws(Exception::class)
    fun checkSearhInputOutput() {
        val inputString = "123"
        mViewModel.searchDB(inputString)
        val outputString =
            LiveDataTestUtil.getValue(
                mViewModel.getSearchQuery()
            )
        Assert.assertEquals(inputString, outputString)
    }

    @Throws(IOException::class)
    fun readFromFile(filename: String?): String {
        val `is` = javaClass.getResourceAsStream(filename!!)
        val stringBuilder = StringBuilder()
        var i: Int
        val b = ByteArray(4096)
        while (`is`!!.read(b).also { i = it } != -1) {
            stringBuilder.append(String(b, 0, i))
        }
        return stringBuilder.toString()
    }
}