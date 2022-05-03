package com.contec.phms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.contec.phms.R;
import com.contec.phms.util.CLog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class RegisterPhoneCityDBManager {
    public static final String DB_NAME = "city_cn.s3db";
    public static final String DB_PATH = ("/data" + Environment.getDataDirectory().getAbsolutePath() + CookieSpec.PATH_DELIM + "com.contec.phms");
    public static final String PACKAGE_NAME = "com.contec.phms";
    private final int BUFFER_SIZE = 1024;
    private File file = null;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public RegisterPhoneCityDBManager(Context mContext2) {
        this.mContext = mContext2;
    }

    public void openDatabase() {
        CLog.e("cc", "openDatabase()");
        this.mDatabase = openDatabase(String.valueOf(DB_PATH) + CookieSpec.PATH_DELIM + DB_NAME);
    }

    public SQLiteDatabase getmDatabase() {
        CLog.e("cc", "getmDatabase()");
        return this.mDatabase;
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            CLog.e("cc", "open and return");
            this.file = new File(dbfile);
            if (!this.file.exists()) {
                CLog.e("cc", "file");
                InputStream is = this.mContext.getResources().openRawResource(R.raw.city);
                if (is != null) {
                    CLog.e("cc", "is null");
                }
                FileOutputStream fos = new FileOutputStream(dbfile);
                if (is != null) {
                    CLog.e("cc", "fos null");
                }
                byte[] buffer = new byte[1024];
                while (true) {
                    int count = is.read(buffer);
                    if (count <= 0) {
                        break;
                    }
                    fos.write(buffer, 0, count);
                    CLog.e("cc", "while");
                    fos.flush();
                }
                fos.close();
                is.close();
            }
            this.mDatabase = SQLiteDatabase.openOrCreateDatabase(dbfile, (SQLiteDatabase.CursorFactory) null);
            return this.mDatabase;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public void closeDatabase() {
        CLog.e("cc", "closeDatabase()");
        if (this.mDatabase != null) {
            this.mDatabase.close();
        }
    }
}
