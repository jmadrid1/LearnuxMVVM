package com.example.learnuxmvvm.data.repository

import com.example.learnuxmvvm.data.Result
import com.example.learnuxmvvm.model.Video
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseVideoRepo @Inject constructor(
    private var database: DatabaseReference) : FirebaseVideoDAO {

    companion object {
        const val FIREBASE_VIDEOS_KEY = "videos"
    }

    @ExperimentalCoroutinesApi
    override fun getVideos() = callbackFlow<Result<List<Video>>> {
        database = Firebase.database
            .getReference(FIREBASE_VIDEOS_KEY)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val videoList = mutableListOf<Video>()
                if(dataSnapshot!!.exists()){
                    for (e in dataSnapshot.children){
                        val video = e.getValue(Video::class.java)
                        if(video != null){
                            videoList.add(video)
                        }
                    }
                    this@callbackFlow.sendBlocking(Result.success(videoList))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        database.addValueEventListener(postListener)

        awaitClose {
            database.removeEventListener(postListener)
        }
    }

}