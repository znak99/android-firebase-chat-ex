package io.github.znak99.androidfirebasechatex.dto

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserDTO(
    var uid: String? = null,
    var email: String? = null,
    var username: String? = null,
    var thumbnailPath: String? = null,
    var friendsId: List<String>? = null,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "email" to email,
            "username" to username,
            "thumbnailPath" to thumbnailPath,
            "friendsId" to friendsId
        )
    }
}
