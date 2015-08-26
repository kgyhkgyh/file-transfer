package com.kgyh.lamda;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Administrator on 2015/7/23.
 */
public class Stream {

    public static void main(String[] args) {
        List<Integer> nums = Lists.newArrayList(1, 1, null, 2, 3, 4, null, 5, 6, 7, 8, 9, 10);
        System.out.println("sum is:"+nums.stream().filter(num -> num != null).
            distinct().mapToInt(num -> num * 2).
                peek(System.out::println).skip(2).limit(4).sum());

        List<Integer> ints = Lists.newArrayList(1, 1, null, 2, 3, 4, null, 5, 6, 7, 8, 9, 10);

        System.out.println(ints.stream().filter(num -> num != null).distinct().reduce((sum, item) -> sum + item).get());
    }

}
