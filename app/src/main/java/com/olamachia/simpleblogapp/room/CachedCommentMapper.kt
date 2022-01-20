package com.olamachia.simpleblogapp.room

import com.olamachia.simpleblogapp.model.Comment
import com.olamachia.simpleblogapp.util.EntityMapper

 class CachedCommentMapper : EntityMapper<CommentEntity, Comment> {
     override fun mapFromEntity(entity: CommentEntity): Comment {
         return Comment(
            id = entity.id,
            body = entity.body,
            email = entity.email,
            name = entity.name,
            postId = entity.postId
        )

     }

     override fun mapToEntity(domainModel: Comment): CommentEntity {
                 return CommentEntity(
            postId = domainModel.postId,
            id = domainModel.id,
            body = domainModel.body,
            email = domainModel.email,
            name = domainModel.name
        )
     }

    fun mapFromEntityList(entities: List<CommentEntity>): List<Comment> {
        return entities.map { mapFromEntity(it) }
    }

 }