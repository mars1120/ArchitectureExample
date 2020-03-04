package com.marschen.architectureexample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marschen.architectureexample.databinding.ActivityDramaInfoBinding
import com.marschen.architectureexample.db.DramaRoomDatabase
import com.marschen.architectureexample.viewmodel.DramaViewModel


class DramaInfoActivity : AppCompatActivity() {
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

    companion object {
        private const val KEY_DRAMA_ID = "key_drama_id"
        fun intentFor(context: Context, dramaID: Int): Intent {

            val intent = Intent(context, DramaInfoActivity::class.java)
            intent.putExtra(KEY_DRAMA_ID, dramaID)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var dramaID: Int = intent.getIntExtra(KEY_DRAMA_ID, 0)
        val binding = DataBindingUtil.setContentView<ActivityDramaInfoBinding>(
            this,
            R.layout.activity_drama_info
        )
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this
        mViewModel.searchByID(dramaID)
        setContentView(binding.root)
    }


}
