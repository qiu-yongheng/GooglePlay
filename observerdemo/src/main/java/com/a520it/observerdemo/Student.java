package com.a520it.observerdemo;

import java.util.Observable;

/**
 * @author 邱永恒
 * @time 2016/8/26  0:18
 * @desc ${TODD}
 */
public class Student extends Observable{

    public void update(Observable o, Object arg) {
        Teacher t = (Teacher) o;

        String message = t.message;

        System.out.println(this.getClass().getSimpleName() + "收到:  " + message);
    }
}
