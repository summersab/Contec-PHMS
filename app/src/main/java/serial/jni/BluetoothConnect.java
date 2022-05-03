package serial.jni;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import com.contec.phms.device.pm10.ReceiveThread;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BluetoothConnect {
    public static final int MESSAGE_CONNECT_FAILED = 512;
    public static final int MESSAGE_CONNECT_INTERRUPTED = 768;
    public static final int MESSAGE_CONNECT_SUCCESS = 256;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Handler IOEHandler;
    private boolean IsIOE;
    private BroadcastReceiver PReceiver = new BroadcastReceiver() {
        String strPsw = "7762";

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
                BluetoothDevice btDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                try {
                    AutoPIN.setPin(btDevice.getClass(), btDevice, this.strPsw);
                    AutoPIN.createBond(btDevice.getClass(), btDevice);
                    AutoPIN.cancelPairingUserInput(btDevice.getClass(), btDevice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private SerialPort bluSerial;
    private Boolean iRead;
    private String mAddress;
    private ConnectThread mConnectThread = null;
    private ConnectedThread mConnectedThread = null;
    private Context mContext;
    private DataProcessing mDataProcess = null;
    private ConcurrentLinkedQueue<Byte> mEcgQueue;

    public BluetoothConnect(Context context, SerialPort bg, String address, Handler handler) {
        this.mContext = context;
        this.mAddress = address;
        this.bluSerial = bg;
        new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
        this.IOEHandler = handler;
        this.IsIOE = true;
    }

    public void Communicate() {
        this.mEcgQueue = new ConcurrentLinkedQueue<>();
        this.mConnectThread = new ConnectThread(this.mAddress);
        this.mConnectThread.start();
    }

    public void bluetoothDestroy() {
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectThread = null;
            this.mConnectedThread = null;
            this.mDataProcess = null;
            this.mEcgQueue.clear();
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothAdapter mmAdapter = BluetoothAdapter.getDefaultAdapter();
        private final BluetoothDevice mmDevice;
        private final BluetoothSocket mmSocket;

        public ConnectThread(String address) {
            this.mmDevice = this.mmAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            if (this.mmDevice.getBondState() != 12) {
                try {
                    AutoPIN.setPin(this.mmDevice.getClass(), this.mmDevice, "7762");
                    AutoPIN.createBond(this.mmDevice.getClass(), this.mmDevice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                tmp = this.mmDevice.createRfcommSocketToServiceRecord(BluetoothConnect.MY_UUID);
            } catch (IOException e2) {
            }
            this.mmSocket = tmp;
        }

        public void run() {
            try {
                this.mmSocket.connect();
                GLView.isGather = true;
                BluetoothConnect.this.IOEHandler.obtainMessage(256, -1, -1).sendToTarget();
                BluetoothConnect.this.mConnectedThread = new ConnectedThread(this.mmSocket);
                BluetoothConnect.this.iRead = true;
                BluetoothConnect.this.mConnectedThread.start();
            } catch (IOException e) {
                GLView.isGather = false;
                BluetoothConnect.this.IOEHandler.obtainMessage(512, -1, -1).sendToTarget();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private byte[] cmd_HOLD = new byte[2];
        private byte[] cmd_START = {-112, 1};
        private byte[] cmd_STOP;
        private int mTryCount;
        private InputStream mmInStream;
        private OutputStream mmOutStream;
        private BluetoothSocket mmSocket;

        public ConnectedThread(BluetoothSocket socket) {
            byte[] bArr = new byte[2];
            bArr[0] = -112;
            this.cmd_STOP = bArr;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            this.mmSocket = socket;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            this.mmInStream = tmpIn;
            this.mmOutStream = tmpOut;
        }

        public void run() {
            byte[] test = new byte[2048];
            write(this.cmd_STOP);
            write(this.cmd_STOP);
            delay(400);
            write(this.cmd_START);
            BluetoothConnect.this.mDataProcess = new DataProcessing(BluetoothConnect.this.mEcgQueue);
            BluetoothConnect.this.mDataProcess.start();
            BluetoothConnect.this.bluSerial.BGStart();
            int delay = 0;
            while (BluetoothConnect.this.iRead.booleanValue()) {
                int len = read(test);
                if (len > 2048) {
                    len = 2048;
                }
                for (int i = 0; i < len; i++) {
                    BluetoothConnect.this.mEcgQueue.offer(Byte.valueOf(test[i]));
                }
                delay++;
                if (delay == 2000) {
                    delay = 0;
                    write(this.cmd_HOLD);
                    if (this.mTryCount > 25000 && BluetoothConnect.this.IsIOE) {
                        this.mTryCount = 0;
                        IOECancel();
                        GLView.isGather = false;
                        BluetoothConnect.this.IOEHandler.obtainMessage(BluetoothConnect.MESSAGE_CONNECT_INTERRUPTED, -1, -1).sendToTarget();
                        BluetoothConnect.this.IsIOE = false;
                    }
                }
            }
        }

        public int read(byte[] buffer) {
            try {
                if (this.mmInStream.available() > 0) {
                    int len = this.mmInStream.read(buffer);
                    this.mTryCount = 0;
                    return len;
                }
                this.mTryCount++;
                return -1;
            } catch (IOException e) {
                if (BluetoothConnect.this.IsIOE) {
                    this.mTryCount = 0;
                    IOECancel();
                    GLView.isGather = false;
                    BluetoothConnect.this.IOEHandler.obtainMessage(BluetoothConnect.MESSAGE_CONNECT_INTERRUPTED, -1, -1).sendToTarget();
                    BluetoothConnect.this.IsIOE = false;
                }
                e.printStackTrace();
                return 0;
            }
        }

        public void write(byte[] buffer) {
            try {
                this.mmOutStream.write(buffer);
                this.mmOutStream.flush();
            } catch (IOException e) {
            }
        }

        public void cancel() {
            BluetoothConnect.this.iRead = false;
            BluetoothConnect.this.bluSerial.BGEnd();
            write(this.cmd_STOP);
            byte[] tmp = new byte[2];
            read(tmp);
            if (tmp[0] == -18 && tmp[1] == 16) {
                try {
                    delay(100);
                    this.mmSocket.close();
                } catch (IOException e) {
                }
            } else {
                write(this.cmd_STOP);
            }
        }

        public void IOECancel() {
            BluetoothConnect.this.iRead = false;
            BluetoothConnect.this.bluSerial.BGEnd();
        }

        public void delay(int length) {
            try {
                Thread.sleep((long) length);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class DataProcessing extends Thread {
        private byte REC_BATTERY = -78;
        private byte REC_ECGHEAD = ReceiveThread.e_back_dateresponse;
        private byte REC_OFFHEAD = -80;
        private byte REC_PACEHEAD = -79;
        private boolean bheader = false;
        private ConcurrentLinkedQueue<Byte> mDataQueue = null;
        private byte[] rawdata = new byte[16];

        public DataProcessing(ConcurrentLinkedQueue<Byte> mTmpQueue) {
            this.mDataQueue = mTmpQueue;
        }

        public void run() {
            while (BluetoothConnect.this.iRead.booleanValue()) {
                if (!this.bheader) {
                    read(this.rawdata, 0, 1);
                    if ((this.rawdata[0] & 240) >= 128) {
                        if (this.rawdata[0] == 238) {
                            read(this.rawdata, 0, 1);
                        }
                    }
                }
                if ((this.rawdata[0] & 240) == (this.REC_ECGHEAD & 240)) {
                    read(this.rawdata, 1, 15);
                    if ((this.rawdata[15] & 255) < 128 || BluetoothConnect.this.bluSerial.BGPreEcgDepackage(this.rawdata) == 0) {
                        this.bheader = false;
                    } else {
                        this.rawdata[0] = this.rawdata[15];
                        this.bheader = true;
                        BluetoothConnect.this.bluSerial.BGDataAdd();
                    }
                } else if ((this.rawdata[0] & 255) == (this.REC_OFFHEAD & 255)) {
                    read(this.rawdata, 1, 3);
                    if ((this.rawdata[3] & 255) < 128 || BluetoothConnect.this.bluSerial.BGLeadDepackage(this.rawdata) == 0) {
                        this.bheader = false;
                    } else {
                        this.rawdata[0] = this.rawdata[3];
                    }
                } else if ((this.rawdata[0] & 255) == this.REC_PACEHEAD) {
                    read(this.rawdata, 1, 2);
                    if (this.rawdata[2] < 128 || BluetoothConnect.this.bluSerial.BGPaceDepackage(this.rawdata) == 0) {
                        this.bheader = false;
                    } else {
                        this.rawdata[0] = this.rawdata[2];
                    }
                } else if (this.rawdata[0] == this.REC_BATTERY) {
                    read(this.rawdata, 1, 2);
                    if ((this.rawdata[2] & 255) < 128 || BluetoothConnect.this.bluSerial.BGBatteryDepackage(this.rawdata) == 0) {
                        this.bheader = false;
                    } else {
                        this.rawdata[0] = this.rawdata[2];
                    }
                } else {
                    this.bheader = false;
                }
            }
        }

        public void read(byte[] buffer, int start, int length) {
            int i = start;
            while (i < length + start) {
                if (!this.mDataQueue.isEmpty()) {
                    buffer[i] = this.mDataQueue.poll().byteValue();
                } else {
                    i--;
                }
                i++;
            }
        }
    }
}
