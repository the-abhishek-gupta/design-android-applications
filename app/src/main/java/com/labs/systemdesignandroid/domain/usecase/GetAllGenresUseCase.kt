package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.model.MovieModel
import javax.inject.Inject

class GetAllGenresUseCase @Inject constructor() {

    operator fun invoke(movies: List<MovieModel>): List<String> =
        movies.flatMap { it.genres }.distinct().sorted()
}