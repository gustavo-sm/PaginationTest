package com.paginationtest.paginationtest.service

import com.paginationtest.paginationtest.domain.Content
import com.paginationtest.paginationtest.domain.PageResponse
import com.paginationtest.paginationtest.`interface`.Repository
import com.paginationtest.paginationtest.repository.Repository1
import com.paginationtest.paginationtest.repository.Repository2
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt

@Service
class PagingService(
    private val repository1: Repository1,
    private val repository2: Repository2
) {

    @PostConstruct
    fun pc() {

        val repositories = listOf(repository1, repository2)
        println(list(repositories, 2, 6))
    }

    fun list(repositories: List<Repository>, page: Int, size: Int): PageResponse<Content> {

        val limInf = (page - 1) * size
        val limSup = limInf + size

        var totalElements = 0L

        repositories.map {
            totalElements += it.getTotalElements()
        }

        val totalPages = if(totalElements % size == 0L) {
            totalElements / size
        } else {
            (totalElements / size) + 1
        }

        var resultsOffset = 0

        val resultsContent = mutableListOf<Content>()

        for (repo in repositories) {

            if (resultsContent.size >= size) {
                break
            }

            var pageToFetch = ceil(limSup.toDouble()/size).roundToInt()

            if(resultsOffset > 0) {
                pageToFetch = 1
            }

            val fetchedRepo = repo.list(pageToFetch, size)
            val currLocalPage = fetchedRepo.contents

            resultsContent.addAll(currLocalPage)
            resultsOffset += resultsContent.size
        }



        val results = resultsContent.subList(0, minOf(size, resultsContent.size))

        return PageResponse(
            results,
            totalElements,
            totalPages.toInt(),
            page,
            results.size
        )
    }
}