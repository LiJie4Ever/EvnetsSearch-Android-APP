package com.example.lijie.csci571hw9.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class FavoriteListDataManager {
    private SQLiteDatabase myDB;

    public FavoriteListDataManager(Context context) {
        myDB = FavoriteListDatabase.createInstance(context);
    }

    public void insertIntoDB(String event_id, String event_name, String event_segment, String event_venue, String event_date) {
        final String insert_query = "insert into " + "t_EventInfo" + " values(null,?,?,?,?,?)";
        try {
            myDB.execSQL(insert_query, new String[] {event_id, event_name, event_segment, event_venue, event_date});
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFromDB(String event_id) {
        final String delete_query = "delete from " + "t_EventInfo" + " where " + "event_id" + "=?";
        try {
            myDB.execSQL(delete_query, new String[] {event_id});
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor findAllEvents() {
        final String find_all_query = "select * from " + "t_EventInfo";
        return myDB.rawQuery(find_all_query, null);
    }

    public Cursor findEventById(String event_id) {
        final String find_oneEvent_query = "select * from " + "t_EventInfo" + " where " + "event_id" + "=?";
        return myDB.rawQuery(find_oneEvent_query, new String[] {event_id});
    }
}
