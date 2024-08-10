package com.ows.gemini.anything.presentation.onboarding

import com.ows.gemini.anything.data.repository.LocalRepository
import com.ows.gemini.anything.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel
    @Inject
    constructor(
        private val localRepository: LocalRepository,
    ) : BaseViewModel() {
        fun launch() {
            localRepository.launch()
        }
    }
