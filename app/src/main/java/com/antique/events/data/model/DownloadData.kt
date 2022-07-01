package com.antique.events.data.model

data class DownloadData(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val createdBy: String,
    val department: String,
    val description: String,
    val fileName: String,
    val permission: Boolean,
    val student: String,
    val studentRequest: Boolean,
    val title: String
)