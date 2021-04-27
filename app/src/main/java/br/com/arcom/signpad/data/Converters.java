package br.com.arcom.signpad.data;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static LocalDateTime toDate(String dateString) {
        return (dateString == null) ? null : LocalDateTime.parse(dateString);
    }

    @TypeConverter
    public static String toDateString(LocalDateTime date) {
        return (date == null) ? null : date.toString();
    }
}
