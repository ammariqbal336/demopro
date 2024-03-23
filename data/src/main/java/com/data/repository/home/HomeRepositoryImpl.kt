package com.data.repository.home.remote

import com.domain.model.response.GenericResponse
import com.domain.model.response.RoasterResponse
import com.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val remoteSource: HomeRemoteSource
):HomeRepository  {
    override suspend fun roasterList(): Flow<GenericResponse<List<RoasterResponse>?>?> {
        return remoteSource.getRoasterInfo()
    }
}