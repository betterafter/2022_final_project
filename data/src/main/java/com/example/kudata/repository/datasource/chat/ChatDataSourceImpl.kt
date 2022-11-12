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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatDataSourceImpl : ChatDataSource {
    private val db = FirebaseDatabase.getInstance()
    private val firebaseAuth = Firebase.auth

    var roomId: String? = null

    override suspend fun initChatRoom(qid: String, uid2: String?, isPrivate: Boolean, initialCallback: (() -> Unit)) {
        if (isPrivate) {
            firebaseAuth.currentUser?.let {
                val users =
                    if (uid2 != null && it.uid != uid2) mapOf(it.uid to true, uid2 to false) else mapOf(it.uid to true)
                val room = ChatRoom(
                    qid,
                    private = isPrivate,
                    users,
                    mapOf()
                )

                checkIfExistPersonalChatRoom(qid, uid2) { id ->
                    CoroutineScope(Dispatchers.IO).launch {
                        if (id == null) {
                            db.reference.child(CHAT_ROOM_KEY).push().setValue(room).await()
                        }

                        checkIfExistPersonalChatRoom(qid, uid2) {
                            initialCallback()
                        }
                    }
                }
            }
        } else {
            enterPublicRoom(qid) {
            }
        }
    }

    // 공개 질문 올릴 때 채팅방 바로 생성
    override suspend fun initPublicChatRoom(qid: String, isPrivate: Boolean) {
        firebaseAuth.currentUser?.let {
            val users = mapOf(it.uid to true)
            val room = ChatRoom(
                qid,
                private = isPrivate,
                users,
                mapOf()
            )

            db.reference.child(CHAT_ROOM_KEY).push().setValue(room)
        }
    }

    override suspend fun getUserChatRoomsAsync(callback: (List<ChatRoom>, List<ChatRoom>) -> Unit) {
        val list = mutableListOf<ChatRoom>()
        val publicList = mutableListOf<ChatRoom>()
        firebaseAuth.currentUser?.let { user ->
            val ev = db.reference.child(CHAT_ROOM_KEY).orderByChild("users/${user.uid}")
            ev.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        it.getValue(ChatRoom::class.java)?.let { room ->
                            if (room.users?.containsKey(user.uid) == true) {
                                Log.d("[keykat]", "room: $room")
                                if (room.private) {
                                    list.add(room)
                                } else {
                                    publicList.add(room)
                                }
                            }
                        }
                    }

                    callback(list, publicList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("[keykat]", "$error")
                }
            })
        }
    }

    private fun checkIfExistPersonalChatRoom(
        qid: String,
        uid2: String?,
        getChatRoomIdCallback: ((String?) -> Unit)
    ) {
        try {
            firebaseAuth.currentUser?.let { user ->
                db.reference.child(CHAT_ROOM_KEY).orderByChild("/qid").equalTo(qid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children.forEach { data ->
                                val chatRoom: ChatRoom? = data.getValue(ChatRoom::class.java)
                                chatRoom?.let { room ->
                                    room.users?.let { map ->
                                        if (map.containsKey(user.uid) && map.containsKey(uid2)
                                            && map.size <= 2 && chatRoom.private
                                        ) {
                                            roomId = data.key
                                            getChatRoomIdCallback(data.key)

                                            return
                                        }
                                    }
                                }
                            }
                            getChatRoomIdCallback(null)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("[keykat]", "$error")
                        }
                    })
            } ?: run {
                getChatRoomIdCallback(null)
            }
        } catch (e: Exception) {
            Log.d("[keykat]", "$e")
            getChatRoomIdCallback(null)
        }
    }

    override suspend fun enterPublicRoom(qid: String, callback: () -> Unit) {
        try {
            firebaseAuth.currentUser?.let { user ->
                db.reference.child(CHAT_ROOM_KEY).orderByChild("/qid").equalTo(qid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children.forEach { data ->
                                val chatRoom: ChatRoom? = data.getValue(ChatRoom::class.java)
                                chatRoom?.let { room ->
                                    roomId = data.key
                                    if (!chatRoom.private) {
                                        val map = chatRoom.users?.toMutableMap()
                                        map?.set(user.uid, true)
                                        map?.toMap()?.let {
                                            db.reference.child(CHAT_ROOM_KEY).child("/$roomId/users")
                                                .updateChildren(it)
                                        }
                                    }
                                    callback()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("[keykat]", "$error")
                        }
                    })
            } ?: run { }
        } catch (e: Exception) {
            Log.d("[keykat]", "$e")
        }
    }

    override suspend fun sendMessage(message: String, timeStamp: Long) {
        firebaseAuth.currentUser?.let {
            val content = ChatContent(it.uid, message, timeStamp)
            roomId?.let { id ->
                db.reference.child(CHAT_ROOM_KEY).child(id).child(CHAT_ROOM_CONTENT_KEY).push().setValue(content)
            }
        }
    }

    override suspend fun getRealtimeMessage(updatedMessageCallback: ((Map<String, ChatContent>) -> Unit)) {
        roomId?.let { id ->
            db.reference.child(CHAT_ROOM_KEY).child(id).child(CHAT_ROOM_CONTENT_KEY)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val map = mutableMapOf<String, ChatContent>()
                        snapshot.children.forEach {
                            it.getValue(ChatContent::class.java)?.let { content -> map[it.key!!] = content }
                        }

                        updatedMessageCallback(map)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("[keykat]", "canceled update chat")
                    }

                })
        }
    }
}