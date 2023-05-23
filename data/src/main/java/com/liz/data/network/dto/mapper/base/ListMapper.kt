package com.liz.data.network.dto.mapper.base

interface ListMapper<T, R> {
    fun map(input: List<T?>?): List<R>
}