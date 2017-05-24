package com.streaming.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skull.databases.DataBaseHelper;
import com.streaming.model.HLSModel;
import com.webservices.models.Banner;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by Administrator on 3/17/2017.
 */

public class HLSDatabaseManager {

    public static final String TBL_HLS = "hls";
    private SQLiteDatabase db;

    private DataBaseHelper databaseHelper;


    private Context mContext;

    public HLSDatabaseManager(Context context) {
        this.databaseHelper = new DataBaseHelper(context);
        this.mContext = context;
    }

    /// **********************  commmon used methods
    public ContentValues getContentValues(HLSModel model) {
        ContentValues cv = new ContentValues();
        cv.put("id", model.getId());
        cv.put("f_name", model.getFileName());
        cv.put("f_link", model.getFileLink());
        cv.put("f_data", model.getFileData());
        cv.put("total_ts", model.getTotalFile());
        cv.put("current_loc", model.getCurrentFileLoc());
        cv.put("rate", model.getPercentage());
        cv.put("status", model.getStatus());
        return cv;
    }

    public HLSModel getCursorModel(Cursor c) {
        HLSModel model = new HLSModel();
        model.setId(c.getInt(c.getColumnIndex("id")));
        model.setFileData(c.getString(c.getColumnIndex("f_data")));
        model.setFileLink(c.getString(c.getColumnIndex("f_link")));
        model.setFileName(c.getString(c.getColumnIndex("f_name")));
        model.setTotalFile(c.getInt(c.getColumnIndex("total_ts")));
        model.setCurrentFileLoc(c.getInt(c.getColumnIndex("current_loc")));
        model.setPercentage(c.getInt(c.getColumnIndex("rate")));
        model.setStatus(c.getInt(c.getColumnIndex("status")));
        return model;
    }


//*********************************************************************888  Close Database

    public boolean insertHLSData(HLSModel model) {


        if (getHLSDataById(model.getId()) != null) {

            return updateHLSfiles(model);
        } else {
            //insert new row
            SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
            if (db == null)
                return false;

            long id = db.insert(TBL_HLS, null, getContentValues(model));
            db.close();
            if (id > -1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public HLSModel getHLSDataById(int id) {
        HLSModel hlsModel = new HLSModel();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_HLS + " where id = " + id, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    hlsModel = getCursorModel(c);
                } while (c.moveToNext());
            } else {
                return null;
            }
        }
        c.close();
        db.close();
        return hlsModel;
    }
    public HLSModel getHLSDataByFileName(String fileName) {
        HLSModel hlsModel = new HLSModel();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_HLS + " where f_name = " + fileName, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    hlsModel = getCursorModel(c);
                } while (c.moveToNext());
            } else {
                return null;
            }
        }
        c.close();
        db.close();
        return hlsModel;
    }

    public List<HLSModel> getHLSList() {
        List<HLSModel> hlsModelList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_HLS, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    hlsModelList.add(getCursorModel(c));
                } while (c.moveToNext());
            } else {
                return hlsModelList;
            }
        }
        c.close();
        db.close();
        return hlsModelList;
    }

    public boolean updateHLSfiles(HLSModel model) {
        ContentValues cv = getContentValues(model);
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return false;
        int va = db.update(TBL_HLS, cv, "id =" + model.getId(), null);
        db.close();
        if (va < -1)
            return false;
        else
            return true;
    }


    public void deleteHlsData(int id) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();


        if (db == null)
            return;
        Cursor c = db.rawQuery("DELETE FROM " + TBL_HLS +" where id="+id, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {


                } while (c.moveToNext());
            } else {
                return;
            }
        }
        c.close();
        db.close();


    }
}

