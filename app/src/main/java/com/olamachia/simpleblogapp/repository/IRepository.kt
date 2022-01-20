package com.olamachia.simpleblogapp.repository

import com.olamachia.simpleblogapp.model.Comment
import com.olamachia.simpleblogapp.model.Post
import com.olamachia.simpleblogapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getPosts(): Flow<Resource<List<Post>>>

    suspend fun getComments(postId: Int): Flow<Resource<List<Comment>>>

    suspend fun getAllComments(): Flow<Resource<List<Comment>>>

    suspend fun pushComment(comment: Comment)

    suspend fun addPost(post: Post)
}