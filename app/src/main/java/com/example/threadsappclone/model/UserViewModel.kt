package com.example.threadsappclone.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    private val threadRef = db.getReference("threads")
    private val userRef = db.getReference("users")

    private val _threads = MutableLiveData(listOf<AddThreadModel>())
    val threads: LiveData<List<AddThreadModel>> get() = _threads

    private val _user = MutableLiveData(UserModel())
    val user: LiveData<UserModel> get() = _user

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList: LiveData<List<String>> get() = _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList: LiveData<List<String>> get() = _followingList

    fun fetchUser(uid: String) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _user.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                // Optionally log the error or show a message to the user
                println("Error fetching user: ${error.message}")
            }
        })
    }

    fun fetchThread(uid: String) {
        threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList = snapshot.children.mapNotNull{
                    it.getValue(AddThreadModel::class.java)
                }
                _threads.postValue(threadList.toMutableList())
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                // Optionally log the error or show a message to the user
                println("Error fetching threads: ${error.message}")
            }
        })
    }

    val firestoreDb = Firebase.firestore

    fun followUsers(userId:String,currentUserId:String){
        val ref = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        ref.update("followingIds",FieldValue.arrayUnion(userId))
        followerRef.update("followerIds",FieldValue.arrayUnion(currentUserId))
    }

    fun getFollowers(userId:String){
        firestoreDb.collection("followers").document(userId)
            .addSnapshotListener{
                value,error->
                val followerId = value?.get("followerIds") as? List<String> ?: listOf()
                _followerList.postValue(followerId)
            }
    }

    fun getFollowing(userId:String){
        firestoreDb.collection("following").document(userId)
            .addSnapshotListener{
                    value,error->
                val followingId = value?.get("followingIds") as? List<String> ?: listOf()
                _followingList.postValue(followingId)
            }
    }
}
