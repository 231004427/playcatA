package com.sunlin.playcat;

import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sunlin on 2017/7/2.
 */

public class RegistActivityTest
{
    @Test
    public void testAdd() {

        int i = 0;
        i = 4+4;
        System.out.print(".............. "+i);
        // 比较 i 是否 等于 8 ，相等的话通过测试！！！
        Assert.assertEquals(8, i);
    }
}
