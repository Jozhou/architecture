package com.architecture.utils;

import java.text.DecimalFormat;

/**
 * Created by Ucar on 2016/8/24.
 */
public class NumberUtils {

    public static final String parseMoney(double d){
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(d);
    }
}
