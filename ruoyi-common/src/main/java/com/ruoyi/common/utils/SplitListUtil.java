package com.ruoyi.common.utils;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :   Tizzy <br/>
 * @version :   1.0
 * @description :  切割list
 * @date 2019/9/12
 */

@Slf4j
public class SplitListUtil {


    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @param source 原集合
     * @param n   表示切割的份数
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {

        log.info(" 切割的元数据  大小  {}  ", source.size());

        List<List<T>> result = new ArrayList<List<T>>();

        int remaider = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量

        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }

        log.info("  切割完后的数据为 ：   {} ", result);

        return result;
    }

    public static void main(String[] args) {


        List<Integer>  lsit = new ArrayList<>();
        lsit.add(0);
        lsit.add(10);
        lsit.add(20);
        lsit.add(30);
        lsit.add(40);
        lsit.add(50);
        lsit.add(60);
        lsit.add(70);
        lsit.add(41);
        lsit.add(71);
        lsit.add(71);

        //分成两份
        List<List<Integer>> lists = averageAssign(lsit, 2);

        System.out.println(lists);

    }

}





