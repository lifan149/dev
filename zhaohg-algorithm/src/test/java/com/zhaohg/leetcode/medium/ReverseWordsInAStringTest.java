package com.zhaohg.leetcode.medium;

import org.junit.Assert;
import org.junit.Test;

public class ReverseWordsInAStringTest {
    @Test
    public void reverseWords() throws Exception {
        String string = "the sky is blue";
        ReverseWordsInAString rever = new ReverseWordsInAString();
        String s = rever.reverseWords(string);
        Assert.assertEquals("blue is sky the", s);
    }

    @Test
    public void reverseWords2() throws Exception {
        String string = "a";
        ReverseWordsInAString rever = new ReverseWordsInAString();
        String s = rever.reverseWords(string);
        Assert.assertEquals("a", s);
    }

    @Test
    public void reverseWords3() throws Exception {
        String string = "   a   b ";
        ReverseWordsInAString rever = new ReverseWordsInAString();
        String s = rever.reverseWords(string);
        Assert.assertEquals("b a", s);
    }

    @Test
    public void reverseWords4() throws Exception {
        String string = " ";
        ReverseWordsInAString rever = new ReverseWordsInAString();
        String s = rever.reverseWords(string);
        Assert.assertEquals("", s);
    }

    @Test
    public void reverseWords5() throws Exception {
        String string = "";
        ReverseWordsInAString rever = new ReverseWordsInAString();
        String s = rever.reverseWords(string);
        Assert.assertEquals("", s);
    }

}