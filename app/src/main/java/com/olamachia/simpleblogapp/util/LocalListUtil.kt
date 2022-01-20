package com.olamachia.simpleblogapp.util

import com.olamachia.simpleblogapp.model.Comment
import com.olamachia.simpleblogapp.model.Post


object LocalListUtil {
    private var localPostList: MutableList<Post> = ArrayList()
    private var localCommentList: MutableList<Comment> = ArrayList()

    fun addComment(item: Comment) {
        localCommentList.add(item)
    }

    fun getPostList(): List<Post> {
        return localPostList
    }

    fun getCommentList(): List<Comment> {
        return localCommentList
    }

    fun getCommentListCount(): Int {
        return  localCommentList.size
    }
}