package com.contec.jar.cms50ew;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class SntpClient {
    private static final int NTP_MODE_CLIENT = 3;
    private static final int NTP_PACKET_SIZE = 48;
    private static final int NTP_PORT = 123;
    private static final int NTP_VERSION = 3;
    private static final long OFFSET_1900_TO_1970 = 2208988800L;
    private static final int ORIGINATE_TIME_OFFSET = 24;
    private static final int RECEIVE_TIME_OFFSET = 32;
    private static final int TRANSMIT_TIME_OFFSET = 40;
    private long mNtpTime;
    private long mNtpTimeReference;
    private long mRoundTripTime;

    SntpClient() {
    }

    public boolean requestTime(String host, int timeout) {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
            byte[] buffer = new byte[48];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(host), NTP_PORT);
            buffer[0] = 27;
            long requestTime = System.currentTimeMillis();
            long requestTicks = System.nanoTime() / 1000;
            writeTimeStamp(buffer, TRANSMIT_TIME_OFFSET, requestTime);
            socket.send(request);
            socket.receive(new DatagramPacket(buffer, buffer.length));
            long responseTicks = System.nanoTime() / 1000;
            long responseTime = requestTime + (responseTicks - requestTicks);
            socket.close();
            long originateTime = readTimeStamp(buffer, 24);
            long receiveTime = readTimeStamp(buffer, 32);
            long transmitTime = readTimeStamp(buffer, TRANSMIT_TIME_OFFSET);
            this.mNtpTime = responseTime + (((receiveTime - originateTime) + (transmitTime - responseTime)) / 2);
            this.mNtpTimeReference = responseTicks;
            this.mRoundTripTime = (responseTicks - requestTicks) - (transmitTime - receiveTime);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getNtpTime() {
        return this.mNtpTime;
    }

    public long getNtpTimeReference() {
        return this.mNtpTimeReference;
    }

    public long getRoundTripTime() {
        return this.mRoundTripTime;
    }

    private long read32(byte[] buffer, int offset) {
        int i0;
        int i1;
        int i2;
        int i3;
        byte b0 = buffer[offset];
        byte b1 = buffer[offset + 1];
        byte b2 = buffer[offset + 2];
        byte b3 = buffer[offset + 3];
        if ((b0 & 128) == 128) {
            i0 = (b0 & Byte.MAX_VALUE) + 128;
        } else {
            i0 = b0;
        }
        if ((b1 & 128) == 128) {
            i1 = (b1 & Byte.MAX_VALUE) + 128;
        } else {
            i1 = b1;
        }
        if ((b2 & 128) == 128) {
            i2 = (b2 & Byte.MAX_VALUE) + 128;
        } else {
            i2 = b2;
        }
        if ((b3 & 128) == 128) {
            i3 = (b3 & Byte.MAX_VALUE) + 128;
        } else {
            i3 = b3;
        }
        return (((long) i0) << 24) + (((long) i1) << 16) + (((long) i2) << 8) + ((long) i3);
    }

    private long readTimeStamp(byte[] buffer, int offset) {
        return ((read32(buffer, offset) - OFFSET_1900_TO_1970) * 1000) + ((1000 * read32(buffer, offset + 4)) / 4294967296L);
    }

    private void writeTimeStamp(byte[] buffer, int offset, long time) {
        long seconds = time / 1000;
        long milliseconds = time - (1000 * seconds);
        long seconds2 = seconds + OFFSET_1900_TO_1970;
        int offset2 = offset + 1;
        buffer[offset] = (byte) ((int) (seconds2 >> 24));
        int offset3 = offset2 + 1;
        buffer[offset2] = (byte) ((int) (seconds2 >> 16));
        int offset4 = offset3 + 1;
        buffer[offset3] = (byte) ((int) (seconds2 >> 8));
        int offset5 = offset4 + 1;
        buffer[offset4] = (byte) ((int) (seconds2 >> 0));
        long fraction = (4294967296L * milliseconds) / 1000;
        int offset6 = offset5 + 1;
        buffer[offset5] = (byte) ((int) (fraction >> 24));
        int offset7 = offset6 + 1;
        buffer[offset6] = (byte) ((int) (fraction >> 16));
        int offset8 = offset7 + 1;
        buffer[offset7] = (byte) ((int) (fraction >> 8));
        int i = offset8 + 1;
        buffer[offset8] = (byte) ((int) (Math.random() * 255.0d));
    }
}
