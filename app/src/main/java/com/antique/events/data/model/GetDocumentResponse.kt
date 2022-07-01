package com.antique.events.data.model

data class GetDocumentResponse(
    val data: List<DownloadData>,
    val success: Boolean
)