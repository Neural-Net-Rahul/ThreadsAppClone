package com.example.threadsappclone.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.snap
import androidx.lifecycle.LiveData
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

class SearchViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val usersRef = db.getReference("users")

    private val _users = MutableLiveData<List<UserModel>>()
    val users : LiveData<List<UserModel>> = _users

    init{
        fetchUsers{
            _users.value = it
        }
    }

    private fun fetchUsers(onResult : (List<UserModel>) -> Unit){
        usersRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot : DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for(userSnap in snapshot.children){
                    val userDet = userSnap.getValue(UserModel::class.java)
                    if(userDet!!.uid != FirebaseAuth.getInstance().currentUser!!.uid){
                        result.add(userDet);
                    }
                }
                onResult(result)
            }

            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}