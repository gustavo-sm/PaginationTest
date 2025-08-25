package com.paginationtest.paginationtest.domain

class PageResponse<T> {

    var contents: List<T> = mutableListOf()
    var totalElements: Long = 0
    var totalPages: Int = 0
    var page: Int = 0
    var pageSize: Int = 0

    constructor()

    constructor(contents: List<T>, totalElements: Long, totalPages: Int, page: Int, pageSize: Int) {
        this.contents = contents
        this.totalElements = totalElements
        this.totalPages = totalPages
        this.page = page
        this.pageSize = pageSize
    }


    override fun toString(): String {
        return "Contents: ${this.contents}\n" +
                "Page number: ${this.page}\n" +
                "Page size: ${this.pageSize}\n" +
                "Total pages: ${this.totalPages}\n" +
                "Total elements: ${this.totalElements}\n"
    }
}