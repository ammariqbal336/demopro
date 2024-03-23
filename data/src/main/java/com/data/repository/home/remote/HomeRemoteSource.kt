package com.data.repository.home.remote

import com.data.network.API
import com.data.network.NetworkCall
import com.domain.model.response.GenericResponse
import com.domain.model.response.RoasterResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRemoteSource @Inject constructor(
    private val networkCall: NetworkCall
) {

    suspend fun getRoasterInfo(): Flow<GenericResponse<List<RoasterResponse>?>?>{
        return networkCall.get<GenericResponse<List<RoasterResponse>?>?>(API.RoasterAPI,null)
    }
}