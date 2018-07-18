package com.example.hannabotar.inventoryapp.converters;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;

/**
 * Created by hanna on 7/18/2018.
 */

public class Converters {
    @TypeConverter
    public BigDecimal fromDouble(double value) {
        return BigDecimal.valueOf(value);
    }

    @TypeConverter
    public double toDouble(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        } else {
            return bigDecimal.doubleValue();
        }
    }
}
