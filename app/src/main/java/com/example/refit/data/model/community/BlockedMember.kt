package com.example.refit.data.model.community

import com.google.gson.annotations.SerializedName

data class Member (
    @SerializedName("name")
    val name: String,
)
data class BlockDto(
    @SerializedName("blockedMember")
    val blockedMember: Member
)

data class ReportedUser(
    @SerializedName("reportedMember")
    val reportedMember: Member,
    @SerializedName("reason")
    val reason: String
)
