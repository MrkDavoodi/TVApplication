package com.example.tvapplication.data.remote

import com.example.tvapplication.data.ApiURL.URL_VERSION
import com.example.tvapplication.model.VsersionModel
import retrofit2.Response
import retrofit2.http.GET

interface GetVersionApiService {
    @GET(URL_VERSION)
    suspend fun getVersion(): Response<VsersionModel>
}