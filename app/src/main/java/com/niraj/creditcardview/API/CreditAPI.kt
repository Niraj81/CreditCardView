package com.niraj.creditcardview.API

import com.niraj.creditcardview.data.CreditInfo
import retrofit2.Response
import retrofit2.http.GET

interface CreditAPI {

    @GET("v3/b/63b7e92b15ab31599e2ea89f?meta=false")
    suspend fun getCards() : Response<CreditInfo>
}