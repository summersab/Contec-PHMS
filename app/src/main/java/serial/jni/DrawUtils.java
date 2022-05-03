package serial.jni;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class DrawUtils {
    private static float ScaleFactor = 1.0f;
    public static float[] coord = {0.0f, ScaleFactor * 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    private static float displayGain = 0.00111f;
    private static final float displayGain10 = 0.00222f;
    private static final float displayGain20 = 0.00444f;
    private static final float displayGain5 = 0.00111f;
    private static int displayMode = 0;
    private static int displayMode2x6 = 0;
    private static float displaySpeed = 0.014f;
    private static final float displaySpeed125 = 0.007f;
    private static final float displaySpeed25 = 0.014f;
    private static final float displaySpeed50 = 0.028f;
    private static float fHeight = 0.6f;
    private static boolean fontChanged = false;
    private static boolean[] fontFlag = null;
    private static final float hScale = 0.014f;
    private static float revise_Y = 0.65f;
    private static int rhythmIndex = 0;
    private static float unit = 0.41f;
    private static final float vScale = 0.00111f;
    public static float[] vertex26_1 = {-10.5f, (11.0f * unit) - revise_Y, 0.0f, -9.4f, (11.0f * unit) - revise_Y, 0.0f, -10.5f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f, -9.4f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex26_2 = {-10.5f, (7.0f * unit) - revise_Y, 0.0f, -9.3f, (7.0f * unit) - revise_Y, 0.0f, -10.5f, ((7.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((7.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex26_3 = {-10.5f, (3.0f * unit) - revise_Y, 0.0f, -9.3f, (3.0f * unit) - revise_Y, 0.0f, -10.5f, ((3.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((3.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex26_4 = {-10.5f, (-1.0f * unit) - revise_Y, 0.0f, -9.3f, (-1.0f * unit) - revise_Y, 0.0f, -10.5f, ((-1.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((-1.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex26_5 = {-10.5f, (-5.0f * unit) - revise_Y, 0.0f, -9.3f, (-5.0f * unit) - revise_Y, 0.0f, -10.5f, ((-5.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((-5.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex26_6 = {-10.5f, (-9.0f * unit) - revise_Y, 0.0f, -9.3f, (-9.0f * unit) - revise_Y, 0.0f, -10.5f, ((-9.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((-9.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_1 = {-10.5f, (11.0f * unit) - revise_Y, 0.0f, -9.4f, (11.0f * unit) - revise_Y, 0.0f, -10.5f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f, -9.4f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_2 = {-10.5f, (7.5f * unit) - revise_Y, 0.0f, -9.3f, (7.5f * unit) - revise_Y, 0.0f, -10.5f, ((7.5f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((7.5f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_3 = {-10.5f, (4.0f * unit) - revise_Y, 0.0f, -9.3f, (4.0f * unit) - revise_Y, 0.0f, -10.5f, ((4.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((4.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_V1 = {0.04f, (11.0f * unit) - revise_Y, 0.0f, 1.06f, (11.0f * unit) - revise_Y, 0.0f, 0.04f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_V2 = {0.04f, (7.5f * unit) - revise_Y, 0.0f, 1.06f, (7.5f * unit) - revise_Y, 0.0f, 0.04f, ((7.5f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((7.5f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_V3 = {0.04f, (4.0f * unit) - revise_Y, 0.0f, 1.06f, (4.0f * unit) - revise_Y, 0.0f, 0.04f, ((4.0f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((4.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_V4 = {0.04f, (0.5f * unit) - revise_Y, 0.0f, 1.06f, (0.5f * unit) - revise_Y, 0.0f, 0.04f, ((0.5f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((0.5f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_V5 = {0.04f, (-3.0f * unit) - revise_Y, 0.0f, 1.06f, (-3.0f * unit) - revise_Y, 0.0f, 0.04f, ((-3.0f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((-3.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_V6 = {0.04f, (-6.5f * unit) - revise_Y, 0.0f, 1.06f, (-6.5f * unit) - revise_Y, 0.0f, 0.04f, ((-6.5f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((-6.5f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_X = {-10.5f, (-10.0f * unit) - revise_Y, 0.0f, -9.3f, (-10.0f * unit) - revise_Y, 0.0f, -10.5f, ((-10.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((-10.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_aVF = {-10.5f, (-6.5f * unit) - revise_Y, 0.0f, -9.3f, (-6.5f * unit) - revise_Y, 0.0f, -10.5f, ((-6.5f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((-6.5f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_aVL = {-10.5f, (-3.0f * unit) - revise_Y, 0.0f, -9.3f, (-3.0f * unit) - revise_Y, 0.0f, -10.5f, ((-3.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((-3.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex621_aVR = {-10.5f, (0.5f * unit) - revise_Y, 0.0f, -9.3f, (0.5f * unit) - revise_Y, 0.0f, -10.5f, ((0.5f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((0.5f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_1 = {-10.5f, (11.0f * unit) - revise_Y, 0.0f, -9.4f, (11.0f * unit) - revise_Y, 0.0f, -10.5f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f, -9.4f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_2 = {-10.5f, (7.0f * unit) - revise_Y, 0.0f, -9.3f, (7.0f * unit) - revise_Y, 0.0f, -10.5f, ((7.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((7.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_3 = {-10.5f, (3.0f * unit) - revise_Y, 0.0f, -9.3f, (3.0f * unit) - revise_Y, 0.0f, -10.5f, ((3.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((3.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_V1 = {0.04f, (11.0f * unit) - revise_Y, 0.0f, 1.06f, (11.0f * unit) - revise_Y, 0.0f, 0.04f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_V2 = {0.04f, (7.0f * unit) - revise_Y, 0.0f, 1.06f, (7.0f * unit) - revise_Y, 0.0f, 0.04f, ((7.0f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((7.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_V3 = {0.04f, (3.0f * unit) - revise_Y, 0.0f, 1.06f, (3.0f * unit) - revise_Y, 0.0f, 0.04f, ((3.0f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((3.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_V4 = {0.04f, (-unit) - revise_Y, 0.0f, 1.06f, (-unit) - revise_Y, 0.0f, 0.04f, ((-unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((-unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_V5 = {0.04f, (-5.0f * unit) - revise_Y, 0.0f, 1.06f, (-5.0f * unit) - revise_Y, 0.0f, 0.04f, ((-5.0f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((-5.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_V6 = {0.04f, (-9.0f * unit) - revise_Y, 0.0f, 1.06f, (-9.0f * unit) - revise_Y, 0.0f, 0.04f, ((-9.0f * unit) - revise_Y) + fHeight, 0.0f, 1.06f, ((-9.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_aVF = {-10.5f, (-9.0f * unit) - revise_Y, 0.0f, -9.3f, (-9.0f * unit) - revise_Y, 0.0f, -10.5f, ((-9.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((-9.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_aVL = {-10.5f, (-5.0f * unit) - revise_Y, 0.0f, -9.3f, (-5.0f * unit) - revise_Y, 0.0f, -10.5f, ((-5.0f * unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((-5.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex62_aVR = {-10.5f, (-unit) - revise_Y, 0.0f, -9.3f, (-unit) - revise_Y, 0.0f, -10.5f, ((-unit) - revise_Y) + fHeight, 0.0f, -9.3f, ((-unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_1 = {-10.5f, (11.0f * unit) - revise_Y, 0.0f, -9.5f, (11.0f * unit) - revise_Y, 0.0f, -10.5f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((11.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_2 = {-10.5f, (9.0f * unit) - revise_Y, 0.0f, -9.5f, (9.0f * unit) - revise_Y, 0.0f, -10.5f, ((9.0f * unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((9.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_3 = {-10.5f, (7.0f * unit) - revise_Y, 0.0f, -9.5f, (7.0f * unit) - revise_Y, 0.0f, -10.5f, ((7.0f * unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((7.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_V1 = {-10.5f, (-unit) - revise_Y, 0.0f, -9.5f, (-unit) - revise_Y, 0.0f, -10.5f, ((-unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((-unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_V2 = {-10.5f, (-3.0f * unit) - revise_Y, 0.0f, -9.5f, (-3.0f * unit) - revise_Y, 0.0f, -10.5f, ((-3.0f * unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((-3.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_V3 = {-10.5f, (-5.0f * unit) - revise_Y, 0.0f, -9.5f, (-5.0f * unit) - revise_Y, 0.0f, -10.5f, ((-5.0f * unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((-5.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_V4 = {-10.5f, (-7.0f * unit) - revise_Y, 0.0f, -9.5f, (-7.0f * unit) - revise_Y, 0.0f, -10.5f, ((-7.0f * unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((-7.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_V5 = {-10.5f, (-9.0f * unit) - revise_Y, 0.0f, -9.5f, (-9.0f * unit) - revise_Y, 0.0f, -10.5f, ((-9.0f * unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((-9.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_V6 = {-10.5f, (-11.0f * unit) - revise_Y, 0.0f, -9.5f, (-11.0f * unit) - revise_Y, 0.0f, -10.5f, ((-11.0f * unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((-11.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_aVF = {-10.5f, unit - revise_Y, 0.0f, -9.5f, unit - revise_Y, 0.0f, -10.5f, (unit - revise_Y) + fHeight, 0.0f, -9.5f, (unit - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_aVL = {-10.5f, (3.0f * unit) - revise_Y, 0.0f, -9.5f, (3.0f * unit) - revise_Y, 0.0f, -10.5f, ((3.0f * unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((3.0f * unit) - revise_Y) + fHeight, 0.0f};
    public static float[] vertex_aVR = {-10.5f, (5.0f * unit) - revise_Y, 0.0f, -9.5f, (5.0f * unit) - revise_Y, 0.0f, -10.5f, ((5.0f * unit) - revise_Y) + fHeight, 0.0f, -9.5f, ((5.0f * unit) - revise_Y) + fHeight, 0.0f};

    static {
        boolean[] zArr = new boolean[12];
        zArr[2] = true;
        zArr[5] = true;
        zArr[8] = true;
        fontFlag = zArr;
    }

    public static Bitmap initFontBitmap(String font) {
        Bitmap bitmap = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0);
        Paint p = new Paint();
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, 3);
        p.setAntiAlias(true);
        p.setColor(-16711936);
        p.setTypeface(typeface);
        p.setTextSize(24.0f);
        canvas.drawText(font, 2.0f, 35.0f, p);
        return bitmap;
    }

    public static Bitmap initFontBitmap(String font, int index) {
        Bitmap bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0);
        Paint p = new Paint();
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, 3);
        p.setAntiAlias(true);
        if (fontFlag[index]) {
            p.setColor(Color.argb(255, 144, 206, 245));
        } else {
            p.setColor(-16711936);
        }
        p.setTypeface(typeface);
        p.setTextSize(30.0f);
        canvas.drawText(font, 2.0f, 35.0f, p);
        return bitmap;
    }

    public static FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    public static void setFontFlag(int index) {
        fontFlag[index] = !fontFlag[index];
    }

    public static void setChangState() {
        fontChanged = true;
    }

    public static boolean getChangState() {
        return fontChanged;
    }

    public static void setRhtythmIndex(int mode) {
        rhythmIndex = mode;
    }

    public static int getRhythmIndex() {
        return rhythmIndex;
    }

    public static void setDisplayMode(int mode) {
        displayMode = mode;
    }

    public static int getDisplayMode() {
        return displayMode;
    }

    public static void setDisplayMode2x6(int mode2x6) {
        displayMode2x6 = mode2x6;
    }

    public static int getDisplayMode2x6() {
        return displayMode2x6;
    }

    public static void setDisplaySpeed(int speed) {
        switch (speed) {
            case 1:
                displaySpeed = displaySpeed125;
                return;
            case 2:
                displaySpeed = 0.014f;
                return;
            case 3:
                displaySpeed = displaySpeed50;
                return;
            default:
                return;
        }
    }

    public static float getDisplaySpeed() {
        return displaySpeed;
    }

    public static void setDisplayGain(int gain) {
        switch (gain) {
            case 1:
                displayGain = 0.00111f;
                return;
            case 2:
                displayGain = displayGain10;
                return;
            case 3:
                displayGain = displayGain20;
                return;
            default:
                return;
        }
    }

    public static float getDisplayGain() {
        return displayGain;
    }
}
