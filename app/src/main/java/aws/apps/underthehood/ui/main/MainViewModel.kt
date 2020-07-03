package aws.apps.underthehood.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.co.alt236.underthehood.commandrunner.model.Result

class MainViewModel : ViewModel() {
    // Create a LiveData with a String
    var currentData: MutableLiveData<Result>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }
        private set
}