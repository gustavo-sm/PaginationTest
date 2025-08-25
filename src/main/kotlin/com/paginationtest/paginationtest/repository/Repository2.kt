package com.paginationtest.paginationtest.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import utils.createPage
import com.paginationtest.paginationtest.domain.Content
import com.paginationtest.paginationtest.domain.PageResponse
import com.paginationtest.paginationtest.dto.ContentDTO
import com.paginationtest.paginationtest.dto.DataDTO
import com.paginationtest.paginationtest.`interface`.Repository
import org.springframework.stereotype.Component

@Component
class Repository2: Repository {

    private val mapper = ObjectMapper()


    override fun list(page: Int, size: Int): PageResponse<Content> {

        val contentFile = this::class.java.classLoader.getResource("data/contentRepo2.json")?.readText()

        val contentDTO = mapper.readValue(contentFile!!, object: TypeReference<DataDTO<ContentDTO>>(){})

        val contents = mutableListOf<Content>()

        for(content in contentDTO.data) {
            contents.add(content.toDomain())
        }

        return createPage(contents, page, size, contents.size.toLong())
    }

    override fun getTotalElements(): Long {

        val contentFile = this::class.java.classLoader.getResource("data/contentRepo2.json")?.readText()
        val contentDTO = mapper.readValue(contentFile!!, object: TypeReference<DataDTO<ContentDTO>>(){})

        return contentDTO.data.size.toLong()
    }
}