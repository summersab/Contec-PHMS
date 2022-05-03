package u.aly;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.microedition.khronos.opengles.GL10;

public class bs {
    protected static final String a = bs.class.getName();
    public static final String b = "";
    public static final String c = "2G/3G";
    public static final String d = "Wi-Fi";
    public static final int e = 8;

    public static boolean a(Context context) {
        return context.getResources().getConfiguration().locale.toString().equals(Locale.CHINA.toString());
    }

    public static boolean b(Context context) {
        if (context.getResources().getConfiguration().orientation == 1) {
            return true;
        }
        return false;
    }

    public static String c(Context context) {
        try {
            return String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            return b;
        }
    }

    public static String d(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return b;
        }
    }

    public static String e(Context context) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = context.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : b);
    }

    public static String[] a(GL10 gl10) {
        try {
            return new String[]{gl10.glGetString(7936), gl10.glGetString(7937)};
        } catch (Exception e) {
            bt.b(a, "Could not read gpu infor:", e);
            return new String[0];
        }
    }

    public static String a() {
        String str = null;
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            if (fileReader != null) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(fileReader, 1024);
                    str = bufferedReader.readLine();
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException e) {
                    bt.b(a, "Could not read from file /proc/cpuinfo", e);
                }
            }
        } catch (FileNotFoundException e2) {
            bt.b(a, "Could not open file /proc/cpuinfo", e2);
        }
        if (str != null) {
            return str.substring(str.indexOf(58) + 1).trim();
        }
        return b;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String f(android.content.Context r4) {
        /*
            java.lang.String r0 = "phone"
            java.lang.Object r0 = r4.getSystemService(r0)
            android.telephony.TelephonyManager r0 = (android.telephony.TelephonyManager) r0
            if (r0 != 0) goto L_0x0011
            java.lang.String r1 = a
            java.lang.String r2 = "No IMEI."
            u.aly.bt.e(r1, r2)
        L_0x0011:
            java.lang.String r1 = ""
            java.lang.String r2 = "android.permission.READ_PHONE_STATE"
            boolean r2 = a((android.content.Context) r4, (java.lang.String) r2)     // Catch:{ Exception -> 0x005c }
            if (r2 == 0) goto L_0x0064
            java.lang.String r0 = r0.getDeviceId()     // Catch:{ Exception -> 0x005c }
        L_0x001f:
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x005b
            java.lang.String r0 = a
            java.lang.String r1 = "No IMEI."
            u.aly.bt.e(r0, r1)
            java.lang.String r0 = p(r4)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x005b
            java.lang.String r0 = a
            java.lang.String r1 = "Failed to take mac as IMEI. Try to use Secure.ANDROID_ID instead."
            u.aly.bt.e(r0, r1)
            android.content.ContentResolver r0 = r4.getContentResolver()
            java.lang.String r1 = "android_id"
            java.lang.String r0 = android.provider.Settings.Secure.getString(r0, r1)
            java.lang.String r1 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "getDeviceId: Secure.ANDROID_ID: "
            r2.<init>(r3)
            java.lang.StringBuilder r2 = r2.append(r0)
            java.lang.String r2 = r2.toString()
            u.aly.bt.a(r1, r2)
        L_0x005b:
            return r0
        L_0x005c:
            r0 = move-exception
            java.lang.String r2 = a
            java.lang.String r3 = "No IMEI."
            u.aly.bt.e(r2, r3, r0)
        L_0x0064:
            r0 = r1
            goto L_0x001f
        */
        throw new UnsupportedOperationException("Method not decompiled: u.aly.C1522bs.f(android.content.Context):java.lang.String");
    }

    public static boolean b() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public static int m(Context context) {
        try {
            Calendar instance = Calendar.getInstance(x(context));
            if (instance != null) {
                return instance.getTimeZone().getRawOffset() / 3600000;
            }
        } catch (Exception e) {
            bt.a(a, "error in getTimeZone", e);
        }
        return 8;
    }

    public static String[] n(Context context) {
        String[] strArr = new String[2];
        try {
            Locale x = x(context);
            if (x != null) {
                strArr[0] = x.getCountry();
                strArr[1] = x.getLanguage();
            }
            if (TextUtils.isEmpty(strArr[0])) {
                strArr[0] = "Unknown";
            }
            if (TextUtils.isEmpty(strArr[1])) {
                strArr[1] = "Unknown";
            }
        } catch (Exception e) {
            bt.b(a, "error in getLocaleInfo", e);
        }
        return strArr;
    }

    private static Locale x(Context context) {
        Locale locale = null;
        try {
            Configuration configuration = new Configuration();
            configuration.setToDefaults();
            Settings.System.getConfiguration(context.getContentResolver(), configuration);
            if (configuration != null) {
                locale = configuration.locale;
            }
        } catch (Exception e) {
            bt.b(a, "fail to read user config locale");
        }
        if (locale == null) {
            return Locale.getDefault();
        }
        return locale;
    }

    private static int a(Object obj, String str) {
        try {
            Field declaredField = DisplayMetrics.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField.getInt(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String a(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
    }

    public static String c() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    public static Date a(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static int a(Date date, Date date2) {
        if (!date.after(date2)) {
            Date date3 = date2;
            date2 = date;
            date = date3;
        }
        return (int) ((date.getTime() - date2.getTime()) / 1000);
    }

    public static String u(Context context) {
        return context.getPackageName();
    }

    public static String v(Context context) {
        return context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();
    }

    public static boolean w(Context context) {
        try {
            return (context.getApplicationInfo().flags & 2) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
