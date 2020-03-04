package com.marschen.architectureexample

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marschen.architectureexample.db.DramaRoomDatabase
import com.marschen.architectureexample.db.PreferencesManager
import com.marschen.architectureexample.extension.afterTextChanged
import com.marschen.architectureexample.viewmodel.DramaViewModel
import kotlinx.android.synthetic.main.activity_main.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import tw.sonet.picktime.ui.eventlist.adapter.DramasAdapter


class MainActivity : AppCompatActivity() {
    private val mViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @NonNull
                override fun <T : ViewModel> create(@NonNull modelClass: Class<T>): T {
                    return DramaViewModel(
                        DataRepository.getInstance(DramaRoomDatabase.getDatabase(application))
                    ) as T
                }
            }
        ).get(DramaViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_recycler.apply {
            adapter = DramasAdapter()
        }

        lateinit var failCallback: () -> Unit
        lateinit var retryDialog: AlertDialog.Builder
        failCallback = {
            main_refresh.isRefreshing = false
            retryDialog.show()

        }
        retryDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.network_error))
            .setPositiveButton(getString(R.string.retry)) { dialog, which ->
                mViewModel.callDramaApi(failCallback)
            }.setNeutralButton(android.R.string.cancel, null)


        main_refresh.setOnRefreshListener {
            mViewModel.callDramaApi(failCallback)
        }
        setEventListener(this, this, object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if (!isOpen) {
                    main_input.clearFocus()
                }
            }
        })
        main_input.afterTextChanged { keyword ->
            mViewModel.searchDB(keyword)
        }
        main_input_clear.setOnClickListener {
            main_input.setText("")
        }

        mViewModel.getSearchQuery().observe(this, Observer { keyword ->
            PreferencesManager.setLastSearchKeyword(this, keyword)
            main_input_clear.visibility = if (keyword.equals("")) View.GONE else View.VISIBLE
        })

        mViewModel.getDramasForAdapter().observe(this, Observer {
            (main_recycler.adapter as DramasAdapter).update(it)
            if (main_refresh.isRefreshing) main_refresh.isRefreshing = false
        })

        mViewModel.callDramaApi(failCallback)
        main_input.setText(PreferencesManager.getLastSearchKeyword(this))
    }

}
