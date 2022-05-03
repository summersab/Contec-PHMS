package cn.com.contec.jar.cases;

import java.io.OutputStream;
import java.io.Serializable;

public class SerialNumber implements Serializable {
    private static final long serialVersionUID = 1;
    public byte[] mSerial;

    public SerialNumber() {
        init();
    }

    public void init() {
        this.mSerial = new byte[20];
    }

    public void save(OutputStream out) {
        SaveHelper.saveByteArray(out, this.mSerial);
    }
}
