package com.example.learnuxmvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnuxmvvm.data.Result
import com.example.learnuxmvvm.data.Status
import com.example.learnuxmvvm.data.repository.FirebaseVideoRepo
import com.example.learnuxmvvm.model.Video
import com.google.android.youtube.player.YouTubePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val _firebase: FirebaseVideoRepo): ViewModel() {

    private val _videoList = MutableLiveData<Result<List<Video>>>()
    val videoList: LiveData<Result<List<Video>>> = _videoList

    @ExperimentalCoroutinesApi
    suspend fun getVideos(){
        _firebase.getVideos().collect {
            val videoList = mutableListOf<Video>()
            _videoList.postValue(Result.loading(null))
            when(it.status) {
                Status.SUCCESS -> {
                    it.data?.let { items ->
                        for(e in items){
                            videoList.add(e)
                        }
                        _videoList.postValue(Result.success(videoList))
                    }
                }
                Status.ERROR -> {  _videoList.postValue(Result.error("Failed to grab items from Firebase", emptyList())) }
            }
        }
    }

    fun pauseVideo(mPlayer: YouTubePlayer){
        mPlayer.pause()
    }

    fun playVideo(id: String, mPlayer: YouTubePlayer?){
        mPlayer!!.loadVideo(id)
        mPlayer.play()
    }

}