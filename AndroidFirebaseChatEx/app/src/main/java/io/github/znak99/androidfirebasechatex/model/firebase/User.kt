package io.github.znak99.androidfirebasechatex.model.firebase

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid: String,
    var email: String,
    var username: String,
    var thumbnailPath: String,
    var friendsId: List<String>
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
