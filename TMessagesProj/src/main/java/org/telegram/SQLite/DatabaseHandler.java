package org.telegram.SQLite;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.telegram.messenger.FileLog;
import java.util.ArrayList;

@SuppressWarnings("SameParameterValue")
public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "favourites";
    private static final String TABLE_FAVORITE_CHATS = "favorite_chats";
    private static final String TABLE_HIDDEN_CHATS = "hidden_chats";
    private static final String TABLE_USERS_NOTIFICATIONS_EXCEPTION = "users_notifications_exceptions";

    private static final String KEY_ID = "id";
    private static final String KEY_CHAT_ID = "chat_id";
    private static final String KEY_USER_ID = "user_id";

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        buildDatabase(db, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion != 3)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_CHATS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIDDEN_CHATS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS_NOTIFICATIONS_EXCEPTION);
        }

        buildDatabase(db, newVersion);
    }

    private void buildDatabase(SQLiteDatabase db, int version)
    {
        if (version != 3)
        {
            String CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_FAVORITE_CHATS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CHAT_ID + " INTEGER" + ")";

            String CREATE_HIDDEN_TABLE = "CREATE TABLE " + TABLE_HIDDEN_CHATS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CHAT_ID + " INTEGER" + ")";

            db.execSQL(CREATE_FAVORITE_TABLE);
            db.execSQL(CREATE_HIDDEN_TABLE);
        }

        String CREATE_USERS_NOTIFICATIONS_EXCEPTIONS_TABLE = "CREATE TABLE " + TABLE_USERS_NOTIFICATIONS_EXCEPTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID + " INTEGER" + ")";

        db.execSQL(CREATE_USERS_NOTIFICATIONS_EXCEPTIONS_TABLE);
    }

    public ArrayList<Long> getFavoriteChatsList()
    {
        return getChatsList(TABLE_FAVORITE_CHATS);
    }

    public ArrayList<Long> getHiddenChatsList()
    {
        return getChatsList(TABLE_HIDDEN_CHATS);
    }

    public ArrayList<Integer> getUsersNotificationsExceptions()
    {
        return getUsersList(TABLE_USERS_NOTIFICATIONS_EXCEPTION);
    }


    public void addFavorite(Long id)
    {
        addChatItem(TABLE_FAVORITE_CHATS, id);
    }

    public void deleteFavorite(Long id)
    {
        deleteChatItem(TABLE_FAVORITE_CHATS, id);
    }

    public void addHiddenChat(Long id)
    {
        addChatItem(TABLE_HIDDEN_CHATS, id);
    }

    public void deleteHiddenChat(Long id)
    {
        deleteChatItem(TABLE_HIDDEN_CHATS, id);
    }

    public void addUserNotificationException(int id)
    {
        addUserItem(TABLE_USERS_NOTIFICATIONS_EXCEPTION, id);
    }

    public void deleteUserNotificationException(int id)
    {
        deleteUserItem(TABLE_USERS_NOTIFICATIONS_EXCEPTION, id);
    }

    private ArrayList<Long> getChatsList(String table)
    {
        ArrayList<Long> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try
        {
            cursor = db.query(table, new String[] { KEY_CHAT_ID },
                    null, null, null,
                    null, null, null);
            if (cursor.moveToFirst())
            {
                do
                {
                    list.add(cursor.getLong(0));
                }
                while (cursor.moveToNext());
            }

            db.close();
        }
        catch (Exception e)
        {
            if (cursor != null)
                cursor.close();

            FileLog.e(e);
        }
        if (cursor != null)
            cursor.close();

        return list;
    }

    private ArrayList<Integer> getUsersList(String table)
    {
        ArrayList<Integer> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try
        {
            cursor = db.query(table, new String[] { KEY_USER_ID },
                    null, null, null,
                    null, null, null);
            if (cursor.moveToFirst())
            {
                do
                {
                    list.add(cursor.getInt(0));
                }
                while (cursor.moveToNext());
            }

            db.close();
        }
        catch (Exception e)
        {
            if (cursor != null)
                cursor.close();

            FileLog.e(e);
        }
        if (cursor != null)
            cursor.close();

        return list;
    }

    private void addChatItem(String table, Long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHAT_ID, id);
        db.insert(table, null, values);
        db.close();
    }

    private void deleteChatItem(String table, Long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, KEY_CHAT_ID + "=?", new String[]{ String.valueOf(id) });
        db.close();
    }

    private void addUserItem(String table, Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, id);
        db.insert(table, null, values);
        db.close();
    }

    private void deleteUserItem(String table, Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, KEY_USER_ID + "=?", new String[]{ String.valueOf(id) });
        db.close();
    }
}
