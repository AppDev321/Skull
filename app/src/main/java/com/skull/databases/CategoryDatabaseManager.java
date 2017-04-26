package com.skull.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.webservices.models.Category;

import java.util.ArrayList;
import java.util.List;

import static com.skull.databases.BannerDatabaseManager.TBL_BANNER;

/**
 * Created by Administrator on 3/17/2017.
 */

public class CategoryDatabaseManager {

    public static final String TBL_CATEGORY = "category";
    private SQLiteDatabase db;

    private DataBaseHelper databaseHelper;


    private Context mContext;

    public CategoryDatabaseManager(Context context) {
        this.databaseHelper = new DataBaseHelper(context);
        this.mContext = context;
    }

    /// **********************  commmon used methods
    public ContentValues getContentValues(Category model) {
        ContentValues cv = new ContentValues();
        cv.put("id", model.getId());
        cv.put("name", model.getName());
        cv.put("image", model.getImage());
        cv.put("is_discover", model.getIsDiscover());
        return cv;
    }

    public Category getCursorModel(Cursor c) {
        Category model = new Category();
        model.setId(c.getInt(c.getColumnIndex("id")));
        model.setName(c.getString(c.getColumnIndex("name")));
        model.setImage(c.getString(c.getColumnIndex("image")));
        model.setIsDiscover(c.getInt(c.getColumnIndex("is_discover")));
        return model;
    }


//*********************************************************************888  Close Database

    public boolean insertCategoryData(Category model) {


        if (getCategoryById(model.getId()) != null) {
            return updateSalatTracker(model);
        } else {
            //insert new row
            SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
            if (db == null)
                return false;

            long id = db.insert(TBL_CATEGORY, null, getContentValues(model));
            db.close();
            if (id > -1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Category getCategoryById(int id) {
        Category salatModelList = new Category();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_CATEGORY + " where id = " + id , null);
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


    public List<Category> getBrowseCategories() {
        List<Category> salatModelList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_CATEGORY +" where is_discover = 0 ORDER BY RANDOM()", null);
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


    public List<Category> getDiscoverCategories() {
        List<Category> salatModelList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_CATEGORY +" where is_discover = 1 ORDER BY RANDOM()", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    salatModelList.add( getCursorModel(c));
                } while (c.moveToNext());
            } else {
                return salatModelList;
            }
        }
        c.close();
        db.close();
        return salatModelList;
    }




    public boolean updateSalatTracker(Category model) {
        ContentValues cv = getContentValues(model);
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return false;
        int va = db.update(TBL_CATEGORY, cv, "id =" + model.getId(), null);
        db.close();
        if (va < -1)
            return false;
        else
            return true;
    }
    public  void deleteCategoryData() {
        SQLiteDatabase db =this.databaseHelper.getReadableDatabase();


        if (db == null)
            return ;
        Cursor c = db.rawQuery("DELETE FROM " + TBL_CATEGORY, null);
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

