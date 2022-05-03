package serial.jni;

import android.bluetooth.BluetoothDevice;
import java.lang.reflect.Method;

public class AutoPIN {
    public static boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        return ((Boolean) btClass.getMethod("createBond", new Class[0]).invoke(btDevice, new Object[0])).booleanValue();
    }

    public static boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        return ((Boolean) btClass.getMethod("removeBond", new Class[0]).invoke(btDevice, new Object[0])).booleanValue();
    }

    public static boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
        try {
            Boolean bool = (Boolean) btClass.getDeclaredMethod("setPin", new Class[]{byte[].class}).invoke(btDevice, new Object[]{str.getBytes()});
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return true;
    }

    public static boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput", new Class[0]);
        cancelBondProcess(btClass, device);
        return ((Boolean) createBondMethod.invoke(device, new Object[0])).booleanValue();
    }

    public static boolean cancelBondProcess(Class btClass, BluetoothDevice device) throws Exception {
        return ((Boolean) btClass.getMethod("cancelBondProcess", new Class[0]).invoke(device, new Object[0])).booleanValue();
    }
}
