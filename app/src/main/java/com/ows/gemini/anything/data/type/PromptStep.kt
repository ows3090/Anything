package com.ows.gemini.anything.data.type

enum class PromptStep(
    val value: Int,
) {
    Step1(1),
    Step2(2),
    Step3(3),
    Step4(4),
    Step5(5),
}

fun PromptStep.toNextStep() =
    when (this) {
        PromptStep.Step1 -> PromptStep.Step2
        PromptStep.Step2 -> PromptStep.Step3
        PromptStep.Step3 -> PromptStep.Step4
        PromptStep.Step4 -> PromptStep.Step5
        PromptStep.Step5 -> PromptStep.Step5
    }

fun PromptStep.toBackStep() =
    when (this) {
        PromptStep.Step1 -> PromptStep.Step1
        PromptStep.Step2 -> PromptStep.Step1
        PromptStep.Step3 -> PromptStep.Step2
        PromptStep.Step4 -> PromptStep.Step3
        PromptStep.Step5 -> PromptStep.Step4
    }
