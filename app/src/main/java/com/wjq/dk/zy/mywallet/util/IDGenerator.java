package com.wjq.dk.zy.mywallet.util;

import java.util.UUID;

/**
 * Created by wangjiaqi on 16/11/10.
 */

public class IDGenerator {
    public static String generateID() {
        UUID uuID = UUID.randomUUID();
        return uuID.toString();
    }
}
