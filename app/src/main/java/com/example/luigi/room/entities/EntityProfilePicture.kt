package com.example.luigi.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_pictures")
data class EntityProfilePicture (
        @PrimaryKey(autoGenerate = true)
        var profilePictureId : Int,
        var userId : Int,
        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        var image : ByteArray?
)