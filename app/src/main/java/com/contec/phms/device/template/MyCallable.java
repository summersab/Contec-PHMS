package com.contec.phms.device.template;

import android.bluetooth.BluetoothSocket;
import com.contec.phms.util.CLog;
import java.io.IOException;
import java.util.concurrent.Callable;

public class MyCallable implements Callable<BluetoothSocket> {
    BluetoothSocket mSocket;

    MyCallable(BluetoothSocket p_BluetoothSocket) {
        this.mSocket = p_BluetoothSocket;
    }

    public BluetoothSocket call() throws Exception {
        Thread.sleep(100);
        try {
            this.mSocket.connect();
            return this.mSocket;
        } catch (Exception e) {
            try {
                this.mSocket.close();
                this.mSocket = null;
                CLog.e("BTConnect", "Connect Device Failed  \tmSocket.close()!");
                return this.mSocket;
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }
}
