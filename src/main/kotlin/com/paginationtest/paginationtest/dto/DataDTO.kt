package com.paginationtest.paginationtest.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class DataDTO<T> {

    @JsonProperty("data")
    var data: List<T> = emptyList()
}