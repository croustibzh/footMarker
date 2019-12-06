package com.georgebrown.comp2074.footmarker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static String TABLE_NAME = "ROUTESDATA";
    private static String COL0 = "ID";
    private static String COL1 = "NAME";
    private static String COL2 = "DISTANCE";
    private static String COL3 = "TIME";
    private static String COL4 = "RATING";
    private static String COL5 = "IMAGE";
    private static String COL6 = "HASHTAG";
    public DataBaseHelper( Context context) {
        super(context, TABLE_NAME, null,2);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String  createTable = "CREATE TABLE "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL1 + " TEXT, " + COL2 + " DOUBLE, "+COL3+ " LONG, "+ COL4 +" FLOAT, " + COL5 + " BLOB, " + COL6 + " TEXT )" ;
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTable = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        sqLiteDatabase.execSQL(dropTable);
        onCreate(sqLiteDatabase);
    }
    public void addRoute(String name, double distance, Long time, float rating, byte[] image,String hashTag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (name=="")
          name = "Route";
        contentValues.put(COL1,name);
        contentValues.put(COL2,distance);
        contentValues.put(COL3,time);
        contentValues.put(COL4,rating);
        contentValues.put(COL5, image);
        contentValues.put(COL6, hashTag);
        db.insert(TABLE_NAME,null,contentValues);

    }
    public Cursor getAllRoutes(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+ TABLE_NAME,null);
        return data;
    }

    public Cursor getRoute(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+ TABLE_NAME + " WHERE " + COL0 + " = "+ id,null);
        return data;
    }

    public Cursor getRouteName(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL1 + " FROM "+ TABLE_NAME + " WHERE " + COL0 + " = "+ id,null);
        return data;
    }

    public void deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID = "+id,null);
    }

    public void updateName(String updatedName, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME +
                " SET " + COL1 + " = '" + updatedName +
                "' WHERE " + COL0 + " = '" + id + "'" ;
        db.execSQL(query);
    }

    public void updateRating(Float updatedRating, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME +
                " SET " + COL4 + " = '" + updatedRating +
                "' WHERE " + COL0 + " = '" + id + "'" ;
        db.execSQL(query);
    }

    public int getLastId(){
        int id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT count(ID) AS counter FROM "+ TABLE_NAME ,null);
        data.moveToNext();
        id = data.getInt(0);
        return id;
    }
}
