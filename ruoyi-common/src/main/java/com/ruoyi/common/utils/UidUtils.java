package com.ruoyi.common.utils;

import org.hashids.Hashids;

import java.util.UUID;

public class UidUtils {
    /**
     * 获取uuid
     *
     * @param isConcise
     * @return
     */
    public static String getUUID(boolean isConcise) {
        if (!isConcise) {
            return UUID.randomUUID().toString();
        }
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getInviteCode(Long userId) {
        final String SALT = "E5FCDG3HQA4B1NOPIJ2RSTUV67MWX89KLYZ";
        final int MIN_HASH_LENGTH = 6;
        Hashids hashids = new Hashids(SALT, MIN_HASH_LENGTH);
        return hashids.encode(userId);
    }

}
