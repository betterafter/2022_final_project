package com.example.kudata.repository.datasource.chat

import android.util.Log
import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom
import com.example.kudata.utils.CHAT_ROOM_CONTENT_KEY
import com.example.kudata.utils.CHAT_ROOM_KEY
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Comment

class ChatDataSourceImpl : ChatDataSource {
    private val db = FirebaseDatabase.getInstance()
    private val firebaseAuth = Firebase.auth

    var roomId: String? = null

    override suspend fun initChatRoom(uid2: String) {
        firebaseAuth.currentUser?.let {
            val room = ChatRoom(
                mapOf(it.uid to true, uid2 to true),
                listOf()
            )

            checkIfExistPersonalChatRoom(uid2) { id ->
                if (id == null) {
                    Log.d("[keykat]", "id!!: $id")
                    db.reference.child(CHAT_ROOM_KEY).push().setValue(room)
                }
            }
        }
    }

    private fun checkIfExistPersonalChatRoom(
        uid2: String,
        getChatRoomIdCallback: ((String?) -> Unit)
    ) {
        firebaseAuth.currentUser?.let {
            db.reference.child(CHAT_ROOM_KEY).orderByChild("users/${it.uid}").equalTo(true)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { data ->
                            Log.d("[keykat]", "data: $data")
                            val chatRoom: ChatRoom? = data.getValue(ChatRoom::class.java)
                            chatRoom?.let { room ->
                                room.users?.let { map ->
                                    if (map.containsKey(uid2)) {
                                        Log.d("[keykat]", "roomId: ${data.key}")
                                        roomId = data.key
                                        getChatRoomIdCallback(data.key)
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("[keykat]", "cancelled.")
                    }
                })
        } ?: run {
            getChatRoomIdCallback(null)
        }
    }

    override suspend fun enterRoom() {

    }

    override suspend fun sendMessage(message: String, timeStamp: String) {
        firebaseAuth.currentUser?.let {
            Log.d("[keykat]", "$message, $timeStamp")
            val content = ChatContent(it.uid, message, timeStamp)
            roomId?.let { id ->
                db.reference.child(CHAT_ROOM_KEY).child(id).child(CHAT_ROOM_CONTENT_KEY).push().setValue(content)
            }
        }
    }

    override suspend fun getRealtimeMessage(updatedMessageCallback: ((List<ChatContent>) -> Unit))  {
        roomId?.let { id ->
            db.reference.child(CHAT_ROOM_KEY).child(id).child(CHAT_ROOM_CONTENT_KEY)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d("[keykat]", "data changed")
                        val list = mutableListOf<ChatContent>()
                        snapshot.children.forEach {
                            it.getValue(ChatContent::class.java)?.let { content -> list.add(content) }
                        }

                        updatedMessageCallback(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("[keykat]", "canceled update chat")
                    }

                })
        }
    }
}