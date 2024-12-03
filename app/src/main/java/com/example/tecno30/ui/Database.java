package com.example.tecno30.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private static final String Database_Name = "registros.db";
    private static final String Table_name = "tareas";
    private static final String Col_1 = "ID";
    private static final String Col_2 = "TAREAS";

    public Database(Context context ) {
        super(context, Database_Name,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+ Table_name + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TAREAS TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Table_name);
        onCreate(db);
    }
    public boolean insertTarea(String tarea) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2, tarea);
        long result = db.insert(Table_name,null,contentValues);
        return result != -1;
    }
    public ArrayList<String> getAllTareas() {
        ArrayList<String> tareas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + Table_name, null);
        if(res.moveToFirst()) {
            do {
                tareas.add(res.getString(1));
            } while(res.moveToNext());
        }
        return tareas;
    }
    public void deleteTarea(String tarea){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Table_name,"TAREAS = ?", new String[]{tarea});
    }
    public void updateTarea(String oldTarea, String newTarea){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2, newTarea);
        db.update(Table_name, contentValues, "TAREAS = ?", new String[]{oldTarea});
    }


}

