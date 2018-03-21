package com.example.eshika.getalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eshika on 04-Feb-18.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION=1;
    private static final String DB_Name="db2";
    private static final String DB_Col1="name";
    private static final String DB_Col2="lat";
    private static final String DB_Col3="lng";
    private static final String DB_table="geo";





    public DbHelper(Context context) {
        super(context, DB_Name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query=String.format("CREATE TABLE %s(ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL)",DB_table,DB_Col1,DB_Col2,DB_Col3);
        sqLiteDatabase.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String query=String.format("");

    }

    public void addPlace(String name,double lat,double lng){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DbHelper.DB_Col1,name);
        values.put(DbHelper.DB_Col2,lat+"");
        values.put(DbHelper.DB_Col3,lng+"");
        db.insertWithOnConflict(DB_table,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<CustomList> getAll(){
        List<CustomList> list=new ArrayList<>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query(DB_table,new String[]{DB_Col1,DB_Col2,DB_Col3},null,null,null,null,null);
            while(cursor.moveToNext()){
                int pos1=cursor.getColumnIndex(DB_Col1);
                int pos2=cursor.getColumnIndex(DB_Col2);
                int pos3=cursor.getColumnIndex(DB_Col3);
                list.add(new CustomList(cursor.getString(pos1),Double.parseDouble(cursor.getString(pos2)),Double.parseDouble(cursor.getString(pos3))));
            }
            cursor.close();
            db.close();
return list;
    }
}
