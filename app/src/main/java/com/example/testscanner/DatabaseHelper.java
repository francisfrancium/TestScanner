package com.example.testscanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Inventory.db";
    public static final String TABLE_NAME = "products_table";
    public static final String COL_1 = "ID";
    public static final String COL_2  = "BARCODE";
    public static final String COL_3 = "DESCRIPTION";
    public static final String COL_4 = "PURCHASEORDER";
    public static final String COL_5 = "RECEIVED";
    public static final String COL_6 = "DELIVERED";
    public static final String COL_7 = "USER";
    public static final String COL_8 = "LOCATION";



    public static final String LOG_TABLE = "transaction_table";
    public static final String LOG_1 = "ID";
    public static final String LOG_2 = "BARCODE";
    public static final String LOG_3 = "TRANS";
    public static final String LOG_4 = "DATETRANS";


    public static final String INV_TABLE = "purchaseorder_table";
    public static final String INV_1 = "ID";
    public static final String INV_2  = "BARCODE";
    public static final String INV_3 = "DESCRIPTION";
    public static final String INV_4 = "QUANTITY";
    public static final String INV_5 = "COUNTED";
    public static final String INV_6 = "USER";
    public static final String INV_7 = "LOCATION";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, BARCODE STRING, DESCRIPTION STRING, PURCHASEORDER INTEGER, RECEIVED INTEGER, DELIVERED INTEGER, USER STRING, LOCATION STRING)");
        db.execSQL("create table " + LOG_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,BARCODE STRING,TRANS STRING,DATETRANS STRING)");
        db.execSQL("create table " + INV_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,BARCODE STRING,DESCRIPTION STRING,QUANTITY STRING, COUNTED STRING, USER STRING, LOCATION STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +LOG_TABLE);
        onCreate(db);
    }

    public boolean insertData(String Barcode, String Description, Integer PurchaseOrder, Integer Received ,Integer Delivered, String User, String Location ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Barcode);
        contentValues.put(COL_3, Description);
        contentValues.put(COL_4, PurchaseOrder);
        contentValues.put(COL_5, Received);
        contentValues.put(COL_6, Delivered);
        contentValues.put(COL_7, User);
        contentValues.put(COL_8, Location);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        } else {
            return true;
        }
    }
    public boolean logInsert(String Barcode, String Trans, String TransDate ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOG_2, Barcode);
        contentValues.put(LOG_3, Trans);
        contentValues.put(LOG_4, TransDate);

        long result = db.insert(LOG_TABLE, null, contentValues);
        if (result == -1){
            return false;
        } else {
            return true;
        }
    }


    public boolean updateReceivedData(String Barcode, Integer Received, String User ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Barcode);
        contentValues.put(COL_5, Received);
        contentValues.put(COL_7, User);


        db.update(TABLE_NAME, contentValues, COL_2 + " = ? ",new String[] {Barcode} );
        return true;

    }

    public boolean updateMasterData(String Barcode, Integer Received, String User, String Location ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Barcode);
        contentValues.put(COL_5, Received);
        contentValues.put(COL_7, User);
        contentValues.put(COL_8, Location);

        db.update(TABLE_NAME, contentValues, COL_2 + " = ? AND " + COL_8 + " = ? ",new String[] {Barcode, Location} );
        return true;

    }


    public boolean updateReleasedData(String Barcode, Integer Released ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Barcode);
        contentValues.put(COL_6, Released);

        db.update(TABLE_NAME, contentValues, COL_2 + " = ? ",new String[] {Barcode} );
        return true;
    }



    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase() ;
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;


    }

        public Cursor LogData() {
        SQLiteDatabase db = this.getReadableDatabase() ;
        String query = "SELECT * FROM "+LOG_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }


    public Cursor searchData() {
        SQLiteDatabase db = this.getReadableDatabase() ;
        String searchCode = CheckFragment.inputed;
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " + COL_2 + " LIKE '" + searchCode + "'" ;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }

    public Cursor validateReceivedData() {
        SQLiteDatabase db = this.getReadableDatabase() ;
        String searchCode = ReceiveFragment.inputed;
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " + COL_2 + " LIKE '" + searchCode + "'" ;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

    }

    public Cursor validateTaggedData() {
        SQLiteDatabase db = this.getReadableDatabase() ;
        String searchCode = CountFragment.inputed;
        String locCode = CountFragment.inputedLocation;
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " + COL_2 + " LIKE '" + searchCode + "'" + " AND " + COL_8 + " LIKE '" + locCode + "'" ;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

    }

    public Cursor validateReleasedData() {
        SQLiteDatabase db = this.getReadableDatabase() ;
        String searchCode = ReleaseFragment.inputed;
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " + COL_2 + " LIKE '" + searchCode + "'" ;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

    }


    public void deleteTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.execSQL("DROP TABLE "+ TABLE_NAME);
        db.execSQL("CREATE table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, BARCODE STRING, DESCRIPTION STRING, PURCHASEORDER INTEGER, RECEIVED INTEGER, DELIVERED INTEGER, USER STRING, LOCATION STRING)");
        db.close();
    }

    public void clearAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.execSQL("DELETE FROM "+ TABLE_NAME);
        db.close();
    }


}

