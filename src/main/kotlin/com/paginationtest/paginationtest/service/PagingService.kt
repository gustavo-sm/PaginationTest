package com.paginationtest.paginationtest.service

import com.paginationtest.paginationtest.domain.Content
import com.paginationtest.paginationtest.domain.PageResponse
import com.paginationtest.paginationtest.`interface`.Repository
import com.paginationtest.paginationtest.repository.Repository1
import com.paginationtest.paginationtest.repository.Repository2
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class PagingService(
    private val repository1: Repository1,
    private val repository2: Repository2
) {

    @PostConstruct
    fun pc() {

        val repositories = listOf(repository1, repository2)
        println(list(repositories, 1, 6))
    }

    fun list(repositories: List<Repository>, page: Int, size: Int): PageResponse<Content> {

        val limInf = (page - 1) * size
        val limSup = limInf + size

        var localPage = 1

        var totalElements = 0L

        repositories.map {
            totalElements += it.getTotalElements()
        }

        val totalPages = if(totalElements % size == 0L) {
            totalElements / size
        } else {
            (totalElements / size) + 1
        }

        val fetchedContents = mutableListOf<Content>()

        var passedOffset = 0

        for(repo in repositories) {

            if(fetchedContents.size >= size) {
                break
            }

            var fetchedRepo = repo.list(localPage, size) //primeira pagina do repositorio iterado
            var currLocalPage = fetchedRepo.contents
            passedOffset += currLocalPage.size


            if(fetchedRepo.totalPages > 1) {

                for (pageNumber in 2 .. fetchedRepo.totalPages) { // segunda pag em diante

                    while (limInf >= passedOffset && localPage < fetchedRepo.totalPages) { // pula paginas que ja foram

                        localPage++

                        fetchedRepo = repo.list(localPage, size)
                        currLocalPage = fetchedRepo.contents

                        passedOffset += currLocalPage.size
                    }

                    if(limInf < passedOffset) {
                        fetchedContents.addAll(currLocalPage.subList(0, currLocalPage.size))
                    }

                    if(localPage >= fetchedRepo.totalPages) {
                        break
                    }
                }
            }

            if(limInf < passedOffset) {
                fetchedContents.addAll(currLocalPage.subList(0, currLocalPage.size))
            }

            if(fetchedContents.size >= size) {
                break
            }

            localPage = 1
        }

        val results = fetchedContents.subList(0, minOf(size, fetchedContents.size))

        return PageResponse(
            results,
            totalElements,
            totalPages.toInt(),
            page,
            results.size
        )
    }
}