package com.paginationtest.paginationtest.`interface`

import com.paginationtest.paginationtest.domain.Content
import com.paginationtest.paginationtest.domain.PageResponse

interface Repository {
    fun list(page: Int, size: Int): PageResponse<Content>
    fun getTotalElements(): Long
}