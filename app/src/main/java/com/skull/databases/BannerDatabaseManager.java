package com.skull.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.webservices.models.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 3/17/2017.
 */

public class BannerDatabaseManager {

    public static final String TBL_BANNER = "banner";
    private SQLiteDatabase db;

    private DataBaseHelper databaseHelper;


    private Context mContext;

    public BannerDatabaseManager(Context context) {
        this.databaseHelper = new DataBaseHelper(context);
        this.mContext = context;
    }

    /// **********************  commmon used methods
    public ContentValues getContentValues(Banner model) {
        ContentValues cv = new ContentValues();
        cv.put("id", model.getId());
        cv.put("url", model.getUrl());
        cv.put("video_id", model.getVideoId());
        cv.put("is_youtube", model.getIsYoutube());
        return cv;
    }

    public Banner getCursorModel(Cursor c) {
        Banner model = new Banner();
        model.setId(c.getInt(c.getColumnIndex("id")));
        model.setUrl(c.getString(c.getColumnIndex("url")));
        model.setIsYoutube(c.getString(c.getColumnIndex("is_youtube")));
        model.setVideoId(c.getString(c.getColumnIndex("video_id")));

        return model;
    }


//*********************************************************************888  Close Database

    public boolean insertBannerData(Banner model) {


        if (getBannerById(model.getId()) != null) {

            return updateSalatTracker(model);
        } else {
            //insert new row
            SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
            if (db == null)
                return false;

            long id = db.insert(TBL_BANNER, null, getContentValues(model));
            db.close();
            if (id > -1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Banner getBannerById(int id) {
        Banner salatModelList = new Banner();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_BANNER + " where id = " + id , null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    salatModelList = getCursorModel(c);
                } while (c.moveToNext());
            } else {
                return null;
            }
        }
        c.close();
        db.close();
        return salatModelList;
    }


    public List<Banner> getBannerList() {
        List<Banner> salatModelList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_BANNER, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    salatModelList .add( getCursorModel(c));
                } while (c.moveToNext());
            } else {
                return salatModelList;
            }
        }
        c.close();
        db.close();
        return salatModelList;
    }

    public boolean updateSalatTracker(Banner model) {
        ContentValues cv = getContentValues(model);
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return false;
        int va = db.update(TBL_BANNER, cv, "id =" + model.getId(), null);
        db.close();
        if (va < -1)
            return false;
        else
            return true;
    }



    public  void deleteBannerData() {
        SQLiteDatabase db =this.databaseHelper.getReadableDatabase();


        if (db == null)
            return ;
        Cursor c = db.rawQuery("DELETE FROM " + TBL_BANNER, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {


                } while (c.moveToNext());
            } else {
                return ;
            }
        }
        c.close();
        db.close();


    }
}

