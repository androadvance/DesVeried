package com.example.dscveriy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteDatabase extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;
    SqliteDatabase cxt;

    public SqliteDatabase(@Nullable Context context) {
        super(context, ".db", null, 1);
        sqLiteDatabase = getWritableDatabase();
        cxt = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table WorkSationType(Typeone text,Typetwo text,Typethree text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long saveType(String s_typeone, String s_typetwo, String s_typethree) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Typeone", s_typeone);
        contentValues.put("Typetwo", s_typetwo);
        contentValues.put("Typethree", s_typethree);
        long k = sqLiteDatabase.insert("WorkSationType", null, contentValues);
        return k;
    }

    public Cursor fetchTypeList() {
        Cursor cursor;
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM WorkSationType", new String[]{});
        cursor.moveToFirst();
        return cursor;
    }
}
