package com.example.threadsappclone.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsappclone.model.AddThreadModel
import com.example.threadsappclone.model.UserModel
import com.example.threadsappclone.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddThreadsViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val threadRef = db.getReference("threads")

    private val storageRef = FirebaseStorage.getInstance().reference
    private val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: MutableLiveData<Boolean> = _isPosted

    fun saveImage(
        thread: String,
        imageUri: Uri,
        userId : String,
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(thread,userId,it.toString())
            }
        }
    }

    fun saveData(
        thread: String,
        userId : String,
        imageLink : String,
    ) {
        val threadData = AddThreadModel(thread,imageLink,userId,System.currentTimeMillis().toString())
        threadRef.child(threadRef.push().key!!).setValue(threadData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener {
                _isPosted.postValue(false)
            }
    }

}
