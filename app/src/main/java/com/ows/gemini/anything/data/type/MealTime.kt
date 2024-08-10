package com.ows.gemini.anything.data.type

enum class MealTime(
    val text: String,
) {
    BreakFast("Breakfast"),
    Lunch("Lunch"),
    Dinner("Dinner"),

    @Suppress("ktlint:standard:enum-entry-name-case")
    Midnight_meal("Midnight meal"),
}
