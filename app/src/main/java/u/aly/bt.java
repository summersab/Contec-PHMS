package u.aly;

import android.util.Log;

public class bt {

    public static boolean a = false;

    public static void a(String str, String str2) {
        if (a) {
            Log.i(str, str2);
        }
    }

    public static void a(String str, String str2, Exception exc) {
        if (a) {
            Log.i(str, String.valueOf(exc.toString()) + ":  [" + str2 + "]");
        }
    }

    public static void b(String str, String str2) {
        if (a) {
            Log.e(str, str2);
        }
    }

    public static void b(String str, String str2, Exception exc) {
        if (a) {
            Log.e(str, String.valueOf(exc.toString()) + ":  [" + str2 + "]");
            StackTraceElement[] stackTrace = exc.getStackTrace();
            int length = stackTrace.length;
            for (int i = 0; i < length; i++) {
                Log.e(str, "        at\t " + stackTrace[i].toString());
            }
        }
    }

    public static void c(String str, String str2) {
        if (a) {
            Log.d(str, str2);
        }
    }

    public static void c(String str, String str2, Exception exc) {
        if (a) {
            Log.d(str, String.valueOf(exc.toString()) + ":  [" + str2 + "]");
        }
    }

    public static void d(String str, String str2) {
        if (a) {
            Log.v(str, str2);
        }
    }

    public static void d(String str, String str2, Exception exc) {
        if (a) {
            Log.v(str, String.valueOf(exc.toString()) + ":  [" + str2 + "]");
        }
    }

    public static void e(String str, String str2) {
        if (a) {
            Log.w(str, str2);
        }
    }

    public static void e(String str, String str2, Exception exc) {
        if (a) {
            Log.w(str, String.valueOf(exc.toString()) + ":  [" + str2 + "]");
            StackTraceElement[] stackTrace = exc.getStackTrace();
            int length = stackTrace.length;
            for (int i = 0; i < length; i++) {
                Log.w(str, "        at\t " + stackTrace[i].toString());
            }
        }
    }
}
