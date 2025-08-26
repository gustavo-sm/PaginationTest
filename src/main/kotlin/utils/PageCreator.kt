package utils

import com.paginationtest.paginationtest.domain.PageResponse
import kotlin.math.ceil
import kotlin.math.roundToInt

fun <T> createPage(contents: List<T>, page: Int, size: Int, totalElements: Long): PageResponse<T> {

    val limSup = page * size
    val limInf = limSup - size


    val totalPages = ceil(totalElements.toDouble() / size).roundToInt()

    if(limInf > contents.size) {
        return PageResponse(emptyList(), totalElements, totalPages, page, 0)
    }

    val slicedContents = contents.subList(limInf , minOf(limSup,  contents.size))

    return PageResponse(
        slicedContents,
        totalElements,
        totalPages,
        page,
        slicedContents.size
    )
}