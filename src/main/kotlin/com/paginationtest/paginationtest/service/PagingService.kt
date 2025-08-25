package com.paginationtest.paginationtest.service

import com.paginationtest.paginationtest.domain.Content
import com.paginationtest.paginationtest.domain.PageResponse
import com.paginationtest.paginationtest.`interface`.Repository
import com.paginationtest.paginationtest.repository.Repository1
import com.paginationtest.paginationtest.repository.Repository2
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import utils.createPage

@Service
class PagingService(
    private val repository1: Repository1,
    private val repository2: Repository2
) {

    @PostConstruct
    fun pc() {

        val repositories = listOf(repository1, repository2)
        println(list(repositories, 2, 14))
    }

    fun list(repositories: List<Repository>, page: Int, size: Int): PageResponse<Content> {

        val currOffset = (page - 1) * size

        var localPage = 1
        var remaining = size

        var totalElements = 0L

        repositories.map {
            totalElements += it.list(1,1).totalElements
        }

        val fetchedContents = mutableListOf<Content>()

        for(repo in repositories) {

            if(fetchedContents.size >= size) {
                break
            }

            val results = repo.list(localPage, size).contents
            if(results.size >= size) {
                fetchedContents.addAll(results)
                break
            }

            fetchedContents.addAll(results)

            remaining -= results.size
        }

        val results = fetchedContents.subList(currOffset, minOf(size, fetchedContents.size))
        return createPage(results, page, size, totalElements)
    }
}