package com.contec.jar.cms50k;

import java.util.ArrayList;

public class DeviceDataSPO2H {
    public int MovementEnd;
    public byte[] MovementPoint;
    public int MovementStart;
    public int[] MovementTime = new int[6];
    public byte[] PulseSegment;
    public int[] PulseTime = new int[6];
    public byte[] RRPoint;
    public int[] RrTime = new int[6];
    public ArrayList<Object> Spo2Point = new ArrayList<>();
    public byte[] Spo2Segment;
    public int[] Spo2Time = new int[6];
    public byte[] mCodedata;
}
