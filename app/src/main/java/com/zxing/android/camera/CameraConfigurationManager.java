package com.zxing.android.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.util.Collection;
import java.util.Iterator;

final class CameraConfigurationManager {
    public static final String KEY_FRONT_LIGHT = "preferences_front_light";
    private static final int MAX_PREVIEW_PIXELS = 384000;
    private static final int MIN_PREVIEW_PIXELS = 76800;
    private static final String TAG = "CameraConfiguration";
    private Point cameraResolution;
    private final Context context;
    private Point screenResolution;

    CameraConfigurationManager(Context context2) {
        this.context = context2;
    }

    void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Display display = ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        if (width < height) {
            Log.i(TAG, "Display reports portrait orientation; assuming this is incorrect");
            int temp = width;
            width = height;
            height = temp;
        }
        this.screenResolution = new Point(height, width);
        Log.i(TAG, "Screen resolution: " + this.screenResolution);
        this.cameraResolution = findBestPreviewSizeValue(parameters, new Point(width, height), false);
        Log.i(TAG, "Camera resolution: " + this.cameraResolution);
    }

    void setDesiredCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
            return;
        }
        initializeTorch(parameters, PreferenceManager.getDefaultSharedPreferences(this.context));
        String focusMode = findSettableValue(parameters.getSupportedFocusModes(), "auto", "macro");
        if (focusMode != null) {
            parameters.setFocusMode(focusMode);
        }
        parameters.setPreviewSize(this.cameraResolution.x, this.cameraResolution.y);
        camera.setDisplayOrientation(90);
        camera.setParameters(parameters);
    }

    Point getCameraResolution() {
        return this.cameraResolution;
    }

    Point getScreenResolution() {
        return this.screenResolution;
    }

    void setTorch(Camera camera, boolean newSetting) {
        Camera.Parameters parameters = camera.getParameters();
        doSetTorch(parameters, newSetting);
        camera.setParameters(parameters);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        if (prefs.getBoolean(KEY_FRONT_LIGHT, false) != newSetting) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_FRONT_LIGHT, newSetting);
            editor.commit();
        }
    }

    private static void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs) {
        doSetTorch(parameters, prefs.getBoolean(KEY_FRONT_LIGHT, false));
    }

    private static void doSetTorch(Camera.Parameters parameters, boolean newSetting) {
        String flashMode;
        if (newSetting) {
            flashMode = findSettableValue(parameters.getSupportedFlashModes(), "torch", "on");
        } else {
            flashMode = findSettableValue(parameters.getSupportedFlashModes(), "off");
        }
        if (flashMode != null) {
            parameters.setFlashMode(flashMode);
        }
    }

    private static Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution2, boolean portrait) {
        Point bestSize = null;
        int diff = Integer.MAX_VALUE;
        Iterator<Camera.Size> it = parameters.getSupportedPreviewSizes().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Camera.Size supportedPreviewSize = it.next();
            int pixels = supportedPreviewSize.height * supportedPreviewSize.width;
            if (pixels >= MIN_PREVIEW_PIXELS && pixels <= MAX_PREVIEW_PIXELS) {
                int supportedWidth = portrait ? supportedPreviewSize.height : supportedPreviewSize.width;
                int supportedHeight = portrait ? supportedPreviewSize.width : supportedPreviewSize.height;
                int newDiff = Math.abs((screenResolution2.x * supportedHeight) - (screenResolution2.y * supportedWidth));
                if (newDiff == 0) {
                    bestSize = new Point(supportedWidth, supportedHeight);
                    break;
                } else if (newDiff < diff) {
                    bestSize = new Point(supportedWidth, supportedHeight);
                    diff = newDiff;
                }
            }
        }
        if (bestSize != null) {
            return bestSize;
        }
        Camera.Size defaultSize = parameters.getPreviewSize();
        return new Point(defaultSize.width, defaultSize.height);
    }

    private static String findSettableValue(Collection<String> supportedValues, String... desiredValues) {
        Log.i(TAG, "Supported values: " + supportedValues);
        String result = null;
        if (supportedValues != null) {
            int length = desiredValues.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String desiredValue = desiredValues[i];
                if (supportedValues.contains(desiredValue)) {
                    result = desiredValue;
                    break;
                }
                i++;
            }
        }
        Log.i(TAG, "Settable value: " + result);
        return result;
    }
}
