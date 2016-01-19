package com.hisham.mahmoud.popularmovies.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieLite extends SQLiteOpenHelper {
    public MovieLite(Context context) {
        super(context, Data.DATABASE, null, Data.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql  = "create table "+Data.TABLENAME+"("+Data._ID +" integer primary key autoincrement , "+ Data.MOVIE_ID+" text , "+
                Data.MOVIE_TITLE+" text , "+Data.MOVE_OVERVIEW+" text , "+Data.MOVIE_AVERAGE_VOTE+" text , "+
                Data.MOVIE_RELEASE_DATE+" text , "+Data.MOVIE_POSTER+" text , "+ Data.IS_FAV+" integer )";

        db.execSQL(sql);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql ="drop table "+Data.TABLENAME;
        db.execSQL(sql);
        onCreate(db);

    }
}
