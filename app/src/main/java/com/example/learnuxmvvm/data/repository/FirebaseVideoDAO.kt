package com.example.learnuxmvvm.data.repository

import com.example.learnuxmvvm.data.Result
import com.example.learnuxmvvm.model.Video
import kotlinx.coroutines.flow.Flow

interface FirebaseVideoDAO {

    fun getVideos(): Flow<Result<List<Video>>>


}