package com.domain.repository

import com.domain.model.response.GenericResponse
import com.domain.model.response.RoasterResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
  suspend fun roasterList(): Flow<GenericResponse<List<RoasterResponse>?>?>
}