package com.domain.usecase.home

import com.domain.model.response.GenericResponse
import com.domain.model.response.RoasterResponse
import com.domain.repository.HomeRepository
import com.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRoasterUC @Inject constructor(var homeRepository: HomeRepository) :
    UseCase<GenericResponse<List<RoasterResponse>?>?, UseCase.None>() {
    override suspend fun run(params: None): Flow<GenericResponse<List<RoasterResponse>?>?> {
        return homeRepository.roasterList()
    }

}