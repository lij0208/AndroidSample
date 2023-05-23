package com.liz.domain.entity

data class SearchEntity(
    val lastbuilddate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val itemsEntity: List<SearchItemEntity>
)