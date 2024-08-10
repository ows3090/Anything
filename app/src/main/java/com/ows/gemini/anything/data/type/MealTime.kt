package com.ows.gemini.anything.data.type

enum class MealTime(
    val text: String,
) {
    BreakFast("breakfast"),
    Lunch("lunch"),
    Dinner("dinner"),

    @Suppress("ktlint:standard:enum-entry-name-case")
    Midnight_meal("midnight meal"),
}
