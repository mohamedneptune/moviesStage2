package com.udacity.moviestage2.data.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class DataConverter implements Serializable{
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    @TypeConverter // note this annotation
    public String fromOptionValuesList(List<ReviewEntry> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ReviewEntry>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<ReviewEntry> toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ReviewEntry>>() {
        }.getType();
        List<ReviewEntry> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }


}
