package com.kgyh.lamda;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2015/7/23.
 */
public class LamdaTest1 {

    public static void main(String[] args) {
        List<Integer> list = Lists.newArrayList();
        list.add(2);
        list.add(3);
        list.add(1);
        list.add(4);
        Collections.sort(list, (o1, o2) -> o1.compareTo(o2));
        System.out.println(list);
    }

}
