package com.ows.gemini.anything.presentation.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.ows.gemini.anything.data.model.RankModel
import com.ows.gemini.anything.data.repository.ImageRepository
import com.ows.gemini.anything.data.repository.LocalRepository
import com.ows.gemini.anything.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ResultViewModel
    @Inject
    constructor(
        private val imageRepository: ImageRepository,
        private val localRepository: LocalRepository,
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

        fun saveRecommendation(
            name: String,
            meta: String,
        ) {
            compositeDisposable.add(
                Single
                    .create<Long> { emitter ->
                        emitter.onSuccess(
                            localRepository.insertFood(
                                name,
                                System.currentTimeMillis(),
                                meta,
                            ),
                        )
                    }.subscribeOn(Schedulers.io())
                    .subscribe({
                        Timber.d("success")
                    }, {
                        Timber.d("saveRecommendation ${it.localizedMessage}")
                    }),
            )
        }

        fun updateRecommendation(name: String) {
            val databaseRef = FirebaseDatabase.getInstance().reference
            val dataRef = databaseRef.child("ranks").child(name)
            dataRef
                .get()
                .addOnSuccessListener {
                    val rankModel = it.getValue(RankModel::class.java)
                    if (rankModel != null) {
                        dataRef.setValue(rankModel.copy(number = rankModel.number + 1))
                    } else {
                        dataRef.setValue(RankModel(name = name))
                    }
                }.addOnFailureListener {
                    Timber.d("updateRank")
                }
        }
    }
