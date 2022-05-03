package com.contec.jar.pair;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClsUtils {
    public static boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        return ((Boolean) btClass.getMethod("createBond", new Class[0]).invoke(btDevice, new Object[0])).booleanValue();
    }

    public static boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        return ((Boolean) btClass.getMethod("removeBond", new Class[0]).invoke(btDevice, new Object[0])).booleanValue();
    }

    public static boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
        try {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
            Object[] objArr = {str.getBytes()};
            Log.d("returnValue", new StringBuilder().append((Boolean) removeBondMethod.invoke(btDevice, objArr)).toString());
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
        return ((Boolean) btClass.getMethod("cancelPairingUserInput", new Class[0]).invoke(device, new Object[0])).booleanValue();
    }

    public static boolean cancelBondProcess(Class btClass, BluetoothDevice device) throws Exception {
        return ((Boolean) btClass.getMethod("cancelBondProcess", new Class[0]).invoke(device, new Object[0])).booleanValue();
    }

    public static void printAllInform(Class clsShow) {
        try {
            Method[] hideMethod = clsShow.getMethods();
            for (int i = 0; i < hideMethod.length; i++) {
                Log.e("method name", String.valueOf(hideMethod[i].getName()) + ";and the i is:" + i);
            }
            Field[] allFields = clsShow.getFields();
            for (Field name : allFields) {
                Log.e("Field name", name.getName());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static boolean pair(String strAddr, String strPsw) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) {
            Log.d("mylog", "devAdd un effient!");
        }
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
        if (device.getBondState() != 12) {
            try {
                Log.d("mylog", "NOT BOND_BONDED");
                setPin(device.getClass(), device, strPsw);
                createBond(device.getClass(), device);
                return true;
            } catch (Exception e) {
                Log.d("mylog", "setPiN failed!");
                e.printStackTrace();
                return false;
            }
        } else {
            Log.d("mylog", "HAS BOND_BONDED");
            try {
                createBond(device.getClass(), device);
                setPin(device.getClass(), device, strPsw);
                createBond(device.getClass(), device);
                return true;
            } catch (Exception e2) {
                Log.d("mylog", "setPiN failed!");
                e2.printStackTrace();
                return false;
            }
        }
    }
}
