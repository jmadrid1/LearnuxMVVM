package com.example.learnuxmvvm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnuxmvvm.R
import com.example.learnuxmvvm.data.Result
import com.example.learnuxmvvm.model.Video
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(): ViewModel() {

    companion object {
        const val firebase_videos_key = "videos"
    }

    private val _videoList = MutableLiveData<Result<List<Video>>>()
    val videoList: LiveData<Result<List<Video>>> = _videoList

    private lateinit var fireReference : DatabaseReference

    fun getVideos(){
        viewModelScope.launch {
            _videoList.postValue(Result.loading(null))
            val queryList = mutableListOf<Video>()
            fireReference = Firebase.database.getReference(firebase_videos_key)
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        for (e in dataSnapshot.children){
                            val video = e.getValue(Video::class.java)
                            if(video != null){
                                queryList.add(video)
                            }
                        }
                        _videoList.postValue(Result.success(queryList))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    _videoList.postValue(Result.error("Failed to get videos. Please try again.", queryList))
                }
            }
            fireReference.addValueEventListener(postListener)
        }
    }

}