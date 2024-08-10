package com.ows.gemini.anything.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ows.gemini.anything.data.repository.LocalRepository
import com.ows.gemini.anything.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val localRepository: LocalRepository,
    ) : BaseViewModel() {
        private val _isFirst = MutableLiveData<Boolean>()
        val isFirst: LiveData<Boolean> = _isFirst

        fun checkFirstLaunch() {
            compositeDisposable.add(
                localRepository
                    .isFirstLaunch()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Timber.d("subscribe $it")
                        _isFirst.value = it
                    },
            )
        }
    }
