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

class HomeViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val homeRef = db.getReference("threads")

    private val _threadsAndUsers = MutableLiveData<List<Pair<AddThreadModel,UserModel>>>()
    val threadsAndUsers : LiveData<List<Pair<AddThreadModel,UserModel>>> = _threadsAndUsers

    init{
        fetchThreadsAndUsers {
            _threadsAndUsers.value = it
        }
    }

    private fun fetchThreadsAndUsers(onResult : (List<Pair<AddThreadModel,UserModel>>) -> Unit){
        homeRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot : DataSnapshot) {
                val result = mutableListOf<Pair<AddThreadModel,UserModel>>()
                for(threadSnapshot in snapshot.children){
                    val thread = threadSnapshot.getValue(AddThreadModel::class.java)
                    thread.let{
                        fetUserFromThread(it!!){
                            user ->
                            result.add(0,it to user)
                            if(result.size == snapshot.childrenCount.toInt()){
                                onResult(result)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetUserFromThread(thread : AddThreadModel, onResult:(UserModel) -> Unit){
        db.getReference("users").child(thread.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot : DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error : DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}