package com.ows.gemini.anything.presentation.recommendations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.ows.gemini.anything.data.model.RecentlyFoodModel
import com.ows.gemini.anything.data.repository.LocalRepository
import com.ows.gemini.anything.data.type.FoodType
import com.ows.gemini.anything.data.type.MealTime
import com.ows.gemini.anything.data.type.PromptStep
import com.ows.gemini.anything.data.type.toBackStep
import com.ows.gemini.anything.data.type.toNextStep
import com.ows.gemini.anything.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel
    @Inject
    constructor(
        private val localRepository: LocalRepository,
    ) : BaseViewModel() {
        private val _promptStep = MutableLiveData(PromptStep.Step1)
        val promptStep: LiveData<PromptStep> = _promptStep

        private val _mealTime = MutableLiveData<MealTime>(null)
        val mealTime: LiveData<MealTime> = _mealTime

        private val _likeFoodTypes = MutableLiveData<List<FoodType>>(listOf())
        val likeFoodTypes: LiveData<List<FoodType>> = _likeFoodTypes

        private val _dislikeFoodTypes = MutableLiveData<List<FoodType>>(listOf())
        val dislikeFoodTypes: LiveData<List<FoodType>> = _dislikeFoodTypes

        private val _recentlyFoods =
            MutableLiveData<List<RecentlyFood>>(
                listOf(),
            )
        val recentlyFoods: LiveData<List<RecentlyFood>> = _recentlyFoods
        val additionalText = MutableLiveData<String>("")

        private val _prompt = MutableLiveData<String>()
        val prompt: LiveData<String> = _prompt

        init {
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
                        Single.create<RecentlyFood> { emitter ->
                            storageRef
                                .child("images/${foodModel.name}")
                                .downloadUrl
                                .addOnSuccessListener { uri ->
                                    emitter.onSuccess(
                                        RecentlyFood(name = foodModel.name),
                                    )
                                }
                        }
                    }.toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { foods ->
                        _recentlyFoods.value = foods
                    },
            )
        }

        fun toNextStep() {
            if (_promptStep.value == PromptStep.Step5) {
                makePrompt()
                return
            }

            if (_promptStep.value == PromptStep.Step1) {
                if (_mealTime.value != null) {
                    _promptStep.value = _promptStep.value?.toNextStep()
                }
            } else {
                _promptStep.value = _promptStep.value?.toNextStep()
            }
        }

        fun toBackStep() {
            when (_promptStep.value) {
                PromptStep.Step2 -> {
                    _likeFoodTypes.value = listOf()
                }

                PromptStep.Step3 -> {
                    _dislikeFoodTypes.value = listOf()
                }

                PromptStep.Step4 -> {
                    _recentlyFoods.value =
                        _recentlyFoods.value?.map { it.copy(isExcept = false) } ?: listOf()
                }

                PromptStep.Step5 -> {
                    additionalText.value = ""
                }

                else -> Unit
            }
            _promptStep.value = _promptStep.value?.toBackStep()
        }

        fun toSkip() {
            when (_promptStep.value) {
                PromptStep.Step2 -> {
                    _likeFoodTypes.value = listOf()
                }

                PromptStep.Step3 -> {
                    _dislikeFoodTypes.value = listOf()
                }

                PromptStep.Step4 -> {
                }

                PromptStep.Step5 -> {
                }

                else -> Unit
            }
            toNextStep()
        }

        fun selectMealTime(mealTime: MealTime?) {
            _mealTime.value = if (_mealTime.value != mealTime) mealTime else null
        }

        fun selectFoodType(foodType: FoodType) {
            if (_promptStep.value == PromptStep.Step2) {
                val temp = _likeFoodTypes.value?.toMutableList()
                if (temp?.contains(foodType) == true) {
                    temp.remove(foodType)
                } else {
                    temp?.add(foodType)
                }
                _likeFoodTypes.value = temp
            } else {
                val temp = _dislikeFoodTypes.value?.toMutableList()
                if (temp?.contains(foodType) == true) {
                    temp.remove(foodType)
                } else {
                    temp?.add(foodType)
                }
                _dislikeFoodTypes.value = temp
            }
        }

        fun selectExceptionFood(food: RecentlyFood) {
            val temp =
                recentlyFoods.value?.map {
                    if (it == food) {
                        it.copy(isExcept = it.isExcept.not())
                    } else {
                        it
                    }
                }
            _recentlyFoods.value = temp
        }

        fun makePrompt() {
            var result = "Please give me one menu recommendation.\n"
            val mealtimePrompt = "I'm trying to choose what to eat for ${mealTime.value?.text}\n"
            val likeFoodTypePrompt =
                if (likeFoodTypes.value?.isNotEmpty() == true) {
                    "I tend to like ${
                        likeFoodTypes.value?.shuffled()?.joinToString(" and ")
                    } food\n"
                } else {
                    ""
                }
            val dislikeFoodTypePrompt =
                if (dislikeFoodTypes.value?.isNotEmpty() == true) {
                    "I don't like ${
                        dislikeFoodTypes.value?.shuffled()?.joinToString(" or ")
                    } food\n"
                } else {
                    ""
                }
            val exceptionFoodList = recentlyFoods.value?.filter { it.isExcept }?.map { it.name }
            val exceptionfoodPrompt =
                if (exceptionFoodList?.isNotEmpty() == true) {
                    "I want to exclude the ${
                        exceptionFoodList.shuffled().joinToString(" and ")
                    } I recently ate from the recommended menu.\n"
                } else {
                    ""
                }
            val additionalPrompt = "And I hope you consider my last words. ${additionalText.value}\n"
            val final =
                "Be sure to exclude menu items that were recently recommended.\n" +
                    "Considering the above, please recommend menu food with various combinations\n" +
                    "The most important thing is just tell me the name of the menu. only the menu name\n"
            result =
                result + mealtimePrompt + likeFoodTypePrompt + dislikeFoodTypePrompt + exceptionfoodPrompt + additionalPrompt + final

            _prompt.value = result
        }
    }
