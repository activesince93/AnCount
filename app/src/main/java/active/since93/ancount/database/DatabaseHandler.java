package active.since93.ancount.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import active.since93.ancount.model.UnlockDataItem;

/**
 * Created by Darshan on 12-Sep-15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // database version
    private static final int DATABASE_VERSION = 1;
    // database name
    private static final String DATABASE_NAME = "unlock_database";
    // table name
    private static final String TABLE_UNLOCKS = "unlockdata";

    //columns
    private static final String UNLOCK_COUNT = "count";
    private static final String UNLOCK_TIME = "time";
    private static final String UNLOCK_YEAR = "year";
    private static final String UNLOCK_MONTH = "month";
    private static final String UNLOCK_DATE = "date";
    private static final String UNLOCK_HOUR = "hour";
    private static final String UNLOCK_MINUTE = "minute";
    private static final String UNLOCK_SECOND = "second";
    private static final String UNLOCK_DAY_NAME = "dayname";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_UNLOCKS_TABLE = "CREATE TABLE " + TABLE_UNLOCKS + "("
                + UNLOCK_COUNT + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UNLOCK_TIME + " TEXT,"
                + UNLOCK_YEAR + " TEXT,"
                + UNLOCK_MONTH + " TEXT,"
                + UNLOCK_DAY_NAME + " TEXT,"
                + UNLOCK_DATE + " TEXT,"
                + UNLOCK_HOUR + " TEXT,"
                + UNLOCK_MINUTE + " TEXT,"
                + UNLOCK_SECOND + " TEXT" + ")";
        db.execSQL(CREATE_UNLOCKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNLOCKS);
        onCreate(db);
    }

    public void addData(UnlockDataItem unlockDataItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String milliseconds = unlockDataItem.getTime();
        ArrayList<String> arrayList;
        arrayList = getDateDetails(milliseconds);
        values.put(UNLOCK_TIME, milliseconds);
        values.put(UNLOCK_YEAR, arrayList.get(0));
        values.put(UNLOCK_MONTH, arrayList.get(1));
        values.put(UNLOCK_DAY_NAME, getDayName(milliseconds));
        values.put(UNLOCK_DATE, arrayList.get(2));
        values.put(UNLOCK_HOUR, arrayList.get(3));
        values.put(UNLOCK_MINUTE, arrayList.get(4));
        values.put(UNLOCK_SECOND, arrayList.get(5));

        db.insert(TABLE_UNLOCKS, null, values);
        db.close();
    }

    String getDayName(String milliseconds) {
        Date date = new Date(Long.parseLong(milliseconds));
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        return dayOfWeek;
    }

    public ArrayList<UnlockDataItem> getAllData() {
        ArrayList<UnlockDataItem> unlockDataItemArrayList = new ArrayList<UnlockDataItem>();
        String selectQuery = "SELECT * FROM " + TABLE_UNLOCKS + " ORDER BY " + UNLOCK_COUNT + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                UnlockDataItem unlockDataItem = new UnlockDataItem();
                unlockDataItem.setCount(Integer.parseInt(cursor.getString(0)));
                unlockDataItem.setTime(cursor.getString(1));
                unlockDataItem.setYear(cursor.getString(2));
                unlockDataItem.setMonth(cursor.getString(3));
                unlockDataItem.setDayName(cursor.getString(4));
                unlockDataItem.setDate(cursor.getString(5));
                unlockDataItem.setHour(cursor.getString(6));
                unlockDataItem.setMinute(cursor.getString(7));
                unlockDataItem.setSecond(cursor.getString(8));

                unlockDataItemArrayList.add(unlockDataItem);
            } while(cursor.moveToNext());
        }
        return unlockDataItemArrayList;
    }

    public int getTotalUnlocksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_UNLOCKS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if(cursor != null) {
            count = cursor.getCount();
        }
        cursor.close();
        return count;
    }

    public int getTodayUnlockCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Calendar cal = Calendar.getInstance();
        int date = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        String countQuery = "SELECT * FROM " + TABLE_UNLOCKS + " WHERE " + UNLOCK_DATE + "='" + date + "' AND "
                + UNLOCK_MONTH + "='" + month + "' AND " + UNLOCK_YEAR + "='" + year + "'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if(cursor != null) {
            count = cursor.getCount();
        }
        cursor.close();
        return count;
    }

    public void deleteTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_UNLOCKS, null, null);
        db.close();
    }

    public ArrayList<String> getDateDetails(String milliseconds) {
        ArrayList<String> detailsArrayList = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(milliseconds));
        detailsArrayList.add(String.valueOf(cal.get(Calendar.YEAR)));           // Year: 0
        detailsArrayList.add(String.valueOf(cal.get(Calendar.MONTH)));          // Month: 1
        detailsArrayList.add(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));   // Date: 2
        detailsArrayList.add(String.valueOf(cal.get(Calendar.HOUR)));           // Hour: 3
        detailsArrayList.add(String.valueOf(cal.get(Calendar.MINUTE)));         // Minute: 4
        detailsArrayList.add(String.valueOf(cal.get(Calendar.SECOND)));         // Second: 5
        return detailsArrayList;
    }

    public ArrayList<String> getNumberOfYearsFromDB() {
        String strQuery = "SELECT DISTINCT "+ UNLOCK_YEAR + " FROM " + TABLE_UNLOCKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(strQuery, null);
        ArrayList<String> yearsCount = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                yearsCount.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return yearsCount;
    }

    public ArrayList<UnlockDataItem> getDataOfSelectedYear(String year) {
        ArrayList<UnlockDataItem> yearDataArrayList = new ArrayList<UnlockDataItem>();
        String strQuery = "SELECT * FROM " + TABLE_UNLOCKS + " WHERE " + UNLOCK_YEAR + "='" + year + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(strQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UnlockDataItem unlockDataItem = new UnlockDataItem();
                unlockDataItem.setCount(Integer.parseInt(cursor.getString(0)));
                unlockDataItem.setTime(cursor.getString(1));
                unlockDataItem.setYear(cursor.getString(2));
                unlockDataItem.setMonth(cursor.getString(3));
                unlockDataItem.setDayName(cursor.getString(4));
                unlockDataItem.setDate(cursor.getString(5));
                unlockDataItem.setHour(cursor.getString(6));
                unlockDataItem.setMinute(cursor.getString(7));
                unlockDataItem.setSecond(cursor.getString(8));

                yearDataArrayList.add(unlockDataItem);
            } while(cursor.moveToNext());
        }
        return yearDataArrayList;
    }

    public ArrayList<String> getNumberOfMonthsForSelectedYear(String year) {
        String strQuery = "SELECT DISTINCT "+ UNLOCK_MONTH + " FROM " + TABLE_UNLOCKS + " WHERE " + UNLOCK_YEAR + "='" + year + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(strQuery, null);
        ArrayList<String> monthsCount = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                monthsCount.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return monthsCount;
    }

    public int getNumberOfTotalDays() {
        String strQuery = "SELECT DISTINCT "+ UNLOCK_DATE + ","+ UNLOCK_MONTH + "," + UNLOCK_YEAR + " FROM " + TABLE_UNLOCKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(strQuery, null);
        int daysCount = 0;
        if(cursor.moveToFirst()) {
            do {
                daysCount += 1;
            } while(cursor.moveToNext());
        }
        return daysCount;
    }

    public ArrayList<String> getNumberOfDaysForSelectedMonthAndYear(String month, String year) {
        String strQuery = "SELECT DISTINCT " + UNLOCK_DATE + " FROM " + TABLE_UNLOCKS
                + " WHERE " + UNLOCK_MONTH + "='" + month + "' AND " + UNLOCK_YEAR + "='" + year + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(strQuery, null);
        ArrayList<String> daysCount = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                daysCount.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return daysCount;
    }

    public ArrayList<UnlockDataItem> getPreviousData(int days, int months, int years) {
//        SELECT * from Orders WHERE OrderDate > strftime('%s','now', '-2 day') ORDER BY OrderDate DESC
        SQLiteDatabase db = this.getReadableDatabase();
        String selectChoice;
        if(days > 0)
            selectChoice = "SELECT strftime('%s','now','-" + days + " day')" ;
        else if(months > 0)
            selectChoice = "SELECT strftime('%s','now','-" + months + " month')" ;
        else
            selectChoice = "SELECT strftime('%s','now','-" + years + " year')" ;

        /*long seconds = DatabaseUtils.longForQuery(db, selectChoice, null);
        seconds *= 1000;*/

        Cursor cursor1 = db.rawQuery(selectChoice, null);
        long seconds = 0;
        try {
            if (cursor1.moveToFirst()) {
                seconds = cursor1.getLong(0);
            }
        } finally {
            cursor1.close();
        }
        seconds *= 1000;


        String strQuery = "SELECT * FROM " + TABLE_UNLOCKS + " WHERE " + UNLOCK_TIME + ">"
                + seconds /*+ " ORDER BY " + UNLOCK_TIME + " DESC"*/;

        Cursor cursor = db.rawQuery(strQuery, null);
        ArrayList<UnlockDataItem> unlockDataItemArrayList = new ArrayList<UnlockDataItem>();
        if(cursor.moveToFirst()) {
            do {
                UnlockDataItem unlockDataItem = new UnlockDataItem();
                unlockDataItem.setCount(Integer.parseInt(cursor.getString(0)));
                unlockDataItem.setTime(cursor.getString(1));
                unlockDataItem.setYear(cursor.getString(2));
                unlockDataItem.setMonth(cursor.getString(3));
                unlockDataItem.setDayName(cursor.getString(4));
                unlockDataItem.setDate(cursor.getString(5));
                unlockDataItem.setHour(cursor.getString(6));
                unlockDataItem.setMinute(cursor.getString(7));
                unlockDataItem.setSecond(cursor.getString(8));
                unlockDataItemArrayList.add(unlockDataItem);
            } while(cursor.moveToNext());
        }
        return unlockDataItemArrayList;
    }
    public ArrayList<String> getLast7daysNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectChoice = "SELECT strftime('%s','now','-6 day')";
        long seconds = DatabaseUtils.longForQuery(db, selectChoice, null);
        seconds *= 1000;

        String strQuery = "SELECT DISTINCT " + UNLOCK_DAY_NAME + " FROM " + TABLE_UNLOCKS + " WHERE " + UNLOCK_TIME + ">"
                + seconds/* + " ORDER BY " + UNLOCK_TIME + " DESC"*/;

        Cursor cursor = db.rawQuery(strQuery, null);
        ArrayList<String> last7DaysNameArrayList = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                last7DaysNameArrayList.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return last7DaysNameArrayList;
    }
}