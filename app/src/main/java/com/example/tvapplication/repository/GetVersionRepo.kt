package com.example.tvapplication.repository

import com.example.tvapplication.data.remote.GetVersionApiService
import com.example.tvapplication.model.VsersionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class GetVersionRepo @Inject
constructor(private val versionApiService: GetVersionApiService) : GetVersionRepositoryInterface {
    override suspend fun getVersion(): Flow<Response<VsersionModel>> = flow {
        emit(versionApiService.getVersion())
    }.flowOn(Dispatchers.IO)
}