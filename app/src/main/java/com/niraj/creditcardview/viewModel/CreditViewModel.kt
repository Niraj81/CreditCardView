package com.niraj.creditcardview.viewModel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niraj.creditcardview.API.CreditInfoRepository
import com.niraj.creditcardview.data.Card
import com.niraj.creditcardview.data.CreditInfo
import com.niraj.creditcardview.data.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditViewModel @Inject constructor(
    private val repository: CreditInfoRepository
) : ViewModel() {

    val CreditInfo : StateFlow<CreditInfo>
        get() = repository.creditInfo

    private val _transactions = mutableStateListOf<Transaction>()
    var transactions : List<Transaction> = _transactions

    init {
        viewModelScope.launch {
            Log.d("API", "Started")
            repository.getCreditInfo()
        }
    }

}