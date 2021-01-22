package com.example.dscveriy;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BackgroudEmpCodeUpload extends Worker {

    SqliteDatabase sqliteDatabase;
    Context context;

    public BackgroudEmpCodeUpload(@NonNull Context context, @NonNull WorkerParameters workerParams, SqliteDatabase sqliteDatabase, Context context1) {
        super(context, workerParams);
        this.sqliteDatabase = sqliteDatabase;
        this.context = context1;
    }

    @NonNull
    @Override
    public Result doWork() {

        sqliteDatabase = new SqliteDatabase(context);
        String Empcode = getInputData().getString("Empcode");
        String UnitName = getInputData().getString("UnitName");
        sqliteDatabase.saveEmpNo(Empcode,UnitName);
        return Result.success();
    }
}
