package oppen.smolicon

sealed class Event{
    object ToggleGrid : Event()
    object ToggleEraser : Event()
    object ToggleDropper : Event()
    object ToggleFill : Event()
    object ColourPicker : Event()
    object Info : Event()
    object EraseAll : Event()
    object Save : Event()
}
