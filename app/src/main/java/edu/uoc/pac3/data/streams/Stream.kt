package edu.uoc.pac3.data.streams

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Created by alex on 07/09/2020.
 */

@Serializable
data class Stream(
    //val userName: String? = null,
    val user_name: String? = null,
    val title: String? = null,
    val thumbnail_url: String? = null,
    //val pagination: String? = null,
)

@Serializable
data class StreamPagination(
    val cursor: String? = null,
)

@Serializable
data class StreamsResponse(
    val data: List<Stream>? = null,
    val pagination: List<StreamPagination>? = null,
    //@SerialName("data")         val data: List<Stream>? = null,
    @SerialName("id")           val id: String? = null,
    @SerialName("user_id")      val user_id: String? = null,
    @SerialName("user_name")    val user_name: String? = null,
    @SerialName("game_id")      val game_id: String? = null,
    @SerialName("title")        val title: String? = null,
    @SerialName("viewer_count") val viewer_count: String? = null,
    @SerialName("started_at")   val started_at: String? = null,
    @SerialName("language")     val language: String? = null,
    @SerialName("thumbnail_url") val thumbnail_url: String? = null,
//    @SerialName("tag_ids")      val tag_ids: String? = null,
   // @SerialName("pagination")   val pagination: String? = null,
)