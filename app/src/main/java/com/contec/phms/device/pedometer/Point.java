package com.contec.phms.device.pedometer;

public class Point {
    public int sqrt = ((int) Math.sqrt((double) (((this.x * this.x) + (this.y * this.y)) + (this.z * this.z))));
    public long time;
    public float x;
    public float y;
    public float z;

    public Point(float[] value, long pTime) {
        this.x = value[0];
        this.y = value[1];
        this.z = value[2];
        this.time = pTime;
    }
}
