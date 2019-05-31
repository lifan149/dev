package com.zhaohg.leetcode.medium;

import org.junit.Test;

import java.util.List;

public class TopKFrequentElementsTest {
    @Test
    public void topKFrequent() throws Exception {
        int[] a = {1, 1, 1, 2, 2, 3, 3, 3};
        List<Integer> integers = new TopKFrequentElements().topKFrequent(a, 2);
        System.out.println(integers);
    }

    @Test
    public void topKFrequent2() throws Exception {
        int[] a = {1, 1, 1, 2, 2, 2, 3, 3, 3};
        List<Integer> integers = new TopKFrequentElements().topKFrequent(a, 2);
        System.out.println(integers);
    }

    @Test
    public void topKFrequent3() throws Exception {
        int[] a = {1, 1, 1, 2, 2, 3};
        List<Integer> integers = new TopKFrequentElements().topKFrequent(a, 2);
        System.out.println(integers);
    }

    @Test
    public void topKFrequent4() throws Exception {
        int[] a = {1, 1, 1};
        List<Integer> integers = new TopKFrequentElements().topKFrequent(a, 2);
        System.out.println(integers);
    }

}