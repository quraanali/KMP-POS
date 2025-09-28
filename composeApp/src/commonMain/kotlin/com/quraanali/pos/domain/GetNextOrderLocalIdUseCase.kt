package com.quraanali.pos.domain

import com.quraanali.pos.data.HomeRepository


class GetNextOrderLocalIdUseCase(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(): Int {
        return homeRepository.getNextOrderLocalId()
    }
}