package com.paginationtest.paginationtest.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.paginationtest.paginationtest.domain.Content

@JsonIgnoreProperties(ignoreUnknown = true)
class ContentDTO {

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("repository")
    var repository: String = ""

    @JsonIgnore
    fun toDomain(): Content {
        return Content(name, repository)
    }
}