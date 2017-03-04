package com.wjq.dk.zy.mywallet.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by wangjiaqi on 16/12/12.
 */

public class NumberFormatUtil {
    public static String format(String number){
        BigDecimal bigDecimal = new BigDecimal(number);
        DecimalFormat format = new DecimalFormat("0.0");
        return format.format(bigDecimal.doubleValue());
    }
}
