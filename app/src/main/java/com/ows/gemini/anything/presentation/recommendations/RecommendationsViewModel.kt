package com.ows.gemini.anything.presentation.recommendations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

        fun toNextStep() {
            _promptStep.value = _promptStep.value?.toNextStep()
        }

        fun toBackStep() {
            _promptStep.value = _promptStep.value?.toBackStep()
        }

        fun selectMealTime(mealTime: MealTime?) {
            _mealTime.value = if (_mealTime.value != mealTime) mealTime else null
        }
    }
