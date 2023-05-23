package com.liz.data.network.dto.mapper.base

interface Mapper<T, R> {
    fun map(input: T?): R
}