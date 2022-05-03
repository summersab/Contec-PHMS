package com.zxing.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.contec.phms.R;
import com.contec.phms.db.localdata.PluseDataDao;
import com.contec.scanpickphotocode.ImagecheckActivity;
import com.contec.scanpickphotocode.QrcodeActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.zxing.android.camera.CameraManager;
import com.zxing.android.decoding.CaptureActivityHandler;
import com.zxing.android.decoding.InactivityTimer;
import com.zxing.android.decoding.RGBLuminanceSource;
import com.zxing.android.view.ViewfinderView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class CaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final float BEEP_VOLUME = 0.1f;
    private static final int PARSE_BARCODE_FAIL = 303;
    public static final String QR_RESULT = "RESULT";
    private static final long VIBRATE_DURATION = 200;
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private View.OnClickListener buttonlistener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.closeButton:
                    CaptureActivity.this.finish();
                    return;
                case R.id.photoButton:
                    CaptureActivity.this.startActivityForResult(new Intent(CaptureActivity.this, ImagecheckActivity.class), 0);
                    return;
                default:
                    return;
            }
        }
    };
    CameraManager cameraManager;
    private String characterSet;
    private TextView closeb = null;
    private Vector<BarcodeFormat> decodeFormats;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CaptureActivity.this.mProgress.dismiss();
            switch (msg.what) {
                case 303:
                    Toast.makeText(CaptureActivity.this, "Not recognized", 1).show();
                    return;
                default:
                    return;
            }
        }
    };
    private ProgressDialog mProgress;
    private MediaPlayer mediaPlayer;
    MultiFormatReader multiFormatReader;
    private String photo_path;
    private TextView photob = null;
    private boolean playBeep;
    private Bitmap scanBitmap;
    private SurfaceView surfaceView;
    private boolean vibrate;
    private ViewfinderView viewfinderView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_capture);
        this.surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        this.viewfinderView = (ViewfinderView) findViewById(R.id.viewfinderview);
        this.closeb = (TextView) findViewById(R.id.closeButton);
        this.photob = (TextView) findViewById(R.id.photoButton);
        this.closeb.setOnClickListener(this.buttonlistener);
        this.photob.setOnClickListener(this.buttonlistener);
        getWindow().addFlags(128);
        this.hasSurface = false;
        this.inactivityTimer = new InactivityTimer(this);
    }

    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != 1) {
            setRequestedOrientation(1);
        }
        this.cameraManager = new CameraManager(getApplication());
        this.viewfinderView.setCameraManager(this.cameraManager);
        SurfaceHolder surfaceHolder = this.surfaceView.getHolder();
        if (this.hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(3);
        }
        this.decodeFormats = null;
        this.characterSet = null;
        this.playBeep = true;
        if (((AudioManager) getSystemService("audio")).getRingerMode() != 2) {
            this.playBeep = false;
        }
        initBeepSound();
        this.vibrate = true;
    }

    protected void onPause() {
        super.onPause();
        if (this.handler != null) {
            this.handler.quitSynchronously();
            this.handler = null;
        }
        this.cameraManager.closeDriver();
    }

    protected void onDestroy() {
        this.inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            this.cameraManager.openDriver(surfaceHolder);
            if (this.handler == null) {
                this.handler = new CaptureActivityHandler(this, this.decodeFormats, this.characterSet);
            }
        } catch (IOException e) {
        } catch (RuntimeException e2) {
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!this.hasSurface) {
            this.hasSurface = true;
            initCamera(holder);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.hasSurface = false;
    }

    public CameraManager getCameraManager() {
        return this.cameraManager;
    }

    public ViewfinderView getViewfinderView() {
        return this.viewfinderView;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void drawViewfinder() {
        this.viewfinderView.drawViewfinder();
    }

    public void handleDecode(Result obj, Bitmap barcode) {
        this.inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        Intent intent = new Intent();
        intent.putExtra(PluseDataDao.RESULT, obj.getText());
        setResult(-1, intent);
        finish();
        finish();
    }

    private void dealwithmessage(Result obj) {
        String Barcode = obj.getBarcodeFormat().toString();
        String Text = obj.getText().toString();
        if (!"QR_CODE".equals(Barcode) && !"DATA_MATRIX".equals(Barcode)) {
            return;
        }
        if (Text.length() <= 7) {
            Intent intent = new Intent();
            intent.setClass(this, QrcodeActivity.class);
            intent.putExtra("string", Text);
            startActivity(intent);
        } else if ("http://".equals(Text.substring(0, 7))) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Text)));
        } else {
            Intent intent2 = new Intent();
            intent2.setClass(this, QrcodeActivity.class);
            intent2.putExtra("string", Text);
            startActivity(intent2);
        }
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (this.handler != null) {
            this.handler.sendEmptyMessageDelayed(8, delayMS);
        }
    }

    private void playBeepSoundAndVibrate() {
        if (this.playBeep && this.mediaPlayer != null) {
            this.mediaPlayer.start();
        }
        if (this.vibrate) {
            ((Vibrator) getSystemService("vibrator")).vibrate(VIBRATE_DURATION);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            setResult(0);
            finish();
            return true;
        } else if (keyCode == 80 || keyCode == 27) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void initBeepSound() {
        if (this.playBeep && this.mediaPlayer == null) {
            setVolumeControlStream(3);
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setAudioStreamType(3);
            this.mediaPlayer.setOnCompletionListener(this.beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                this.mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                this.mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                this.mediaPlayer.prepare();
            } catch (IOException e) {
                this.mediaPlayer = null;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            this.photo_path = data.getStringExtra(QR_RESULT);
            System.out.println("-----" + this.photo_path);
            this.mProgress = new ProgressDialog(this);
            this.mProgress.setMessage("Scanning...");
            this.mProgress.setCancelable(false);
            this.mProgress.show();
            new Thread(new Runnable() {
                public void run() {
                    Result result = CaptureActivity.this.scanningImage(CaptureActivity.this.photo_path);
                    if (result != null) {
                        CaptureActivity.this.dealwithmessage(result);
                        CaptureActivity.this.mProgress.dismiss();
                        CaptureActivity.this.finish();
                        return;
                    }
                    CaptureActivity.this.mProgress.dismiss();
                    Message m = CaptureActivity.this.mHandler.obtainMessage();
                    m.what = 303;
                    CaptureActivity.this.mHandler.sendMessage(m);
                }
            }).start();
        }
    }

    public Result scanningImage(String path) {
        Result result = null;
        if (!TextUtils.isEmpty(path)) {
            this.multiFormatReader = new MultiFormatReader();
            Hashtable<DecodeHintType, String> hints = new Hashtable<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
            this.multiFormatReader.setHints(hints);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            this.scanBitmap = BitmapFactory.decodeFile(path, options);
            options.inJustDecodeBounds = false;
            if (((int) (((float) options.outHeight) / 200.0f)) <= 0) {
            }
            this.scanBitmap = BitmapFactory.decodeFile(path, options);
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(this.scanBitmap)));
            int width = this.scanBitmap.getWidth();
            int height = this.scanBitmap.getHeight();
            try {
                result = this.multiFormatReader.decodeWithState(bitmap1);
            } catch (ReaderException e) {
            } finally {
                this.multiFormatReader.reset();
            }
        }
        return result;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
