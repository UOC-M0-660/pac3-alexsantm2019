package edu.uoc.pac3.data.user

import edu.uoc.pac3.data.streams.Stream
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by alex on 07/09/2020.
 */

@Serializable
data class User(
    @SerialName("display_name") val display_name: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("profile_image_url") val profile_image_url: String? = null,
    @SerialName("view_count") val view_count: Int? = 0,
)

@Serializable
data class UserResponse(
    @SerialName("data")  val data: List<User>? = null,
)