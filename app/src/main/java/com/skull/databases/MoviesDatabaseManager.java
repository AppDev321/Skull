package com.skull.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.webservices.models.MovieByCategory;

import java.util.ArrayList;
import java.util.List;

import static com.skull.databases.BannerDatabaseManager.TBL_BANNER;

/**
 * Created by Administrator on 3/17/2017.
 */

public class MoviesDatabaseManager {

    public static final String TBL_MOVIES = "movies";


    private DataBaseHelper databaseHelper;


    private Context mContext;

    public MoviesDatabaseManager(Context context) {
        this.databaseHelper = new DataBaseHelper(context);
        this.mContext = context;
    }

    /// **********************  commmon used methods
    public ContentValues getContentValues(MovieByCategory model) {
        ContentValues cv = new ContentValues();
        cv.put("id", model.getId());
        cv.put("name", model.getName());
        cv.put("image", model.getImage());
        cv.put("year", model.getYear());
        cv.put("rate", model.getRate());
        cv.put("category_id", model.getCategoryId());
        cv.put("director", model.getDirector());
        cv.put("type", model.getType());
        cv.put("video_link", model.getVideoLink());
        cv.put("is_youtube", model.getIsYoutube());
        cv.put("prod", model.getProd());
        cv.put("writer", model.getWriter());
        cv.put("countries", model.getCountries());
        cv.put("dur", model.getDur());
        cv.put("rate2", model.getRate2());
        cv.put("cov", model.getCov());
        cv.put("plot", model.getPlot());
        cv.put("plotout", model.getPlotout());
        cv.put("cast", model.getCast());

        return cv;
    }

    public MovieByCategory getCursorModel(Cursor c) {
        MovieByCategory model = new MovieByCategory();
        model.setId(c.getInt(c.getColumnIndex("id")));
        model.setName(c.getString(c.getColumnIndex("name")));
        model.setImage(c.getString(c.getColumnIndex("image")));
        model.setYear(c.getInt(c.getColumnIndex("year")));
        model.setRate(c.getInt(c.getColumnIndex("rate")));
        model.setCategoryId(c.getInt(c.getColumnIndex("category_id")));
        model.setType(c.getInt(c.getColumnIndex("type")));
        model.setVideoLink(c.getString(c.getColumnIndex("video_link")));
        model.setIsYoutube(c.getInt(c.getColumnIndex("is_youtube")));
        model.setDirector(c.getString(c.getColumnIndex("director")));
        model.setProd(c.getString(c.getColumnIndex("prod")));
        model.setWriter(c.getString(c.getColumnIndex("writer")));
        model.setCountries(c.getString(c.getColumnIndex("countries")));
        model.setDur(c.getInt(c.getColumnIndex("dur")));
        model.setRate2(c.getString(c.getColumnIndex("rate2")));
        model.setCov(c.getString(c.getColumnIndex("cov")));
        model.setPlot(c.getString(c.getColumnIndex("plot")));
        model.setPlotout(c.getString(c.getColumnIndex("plotout")));
        model.setCast(c.getString(c.getColumnIndex("cast")));

        return model;
    }


//*********************************************************************888  Close Database

    public boolean insertMoviesData(MovieByCategory model) {


        if (getMovieById(model.getId()) != null) {

            return updateSalatTracker(model);
        } else {
            //insert new row
            SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
            if (db == null)
                return false;

            long id = db.insert(TBL_MOVIES, null, getContentValues(model));
            db.close();
            if (id > -1) {
                return true;
            } else {
                return false;
            }
        }
    }


    public List<MovieByCategory> getBannerMovies() {
        List<MovieByCategory> salatModelList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_MOVIES + " ORDER BY RANDOM() limit 6", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    salatModelList.add(getCursorModel(c));
                } while (c.moveToNext());
            } else {
                return null;
            }
        }
        c.close();
        db.close();
        return salatModelList;
    }



    public MovieByCategory getMovieById(int id) {
        MovieByCategory salatModelList = new MovieByCategory();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_MOVIES + " where id = " + id, null);
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


    public List<MovieByCategory> getMovieByCategory(int catId) {
        List<MovieByCategory> salatModelList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_MOVIES + " where category_id = " + catId+" ORDER BY RANDOM()", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    salatModelList.add(getCursorModel(c));
                } while (c.moveToNext());
            } else {
                return null;
            }
        }
        c.close();
        db.close();
        return salatModelList;
    }



    public List<MovieByCategory> getDiscoverCatMovies(int catId) {
        List<MovieByCategory> salatModelList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_MOVIES + " where category_id = " + catId +" ORDER BY RANDOM() limit 2", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    salatModelList.add(getCursorModel(c));
                } while (c.moveToNext());
            } else {
                return null;
            }
        }
        c.close();
        db.close();
        return salatModelList;
    }


    public List<MovieByCategory> getAlsoLikedMovies(int catId) {
        List<MovieByCategory> salatModelList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_MOVIES + " where category_id = " + catId +" ORDER BY RANDOM() Limit 5", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    salatModelList.add(getCursorModel(c));
                } while (c.moveToNext());
            } else {
                return salatModelList;
            }
        }
        c.close();
        db.close();
        return salatModelList;
    }



    public List<MovieByCategory> getSearchMoviesList(String movieName) {
        List<MovieByCategory> salatModelList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_MOVIES + " where name like '%" + movieName +"%'", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    salatModelList.add(getCursorModel(c));
                } while (c.moveToNext());
            } else {
                return salatModelList;
            }
        }
        c.close();
        db.close();
        return salatModelList;
    }
    public boolean updateSalatTracker(MovieByCategory model) {
        ContentValues cv = getContentValues(model);
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        if (db == null)
            return false;
        int va = db.update(TBL_MOVIES, cv, "id =" + model.getId(), null);
        db.close();
        if (va < -1)
            return false;
        else
            return true;
    }

    public void deleteMoviesData() {
         SQLiteDatabase db =this.databaseHelper.getReadableDatabase();


        if (db == null)
            return ;
        Cursor c = db.rawQuery("DELETE FROM " + TBL_MOVIES, null);
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

