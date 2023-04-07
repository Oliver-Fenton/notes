// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.shared.preferences

enum class Theme {
    DARK,
    LIGHT
}

data class Preferences(
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double,
    val dividerPos: Double,
    val isListCollapsed: Boolean,
    val theme: Theme
)
