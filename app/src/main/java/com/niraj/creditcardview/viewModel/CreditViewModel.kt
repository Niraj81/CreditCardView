package com.niraj.creditcardview.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niraj.creditcardview.API.CreditInfoRepository
import com.niraj.creditcardview.data.CreditInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditViewModel @Inject constructor(
    private val repository: CreditInfoRepository
) : ViewModel() {

    val CreditInfo : StateFlow<CreditInfo>
        get() = repository.creditInfo

    init {
        viewModelScope.launch {
            Log.d("API", "Started")
            repository.getCreditInfo()
        }
    }

}