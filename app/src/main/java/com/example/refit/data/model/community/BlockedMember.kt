package com.example.refit.data.model.community

import com.google.gson.annotations.SerializedName

data class BlockedMember (
    @SerializedName("name")
    val name: String,
)
data class ReportedUser(
    @SerializedName("reportedMember")
    val reportedMember: ReportedMember,
    @SerializedName("reason")
    val reason: String
)

data class ReportedMember (
    @SerializedName("name")
    val name: String,
)