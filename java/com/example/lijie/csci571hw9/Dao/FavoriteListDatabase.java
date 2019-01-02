package com.example.lijie.csci571hw9.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class FavoriteListDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "db_favorEvents";

    private static FavoriteListDatabase favoriteListDatabase;

    public FavoriteListDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String t_favorevents_query = "create table " + "t_EventInfo" + " ( "
                + "id" + " integer primary key autoincrement, "
                + "event_id" + " text not null, "
                + "event_name" + " text not null, "
                + "event_segment" + " text not null, "
                + "event_venue" + " text not null, "
                + "event_date" + " text not null);";
        try {
            sqLiteDatabase.execSQL(t_favorevents_query);
        } catch (SQLiteDatabaseCorruptException e) {
            e.printStackTrace();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String drpo_before_create = "DROP TABLE IF EXISTS " + DB_NAME;
        try {
            sqLiteDatabase.execSQL(drpo_before_create);
        } catch (SQLiteDatabaseCorruptException e) {
            e.printStackTrace();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreate(sqLiteDatabase);
    }

    public static SQLiteDatabase createInstance(Context context) {
        if (null == favoriteListDatabase) {
            favoriteListDatabase = new FavoriteListDatabase(context, DB_NAME, null, 1);
        }
        return favoriteListDatabase.getReadableDatabase();
    }
}
