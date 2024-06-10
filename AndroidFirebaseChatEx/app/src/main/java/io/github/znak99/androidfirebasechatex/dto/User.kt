package io.github.znak99.androidfirebasechatex.dto

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String? = null,
    val email: String? = null,
    val username: String? = null,
    val thumbnailPath: String? = null,
    val friendsId: List<String>? = null,
)
