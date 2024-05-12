package com.example.tvapplication.repository

import com.example.tvapplication.model.version.VsersionModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface GetVersionRepositoryInterface {
    suspend fun getVersion(): Flow<Response<VsersionModel>>
}