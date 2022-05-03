package serial.jni;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class GLView extends GLSurfaceView {
    public static SerialPort gather = null;
    public static boolean isGather = false;

    private BackgroundUtils bk = new BackgroundUtils();
    private MyRenderer myRenderer;
    private int vHeight;
    private int vWidth;

    public GLView(Context context, AttributeSet attr) {
        super(context, attr);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.myRenderer = new MyRenderer();
        setRenderer(this.myRenderer);
        setRenderMode(0);
        new WaveRenderer().start();
    }

    public void setGather(DataUtils tmp) {
        gather = tmp.getGather();
    }

    public void setRendererColor(float r, float g, float b, float a) {
        this.myRenderer.setWaveColor(r, g, b, a);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        super.surfaceChanged(holder, format, width, height);
        this.vWidth = width;
        this.vHeight = height;
    }

    public void setBackground(int colorBK, int colorGrid) {
        setBackgroundDrawable(this.bk.getBackgroundDrawable(this.vWidth, this.vHeight, colorBK, colorGrid));
    }

    class QRSRender implements Runnable {
        QRSRender() {
        }

        public void run() {
            while (true) {
                GLView.this.requestRender();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class WaveRenderer extends Thread {
        WaveRenderer() {
        }

        public void run() {
            try {
                sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            while (true) {
                GLView.this.requestRender();
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
