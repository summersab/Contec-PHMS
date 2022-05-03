package com.zxing.android.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import java.io.IOException;

public final class CameraManager {
    public static final String KEY_REVERSE_IMAGE = "preferences_reverse_image";
    private static final int MAX_FRAME_HEIGHT = 400;
    private static final int MAX_FRAME_WIDTH = 400;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MIN_FRAME_WIDTH = 240;
    private static final String TAG = CameraManager.class.getSimpleName();
    private final AutoFocusCallback autoFocusCallback = new AutoFocusCallback();
    private Camera camera;
    private final CameraConfigurationManager configManager = null;
    private final Context context;
    private Rect framingRect;
    private Rect framingRectInPreview;
    private boolean initialized;
    private final PreviewCallback previewCallback = new PreviewCallback(this.configManager);
    private boolean previewing;
    private int requestedFramingRectHeight;
    private int requestedFramingRectWidth;
    private boolean reverseImage;

    public CameraManager(Context context2) {
        this.context = context2;
        //this.configManager = new CameraConfigurationManager(context2);
    }

    public void openDriver(SurfaceHolder holder) throws IOException {
        Camera theCamera = this.camera;
        if (theCamera == null) {
            theCamera = Camera.open();
            if (theCamera == null) {
                throw new IOException();
            }
            this.camera = theCamera;
        }
        theCamera.setPreviewDisplay(holder);
        if (!this.initialized) {
            this.initialized = true;
            this.configManager.initFromCameraParameters(theCamera);
            if (this.requestedFramingRectWidth > 0 && this.requestedFramingRectHeight > 0) {
                setManualFramingRect(this.requestedFramingRectWidth, this.requestedFramingRectHeight);
                this.requestedFramingRectWidth = 0;
                this.requestedFramingRectHeight = 0;
            }
        }
        this.configManager.setDesiredCameraParameters(theCamera);
        this.reverseImage = PreferenceManager.getDefaultSharedPreferences(this.context).getBoolean(KEY_REVERSE_IMAGE, false);
    }

    public void closeDriver() {
        if (this.camera != null) {
            this.camera.release();
            this.camera = null;
            this.framingRect = null;
            this.framingRectInPreview = null;
        }
    }

    public void startPreview() {
        Camera theCamera = this.camera;
        if (theCamera != null && !this.previewing) {
            theCamera.startPreview();
            this.previewing = true;
        }
    }

    public void stopPreview() {
        if (this.camera != null && this.previewing) {
            this.camera.stopPreview();
            this.previewCallback.setHandler((Handler) null, 0);
            this.autoFocusCallback.setHandler((Handler) null, 0);
            this.previewing = false;
        }
    }

    public void requestPreviewFrame(Handler handler, int message) {
        Camera theCamera = this.camera;
        if (theCamera != null && this.previewing) {
            this.previewCallback.setHandler(handler, message);
            theCamera.setOneShotPreviewCallback(this.previewCallback);
        }
    }

    public void requestAutoFocus(Handler handler, int message) {
        if (this.camera != null && this.previewing) {
            this.autoFocusCallback.setHandler(handler, message);
            try {
                this.camera.autoFocus(this.autoFocusCallback);
            } catch (RuntimeException re) {
                Log.w(TAG, "Unexpected exception while focusing", re);
            }
        }
    }

    public Rect getFramingRect() {
        if (this.framingRect == null) {
            if (this.camera == null) {
                return null;
            }
            Point screenResolution = this.configManager.getScreenResolution();
            int width = (screenResolution.x * 3) / 4;
            if (width < 240) {
                width = 240;
            } else if (width > 400) {
                width = 400;
            }
            int height = (screenResolution.y * 3) / 4;
            if (height < 240) {
                height = 240;
            } else if (height > 400) {
                height = 400;
            }
            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = ((screenResolution.y - height) * 2) / 5;
            this.framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
            Log.d(TAG, "Calculated framing rect: " + this.framingRect);
        }
        return this.framingRect;
    }

    public Rect getFramingRectInPreview() {
        if (this.framingRectInPreview == null) {
            Rect framingRect2 = getFramingRect();
            if (framingRect2 == null) {
                return null;
            }
            Rect rect = new Rect(framingRect2);
            Point cameraResolution = this.configManager.getCameraResolution();
            Point screenResolution = this.configManager.getScreenResolution();
            rect.left = (rect.left * cameraResolution.y) / screenResolution.x;
            rect.right = (rect.right * cameraResolution.y) / screenResolution.x;
            rect.top = (rect.top * cameraResolution.x) / screenResolution.y;
            rect.bottom = (rect.bottom * cameraResolution.x) / screenResolution.y;
            this.framingRectInPreview = rect;
        }
        return this.framingRectInPreview;
    }

    public void setManualFramingRect(int width, int height) {
        if (this.initialized) {
            Point screenResolution = this.configManager.getScreenResolution();
            if (width > screenResolution.x) {
                width = screenResolution.x;
            }
            if (height > screenResolution.y) {
                height = screenResolution.y;
            }
            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 2;
            this.framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
            Log.d(TAG, "Calculated manual framing rect: " + this.framingRect);
            this.framingRectInPreview = null;
            return;
        }
        this.requestedFramingRectWidth = width;
        this.requestedFramingRectHeight = height;
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview();
        if (rect == null) {
            return null;
        }
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), this.reverseImage);
    }

    public void openFlashLight() {
        if (this.camera != null) {
            Camera.Parameters parameter = this.camera.getParameters();
            parameter.setFlashMode("torch");
            this.camera.setParameters(parameter);
        }
    }

    public void offFlashLight() {
        if (this.camera != null) {
            Camera.Parameters parameter = this.camera.getParameters();
            parameter.setFlashMode("off");
            this.camera.setParameters(parameter);
        }
    }

    public void switchFlashLight() {
        if (this.camera != null) {
            Camera.Parameters parameter = this.camera.getParameters();
            if (parameter.getFlashMode().equals("torch")) {
                parameter.setFlashMode("off");
            } else {
                parameter.setFlashMode("torch");
            }
            this.camera.setParameters(parameter);
        }
    }
}
