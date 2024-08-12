package com.ows.gemini.anything.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.ows.gemini.anything.data.model.RankModel
import com.ows.gemini.anything.data.model.RecentlyFoodModel
import com.ows.gemini.anything.data.repository.LocalRepository
import com.ows.gemini.anything.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val localRepository: LocalRepository,
    ) : BaseViewModel() {
        private val _recentlyFoodModels = MutableLiveData<List<RecentlyFoodModel>>(null)
        val recentlyFoodModels: LiveData<List<RecentlyFoodModel>> = _recentlyFoodModels

        private val _rankFoodModels = MutableLiveData<List<RecentlyFoodModel>>(null)
        val rankFoodModels: LiveData<List<RecentlyFoodModel>> = _rankFoodModels

        fun getMyRecentlyFoodModels() {
            val storageRef = FirebaseStorage.getInstance().reference
            compositeDisposable.add(
                localRepository
                    .getRecentlyFoods()
                    .subscribeOn(Schedulers.io())
                    .flatMap { foodModels ->
                        if (foodModels.isEmpty()) {
                            Flowable.just(emptyList<RecentlyFoodModel>())
                        } else {
                            Flowable
                                .fromIterable(foodModels)
                                .flatMapSingle { foodModel ->
                                    Single.create<RecentlyFoodModel> { emitter ->
                                        storageRef
                                            .child("images/${foodModel.name}")
                                            .downloadUrl
                                            .addOnSuccessListener { uri ->
                                                emitter.onSuccess(
                                                    foodModel.copy(
                                                        imageUrl = uri.toString(),
                                                        meta = foodModel.meta,
                                                    ),
                                                )
                                            }.addOnFailureListener {
                                                emitter.onSuccess(foodModel)
                                            }
                                    }
                                }.toList()
                                .toFlowable()
                        }
                    }.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { models ->
                            _recentlyFoodModels.value =
                                models
                                    .filter { it.imageUrl.isNotEmpty() }
                                    .sortedByDescending { it.time }
                        },
                        {
                            Timber.e("getMyRecentlyFoodImages ${it.localizedMessage}")
                        },
                    ),
            )
        }

        fun getOtherFoodModels() {
            val storageRef = FirebaseStorage.getInstance().reference
            val databaseRef = FirebaseDatabase.getInstance().reference
            databaseRef
                .child("ranks")
                .orderByChild("number")
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val models =
                                snapshot.children
                                    .map {
                                        it.getValue(RankModel::class.java)
                                    }.mapNotNull {
                                        RecentlyFoodModel(
                                            name = it?.name ?: "",
                                            meta = it?.number.toString(),
                                        )
                                    }.reversed()
                                    .take(10)
                            compositeDisposable.add(
                                Flowable
                                    .fromIterable(models)
                                    .flatMapSingle { foodModel ->
                                        Single.create<RecentlyFoodModel> { emitter ->
                                            storageRef
                                                .child("images/${foodModel.name}")
                                                .downloadUrl
                                                .addOnSuccessListener { uri ->
                                                    emitter.onSuccess(
                                                        foodModel.copy(
                                                            imageUrl = uri.toString(),
                                                            meta = foodModel.meta,
                                                        ),
                                                    )
                                                }.addOnFailureListener {
                                                    emitter.onSuccess(foodModel)
                                                }
                                        }
                                    }.toList()
                                    .toFlowable()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        { models ->
                                            _recentlyFoodModels.value =
                                                models
                                                    .filter { it.imageUrl.isNotEmpty() }
                                                    .sortedByDescending { it.meta.toInt() }
                                                    .map { model ->
                                                        model.copy(meta = "${model.meta} recommends")
                                                    }
                                        },
                                        {
                                            Timber.e("getOtherFoodModels ${it.localizedMessage}")
                                        },
                                    ),
                            )
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Timber.e("getOtherFoodModels $error")
                        }
                    },
                )
        }
    }
