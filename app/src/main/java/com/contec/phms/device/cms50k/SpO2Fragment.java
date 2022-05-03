package com.contec.phms.device.cms50k;

import java.io.Serializable;

public class SpO2Fragment implements Serializable {
    private static final long serialVersionUID = 1;
    public int MovementEnd;
    public byte[] MovementPoint;
    public int MovementStart;
    public int[] MovementTime = new int[6];
    public byte[] PulseSegment;
    public int[] PulseTime = new int[6];
    public byte[] Spo2Segment;
    public int[] Spo2Time = new int[6];
    public byte[] mCode;
}
