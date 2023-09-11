package com.niraj.creditcardview.API

import android.util.Log
import com.niraj.creditcardview.data.CreditInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CreditInfoRepository @Inject constructor(
    private val creditApi: CreditAPI
) {
    private val _creditInfo = MutableStateFlow<CreditInfo>(CreditInfo())
    val creditInfo : StateFlow<CreditInfo>
        get() = _creditInfo

    suspend fun getCreditInfo()  {
        val response = creditApi.getCards()
        if(response.isSuccessful && response.body() != null) {
            _creditInfo.emit(response.body()!!)
        }
        Log.d("API", creditInfo.toString().length.toString())
        Log.d("API", _creditInfo.value.balance.toString())
    }
}