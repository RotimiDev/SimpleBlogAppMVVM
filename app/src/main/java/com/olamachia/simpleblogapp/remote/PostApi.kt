package com.olamachia.simpleblogapp.remote

import com.olamachia.simpleblogapp.model.Comment
import com.olamachia.simpleblogapp.model.Post
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostApi {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("comments")
    suspend fun getComments(
        @Query("postId") postId: Int,
    ): List<Comment>

    @GET("comments")
    suspend fun getAllComments(): List<Comment>

    @POST("comments")
    suspend fun pushComment(
        @Body comment: Comment
    )
}