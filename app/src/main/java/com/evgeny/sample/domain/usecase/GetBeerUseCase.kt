package com.evgeny.sample.domain.usecase

import com.evgeny.sample.data.repository.BeerRepository
import com.evgeny.sample.domain.model.Beer
import javax.inject.Inject

interface GetBeerUseCase {

    enum class SortType { NONE, BY_DATE, BY_NAME }

    suspend operator fun invoke(page: Int, sortType: SortType = SortType.NONE): Result<List<Beer>>
}

class GetBeerUseCaseImpl @Inject constructor(
    private val beerRepository: BeerRepository,
) : GetBeerUseCase {

    override suspend fun invoke(page: Int, sortType: GetBeerUseCase.SortType): Result<List<Beer>> {
        return beerRepository.getBeer(page).map {
            it.asSequence()
                .map {
                    Beer(
                        name = it.name,
                        imageUrl = it.imageUrl.orEmpty(),
                        description = it.description
                    )
                }
                .apply {
                    if (sortType != GetBeerUseCase.SortType.NONE) {
                        sortedBy {
                            when (sortType) {
                                GetBeerUseCase.SortType.BY_DATE -> TODO()
                                GetBeerUseCase.SortType.BY_NAME -> TODO()
                                GetBeerUseCase.SortType.NONE -> throw IllegalStateException()
                            }
                        }
                    }
                }
                .toList()
        }
    }
}
