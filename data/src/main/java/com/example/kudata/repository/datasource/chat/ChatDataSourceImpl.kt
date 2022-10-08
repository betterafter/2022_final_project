package com.example.kudata.repository.datasource.chat

import android.util.Log
import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom
import com.example.kudata.utils.CHAT_ROOM_CONTENT_KEY
import com.example.kudata.utils.CHAT_ROOM_KEY
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChatDataSourceImpl : ChatDataSource {
    private val db = FirebaseDatabase.getInstance()
    private val firebaseAuth = Firebase.auth

    var roomId: String? = null

    override suspend fun initChatRoom(uid2: String) {
        firebaseAuth.currentUser?.let {
            val room = ChatRoom(
                mapOf(it.uid to true, uid2 to true),
                mapOf()
            )

            checkIfExistPersonalChatRoom(uid2) { roomId ->
                if (roomId == null) {
                    db.reference.child(CHAT_ROOM_KEY).push().setValue(room)
                }
            }
        }
    }

    private suspend fun checkIfExistPersonalChatRoom(
        uid2: String,
        getChatRoomIdCallback: ((String?) -> Unit)
    ) {
        firebaseAuth.currentUser?.let {
            db.reference.child(CHAT_ROOM_KEY).orderByChild("users/${it.uid}").equalTo(true)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { data ->
                            val chatRoom: ChatRoom? = data.getValue(ChatRoom::class.java)
                            chatRoom?.let {
                                if (it.users.containsKey(uid2)) {
                                    roomId = snapshot.key
                                    getChatRoomIdCallback(snapshot.key)
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("[keykat]", "cancelled.")
                    }
                })
        }
    }

    override suspend fun enterRoom() {

    }

    override suspend fun sendMessage(message: String, timeStamp: String)  {
        firebaseAuth.currentUser?.let {
            val content = ChatContent(it.uid, message, timeStamp)
            roomId?.let { id ->
                db.reference.child(CHAT_ROOM_KEY).child(id).child(CHAT_ROOM_CONTENT_KEY).push().setValue(content)
            }
        }
    }
}