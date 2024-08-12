package com.ows.gemini.anything.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.ows.gemini.anything.data.model.RecentlyFoodModel
import com.ows.gemini.anything.data.repository.LocalRepository
import com.ows.gemini.anything.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val localRepository: LocalRepository,
    ) : BaseViewModel() {
        private val _recentlyFoodModels = MutableLiveData<List<RecentlyFoodModel>>(null)
        val recentlyFoodModels: LiveData<List<RecentlyFoodModel>> = _recentlyFoodModels

        fun getMyRecentlyFoodImages() {
            val storageRef = FirebaseStorage.getInstance().reference
            compositeDisposable.add(
                Flowable
                    .just(
                        listOf(
                            RecentlyFoodModel(
                                name = "CaliforniaBurritoBowl",
                            ),
                            RecentlyFoodModel(
                                name = "KoreanBBQ",
                            ),
                        ),
                    ).subscribeOn(Schedulers.io())
                    .flatMapIterable { it }
                    .flatMapSingle { foodModel ->
                        Single.create<RecentlyFoodModel> { emitter ->
                            storageRef
                                .child("images/${foodModel.name}")
                                .downloadUrl
                                .addOnSuccessListener { uri ->
                                    emitter.onSuccess(
                                        foodModel.copy(
                                            imageUrl = uri.toString(),
                                            meta = "Dinner",
                                        ),
                                    )
                                }
                        }
                    }.toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { models ->
                        _recentlyFoodModels.value = models
                    },
            )
        }
    }
