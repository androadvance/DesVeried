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
        db.execSQL("create table EmployeeNo(EmpNum text  COLLATE NOCASE,UnitName text  COLLATE NOCASE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public long saveEmpNo(String EmpNo, String Unitname) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("EmpNum", EmpNo);
        contentValues.put("UnitName", Unitname);
        long k = sqLiteDatabase.insert("EmployeeNo", null, contentValues);
        return k;
    }

    public Cursor getEmpNoCount(String Unitname) {
        Cursor cursor;
        cursor = sqLiteDatabase.rawQuery("SELECT count(EmpNum)as Count FROM EmployeeNo where UnitName = ?", new String[]{Unitname});
        cursor.moveToFirst();
        return cursor;
    }

    public void deleteEmpNo() {
        sqLiteDatabase.execSQL("delete from EmployeeNo");
    }

    public Cursor getEmpNo(String Unitname, String Empcode) {
        Cursor cursor;
        String SQL = "SELECT EmpNum FROM EmployeeNo where UnitName = ? and EmpNum LIKE '%" + Empcode + "%'";
        cursor = sqLiteDatabase.rawQuery(SQL, new String[]{Unitname});
        cursor.moveToFirst();
        return cursor;
    }
}
