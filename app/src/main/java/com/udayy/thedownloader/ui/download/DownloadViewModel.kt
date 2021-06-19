package com.udayy.thedownloader.ui.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udayy.thedownloader.R

class DownloadViewModel : ViewModel() {

    private val _downloadForm = MutableLiveData<DownloadFormState>()
    val downloadFormState: LiveData<DownloadFormState> = _downloadForm

    fun downloadDataChanged(url: String) {
        if (!isUrlValid(url)) {
            _downloadForm.value = DownloadFormState(urlError = R.string.invalid_url)
        } else {
            _downloadForm.value = DownloadFormState(isDataValid = true)
        }
    }

    // A placeholder url validation check
    private fun isUrlValid(url: String): Boolean {
        return url.isNotEmpty()
    }
}