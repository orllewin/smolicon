package oppen.smolicon

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IconViewModel: ViewModel() {

    var events = MutableLiveData<Event>()

    fun toggleGrid() = events.postValue(Event.ToggleGrid)
    fun toggleDropper() = events.postValue(Event.ToggleDropper)
    fun toggleEraser() = events.postValue(Event.ToggleEraser)
    fun toggleFill() = events.postValue(Event.ToggleFill)
    fun showColourPicker() = events.postValue(Event.ColourPicker)
    fun showInfo() = events.postValue(Event.Info)
    fun eraseAll() = events.postValue(Event.EraseAll)
    fun save() = events.postValue(Event.Save)
}