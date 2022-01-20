package com.olamachia.simpleblogapp.repository

import android.util.Log
import com.olamachia.simpleblogapp.model.Comment
import com.olamachia.simpleblogapp.model.Post
import com.olamachia.simpleblogapp.remote.RetrofitInstance
import com.olamachia.simpleblogapp.room.CachedCommentMapper
import com.olamachia.simpleblogapp.room.CachedPostMapper
import com.olamachia.simpleblogapp.room.LocalDataBase
import com.olamachia.simpleblogapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class Repository(
    private val db: LocalDataBase,
    private val cachedCommentMapper: CachedCommentMapper,
    private val cachedPostMapper: CachedPostMapper,
) : IRepository {

    /*Get All Posts*/
    override suspend fun getPosts(): Flow<Resource<List<Post>>> = flow {
        emit(Resource.Loading)
        try {
            /*Retrieve Remote Posts*/
            val remotePosts = RetrofitInstance.POST_API.getPosts()
            /*Map posts to Local Database*/
            for (post in remotePosts) {
                db.userDao().addPost(cachedPostMapper.mapToEntity(post))
            }
            /*Retrieve Posts Local DataBAse*/
            val cachedPosts = db.userDao().readAllPost()
            cachedPosts.collect {
                emit(Resource.Success(cachedPostMapper.mapFromEntityList(it)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    /*Get Specific Comments*/
    override suspend fun getComments(postId: Int): Flow<Resource<List<Comment>>> = flow {
        emit(Resource.Loading)
        try {
            /*Retrieve Posts Local DataBAse*/
            val cachedComments = db.userDao().readComments(postId)
            cachedComments.collect {
                emit(Resource.Success(cachedCommentMapper.mapFromEntityList(it)))
            }
        } catch (e: Exception) {
//            error exception
            emit(Resource.Error(e))
        }
    }

    /*Get All Comments*/
    override suspend fun getAllComments(): Flow<Resource<List<Comment>>> = flow {
        emit(Resource.Loading)
        try {
            /*Retrieve Remote Comments*/
            val remoteComments = RetrofitInstance.POST_API.getAllComments()
            /*Map posts to Local Database*/
            for (comment in remoteComments) {
                db.userDao().addComment(cachedCommentMapper.mapToEntity(comment))
            }
            /*Retrieve Posts Local DataBAse*/
            val cachedComments = db.userDao().readAllComments()
            cachedComments.collect {
                emit(Resource.Success(cachedCommentMapper.mapFromEntityList(it)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    /*Add Comment*/
    override suspend fun pushComment(comment: Comment) {
        try {
            /*Add Comment to Local Database*/
            db.userDao().addComment(cachedCommentMapper.mapToEntity(comment))
        } catch (e: Exception) {
            Log.d("COM", "pushComment: ${e.message}")
        }
    }

    override suspend fun addPost(post: Post) {
        try {
            /*Add Comment to Local Database*/
            db.userDao().addPost(cachedPostMapper.mapToEntity(post))
        } catch (e: Exception) {
            Log.d("Post", "Add Post: ${e.message}")
        }
    }


}