package com.ows.gemini.anything.presentation.recommendations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ows.gemini.anything.data.type.FoodType
import com.ows.gemini.anything.data.type.MealTime
import com.ows.gemini.anything.data.type.PromptStep
import com.ows.gemini.anything.data.type.toBackStep
import com.ows.gemini.anything.data.type.toNextStep
import com.ows.gemini.anything.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel
    @Inject
    constructor() : BaseViewModel() {
        private val _promptStep = MutableLiveData(PromptStep.Step1)
        val promptStep: LiveData<PromptStep> = _promptStep

        private val _mealTime = MutableLiveData<MealTime>(null)
        val mealTime: LiveData<MealTime> = _mealTime

        private val _likeFoodType = MutableLiveData<FoodType>(null)
        val likeFoodType: LiveData<FoodType> = _likeFoodType

        private val _dislikeFoodType = MutableLiveData<FoodType>(null)
        val dislikeFoodType: LiveData<FoodType> = _dislikeFoodType

        fun toNextStep() {
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
                    _likeFoodType.value = null
                }

                PromptStep.Step3 -> {
                    _dislikeFoodType.value = null
                }

                PromptStep.Step4 -> {
                }

                PromptStep.Step5 -> {
                }

                else -> Unit
            }
            _promptStep.value = _promptStep.value?.toBackStep()
        }

        fun toSkip() {
            when (_promptStep.value) {
                PromptStep.Step2 -> {
                    _likeFoodType.value = null
                }

                PromptStep.Step3 -> {
                    _dislikeFoodType.value = null
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
                _likeFoodType.value = if (_likeFoodType.value != foodType) foodType else null
            } else {
                _dislikeFoodType.value = if (_dislikeFoodType.value != foodType) foodType else null
            }
        }
    }
