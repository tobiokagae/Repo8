package com.example.tugas8;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBConfig extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db_notes";
    private static final Integer DATABASE_VERSION = 1;

    public DBConfig(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "content TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "last_updated DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteNote(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("notes", "id = ?", new String[]{id.toString()});
        db.close();
    }

    @SuppressLint("Range")
    public List<Notes> getAllNotes() {
        List<Notes> notesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM notes ORDER BY created_at DESC", null);
        if (cursor != null && cursor.moveToFirst()){
            do {
                Notes notes = new Notes();
                notes.setId(cursor.getInt(cursor.getColumnIndex("id")));
                notes.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                notes.setContent(cursor.getString(cursor.getColumnIndex("content")));
                notes.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
                notes.setLastUpdated(cursor.getString(cursor.getColumnIndex("last_updated")));
                notesList.add(notes);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return notesList;
    }

    public long insertData(Notes note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("created_at", note.getCreatedAt());
        values.putNull("last_updated");

        long result = db.insert("notes", null, values);
        db.close();
        return result;
    }

    public Notes getNotesById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE id = ?", new String[]{id + ""});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") Notes notes = new Notes(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("created_at")),
                    cursor.getString(cursor.getColumnIndex("last_updated"))
            );
            cursor.close();
            db.close();
            return notes;
        } else {
            db.close();
            return null;
        }
    }

    public boolean updateNotes(Notes notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", notes.getTitle());
        values.put("content", notes.getContent());
        values.put("last_updated", notes.getLastUpdated());

        int result = db.update("notes", values, "id = ?", new String[]{notes.getId().toString()});
        db.close();
        return result > 0;
    }

//    public boolean updateNotes(Notes notes) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String sql = "UPDATE notes SET title = ?, content = ?, last_updated = datetime('now') WHERE id = ?";
//        db.execSQL(sql, new Object[]{notes.getTitle(), notes.getContent(), notes.getId()});
//        db.close();
//        return true;  // execSQL doesn't return a result, so we assume the update is successful
//    }
}
