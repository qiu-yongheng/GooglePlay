package com.a520it.observerdemo;

import java.util.Observable;

/**
 * @author 邱永恒
 * @time 2016/8/26  0:18
 * @desc ${TODD}
 */
public class Teacher extends Observable{

    public String message;

    //发布消息
    public void publishMessage(String message) {
        this.message = message;
        System.out.println("老师说: " + message);
    }
}
