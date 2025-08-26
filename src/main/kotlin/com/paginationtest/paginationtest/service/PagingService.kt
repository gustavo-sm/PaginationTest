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
        println(list(repositories, 1, 1000))
    }

    fun list(repositories: List<Repository>, page: Int, size: Int): PageResponse<Content> {
        val offsetGlobal = (page - 1) * size
        var collected = 0
        var totalElements = 0L

        val result = mutableListOf<Content>()

        val repoElementCounts = repositories.map {
            val count = it.getTotalElements()
            totalElements += count
            count
        }

        val totalPages = ceil(totalElements.toDouble() / size).toInt()
        var globalOffsetTracker = 0L

        for ((index, repo) in repositories.withIndex()) {
            val repoTotal = repoElementCounts[index]

            if (collected >= size) break

            val repoStart = globalOffsetTracker
            val repoEnd = globalOffsetTracker + repoTotal

            // Se offset global está além deste repo, continue
            if (offsetGlobal >= repoEnd) {
                globalOffsetTracker += repoTotal
                continue
            }

            // Calcular o offset local para este repositório
            val localOffset = maxOf(0, offsetGlobal - repoStart).toInt()

            var localPage = localOffset / size + 1
            val localIndex = localOffset % size

            // Loop de busca dentro do repositório atual
            while (collected < size) {

                val response = repo.list(localPage, size)
                val pageContent = response.contents

                if (pageContent.isEmpty()) break

                // Pular elementos iniciais se for a primeira página com offset
                val fromIndex = if (localPage == (localOffset / size + 1)) localIndex else 0

                val itemsToAdd = pageContent.drop(fromIndex).take(size - collected)
                result.addAll(itemsToAdd)
                collected += itemsToAdd.size

                if (itemsToAdd.size < (pageContent.size - fromIndex)) {
                    break
                }

                if (pageContent.size < size) {
                    break
                }

                localPage += 1
            }

            globalOffsetTracker += repoTotal
        }

        return PageResponse(
            result,
            totalElements,
            totalPages,
            page,
            result.size
        )
    }
}