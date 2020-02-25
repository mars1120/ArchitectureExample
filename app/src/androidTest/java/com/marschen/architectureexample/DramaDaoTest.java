package com.marschen.architectureexample;

/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.gson.Gson;
import com.marschen.architectureexample.api.DramaApi;
import com.marschen.architectureexample.db.Drama;
import com.marschen.architectureexample.db.DramaDao;
import com.marschen.architectureexample.db.DramaRoomDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4ClassRunner.class)
public class DramaDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private DramaDao mDramaDao;
    private DramaRoomDatabase mDb;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mDb = Room.inMemoryDatabaseBuilder(context, DramaRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        mDramaDao = mDb.dramaDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void insertAndGetInfo() throws Exception {
        DramaApi.ListingResponse drama = new Gson().fromJson(readFromFile("/drama.json"), DramaApi.ListingResponse.class);
        mDramaDao.insert(drama.getData());
        List<Drama> allDramas = LiveDataTestUtil.getValue(mDramaDao.searchByName(drama.getData().get(0).getName().substring(0, 1)));
        assertEquals(drama.getData().get(0).getName(), allDramas.get(0).getName());
    }

    @Test
    public void insertAndDelete() throws Exception {
        DramaApi.ListingResponse drama = new Gson().fromJson(readFromFile("/drama.json"), DramaApi.ListingResponse.class);
        mDramaDao.insert(drama.getData());
        assertEquals(drama.getData().size(), 6);
        mDramaDao.deleteAll();
        List<Drama> allDramas = LiveDataTestUtil.getValue(mDramaDao.getAll());
        assertEquals(allDramas.size(), 0);
    }

    public String readFromFile(String filename) throws IOException {
        InputStream is = getClass().getResourceAsStream(filename);
        StringBuilder stringBuilder = new StringBuilder();
        int i;
        byte[] b = new byte[4096];
        while ((i = is.read(b)) != -1) {
            stringBuilder.append(new String(b, 0, i));
        }
        return stringBuilder.toString();
    }

}
