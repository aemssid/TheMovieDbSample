package com.example.savvologytask.data.remote

data class PersonCreditResponse(
    val cast: List<CastDetails>? = null,
    val crew: List<CrewDetails>? = null,
    val id: Long? = null
) {
    data class CastDetails(
        val adult: Boolean? = null,
        val backdrop_path: String? = null,
        val character: String? = null,
        val credit_id: String? = null,
        val genre_ids: List<Int?>? = null,
        val id: Long? = null,
        val order: Int? = null,
        val original_language: String? = null,
        val original_title: String? = null,
        val overview: String? = null,
        val popularity: Double? = null,
        val poster_path: String? = null,
        val release_date: String? = null,
        val title: String? = null,
        val video: Boolean? = null,
        val vote_average: Double? = null,
        val vote_count: Int? = null
    )

    data class CrewDetails(
        val adult: Boolean? = null,
        val backdrop_path: String? = null,
        val credit_id: String? = null,
        val department: String? = null,
        val genre_ids: List<Int?>? = null,
        val id: Long? = null,
        val job: String? = null,
        val original_language: String? = null,
        val original_title: String? = null,
        val overview: String? = null,
        val popularity: Double? = null,
        val poster_path: String? = null,
        val release_date: String? = null,
        val title: String? = null,
        val video: Boolean? = null,
        val vote_average: Double? = null,
        val vote_count: Int? = null
    )


}