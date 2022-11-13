package com.kuroutine.kulture.recommend

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import com.kuroutine.kulture.MainActivity

class Recommend_DBHelper(val context: Context)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        val DB_NAME = "music_list.db"
        val DB_VERSION = 1
        val TABLE_NAME = "music_list"
        val ALBUM = "앨범명"
        val SONGID = "index"
        val SONGNAME = "저작물명"
        val ARTIST = "아티스트명"
        val AGENCY = "대리중개사명"
        val COMPANY = "제작사명"
    }

    fun getALLRecord(){
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql,null)
        cursor.close()
        db.close()
    }

   fun openDB() : SQLiteDatabase {
        //데이터베이스에 쓸수 있는 권한을 리턴해줌(갖게됨)
        return this.readableDatabase
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME("+
                "$SONGID integer primary key autoincrement,"+
                "$SONGNAME text,"+
                "$ARTIST text);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }
}