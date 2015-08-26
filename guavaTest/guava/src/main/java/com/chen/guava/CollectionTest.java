package com.chen.guava;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;


/**
 * Created by Administrator on 2015/7/4.
 */
public class CollectionTest {


    public static void main(String[] args) {
        List<String> stringList = Lists.newLinkedList();
        stringList.add("abc");
        stringList.add("dbf");
        stringList.add("ghi");
        System.out.println(stringList.toString());

        Iterable<String> iterable = Iterables.concat(stringList,stringList);
        System.out.println(iterable.toString());
    }


}
