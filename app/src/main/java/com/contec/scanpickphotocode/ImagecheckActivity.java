package com.contec.scanpickphotocode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.contec.phms.R;
import com.contec.scanpickphoto.GridviewAdapter;
import com.contec.scanpickphoto.ImageFloder;
import com.contec.scanpickphoto.ListviewAdapter;
import com.zxing.android.CaptureActivity;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class ImagecheckActivity extends Activity {
    private AdapterView.OnItemClickListener ClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (ImagecheckActivity.this.v != null) {
                ImagecheckActivity.this.v.setBackgroundColor(0);
            } else {
                ImagecheckActivity.this.v = ListviewAdapter.v.getChildAt(0);
                ImagecheckActivity.this.v.setBackgroundColor(0);
            }
            arg1.setBackgroundColor(Color.parseColor("#87CEFF"));
            ImagecheckActivity.this.v = arg1;
            ((ImageFloder) ImagecheckActivity.this.mImageFloders.get(ImagecheckActivity.this.item)).setFlag(false);
            ((ImageFloder) ImagecheckActivity.this.mImageFloders.get(arg2)).setFlag(true);
            ImagecheckActivity.this.item = arg2;
            ImagecheckActivity.this.mImgDir = new File(((ImageFloder) ImagecheckActivity.this.mImageFloders.get(arg2)).getDir());
            ImagecheckActivity.this.mImgs = Arrays.asList(ImagecheckActivity.this.mImgDir.list(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                        return true;
                    }
                    return false;
                }
            }));
            ImagecheckActivity.this.mAdapter = new GridviewAdapter(ImagecheckActivity.this.getApplicationContext(), ImagecheckActivity.this.mImgs, R.layout.grid_scan_pick_photo, ImagecheckActivity.this.mImgDir.getAbsolutePath());
            ImagecheckActivity.this.mGirdView.setAdapter(ImagecheckActivity.this.mAdapter);
            ImageFloder i = new ImageFloder();
            i.setDir(ImagecheckActivity.this.mImgDir.getAbsolutePath());
            ImagecheckActivity.this.mDirtext.setText(i.getName());
        }
    };
    private Myalbumlist albumlist;
    private View.OnClickListener clickkeyListener = new View.OnClickListener() {
        public void onClick(View arg0) {
            ImagecheckActivity.this.pop.dismiss();
        }
    };
    private int item = 0;
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            Intent intent = new Intent();
            intent.putExtra(CaptureActivity.QR_RESULT, String.valueOf(ImagecheckActivity.this.mImgDir.getAbsolutePath()) + CookieSpec.PATH_DELIM + ((String) ImagecheckActivity.this.mImgs.get(arg2)));
            ImagecheckActivity.this.setResult(-1, intent);
            ImagecheckActivity.this.finish();
        }
    };
    private GridviewAdapter mAdapter;
    private RelativeLayout mBottomLy;
    private HashSet<String> mDirPaths = new HashSet<>();
    private TextView mDirtext;
    private GridView mGirdView;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            ImagecheckActivity.this.mProgressDialog.dismiss();
            ImagecheckActivity.this.data2View();
            ImagecheckActivity.this.initListPopupWindw();
        }
    };
    private TextView mImageCount;
    private List<ImageFloder> mImageFloders = new ArrayList();
    private File mImgDir;
    private List<String> mImgs;
    private ProgressDialog mProgressDialog;
    private int mScreenHeight;
    private ImageButton mcelbutton;
    private Boolean oneflag = true;
    private PopupWindow pop;
    private ImageButton rightbutton;
    private View.OnClickListener scan = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.dirButton) {
                ImagecheckActivity.this.pop.showAtLocation(ImagecheckActivity.this.findViewById(R.id.imagelayout), 3, 0, 0);
            } else if (v.getId() == R.id.ceButton) {
                ImagecheckActivity.this.finish();
            }
        }
    };
    int totalCount = 0;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() != 4) {
                return false;
            }
            ImagecheckActivity.this.pop.dismiss();
            return false;
        }
    };

    View v = null;

    private void data2View() {
        if (this.mImgDir == null) {
            Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到", Toast.LENGTH_SHORT).show();
            return;
        }
        this.mImgs = Arrays.asList(this.mImgDir.list());
        this.mAdapter = new GridviewAdapter(getApplicationContext(), this.mImgs, R.layout.grid_scan_pick_photo, this.mImgDir.getAbsolutePath());
        this.mGirdView.setAdapter(this.mAdapter);
        ImageFloder i = new ImageFloder();
        i.setDir(this.mImgDir.getAbsolutePath());
        this.mDirtext.setText(i.getName());
    }

    protected void initListPopupWindw() {
        int wwidth = ((WindowManager) getSystemService("window")).getDefaultDisplay().getWidth();
        this.albumlist = new Myalbumlist(this);
        this.pop = this.albumlist.getMenu(getApplicationContext(), wwidth, this.mImageFloders, this.clickkeyListener, this.ClickListener);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_imagecheck);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        this.mScreenHeight = outMetrics.heightPixels;
        initView();
        getImages();
    }

    private void getImages() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            Toast.makeText(this, "No external storage", Toast.LENGTH_SHORT).show();
            return;
        }
        this.mProgressDialog = ProgressDialog.show(this, (CharSequence) null, "Loading...");
        new Thread(new Runnable() {
            public void run() {
                String firstImage = null;
                Cursor mCursor = ImagecheckActivity.this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (String[]) null, "mime_type=? or mime_type=?", new String[]{"image/jpeg", "image/png"}, "date_modified");
                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndex("_data"));
                    if (firstImage == null) {
                        firstImage = path;
                    }
                    File parentFile = new File(path).getParentFile();
                    if (parentFile != null) {
                        String dirPath = parentFile.getAbsolutePath();
                        if (!ImagecheckActivity.this.mDirPaths.contains(dirPath)) {
                            ImagecheckActivity.this.mDirPaths.add(dirPath);
                            ImageFloder imageFloder = new ImageFloder();
                            imageFloder.setDir(dirPath);
                            imageFloder.setFirstImagePath(path);
                            imageFloder.setFlag(true);
                            if (parentFile.list() != null) {
                                int picSize = parentFile.list(new FilenameFilter() {
                                    public boolean accept(File dir, String filename) {
                                        if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                                            return true;
                                        }
                                        return false;
                                    }
                                }).length;
                                ImagecheckActivity.this.totalCount += picSize;
                                imageFloder.setCount(picSize);
                                if (ImagecheckActivity.this.oneflag.booleanValue()) {
                                    ImagecheckActivity.this.oneflag = false;
                                    imageFloder.setFlag(true);
                                    ImagecheckActivity.this.mImgDir = parentFile;
                                } else {
                                    imageFloder.setFlag(false);
                                }
                                ImagecheckActivity.this.mImageFloders.add(imageFloder);
                            }
                        }
                    }
                }
                mCursor.close();
                ImagecheckActivity.this.mDirPaths = null;
                ImagecheckActivity.this.mHandler.sendEmptyMessage(272);
            }
        }).start();
    }

    private void initView() {
        this.mGirdView = (GridView) findViewById(R.id.imagegridview);
        this.mDirtext = (TextView) findViewById(R.id.dirtext);
        this.mcelbutton = (ImageButton) findViewById(R.id.ceButton);
        this.rightbutton = (ImageButton) findViewById(R.id.dirButton);
        this.mcelbutton.setOnClickListener(this.scan);
        this.rightbutton.setOnClickListener(this.scan);
        this.mGirdView.setOnItemClickListener(this.listener);
    }
}
