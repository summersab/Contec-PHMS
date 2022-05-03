package serial.jni;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;
import com.alibaba.fastjson.asm.Opcodes;
import com.contec.phms.util.Constants;
import com.example.gltest.GLJNILIB;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {
    private float CX;
    private float CX62;
    private float CX621;
    private float DX;
    private float DX62;
    private float DX621;
    private Boolean IsDrawFont = true;
    private boolean IsNote3_XiaoMi4;
    private boolean IsQualcomm;
    private int RhythmIndex = 0;
    private int checkedCount = 0;
    private float colorALPHA;
    private float colorBLUE;
    private float colorGREEN;
    private float colorRED;
    private FloatBuffer coordBuffer;
    private int displayMode = DrawUtils.getDisplayMode();
    private int displayMode2x6 = 0;
    private boolean fontChanged = false;
    private int glHeight;
    private int glWidth;
    private GLJNILIB gljni;
    private float[] lead;
    private float[] lead621 = {this.xunit * -3.0f, 0.0f, this.xunit * -2.0f, 0.0f, -this.xunit, 0.0f, 0.0f, 0.0f, this.xunit, 0.0f, 2.0f * this.xunit, 0.0f, 3.0f * this.xunit, 0.0f};
    private String[] leadString;
    private float mScale = 0.00111f;
    private float mSpeed = 0.014f;
    private float[] rect = {this.xunit * -6.0f, 6.0f, this.xunit * 6.0f, 6.0f, this.xunit * 6.0f, -6.0f, this.xunit * -6.0f, 6.0f, this.xunit * 6.0f, -6.0f, this.xunit * -6.0f, -6.0f};
    private float[] rect621 = {this.xunit * -6.0f, 6.0f, this.xunit * 6.0f, 6.0f, this.xunit * 6.0f, -3.5f, this.xunit * -6.0f, 6.0f, this.xunit * 6.0f, -3.5f, this.xunit * -6.0f, -3.5f};
    private float[] rect621c = {this.xunit * -6.0f, -3.5f, this.xunit * 6.0f, -3.5f, this.xunit * 6.0f, -6.0f, this.xunit * -6.0f, -3.5f, this.xunit * 6.0f, -6.0f, this.xunit * -6.0f, -6.0f};
    private float[] rectScreen = {-10.5f, 6.0f, -9.0f, 6.0f, -9.0f, -6.0f, -10.5f, 6.0f, -9.0f, -6.0f, -10.5f, -6.0f};
    private final float revise_Y = 0.4f;
    private final float revise_YY = 0.35f;
    private float scale = 0.00111f;
    private short[] temp = new short[72];
    private short[] temp_Last = new short[12];
    private int[] textures;
    private final float unit12 = 0.4f;
    private FloatBuffer vertexBuffer;
    private float xunit = 0.014f;

    public MyRenderer() {
        float[] fArr = new float[Opcodes.JSR];
        fArr[0] = this.xunit * -3.0f;
        fArr[1] = 0.0f;
        fArr[2] = this.xunit * -2.0f;
        fArr[3] = 0.0f;
        fArr[4] = -this.xunit;
        fArr[5] = 0.0f;
        fArr[6] = 0.0f;
        fArr[7] = 0.0f;
        fArr[8] = this.xunit;
        fArr[9] = 0.0f;
        fArr[10] = 2.0f * this.xunit;
        fArr[11] = 0.0f;
        fArr[12] = 3.0f * this.xunit;
        fArr[13] = 0.0f;
        fArr[14] = this.xunit * -3.0f;
        fArr[15] = 0.0f;
        fArr[16] = this.xunit * -2.0f;
        fArr[17] = 0.0f;
        fArr[18] = -this.xunit;
        fArr[19] = 0.0f;
        fArr[20] = 0.0f;
        fArr[21] = 0.0f;
        fArr[22] = this.xunit;
        fArr[23] = 0.0f;
        fArr[24] = 2.0f * this.xunit;
        fArr[25] = 0.0f;
        fArr[26] = 3.0f * this.xunit;
        fArr[27] = 0.0f;
        fArr[28] = this.xunit * -3.0f;
        fArr[29] = 0.0f;
        fArr[30] = this.xunit * -2.0f;
        fArr[31] = 0.0f;
        fArr[32] = -this.xunit;
        fArr[33] = 0.0f;
        fArr[34] = 0.0f;
        fArr[35] = 0.0f;
        fArr[36] = this.xunit;
        fArr[37] = 0.0f;
        fArr[38] = 2.0f * this.xunit;
        fArr[39] = 0.0f;
        fArr[40] = 3.0f * this.xunit;
        fArr[41] = 0.0f;
        fArr[42] = this.xunit * -3.0f;
        fArr[43] = 0.0f;
        fArr[44] = this.xunit * -2.0f;
        fArr[45] = 0.0f;
        fArr[46] = -this.xunit;
        fArr[47] = 0.0f;
        fArr[48] = 0.0f;
        fArr[49] = 0.0f;
        fArr[50] = this.xunit;
        fArr[51] = 0.0f;
        fArr[52] = 2.0f * this.xunit;
        fArr[53] = 0.0f;
        fArr[54] = 3.0f * this.xunit;
        fArr[55] = 0.0f;
        fArr[56] = this.xunit * -3.0f;
        fArr[57] = 0.0f;
        fArr[58] = this.xunit * -2.0f;
        fArr[59] = 0.0f;
        fArr[60] = -this.xunit;
        fArr[61] = 0.0f;
        fArr[62] = 0.0f;
        fArr[63] = 0.0f;
        fArr[64] = this.xunit;
        fArr[65] = 0.0f;
        fArr[66] = 2.0f * this.xunit;
        fArr[67] = 0.0f;
        fArr[68] = 3.0f * this.xunit;
        fArr[69] = 0.0f;
        fArr[70] = this.xunit * -3.0f;
        fArr[71] = 0.0f;
        fArr[72] = this.xunit * -2.0f;
        fArr[73] = 0.0f;
        fArr[74] = -this.xunit;
        fArr[75] = 0.0f;
        fArr[76] = 0.0f;
        fArr[77] = 0.0f;
        fArr[78] = this.xunit;
        fArr[79] = 0.0f;
        fArr[80] = 2.0f * this.xunit;
        fArr[81] = 0.0f;
        fArr[82] = 3.0f * this.xunit;
        fArr[83] = 0.0f;
        fArr[84] = this.xunit * -3.0f;
        fArr[85] = 0.0f;
        fArr[86] = this.xunit * -2.0f;
        fArr[87] = 0.0f;
        fArr[88] = -this.xunit;
        fArr[89] = 0.0f;
        fArr[90] = 0.0f;
        fArr[91] = 0.0f;
        fArr[92] = this.xunit;
        fArr[93] = 0.0f;
        fArr[94] = 2.0f * this.xunit;
        fArr[95] = 0.0f;
        fArr[96] = 3.0f * this.xunit;
        fArr[97] = 0.0f;
        fArr[98] = this.xunit * -3.0f;
        fArr[99] = 0.0f;
        fArr[100] = this.xunit * -2.0f;
        fArr[101] = 0.0f;
        fArr[102] = -this.xunit;
        fArr[103] = 0.0f;
        fArr[104] = 0.0f;
        fArr[105] = 0.0f;
        fArr[106] = this.xunit;
        fArr[107] = 0.0f;
        fArr[108] = 2.0f * this.xunit;
        fArr[109] = 0.0f;
        fArr[110] = 3.0f * this.xunit;
        fArr[111] = 0.0f;
        fArr[112] = this.xunit * -3.0f;
        fArr[113] = 0.0f;
        fArr[114] = this.xunit * -2.0f;
        fArr[115] = 0.0f;
        fArr[116] = -this.xunit;
        fArr[117] = 0.0f;
        fArr[118] = 0.0f;
        fArr[119] = 0.0f;
        fArr[120] = this.xunit;
        fArr[121] = 0.0f;
        fArr[122] = 2.0f * this.xunit;
        fArr[123] = 0.0f;
        fArr[124] = 3.0f * this.xunit;
        fArr[125] = 0.0f;
        fArr[126] = this.xunit * -3.0f;
        fArr[127] = 0.0f;
        fArr[128] = this.xunit * -2.0f;
        fArr[129] = 0.0f;
        fArr[130] = -this.xunit;
        fArr[131] = 0.0f;
        fArr[132] = 0.0f;
        fArr[133] = 0.0f;
        fArr[134] = this.xunit;
        fArr[135] = 0.0f;
        fArr[136] = 2.0f * this.xunit;
        fArr[137] = 0.0f;
        fArr[138] = 3.0f * this.xunit;
        fArr[139] = 0.0f;
        fArr[140] = this.xunit * -3.0f;
        fArr[141] = 0.0f;
        fArr[142] = this.xunit * -2.0f;
        fArr[143] = 0.0f;
        fArr[144] = -this.xunit;
        fArr[145] = 0.0f;
        fArr[146] = 0.0f;
        fArr[147] = 0.0f;
        fArr[148] = this.xunit;
        fArr[149] = 0.0f;
        fArr[150] = 2.0f * this.xunit;
        fArr[151] = 0.0f;
        fArr[152] = 3.0f * this.xunit;
        fArr[153] = 0.0f;
        fArr[154] = this.xunit * -3.0f;
        fArr[155] = 0.0f;
        fArr[156] = this.xunit * -2.0f;
        fArr[157] = 0.0f;
        fArr[158] = -this.xunit;
        fArr[159] = 0.0f;
        fArr[160] = 0.0f;
        fArr[161] = 0.0f;
        fArr[162] = this.xunit;
        fArr[163] = 0.0f;
        fArr[164] = 2.0f * this.xunit;
        fArr[165] = 0.0f;
        fArr[166] = 3.0f * this.xunit;
        fArr[167] = 0.0f;
        this.lead = fArr;
        this.textures = new int[1];
        this.leadString = new String[]{"Ⅰ", "Ⅱ", "Ⅲ", "aVR", "aVL", "aVF", "V1", "V2", "V3", "V4", "V5", "V6"};
        this.colorRED = 1.0f;
        this.colorGREEN = 0.4f;
        this.colorBLUE = 0.0f;
        this.colorALPHA = 1.0f;
    }

    public void DrawFont(GL10 gl, float[] vertex, String leadString2) {
        Bitmap bitmap = DrawUtils.initFontBitmap(leadString2);
        gl.glEnable(3553);
        gl.glGenTextures(1, this.textures, 0);
        gl.glBindTexture(3553, this.textures[0]);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        gl.glTexParameterf(3553, 10240, 9729.0f);
        gl.glTexParameterf(3553, 10241, 9729.0f);
        gl.glLoadIdentity();
        ByteBuffer bb = ByteBuffer.allocateDirect(vertex.length * 4);
        bb.order(ByteOrder.nativeOrder());
        this.vertexBuffer = bb.asFloatBuffer();
        this.vertexBuffer.put(vertex);
        this.vertexBuffer.position(0);
        ByteBuffer coordbb = ByteBuffer.allocateDirect(DrawUtils.coord.length * 4);
        coordbb.order(ByteOrder.nativeOrder());
        this.coordBuffer = coordbb.asFloatBuffer();
        this.coordBuffer.put(DrawUtils.coord);
        this.coordBuffer.position(0);
        gl.glEnableClientState(32884);
        gl.glEnableClientState(32888);
        gl.glTranslatef(0.0f, 0.0f, -5.1f);
        gl.glVertexPointer(3, 5126, 0, this.vertexBuffer);
        gl.glTexCoordPointer(2, 5126, 0, this.coordBuffer);
        gl.glDrawArrays(5, 0, 4);
        gl.glDisableClientState(32884);
        gl.glDisableClientState(32888);
        gl.glDisable(3553);
        gl.glFinish();
    }

    public void DrawFont(GL10 gl, float[] vertex, String leadString2, int index) {
        Bitmap bitmap = DrawUtils.initFontBitmap(leadString2, index);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(3553);
        gl.glGenTextures(1, this.textures, 0);
        gl.glBindTexture(3553, this.textures[0]);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        gl.glTexParameterf(3553, 10240, 9729.0f);
        gl.glTexParameterf(3553, 10241, 9729.0f);
        gl.glLoadIdentity();
        ByteBuffer bb = ByteBuffer.allocateDirect(vertex.length * 4);
        bb.order(ByteOrder.nativeOrder());
        this.vertexBuffer = bb.asFloatBuffer();
        this.vertexBuffer.put(vertex);
        this.vertexBuffer.position(0);
        ByteBuffer coordbb = ByteBuffer.allocateDirect(DrawUtils.coord.length * 4);
        coordbb.order(ByteOrder.nativeOrder());
        this.coordBuffer = coordbb.asFloatBuffer();
        this.coordBuffer.put(DrawUtils.coord);
        this.coordBuffer.position(0);
        gl.glEnableClientState(32884);
        gl.glEnableClientState(32888);
        gl.glTranslatef(0.0f, 0.0f, -5.1f);
        gl.glVertexPointer(3, 5126, 0, this.vertexBuffer);
        gl.glTexCoordPointer(2, 5126, 0, this.coordBuffer);
        gl.glDrawArrays(5, 0, 4);
        gl.glDisableClientState(32884);
        gl.glDisableClientState(32888);
        gl.glDisable(3553);
        gl.glFinish();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public void onDrawFrame(GL10 gl) {
        if (this.IsQualcomm) {
            GLJNILIB.setStartQcom(0, 0, this.glWidth, this.glHeight);
            if (this.IsNote3_XiaoMi4) {
                GLJNILIB.setEndQcom();
            }
        }
        switch (this.displayMode) {
            case 0:
                if (this.IsDrawFont.booleanValue()) {
                    gl.glClear(16640);
                    Log.e("LeadChange", "<->" + this.displayMode);
                    this.DX = -9.0f;
                    this.CX = -8.6f;
                    DrawFont(gl, DrawUtils.vertex_1, this.leadString[0], 0);
                    DrawFont(gl, DrawUtils.vertex_2, this.leadString[1], 1);
                    DrawFont(gl, DrawUtils.vertex_3, this.leadString[2], 2);
                    DrawFont(gl, DrawUtils.vertex_aVR, this.leadString[3], 3);
                    DrawFont(gl, DrawUtils.vertex_aVL, this.leadString[4], 4);
                    DrawFont(gl, DrawUtils.vertex_aVF, this.leadString[5], 5);
                    DrawFont(gl, DrawUtils.vertex_V1, this.leadString[6], 6);
                    DrawFont(gl, DrawUtils.vertex_V2, this.leadString[7], 7);
                    DrawFont(gl, DrawUtils.vertex_V3, this.leadString[8], 8);
                    DrawFont(gl, DrawUtils.vertex_V4, this.leadString[9], 9);
                    DrawFont(gl, DrawUtils.vertex_V5, this.leadString[10], 10);
                    DrawFont(gl, DrawUtils.vertex_V6, this.leadString[11], 11);
                    this.IsDrawFont = false;
                }
                UpdateEcgData();
                ClearRect(gl);
                DrawWave(gl);
                break;
            case 1:
                if (this.IsDrawFont.booleanValue()) {
                    Log.e("LeadChange", "<->" + this.displayMode);
                    gl.glClear(16640);
                    this.DX = -9.0f;
                    this.CX = -8.6f;
                    this.DX62 = 1.3f;
                    this.CX62 = 1.7f;
                    DrawFont(gl, DrawUtils.vertex62_1, this.leadString[0], 0);
                    DrawFont(gl, DrawUtils.vertex62_2, this.leadString[1], 1);
                    DrawFont(gl, DrawUtils.vertex62_3, this.leadString[2], 2);
                    DrawFont(gl, DrawUtils.vertex62_aVR, this.leadString[3], 3);
                    DrawFont(gl, DrawUtils.vertex62_aVL, this.leadString[4], 4);
                    DrawFont(gl, DrawUtils.vertex62_aVF, this.leadString[5], 5);
                    DrawFont(gl, DrawUtils.vertex62_V1, this.leadString[6], 6);
                    DrawFont(gl, DrawUtils.vertex62_V2, this.leadString[7], 7);
                    DrawFont(gl, DrawUtils.vertex62_V3, this.leadString[8], 8);
                    DrawFont(gl, DrawUtils.vertex62_V4, this.leadString[9], 9);
                    DrawFont(gl, DrawUtils.vertex62_V5, this.leadString[10], 10);
                    DrawFont(gl, DrawUtils.vertex62_V6, this.leadString[11], 11);
                    this.IsDrawFont = false;
                }
                UpdateEcgData62();
                ClearRect62(gl, true);
                ClearRect62(gl, false);
                DrawWave_62(gl, true);
                DrawWave_62(gl, false);
                break;
            case 2:
                if (this.IsDrawFont.booleanValue()) {
                    Log.e("LeadChange", "<->" + this.displayMode);
                    gl.glClear(16640);
                    this.DX = -9.0f;
                    this.CX = -8.6f;
                    this.DX62 = 1.3f;
                    this.CX62 = 1.7f;
                    this.DX621 = -9.0f;
                    this.CX621 = -8.6f;
                    DrawFont(gl, DrawUtils.vertex621_1, this.leadString[0], 0);
                    DrawFont(gl, DrawUtils.vertex621_2, this.leadString[1], 1);
                    DrawFont(gl, DrawUtils.vertex621_3, this.leadString[2], 2);
                    DrawFont(gl, DrawUtils.vertex621_aVR, this.leadString[3], 3);
                    DrawFont(gl, DrawUtils.vertex621_aVL, this.leadString[4], 4);
                    DrawFont(gl, DrawUtils.vertex621_aVF, this.leadString[5], 5);
                    DrawFont(gl, DrawUtils.vertex621_V1, this.leadString[6], 6);
                    DrawFont(gl, DrawUtils.vertex621_V2, this.leadString[7], 7);
                    DrawFont(gl, DrawUtils.vertex621_V3, this.leadString[8], 8);
                    DrawFont(gl, DrawUtils.vertex621_V4, this.leadString[9], 9);
                    DrawFont(gl, DrawUtils.vertex621_V5, this.leadString[10], 10);
                    DrawFont(gl, DrawUtils.vertex621_V6, this.leadString[11], 11);
                    DrawFont(gl, DrawUtils.vertex621_X, this.leadString[this.RhythmIndex], this.RhythmIndex);
                    this.IsDrawFont = false;
                }
                UpdateEcgData621();
                ClearRect621(gl, 0);
                ClearRect621(gl, 1);
                ClearRect621(gl, 2);
                DrawWave_621(gl, 0);
                DrawWave_621(gl, 1);
                DrawWave_621(gl, 2);
                break;
            case 3:
                if (this.IsDrawFont.booleanValue()) {
                    Log.e("LeadChange", "<->" + this.displayMode);
                    gl.glClear(16640);
                    this.DX = -9.0f;
                    this.CX = -8.6f;
                    if (this.displayMode2x6 == 0) {
                        DrawFont(gl, DrawUtils.vertex26_1, this.leadString[0], 0);
                        DrawFont(gl, DrawUtils.vertex26_2, this.leadString[1], 1);
                        DrawFont(gl, DrawUtils.vertex26_3, this.leadString[2], 2);
                        DrawFont(gl, DrawUtils.vertex26_4, this.leadString[3], 3);
                        DrawFont(gl, DrawUtils.vertex26_5, this.leadString[4], 4);
                        DrawFont(gl, DrawUtils.vertex26_6, this.leadString[5], 5);
                    } else {
                        DrawFont(gl, DrawUtils.vertex26_1, this.leadString[6]);
                        DrawFont(gl, DrawUtils.vertex26_2, this.leadString[7]);
                        DrawFont(gl, DrawUtils.vertex26_3, this.leadString[8]);
                        DrawFont(gl, DrawUtils.vertex26_4, this.leadString[9]);
                        DrawFont(gl, DrawUtils.vertex26_5, this.leadString[10]);
                        DrawFont(gl, DrawUtils.vertex26_6, this.leadString[11]);
                    }
                    this.IsDrawFont = false;
                }
                UpdateEcgData26();
                ClearRect(gl);
                DrawWave_26(gl, this.displayMode2x6);
                break;
        }
        this.checkedCount++;
        if (this.checkedCount == 30) {
            int tmpMode = DrawUtils.getDisplayMode();
            int tmpMode2x6 = DrawUtils.getDisplayMode2x6();
            float tmpSpeed = DrawUtils.getDisplaySpeed();
            float tmpGain = DrawUtils.getDisplayGain();
            boolean tmpflag = DrawUtils.getChangState();
            int tmpRhythm = DrawUtils.getRhythmIndex();
            if (!(this.displayMode == tmpMode && this.mSpeed == tmpSpeed && this.mScale == tmpGain && this.displayMode2x6 == tmpMode2x6 && this.fontChanged == tmpflag && this.RhythmIndex == tmpRhythm)) {
                this.IsDrawFont = true;
                this.mScale = tmpGain;
                this.scale = this.mScale;
                this.mSpeed = tmpSpeed;
                this.displayMode2x6 = tmpMode2x6;
                this.fontChanged = tmpflag;
                this.RhythmIndex = tmpRhythm;
            }
            this.displayMode = tmpMode;
            this.checkedCount = 0;
        }
    }

    public void UpdateEcgData() {
        if (GLView.isGather) {
            GLView.gather.getDataEX(this.temp);
        }
        this.lead[1] = ((((float) this.temp_Last[0]) * this.scale) + 4.4f) - 0.35f;
        this.lead[3] = ((((float) this.temp[0]) * this.scale) + 4.4f) - 0.35f;
        this.lead[5] = ((((float) this.temp[12]) * this.scale) + 4.4f) - 0.35f;
        this.lead[7] = ((((float) this.temp[24]) * this.scale) + 4.4f) - 0.35f;
        this.lead[9] = ((((float) this.temp[36]) * this.scale) + 4.4f) - 0.35f;
        this.lead[11] = ((((float) this.temp[48]) * this.scale) + 4.4f) - 0.35f;
        this.lead[13] = ((((float) this.temp[60]) * this.scale) + 4.4f) - 0.35f;
        this.lead[15] = ((((float) this.temp_Last[1]) * this.scale) + 3.6000001f) - 0.35f;
        this.lead[17] = ((((float) this.temp[1]) * this.scale) + 3.6000001f) - 0.35f;
        this.lead[19] = ((((float) this.temp[13]) * this.scale) + 3.6000001f) - 0.35f;
        this.lead[21] = ((((float) this.temp[25]) * this.scale) + 3.6000001f) - 0.35f;
        this.lead[23] = ((((float) this.temp[37]) * this.scale) + 3.6000001f) - 0.35f;
        this.lead[25] = ((((float) this.temp[49]) * this.scale) + 3.6000001f) - 0.35f;
        this.lead[27] = ((((float) this.temp[61]) * this.scale) + 3.6000001f) - 0.35f;
        this.lead[29] = ((((float) this.temp_Last[2]) * this.scale) + 2.8f) - 0.35f;
        this.lead[31] = ((((float) this.temp[2]) * this.scale) + 2.8f) - 0.35f;
        this.lead[33] = ((((float) this.temp[14]) * this.scale) + 2.8f) - 0.35f;
        this.lead[35] = ((((float) this.temp[26]) * this.scale) + 2.8f) - 0.35f;
        this.lead[37] = ((((float) this.temp[38]) * this.scale) + 2.8f) - 0.35f;
        this.lead[39] = ((((float) this.temp[50]) * this.scale) + 2.8f) - 0.35f;
        this.lead[41] = ((((float) this.temp[62]) * this.scale) + 2.8f) - 0.35f;
        this.lead[43] = ((((float) this.temp_Last[3]) * this.scale) + 2.0f) - 0.35f;
        this.lead[45] = ((((float) this.temp[3]) * this.scale) + 2.0f) - 0.35f;
        this.lead[47] = ((((float) this.temp[15]) * this.scale) + 2.0f) - 0.35f;
        this.lead[49] = ((((float) this.temp[27]) * this.scale) + 2.0f) - 0.35f;
        this.lead[51] = ((((float) this.temp[39]) * this.scale) + 2.0f) - 0.35f;
        this.lead[53] = ((((float) this.temp[51]) * this.scale) + 2.0f) - 0.35f;
        this.lead[55] = ((((float) this.temp[63]) * this.scale) + 2.0f) - 0.35f;
        this.lead[57] = ((((float) this.temp_Last[4]) * this.scale) + 1.2f) - 0.35f;
        this.lead[59] = ((((float) this.temp[4]) * this.scale) + 1.2f) - 0.35f;
        this.lead[61] = ((((float) this.temp[16]) * this.scale) + 1.2f) - 0.35f;
        this.lead[63] = ((((float) this.temp[28]) * this.scale) + 1.2f) - 0.35f;
        this.lead[65] = ((((float) this.temp[40]) * this.scale) + 1.2f) - 0.35f;
        this.lead[67] = ((((float) this.temp[52]) * this.scale) + 1.2f) - 0.35f;
        this.lead[69] = ((((float) this.temp[64]) * this.scale) + 1.2f) - 0.35f;
        this.lead[71] = ((((float) this.temp_Last[5]) * this.scale) + 0.4f) - 0.35f;
        this.lead[73] = ((((float) this.temp[5]) * this.scale) + 0.4f) - 0.35f;
        this.lead[75] = ((((float) this.temp[17]) * this.scale) + 0.4f) - 0.35f;
        this.lead[77] = ((((float) this.temp[29]) * this.scale) + 0.4f) - 0.35f;
        this.lead[79] = ((((float) this.temp[41]) * this.scale) + 0.4f) - 0.35f;
        this.lead[81] = ((((float) this.temp[53]) * this.scale) + 0.4f) - 0.35f;
        this.lead[83] = ((((float) this.temp[65]) * this.scale) + 0.4f) - 0.35f;
        this.lead[85] = ((((float) this.temp_Last[6]) * this.scale) - 0.4f) - 0.35f;
        this.lead[87] = ((((float) this.temp[6]) * this.scale) - 0.4f) - 0.35f;
        this.lead[89] = ((((float) this.temp[18]) * this.scale) - 0.4f) - 0.35f;
        this.lead[91] = ((((float) this.temp[30]) * this.scale) - 0.4f) - 0.35f;
        this.lead[93] = ((((float) this.temp[42]) * this.scale) - 0.4f) - 0.35f;
        this.lead[95] = ((((float) this.temp[54]) * this.scale) - 0.4f) - 0.35f;
        this.lead[97] = ((((float) this.temp[66]) * this.scale) - 0.4f) - 0.35f;
        this.lead[99] = ((((float) this.temp_Last[7]) * this.scale) - 1.2f) - 0.35f;
        this.lead[101] = ((((float) this.temp[7]) * this.scale) - 1.2f) - 0.35f;
        this.lead[103] = ((((float) this.temp[19]) * this.scale) - 1.2f) - 0.35f;
        this.lead[105] = ((((float) this.temp[31]) * this.scale) - 1.2f) - 0.35f;
        this.lead[107] = ((((float) this.temp[43]) * this.scale) - 1.2f) - 0.35f;
        this.lead[109] = ((((float) this.temp[55]) * this.scale) - 1.2f) - 0.35f;
        this.lead[111] = ((((float) this.temp[67]) * this.scale) - 1.2f) - 0.35f;
        this.lead[113] = ((((float) this.temp_Last[8]) * this.scale) - 2.0f) - 0.35f;
        this.lead[115] = ((((float) this.temp[8]) * this.scale) - 2.0f) - 0.35f;
        this.lead[117] = ((((float) this.temp[20]) * this.scale) - 2.0f) - 0.35f;
        this.lead[119] = ((((float) this.temp[32]) * this.scale) - 2.0f) - 0.35f;
        this.lead[121] = ((((float) this.temp[44]) * this.scale) - 2.0f) - 0.35f;
        this.lead[123] = ((((float) this.temp[56]) * this.scale) - 2.0f) - 0.35f;
        this.lead[125] = ((((float) this.temp[68]) * this.scale) - 2.0f) - 0.35f;
        this.lead[127] = ((((float) this.temp_Last[9]) * this.scale) - 2.8f) - 0.35f;
        this.lead[129] = ((((float) this.temp[9]) * this.scale) - 2.8f) - 0.35f;
        this.lead[131] = ((((float) this.temp[21]) * this.scale) - 2.8f) - 0.35f;
        this.lead[133] = ((((float) this.temp[33]) * this.scale) - 2.8f) - 0.35f;
        this.lead[135] = ((((float) this.temp[45]) * this.scale) - 2.8f) - 0.35f;
        this.lead[137] = ((((float) this.temp[57]) * this.scale) - 2.8f) - 0.35f;
        this.lead[139] = ((((float) this.temp[69]) * this.scale) - 2.8f) - 0.35f;
        this.lead[141] = ((((float) this.temp_Last[10]) * this.scale) - 3.6000001f) - 0.35f;
        this.lead[143] = ((((float) this.temp[10]) * this.scale) - 3.6000001f) - 0.35f;
        this.lead[145] = ((((float) this.temp[22]) * this.scale) - 3.6000001f) - 0.35f;
        this.lead[147] = ((((float) this.temp[34]) * this.scale) - 3.6000001f) - 0.35f;
        this.lead[149] = ((((float) this.temp[46]) * this.scale) - 3.6000001f) - 0.35f;
        this.lead[151] = ((((float) this.temp[58]) * this.scale) - 3.6000001f) - 0.35f;
        this.lead[153] = ((((float) this.temp[70]) * this.scale) - 3.6000001f) - 0.35f;
        this.lead[155] = ((((float) this.temp_Last[11]) * this.scale) - 4.4f) - 0.35f;
        this.lead[157] = ((((float) this.temp[11]) * this.scale) - 4.4f) - 0.35f;
        this.lead[159] = ((((float) this.temp[23]) * this.scale) - 4.4f) - 0.35f;
        this.lead[161] = ((((float) this.temp[35]) * this.scale) - 4.4f) - 0.35f;
        this.lead[163] = ((((float) this.temp[47]) * this.scale) - 4.4f) - 0.35f;
        this.lead[165] = ((((float) this.temp[59]) * this.scale) - 4.4f) - 0.35f;
        this.lead[167] = ((((float) this.temp[71]) * this.scale) - 4.4f) - 0.35f;
        this.temp_Last[0] = this.temp[60];
        this.temp_Last[1] = this.temp[61];
        this.temp_Last[2] = this.temp[62];
        this.temp_Last[3] = this.temp[63];
        this.temp_Last[4] = this.temp[64];
        this.temp_Last[5] = this.temp[65];
        this.temp_Last[6] = this.temp[66];
        this.temp_Last[7] = this.temp[67];
        this.temp_Last[8] = this.temp[68];
        this.temp_Last[9] = this.temp[69];
        this.temp_Last[10] = this.temp[70];
        this.temp_Last[11] = this.temp[71];
        if (this.mSpeed != this.xunit) {
            XUpdate();
        }
    }

    public void UpdateEcgData62() {
        if (GLView.isGather) {
            GLView.gather.getDataEX(this.temp);
        }
        this.lead[1] = ((((float) this.temp_Last[0]) * this.scale) + 4.4f) - 0.4f;
        this.lead[3] = ((((float) this.temp[0]) * this.scale) + 4.4f) - 0.4f;
        this.lead[5] = ((((float) this.temp[12]) * this.scale) + 4.4f) - 0.4f;
        this.lead[7] = ((((float) this.temp[24]) * this.scale) + 4.4f) - 0.4f;
        this.lead[9] = ((((float) this.temp[36]) * this.scale) + 4.4f) - 0.4f;
        this.lead[11] = ((((float) this.temp[48]) * this.scale) + 4.4f) - 0.4f;
        this.lead[13] = ((((float) this.temp[60]) * this.scale) + 4.4f) - 0.4f;
        this.lead[15] = ((((float) this.temp_Last[1]) * this.scale) + 2.8f) - 0.4f;
        this.lead[17] = ((((float) this.temp[1]) * this.scale) + 2.8f) - 0.4f;
        this.lead[19] = ((((float) this.temp[13]) * this.scale) + 2.8f) - 0.4f;
        this.lead[21] = ((((float) this.temp[25]) * this.scale) + 2.8f) - 0.4f;
        this.lead[23] = ((((float) this.temp[37]) * this.scale) + 2.8f) - 0.4f;
        this.lead[25] = ((((float) this.temp[49]) * this.scale) + 2.8f) - 0.4f;
        this.lead[27] = ((((float) this.temp[61]) * this.scale) + 2.8f) - 0.4f;
        this.lead[29] = ((((float) this.temp_Last[2]) * this.scale) + 1.2f) - 0.4f;
        this.lead[31] = ((((float) this.temp[2]) * this.scale) + 1.2f) - 0.4f;
        this.lead[33] = ((((float) this.temp[14]) * this.scale) + 1.2f) - 0.4f;
        this.lead[35] = ((((float) this.temp[26]) * this.scale) + 1.2f) - 0.4f;
        this.lead[37] = ((((float) this.temp[38]) * this.scale) + 1.2f) - 0.4f;
        this.lead[39] = ((((float) this.temp[50]) * this.scale) + 1.2f) - 0.4f;
        this.lead[41] = ((((float) this.temp[62]) * this.scale) + 1.2f) - 0.4f;
        this.lead[43] = ((((float) this.temp_Last[3]) * this.scale) - 0.4f) - 0.4f;
        this.lead[45] = ((((float) this.temp[3]) * this.scale) - 0.4f) - 0.4f;
        this.lead[47] = ((((float) this.temp[15]) * this.scale) - 0.4f) - 0.4f;
        this.lead[49] = ((((float) this.temp[27]) * this.scale) - 0.4f) - 0.4f;
        this.lead[51] = ((((float) this.temp[39]) * this.scale) - 0.4f) - 0.4f;
        this.lead[53] = ((((float) this.temp[51]) * this.scale) - 0.4f) - 0.4f;
        this.lead[55] = ((((float) this.temp[63]) * this.scale) - 0.4f) - 0.4f;
        this.lead[57] = ((((float) this.temp_Last[4]) * this.scale) - 2.0f) - 0.4f;
        this.lead[59] = ((((float) this.temp[4]) * this.scale) - 2.0f) - 0.4f;
        this.lead[61] = ((((float) this.temp[16]) * this.scale) - 2.0f) - 0.4f;
        this.lead[63] = ((((float) this.temp[28]) * this.scale) - 2.0f) - 0.4f;
        this.lead[65] = ((((float) this.temp[40]) * this.scale) - 2.0f) - 0.4f;
        this.lead[67] = ((((float) this.temp[52]) * this.scale) - 2.0f) - 0.4f;
        this.lead[69] = ((((float) this.temp[64]) * this.scale) - 2.0f) - 0.4f;
        this.lead[71] = ((((float) this.temp_Last[5]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[73] = ((((float) this.temp[5]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[75] = ((((float) this.temp[17]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[77] = ((((float) this.temp[29]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[79] = ((((float) this.temp[41]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[81] = ((((float) this.temp[53]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[83] = ((((float) this.temp[65]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[85] = ((((float) this.temp_Last[6]) * this.scale) + 4.4f) - 0.4f;
        this.lead[87] = ((((float) this.temp[6]) * this.scale) + 4.4f) - 0.4f;
        this.lead[89] = ((((float) this.temp[18]) * this.scale) + 4.4f) - 0.4f;
        this.lead[91] = ((((float) this.temp[30]) * this.scale) + 4.4f) - 0.4f;
        this.lead[93] = ((((float) this.temp[42]) * this.scale) + 4.4f) - 0.4f;
        this.lead[95] = ((((float) this.temp[54]) * this.scale) + 4.4f) - 0.4f;
        this.lead[97] = ((((float) this.temp[66]) * this.scale) + 4.4f) - 0.4f;
        this.lead[99] = ((((float) this.temp_Last[7]) * this.scale) + 2.8f) - 0.4f;
        this.lead[101] = ((((float) this.temp[7]) * this.scale) + 2.8f) - 0.4f;
        this.lead[103] = ((((float) this.temp[19]) * this.scale) + 2.8f) - 0.4f;
        this.lead[105] = ((((float) this.temp[31]) * this.scale) + 2.8f) - 0.4f;
        this.lead[107] = ((((float) this.temp[43]) * this.scale) + 2.8f) - 0.4f;
        this.lead[109] = ((((float) this.temp[55]) * this.scale) + 2.8f) - 0.4f;
        this.lead[111] = ((((float) this.temp[67]) * this.scale) + 2.8f) - 0.4f;
        this.lead[113] = ((((float) this.temp_Last[8]) * this.scale) + 1.2f) - 0.4f;
        this.lead[115] = ((((float) this.temp[8]) * this.scale) + 1.2f) - 0.4f;
        this.lead[117] = ((((float) this.temp[20]) * this.scale) + 1.2f) - 0.4f;
        this.lead[119] = ((((float) this.temp[32]) * this.scale) + 1.2f) - 0.4f;
        this.lead[121] = ((((float) this.temp[44]) * this.scale) + 1.2f) - 0.4f;
        this.lead[123] = ((((float) this.temp[56]) * this.scale) + 1.2f) - 0.4f;
        this.lead[125] = ((((float) this.temp[68]) * this.scale) + 1.2f) - 0.4f;
        this.lead[127] = ((((float) this.temp_Last[9]) * this.scale) - 0.4f) - 0.4f;
        this.lead[129] = ((((float) this.temp[9]) * this.scale) - 0.4f) - 0.4f;
        this.lead[131] = ((((float) this.temp[21]) * this.scale) - 0.4f) - 0.4f;
        this.lead[133] = ((((float) this.temp[33]) * this.scale) - 0.4f) - 0.4f;
        this.lead[135] = ((((float) this.temp[45]) * this.scale) - 0.4f) - 0.4f;
        this.lead[137] = ((((float) this.temp[57]) * this.scale) - 0.4f) - 0.4f;
        this.lead[139] = ((((float) this.temp[69]) * this.scale) - 0.4f) - 0.4f;
        this.lead[141] = ((((float) this.temp_Last[10]) * this.scale) - 2.0f) - 0.4f;
        this.lead[143] = ((((float) this.temp[10]) * this.scale) - 2.0f) - 0.4f;
        this.lead[145] = ((((float) this.temp[22]) * this.scale) - 2.0f) - 0.4f;
        this.lead[147] = ((((float) this.temp[34]) * this.scale) - 2.0f) - 0.4f;
        this.lead[149] = ((((float) this.temp[46]) * this.scale) - 2.0f) - 0.4f;
        this.lead[151] = ((((float) this.temp[58]) * this.scale) - 2.0f) - 0.4f;
        this.lead[153] = ((((float) this.temp[70]) * this.scale) - 2.0f) - 0.4f;
        this.lead[155] = ((((float) this.temp_Last[11]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[157] = ((((float) this.temp[11]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[159] = ((((float) this.temp[23]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[161] = ((((float) this.temp[35]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[163] = ((((float) this.temp[47]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[165] = ((((float) this.temp[59]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[167] = ((((float) this.temp[71]) * this.scale) - 3.6000001f) - 0.4f;
        this.temp_Last[0] = this.temp[60];
        this.temp_Last[1] = this.temp[61];
        this.temp_Last[2] = this.temp[62];
        this.temp_Last[3] = this.temp[63];
        this.temp_Last[4] = this.temp[64];
        this.temp_Last[5] = this.temp[65];
        this.temp_Last[6] = this.temp[66];
        this.temp_Last[7] = this.temp[67];
        this.temp_Last[8] = this.temp[68];
        this.temp_Last[9] = this.temp[69];
        this.temp_Last[10] = this.temp[70];
        this.temp_Last[11] = this.temp[71];
        if (this.mSpeed != this.xunit) {
            XUpdate();
        }
    }

    public void UpdateEcgData621() {
        if (GLView.isGather) {
            GLView.gather.getDataEX(this.temp);
        }
        this.lead[1] = ((((float) this.temp_Last[0]) * this.scale) + 4.4f) - 0.4f;
        this.lead[3] = ((((float) this.temp[0]) * this.scale) + 4.4f) - 0.4f;
        this.lead[5] = ((((float) this.temp[12]) * this.scale) + 4.4f) - 0.4f;
        this.lead[7] = ((((float) this.temp[24]) * this.scale) + 4.4f) - 0.4f;
        this.lead[9] = ((((float) this.temp[36]) * this.scale) + 4.4f) - 0.4f;
        this.lead[11] = ((((float) this.temp[48]) * this.scale) + 4.4f) - 0.4f;
        this.lead[13] = ((((float) this.temp[60]) * this.scale) + 4.4f) - 0.4f;
        this.lead[15] = ((((float) this.temp_Last[1]) * this.scale) + 3.0f) - 0.4f;
        this.lead[17] = ((((float) this.temp[1]) * this.scale) + 3.0f) - 0.4f;
        this.lead[19] = ((((float) this.temp[13]) * this.scale) + 3.0f) - 0.4f;
        this.lead[21] = ((((float) this.temp[25]) * this.scale) + 3.0f) - 0.4f;
        this.lead[23] = ((((float) this.temp[37]) * this.scale) + 3.0f) - 0.4f;
        this.lead[25] = ((((float) this.temp[49]) * this.scale) + 3.0f) - 0.4f;
        this.lead[27] = ((((float) this.temp[61]) * this.scale) + 3.0f) - 0.4f;
        this.lead[29] = ((((float) this.temp_Last[2]) * this.scale) + 1.6f) - 0.4f;
        this.lead[31] = ((((float) this.temp[2]) * this.scale) + 1.6f) - 0.4f;
        this.lead[33] = ((((float) this.temp[14]) * this.scale) + 1.6f) - 0.4f;
        this.lead[35] = ((((float) this.temp[26]) * this.scale) + 1.6f) - 0.4f;
        this.lead[37] = ((((float) this.temp[38]) * this.scale) + 1.6f) - 0.4f;
        this.lead[39] = ((((float) this.temp[50]) * this.scale) + 1.6f) - 0.4f;
        this.lead[41] = ((((float) this.temp[62]) * this.scale) + 1.6f) - 0.4f;
        this.lead[43] = ((((float) this.temp_Last[3]) * this.scale) + 0.2f) - 0.4f;
        this.lead[45] = ((((float) this.temp[3]) * this.scale) + 0.2f) - 0.4f;
        this.lead[47] = ((((float) this.temp[15]) * this.scale) + 0.2f) - 0.4f;
        this.lead[49] = ((((float) this.temp[27]) * this.scale) + 0.2f) - 0.4f;
        this.lead[51] = ((((float) this.temp[39]) * this.scale) + 0.2f) - 0.4f;
        this.lead[53] = ((((float) this.temp[51]) * this.scale) + 0.2f) - 0.4f;
        this.lead[55] = ((((float) this.temp[63]) * this.scale) + 0.2f) - 0.4f;
        this.lead[57] = ((((float) this.temp_Last[4]) * this.scale) - 1.2f) - 0.4f;
        this.lead[59] = ((((float) this.temp[4]) * this.scale) - 1.2f) - 0.4f;
        this.lead[61] = ((((float) this.temp[16]) * this.scale) - 1.2f) - 0.4f;
        this.lead[63] = ((((float) this.temp[28]) * this.scale) - 1.2f) - 0.4f;
        this.lead[65] = ((((float) this.temp[40]) * this.scale) - 1.2f) - 0.4f;
        this.lead[67] = ((((float) this.temp[52]) * this.scale) - 1.2f) - 0.4f;
        this.lead[69] = ((((float) this.temp[64]) * this.scale) - 1.2f) - 0.4f;
        this.lead[71] = ((((float) this.temp_Last[5]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[73] = ((((float) this.temp[5]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[75] = ((((float) this.temp[17]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[77] = ((((float) this.temp[29]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[79] = ((((float) this.temp[41]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[81] = ((((float) this.temp[53]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[83] = ((((float) this.temp[65]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[85] = ((((float) this.temp_Last[6]) * this.scale) + 4.4f) - 0.4f;
        this.lead[87] = ((((float) this.temp[6]) * this.scale) + 4.4f) - 0.4f;
        this.lead[89] = ((((float) this.temp[18]) * this.scale) + 4.4f) - 0.4f;
        this.lead[91] = ((((float) this.temp[30]) * this.scale) + 4.4f) - 0.4f;
        this.lead[93] = ((((float) this.temp[42]) * this.scale) + 4.4f) - 0.4f;
        this.lead[95] = ((((float) this.temp[54]) * this.scale) + 4.4f) - 0.4f;
        this.lead[97] = ((((float) this.temp[66]) * this.scale) + 4.4f) - 0.4f;
        this.lead[99] = ((((float) this.temp_Last[7]) * this.scale) + 3.0f) - 0.4f;
        this.lead[101] = ((((float) this.temp[7]) * this.scale) + 3.0f) - 0.4f;
        this.lead[103] = ((((float) this.temp[19]) * this.scale) + 3.0f) - 0.4f;
        this.lead[105] = ((((float) this.temp[31]) * this.scale) + 3.0f) - 0.4f;
        this.lead[107] = ((((float) this.temp[43]) * this.scale) + 3.0f) - 0.4f;
        this.lead[109] = ((((float) this.temp[55]) * this.scale) + 3.0f) - 0.4f;
        this.lead[111] = ((((float) this.temp[67]) * this.scale) + 3.0f) - 0.4f;
        this.lead[113] = ((((float) this.temp_Last[8]) * this.scale) + 1.6f) - 0.4f;
        this.lead[115] = ((((float) this.temp[8]) * this.scale) + 1.6f) - 0.4f;
        this.lead[117] = ((((float) this.temp[20]) * this.scale) + 1.6f) - 0.4f;
        this.lead[119] = ((((float) this.temp[32]) * this.scale) + 1.6f) - 0.4f;
        this.lead[121] = ((((float) this.temp[44]) * this.scale) + 1.6f) - 0.4f;
        this.lead[123] = ((((float) this.temp[56]) * this.scale) + 1.6f) - 0.4f;
        this.lead[125] = ((((float) this.temp[68]) * this.scale) + 1.6f) - 0.4f;
        this.lead[127] = ((((float) this.temp_Last[9]) * this.scale) + 0.2f) - 0.4f;
        this.lead[129] = ((((float) this.temp[9]) * this.scale) + 0.2f) - 0.4f;
        this.lead[131] = ((((float) this.temp[21]) * this.scale) + 0.2f) - 0.4f;
        this.lead[133] = ((((float) this.temp[33]) * this.scale) + 0.2f) - 0.4f;
        this.lead[135] = ((((float) this.temp[45]) * this.scale) + 0.2f) - 0.4f;
        this.lead[137] = ((((float) this.temp[57]) * this.scale) + 0.2f) - 0.4f;
        this.lead[139] = ((((float) this.temp[69]) * this.scale) + 0.2f) - 0.4f;
        this.lead[141] = ((((float) this.temp_Last[10]) * this.scale) - 1.2f) - 0.4f;
        this.lead[143] = ((((float) this.temp[10]) * this.scale) - 1.2f) - 0.4f;
        this.lead[145] = ((((float) this.temp[22]) * this.scale) - 1.2f) - 0.4f;
        this.lead[147] = ((((float) this.temp[34]) * this.scale) - 1.2f) - 0.4f;
        this.lead[149] = ((((float) this.temp[46]) * this.scale) - 1.2f) - 0.4f;
        this.lead[151] = ((((float) this.temp[58]) * this.scale) - 1.2f) - 0.4f;
        this.lead[153] = ((((float) this.temp[70]) * this.scale) - 1.2f) - 0.4f;
        this.lead[155] = ((((float) this.temp_Last[11]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[157] = ((((float) this.temp[11]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[159] = ((((float) this.temp[23]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[161] = ((((float) this.temp[35]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[163] = ((((float) this.temp[47]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[165] = ((((float) this.temp[59]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead[167] = ((((float) this.temp[71]) * this.scale) - 2.6000001f) - 0.4f;
        this.lead621[1] = ((((float) this.temp_Last[this.RhythmIndex]) * this.scale) - 4.0f) - 0.4f;
        this.lead621[3] = ((((float) this.temp[this.RhythmIndex]) * this.scale) - 4.0f) - 0.4f;
        this.lead621[5] = ((((float) this.temp[this.RhythmIndex + 12]) * this.scale) - 4.0f) - 0.4f;
        this.lead621[7] = ((((float) this.temp[this.RhythmIndex + 24]) * this.scale) - 4.0f) - 0.4f;
        this.lead621[9] = ((((float) this.temp[this.RhythmIndex + 36]) * this.scale) - 4.0f) - 0.4f;
        this.lead621[11] = ((((float) this.temp[this.RhythmIndex + 48]) * this.scale) - 4.0f) - 0.4f;
        this.lead621[13] = ((((float) this.temp[this.RhythmIndex + 60]) * this.scale) - 4.0f) - 0.4f;
        this.temp_Last[0] = this.temp[60];
        this.temp_Last[1] = this.temp[61];
        this.temp_Last[2] = this.temp[62];
        this.temp_Last[3] = this.temp[63];
        this.temp_Last[4] = this.temp[64];
        this.temp_Last[5] = this.temp[65];
        this.temp_Last[6] = this.temp[66];
        this.temp_Last[7] = this.temp[67];
        this.temp_Last[8] = this.temp[68];
        this.temp_Last[9] = this.temp[69];
        this.temp_Last[10] = this.temp[70];
        this.temp_Last[11] = this.temp[71];
        if (this.mSpeed != this.xunit) {
            XUpdate();
        }
    }

    public void UpdateEcgData26() {
        if (GLView.isGather) {
            GLView.gather.getDataEX(this.temp);
        }
        this.lead[1] = ((((float) this.temp_Last[0]) * this.scale) + 4.4f) - 0.4f;
        this.lead[3] = ((((float) this.temp[0]) * this.scale) + 4.4f) - 0.4f;
        this.lead[5] = ((((float) this.temp[12]) * this.scale) + 4.4f) - 0.4f;
        this.lead[7] = ((((float) this.temp[24]) * this.scale) + 4.4f) - 0.4f;
        this.lead[9] = ((((float) this.temp[36]) * this.scale) + 4.4f) - 0.4f;
        this.lead[11] = ((((float) this.temp[48]) * this.scale) + 4.4f) - 0.4f;
        this.lead[13] = ((((float) this.temp[60]) * this.scale) + 4.4f) - 0.4f;
        this.lead[15] = ((((float) this.temp_Last[1]) * this.scale) + 2.8f) - 0.4f;
        this.lead[17] = ((((float) this.temp[1]) * this.scale) + 2.8f) - 0.4f;
        this.lead[19] = ((((float) this.temp[13]) * this.scale) + 2.8f) - 0.4f;
        this.lead[21] = ((((float) this.temp[25]) * this.scale) + 2.8f) - 0.4f;
        this.lead[23] = ((((float) this.temp[37]) * this.scale) + 2.8f) - 0.4f;
        this.lead[25] = ((((float) this.temp[49]) * this.scale) + 2.8f) - 0.4f;
        this.lead[27] = ((((float) this.temp[61]) * this.scale) + 2.8f) - 0.4f;
        this.lead[29] = ((((float) this.temp_Last[2]) * this.scale) + 1.2f) - 0.4f;
        this.lead[31] = ((((float) this.temp[2]) * this.scale) + 1.2f) - 0.4f;
        this.lead[33] = ((((float) this.temp[14]) * this.scale) + 1.2f) - 0.4f;
        this.lead[35] = ((((float) this.temp[26]) * this.scale) + 1.2f) - 0.4f;
        this.lead[37] = ((((float) this.temp[38]) * this.scale) + 1.2f) - 0.4f;
        this.lead[39] = ((((float) this.temp[50]) * this.scale) + 1.2f) - 0.4f;
        this.lead[41] = ((((float) this.temp[62]) * this.scale) + 1.2f) - 0.4f;
        this.lead[43] = ((((float) this.temp_Last[3]) * this.scale) - 0.4f) - 0.4f;
        this.lead[45] = ((((float) this.temp[3]) * this.scale) - 0.4f) - 0.4f;
        this.lead[47] = ((((float) this.temp[15]) * this.scale) - 0.4f) - 0.4f;
        this.lead[49] = ((((float) this.temp[27]) * this.scale) - 0.4f) - 0.4f;
        this.lead[51] = ((((float) this.temp[39]) * this.scale) - 0.4f) - 0.4f;
        this.lead[53] = ((((float) this.temp[51]) * this.scale) - 0.4f) - 0.4f;
        this.lead[55] = ((((float) this.temp[63]) * this.scale) - 0.4f) - 0.4f;
        this.lead[57] = ((((float) this.temp_Last[4]) * this.scale) - 2.0f) - 0.4f;
        this.lead[59] = ((((float) this.temp[4]) * this.scale) - 2.0f) - 0.4f;
        this.lead[61] = ((((float) this.temp[16]) * this.scale) - 2.0f) - 0.4f;
        this.lead[63] = ((((float) this.temp[28]) * this.scale) - 2.0f) - 0.4f;
        this.lead[65] = ((((float) this.temp[40]) * this.scale) - 2.0f) - 0.4f;
        this.lead[67] = ((((float) this.temp[52]) * this.scale) - 2.0f) - 0.4f;
        this.lead[69] = ((((float) this.temp[64]) * this.scale) - 2.0f) - 0.4f;
        this.lead[71] = ((((float) this.temp_Last[5]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[73] = ((((float) this.temp[5]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[75] = ((((float) this.temp[17]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[77] = ((((float) this.temp[29]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[79] = ((((float) this.temp[41]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[81] = ((((float) this.temp[53]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[83] = ((((float) this.temp[65]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[85] = ((((float) this.temp_Last[6]) * this.scale) + 4.4f) - 0.4f;
        this.lead[87] = ((((float) this.temp[6]) * this.scale) + 4.4f) - 0.4f;
        this.lead[89] = ((((float) this.temp[18]) * this.scale) + 4.4f) - 0.4f;
        this.lead[91] = ((((float) this.temp[30]) * this.scale) + 4.4f) - 0.4f;
        this.lead[93] = ((((float) this.temp[42]) * this.scale) + 4.4f) - 0.4f;
        this.lead[95] = ((((float) this.temp[54]) * this.scale) + 4.4f) - 0.4f;
        this.lead[97] = ((((float) this.temp[66]) * this.scale) + 4.4f) - 0.4f;
        this.lead[99] = ((((float) this.temp_Last[7]) * this.scale) + 2.8f) - 0.4f;
        this.lead[101] = ((((float) this.temp[7]) * this.scale) + 2.8f) - 0.4f;
        this.lead[103] = ((((float) this.temp[19]) * this.scale) + 2.8f) - 0.4f;
        this.lead[105] = ((((float) this.temp[31]) * this.scale) + 2.8f) - 0.4f;
        this.lead[107] = ((((float) this.temp[43]) * this.scale) + 2.8f) - 0.4f;
        this.lead[109] = ((((float) this.temp[55]) * this.scale) + 2.8f) - 0.4f;
        this.lead[111] = ((((float) this.temp[67]) * this.scale) + 2.8f) - 0.4f;
        this.lead[113] = ((((float) this.temp_Last[8]) * this.scale) + 1.2f) - 0.4f;
        this.lead[115] = ((((float) this.temp[8]) * this.scale) + 1.2f) - 0.4f;
        this.lead[117] = ((((float) this.temp[20]) * this.scale) + 1.2f) - 0.4f;
        this.lead[119] = ((((float) this.temp[32]) * this.scale) + 1.2f) - 0.4f;
        this.lead[121] = ((((float) this.temp[44]) * this.scale) + 1.2f) - 0.4f;
        this.lead[123] = ((((float) this.temp[56]) * this.scale) + 1.2f) - 0.4f;
        this.lead[125] = ((((float) this.temp[68]) * this.scale) + 1.2f) - 0.4f;
        this.lead[127] = ((((float) this.temp_Last[9]) * this.scale) - 0.4f) - 0.4f;
        this.lead[129] = ((((float) this.temp[9]) * this.scale) - 0.4f) - 0.4f;
        this.lead[131] = ((((float) this.temp[21]) * this.scale) - 0.4f) - 0.4f;
        this.lead[133] = ((((float) this.temp[33]) * this.scale) - 0.4f) - 0.4f;
        this.lead[135] = ((((float) this.temp[45]) * this.scale) - 0.4f) - 0.4f;
        this.lead[137] = ((((float) this.temp[57]) * this.scale) - 0.4f) - 0.4f;
        this.lead[139] = ((((float) this.temp[69]) * this.scale) - 0.4f) - 0.4f;
        this.lead[141] = ((((float) this.temp_Last[10]) * this.scale) - 2.0f) - 0.4f;
        this.lead[143] = ((((float) this.temp[10]) * this.scale) - 2.0f) - 0.4f;
        this.lead[145] = ((((float) this.temp[22]) * this.scale) - 2.0f) - 0.4f;
        this.lead[147] = ((((float) this.temp[34]) * this.scale) - 2.0f) - 0.4f;
        this.lead[149] = ((((float) this.temp[46]) * this.scale) - 2.0f) - 0.4f;
        this.lead[151] = ((((float) this.temp[58]) * this.scale) - 2.0f) - 0.4f;
        this.lead[153] = ((((float) this.temp[70]) * this.scale) - 2.0f) - 0.4f;
        this.lead[155] = ((((float) this.temp_Last[11]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[157] = ((((float) this.temp[11]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[159] = ((((float) this.temp[23]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[161] = ((((float) this.temp[35]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[163] = ((((float) this.temp[47]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[165] = ((((float) this.temp[59]) * this.scale) - 3.6000001f) - 0.4f;
        this.lead[167] = ((((float) this.temp[71]) * this.scale) - 3.6000001f) - 0.4f;
        this.temp_Last[0] = this.temp[60];
        this.temp_Last[1] = this.temp[61];
        this.temp_Last[2] = this.temp[62];
        this.temp_Last[3] = this.temp[63];
        this.temp_Last[4] = this.temp[64];
        this.temp_Last[5] = this.temp[65];
        this.temp_Last[6] = this.temp[66];
        this.temp_Last[7] = this.temp[67];
        this.temp_Last[8] = this.temp[68];
        this.temp_Last[9] = this.temp[69];
        this.temp_Last[10] = this.temp[70];
        this.temp_Last[11] = this.temp[71];
        if (this.mSpeed != this.xunit) {
            XUpdate();
        }
    }

    public void setWaveColor(float r, float g, float b, float a) {
        this.colorRED = r;
        this.colorGREEN = g;
        this.colorBLUE = b;
        this.colorALPHA = a;
    }

    public void DrawWave(GL10 gl) {
        gl.glLoadIdentity();
        gl.glTranslatef(this.DX, 0.0f, -5.0f);
        gl.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
        gl.glColor4f(this.colorRED, this.colorGREEN, this.colorBLUE, this.colorALPHA);
        gl.glVertexPointer(2, 5126, 0, DrawUtils.makeFloatBuffer(this.lead));
        gl.glEnableClientState(32884);
        gl.glDrawArrays(3, 0, 7);
        gl.glDrawArrays(3, 7, 7);
        gl.glDrawArrays(3, 14, 7);
        gl.glDrawArrays(3, 21, 7);
        gl.glDrawArrays(3, 28, 7);
        gl.glDrawArrays(3, 35, 7);
        gl.glDrawArrays(3, 42, 7);
        gl.glDrawArrays(3, 49, 7);
        gl.glDrawArrays(3, 56, 7);
        gl.glDrawArrays(3, 63, 7);
        gl.glDrawArrays(3, 70, 7);
        gl.glDrawArrays(3, 77, 7);
        gl.glDisableClientState(32884);
        gl.glFinish();
        this.DX += 6.0f * this.xunit;
        if (this.DX >= 10.12f) {
            this.DX = -9.0f;
        }
    }

    public void DrawWave_62(GL10 gl, Boolean sign) {
        gl.glLoadIdentity();
        if (sign.booleanValue()) {
            gl.glTranslatef(this.DX, 0.0f, -5.0f);
        } else {
            gl.glTranslatef(this.DX62, 0.0f, -5.0f);
        }
        gl.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
        gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        gl.glVertexPointer(2, 5126, 0, DrawUtils.makeFloatBuffer(this.lead));
        gl.glEnableClientState(32884);
        if (sign.booleanValue()) {
            gl.glDrawArrays(3, 0, 7);
            gl.glDrawArrays(3, 7, 7);
            gl.glDrawArrays(3, 14, 7);
            gl.glDrawArrays(3, 21, 7);
            gl.glDrawArrays(3, 28, 7);
            gl.glDrawArrays(3, 35, 7);
        } else {
            gl.glDrawArrays(3, 42, 7);
            gl.glDrawArrays(3, 49, 7);
            gl.glDrawArrays(3, 56, 7);
            gl.glDrawArrays(3, 63, 7);
            gl.glDrawArrays(3, 70, 7);
            gl.glDrawArrays(3, 77, 7);
        }
        gl.glDisableClientState(32884);
        gl.glFinish();
        if (sign.booleanValue()) {
            this.DX += 6.0f * this.xunit;
            if (this.DX >= -0.3f) {
                this.DX = -9.0f;
                return;
            }
            return;
        }
        this.DX62 += 6.0f * this.xunit;
        if (this.DX62 >= 10.0f) {
            this.DX62 = 1.3f;
        }
    }

    public void DrawWave_621(GL10 gl, int sign) {
        gl.glLoadIdentity();
        gl.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
        gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        FloatBuffer verBuffer = DrawUtils.makeFloatBuffer(this.lead);
        FloatBuffer verBuffer621 = DrawUtils.makeFloatBuffer(this.lead621);
        if (sign == 0) {
            gl.glTranslatef(this.DX, 0.0f, -5.0f);
            gl.glVertexPointer(2, 5126, 0, verBuffer);
        } else if (sign == 1) {
            gl.glTranslatef(this.DX62, 0.0f, -5.0f);
            gl.glVertexPointer(2, 5126, 0, verBuffer);
        } else if (sign == 2) {
            gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
            gl.glTranslatef(this.DX621, 0.0f, -5.0f);
            gl.glVertexPointer(2, 5126, 0, verBuffer621);
        }
        gl.glEnableClientState(32884);
        if (sign == 0) {
            gl.glDrawArrays(3, 0, 7);
            gl.glDrawArrays(3, 7, 7);
            gl.glDrawArrays(3, 14, 7);
            gl.glDrawArrays(3, 21, 7);
            gl.glDrawArrays(3, 28, 7);
            gl.glDrawArrays(3, 35, 7);
        } else if (sign == 1) {
            gl.glDrawArrays(3, 42, 7);
            gl.glDrawArrays(3, 49, 7);
            gl.glDrawArrays(3, 56, 7);
            gl.glDrawArrays(3, 63, 7);
            gl.glDrawArrays(3, 70, 7);
            gl.glDrawArrays(3, 77, 7);
        } else if (sign == 2) {
            gl.glDrawArrays(3, 0, 7);
        }
        gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        gl.glDisableClientState(32884);
        gl.glFinish();
        if (sign == 0) {
            this.DX += 6.0f * this.xunit;
            if (this.DX >= -0.3f) {
                this.DX = -9.0f;
            }
        } else if (sign == 1) {
            this.DX62 += 6.0f * this.xunit;
            if (this.DX62 >= 10.0f) {
                this.DX62 = 1.3f;
            }
        } else if (sign == 2) {
            this.DX621 += 6.0f * this.xunit;
            if (this.DX621 >= 10.0f) {
                this.DX621 = -9.0f;
            }
        }
    }

    public void DrawWave_26(GL10 gl, int sign) {
        gl.glLoadIdentity();
        gl.glTranslatef(this.DX, 0.0f, -5.0f);
        gl.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
        gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        gl.glVertexPointer(2, 5126, 0, DrawUtils.makeFloatBuffer(this.lead));
        gl.glEnableClientState(32884);
        if (sign == 0) {
            gl.glDrawArrays(3, 0, 7);
            gl.glDrawArrays(3, 7, 7);
            gl.glDrawArrays(3, 14, 7);
            gl.glDrawArrays(3, 21, 7);
            gl.glDrawArrays(3, 28, 7);
            gl.glDrawArrays(3, 35, 7);
        } else {
            gl.glDrawArrays(3, 42, 7);
            gl.glDrawArrays(3, 49, 7);
            gl.glDrawArrays(3, 56, 7);
            gl.glDrawArrays(3, 63, 7);
            gl.glDrawArrays(3, 70, 7);
            gl.glDrawArrays(3, 77, 7);
        }
        gl.glDisableClientState(32884);
        gl.glFinish();
        this.DX += 6.0f * this.xunit;
        if (this.DX >= 10.12f) {
            this.DX = -9.0f;
        }
    }

    public void ClearScreen(GL10 gl) {
        gl.glLoadIdentity();
        gl.glTranslatef(-10.5f, 0.0f, -5.0f);
        gl.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glVertexPointer(2, 5126, 0, DrawUtils.makeFloatBuffer(this.rectScreen));
        gl.glEnableClientState(32884);
        gl.glDrawArrays(4, 0, 3);
        gl.glDrawArrays(4, 3, 3);
        gl.glDisableClientState(32884);
        gl.glFinish();
    }

    public void ClearRect(GL10 gl) {
        gl.glLoadIdentity();
        gl.glTranslatef(this.CX, 0.0f, -5.0f);
        gl.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glVertexPointer(2, 5126, 0, DrawUtils.makeFloatBuffer(this.rect));
        gl.glEnableClientState(32884);
        gl.glDrawArrays(4, 0, 3);
        gl.glDrawArrays(4, 3, 3);
        gl.glDisableClientState(32884);
        gl.glFinish();
        this.CX += 6.0f * this.xunit;
        if (this.CX >= 10.12f) {
            this.CX = -9.0f;
        }
    }

    public void ClearRect62(GL10 gl, boolean sign) {
        gl.glLoadIdentity();
        if (sign) {
            gl.glTranslatef(this.CX, 0.0f, -5.0f);
        } else {
            gl.glTranslatef(this.CX62, 0.0f, -5.0f);
        }
        gl.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glVertexPointer(2, 5126, 0, DrawUtils.makeFloatBuffer(this.rect));
        gl.glEnableClientState(32884);
        gl.glDrawArrays(4, 0, 3);
        gl.glDrawArrays(4, 3, 3);
        gl.glDisableClientState(32884);
        gl.glFinish();
        if (sign) {
            this.CX += this.xunit * 6.0f;
            if (this.CX >= -0.3f) {
                this.CX = -9.0f;
                return;
            }
            return;
        }
        this.CX62 += this.xunit * 6.0f;
        if (this.CX62 >= 10.0f) {
            this.CX62 = 1.3f;
        }
    }

    public void ClearRect621(GL10 gl, int sign) {
        gl.glLoadIdentity();
        gl.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        FloatBuffer verBuffer = DrawUtils.makeFloatBuffer(this.rect621);
        FloatBuffer verBuffer621 = DrawUtils.makeFloatBuffer(this.rect621c);
        gl.glEnableClientState(32884);
        if (sign == 0) {
            gl.glTranslatef(this.CX, 0.0f, -5.0f);
            gl.glVertexPointer(2, 5126, 0, verBuffer);
        } else if (sign == 1) {
            gl.glTranslatef(this.CX62, 0.0f, -5.0f);
            gl.glVertexPointer(2, 5126, 0, verBuffer);
        } else if (sign == 2) {
            gl.glTranslatef(this.CX621, 0.0f, -5.0f);
            gl.glVertexPointer(2, 5126, 0, verBuffer621);
        }
        gl.glDrawArrays(4, 0, 3);
        gl.glDrawArrays(4, 3, 3);
        gl.glDisableClientState(32884);
        gl.glFinish();
        if (sign == 0) {
            this.CX += this.xunit * 6.0f;
            if (this.CX >= -0.3f) {
                this.CX = -9.0f;
            }
        } else if (sign == 1) {
            this.CX62 += this.xunit * 6.0f;
            if (this.CX62 >= 10.0f) {
                this.CX62 = 1.3f;
            }
        } else if (sign == 2) {
            this.CX621 += this.xunit * 6.0f;
            if (this.CX621 >= 10.0f) {
                this.CX621 = -9.0f;
            }
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        String vendor = gl.glGetString(7936);
        if (vendor.equals("Qualcomm")) {
            this.IsQualcomm = true;
            String model = Build.MODEL;
            if (model.contains("SM-N900") || model.contains("MI 4")) {
                this.IsNote3_XiaoMi4 = true;
            }
        } else {
            this.IsQualcomm = false;
        }
        if (vendor.equals("ARM") || vendor.equals("NVIDIA Corporation") || vendor.equals("Qualcomm")) {
            GLJNILIB.setPreserveAttrib();
        }
        this.glWidth = width;
        this.glHeight = height;
        float f = ((float) height) / ((float) width);
        Log.e("w+h", String.valueOf(width) + "+" + height);
        this.DX = -9.0f;
        this.CX = -10.5f;
        this.DX62 = 1.3f;
        this.CX62 = 1.7f;
        this.DX621 = -9.0f;
        this.CX621 = -8.6f;
        this.IsDrawFont = true;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        gl.glFrustumf(-2.05f, 2.05f, -1.0f, 1.0f, 1.0f, 10.0f);
        gl.glMatrixMode(5888);
        gl.glLoadIdentity();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.IsDrawFont = true;
        gl.glClear(16640);
        Log.e("MyRenderer", "onSurfaceCreated");
        gl.glHint(3152, 4353);
        gl.glShadeModel(7425);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepthf(1.0f);
        gl.glLineWidth(1.75f);
        gl.glEnable(2929);
        gl.glDepthFunc(Constants.UPDATA_NOTIFITION_SUM_STEPS);
        gl.glEnable(2848);
        gl.glHint(3154, 4354);
    }

    public void XUpdate() {
        this.xunit = this.mSpeed;
        for (int i = 0; i < 168; i += 14) {
            this.lead[i] = -3.0f * this.xunit;
            this.lead[i + 2] = -2.0f * this.xunit;
            this.lead[i + 4] = -this.xunit;
            this.lead[i + 8] = this.xunit;
            this.lead[i + 10] = 2.0f * this.xunit;
            this.lead[i + 12] = 3.0f * this.xunit;
        }
        this.lead621[0] = -3.0f * this.xunit;
        this.lead621[2] = -2.0f * this.xunit;
        this.lead621[4] = -this.xunit;
        this.lead621[8] = this.xunit;
        this.lead621[10] = 2.0f * this.xunit;
        this.lead621[12] = 3.0f * this.xunit;
        this.rect[0] = this.xunit * -6.0f;
        this.rect[2] = this.xunit * 6.0f;
        this.rect[4] = this.xunit * 6.0f;
        this.rect[6] = this.xunit * -6.0f;
        this.rect[8] = this.xunit * 6.0f;
        this.rect[10] = this.xunit * -6.0f;
        this.rect621[0] = this.xunit * -6.0f;
        this.rect621[2] = this.xunit * 6.0f;
        this.rect621[4] = this.xunit * 6.0f;
        this.rect621[6] = this.xunit * -6.0f;
        this.rect621[8] = this.xunit * 6.0f;
        this.rect621[10] = this.xunit * -6.0f;
        this.rect621c[0] = this.xunit * -6.0f;
        this.rect621c[2] = this.xunit * 6.0f;
        this.rect621c[4] = this.xunit * 6.0f;
        this.rect621c[6] = this.xunit * -6.0f;
        this.rect621c[8] = this.xunit * 6.0f;
        this.rect621c[10] = this.xunit * -6.0f;
    }
}
