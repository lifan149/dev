package com.zhaohg.factory.demo3;

/**
 * 左偏分发型
 * @author Administrator
 */
public class LeftHair implements HairInterface {

    @Override
    public void draw() {
        System.out.println("-----------------左偏分发型-------------------");
    }

}