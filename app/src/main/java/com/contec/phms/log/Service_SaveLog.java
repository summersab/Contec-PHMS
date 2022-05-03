package com.contec.phms.log;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.contec.phms.log.SaveLog;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import org.dtools.ini.IniUtilities;

public class Service_SaveLog extends Service implements Runnable {
    protected static final String TAG = Service_SaveLog.class.getSimpleName();
    private static boolean runing = false;
    SaveLog.Stub mStub = new SaveLog.Stub() {
        public void startSaveLog() throws RemoteException {
            CLog.i(Service_SaveLog.TAG, "startSaveLog");
            Service_SaveLog.runing = true;
            Service_SaveLog.this.startSavelogThread();
        }

        public void StopSaveLog() throws RemoteException {
            CLog.i(Service_SaveLog.TAG, "StopSaveLog");
            Service_SaveLog.runing = false;
            Service_SaveLog.this.stopSelf();
        }
    };

    public IBinder onBind(Intent intent) {
        return this.mStub;
    }

    public void onCreate() {
        super.onCreate();
        runing = true;
        CLog.i(TAG, "onCreate");
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        CLog.i(TAG, "onstart");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        CLog.i(TAG, "onstartCommand");
        CLog.i("savelog", "保存日志的服务被打开了，可是线程回去执行吗？");
        startSavelogThread();
        return super.onStartCommand(intent, flags, startId);
    }

    public boolean onUnbind(Intent intent) {
        CLog.i(TAG, "onunbind");
        stopService(intent);
        return super.onUnbind(intent);
    }

    private void startSavelogThread() {
        CLog.i("savelog", "保存日志的服务，线程开始运行了");
        Thread _saveLogThread = new Thread(this);
        _saveLogThread.setName("save_log");
        _saveLogThread.start();
    }

    public static void stopServer(Context pContext) {
        runing = false;
        CLog.i("savelog", "保存日志的服务，线程结束了");
        pContext.stopService(new Intent(pContext, Service_SaveLog.class));
    }

    public void run() {
        String _path = String.valueOf(Constants.PATH_BASE) + "/phms/logs/";
        CLog.i(TAG, _path);
        String _logFileName = String.valueOf(PageUtil.getCurrentTime("yyyy-MM-dd hh-mm-ss")) + "_log.txt";
        try {
            PageUtil.isHavePath(_path);
            File _file = new File(String.valueOf(_path) + _logFileName);
            if (!_file.exists()) {
                _file.createNewFile();
            }
            CLog.e("lzerror", "service save log file success ");
            BufferedReader _BufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("logcat -v time *:v  ").getInputStream()));
            try {
                FileOutputStream _FileOutputStream = new FileOutputStream(_file);
                while (true) {
                    try {
                        String _line = _BufferedReader.readLine();
                        if (_line == null || !runing) {
                            _FileOutputStream.flush();
                            _FileOutputStream.close();
                            _BufferedReader.close();
                            FileOutputStream fileOutputStream = _FileOutputStream;
                            BufferedReader bufferedReader = _BufferedReader;
                        } else {
                            _FileOutputStream.write((String.valueOf(_line) + IniUtilities.NEW_LINE).getBytes());
                        }
                    } catch (Exception e) {
                        FileOutputStream fileOutputStream2 = _FileOutputStream;
                        BufferedReader bufferedReader2 = _BufferedReader;
                        return;
                    }
                }
                //_FileOutputStream.flush();
                //_FileOutputStream.close();
                //_BufferedReader.close();
                //FileOutputStream fileOutputStream3 = _FileOutputStream;
                //BufferedReader bufferedReader3 = _BufferedReader;
            } catch (Exception e2) {
                BufferedReader bufferedReader4 = _BufferedReader;
            }
        } catch (Exception e3) {
        }
    }
}
