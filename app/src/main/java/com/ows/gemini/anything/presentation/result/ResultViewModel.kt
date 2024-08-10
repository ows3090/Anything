package com.ows.gemini.anything.presentation.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ows.gemini.anything.data.repository.ImageRepository
import com.ows.gemini.anything.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ResultViewModel
    @Inject
    constructor(
        private val imageRepository: ImageRepository,
    ) : BaseViewModel() {
        private val _foodImage = MutableLiveData<String>()
        val foodImage: LiveData<String> = _foodImage

        fun generateImage(name: String) {
            compositeDisposable.add(
                imageRepository
                    .getImage(name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { model ->
                            _foodImage.value = model.url
                        },
                        { error ->
                            Timber.e(error)
                        },
                    ),
            )
        }
    }
