package com.example.itemlist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.itemlist.Model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "itemListDatabase";
    private static final String ITEM_TABLE = "item";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_ITEM_TABLE = "CREATE TABLE " + ITEM_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertItem(ItemModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getItem());
        cv.put(STATUS, 0);
        db.insert(ITEM_TABLE, null, cv);
    }

    public List<ItemModel> getAllItems(){
        List<ItemModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(ITEM_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ItemModel task = new ItemModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setItem(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(ITEM_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateItem(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(ITEM_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteItem(int id){
        db.delete(ITEM_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
