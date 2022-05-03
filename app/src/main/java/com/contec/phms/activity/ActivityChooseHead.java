package com.contec.phms.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.fragment.ImageHead;
import com.contec.phms.util.FileOperation;
//import com.umeng.analytics.MobclickAgent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class ActivityChooseHead extends Activity {
    public static int isSelectPic = -1;
    public static int isSelectPicSec = -2;
    private final int BEFORE_RETURN = 2;
    private GridView choosehead_grid;
    String headName = "imagehead.jpg";
    private ArrayList<ImageHead> imageHeadArray;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    Intent bintent = new Intent(ActivityChooseHead.this, MainActivityNew.class);
                    bintent.putExtra("grid_item", ((ImageHead) ActivityChooseHead.this.imageHeadArray.get(msg.arg1)).getImage_head());
                    ActivityChooseHead.this.setResult(2, bintent);
                    ActivityChooseHead.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private ImageHeadAdapter mImageHeadAdapter;
    private AlertDialog myDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choosehead_grid);
        init_view();
    }

    private void init_view() {
        isSelectPic = App_phms.getInstance().mCurrentloginUserInfo.getInt(String.valueOf(App_phms.getInstance().GetUserInfoNAME()) + "selectHeadIconIndex", -2);
        isSelectPicSec = -2;
        ((Button) findViewById(R.id.photo_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ActivityChooseHead.this.xiangce();
            }
        });
        ((Button) findViewById(R.id.camera_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ActivityChooseHead.this.paizhao();
            }
        });
        ((RelativeLayout) findViewById(R.id.back_but_ly)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ActivityChooseHead.this.finish();
            }
        });
        this.choosehead_grid = (GridView) findViewById(R.id.choosehead_grid);
        this.choosehead_grid.setSelector(new ColorDrawable(0));
        this.imageHeadArray = new ArrayList<>();
        initheadarray(this.imageHeadArray);
        this.mImageHeadAdapter = new ImageHeadAdapter(this, this.imageHeadArray);
        this.choosehead_grid.setAdapter(this.mImageHeadAdapter);
        this.mImageHeadAdapter.notifyDataSetChanged();
        this.choosehead_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long arg3) {
                ActivityChooseHead.isSelectPic = arg2;
                ActivityChooseHead.isSelectPicSec = arg2;
                ActivityChooseHead.this.mImageHeadAdapter.notifyDataSetChanged();
                Message message = new Message();
                message.what = 2;
                message.arg1 = arg2;
                ActivityChooseHead.this.mHandler.sendMessageDelayed(message, 400);
                SharedPreferences.Editor _editor = App_phms.getInstance().mCurrentloginUserInfo.edit();
                _editor.putInt(String.valueOf(App_phms.getInstance().GetUserInfoNAME()) + "selectHeadIconIndex", ActivityChooseHead.isSelectPic);
                _editor.commit();
            }
        });
    }

    private void initheadarray(ArrayList<ImageHead> arrayList) {
        this.imageHeadArray.add(new ImageHead(R.drawable.img_firstgrandpa));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_secondgrandma));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_firstmiddleage));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_secondmiddleage));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_firstmiddleageman));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_thridmiddleageman));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_firstbelle));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_secondbelle));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_thirdbelle));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_firsthandsomeboy));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_one));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_two));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_three));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_four));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_five));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_six));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_seven));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_eight));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_nine));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_ten));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_eleven));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_twelve));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_thirteen));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_fourteen));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_fifteen));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_sixteen));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_seventeen));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_eightteen));
        this.imageHeadArray.add(new ImageHead(R.drawable.img_140915_nineteen));
    }

    public void xiangce() {
        Intent intent = new Intent("android.intent.action.PICK", (Uri) null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }

    public void paizhao() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", Uri.fromFile(getCaptureTempFile()));
        startActivityForResult(intent, 2);
    }

    private File getCaptureTempFile() {
        return new File(String.valueOf(FileOperation.getSdcardUserHeadPath(App_phms.getInstance().GetUserInfoNAME())) + "doctorpic_head.jpg");
    }

    private void getSdcardPhoto(String puserflag, String picturename) {
        String pSdCardPath = Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM;
        String _append = "contec/userinfo/" + puserflag + CookieSpec.PATH_DELIM;
        FileOperation.makeDirs(String.valueOf(pSdCardPath) + _append);
        try {
            if (new File(String.valueOf(pSdCardPath) + _append + picturename).exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(pSdCardPath) + _append + picturename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data != null) {
                    SharedPreferences.Editor _editor = App_phms.getInstance().mCurrentloginUserInfo.edit();
                    _editor.putInt(String.valueOf(App_phms.getInstance().GetUserInfoNAME()) + "selectHeadIconIndex", -2);
                    _editor.commit();
                    startPhotoZoom(data.getData());
                    break;
                }
                break;
            case 2:
                File temp = getCaptureTempFile();
                if (temp.exists()) {
                    SharedPreferences.Editor _editor2 = App_phms.getInstance().mCurrentloginUserInfo.edit();
                    _editor2.putInt(String.valueOf(App_phms.getInstance().GetUserInfoNAME()) + "selectHeadIconIndex", -2);
                    boolean commit = _editor2.commit();
                    startPhotoZoom(Uri.fromFile(temp));
                    break;
                }
                break;
            case 3:
                if (data != null) {
                    if (data != null) {
                        setPicToView(data);
                    }
                    Bundle extras = data.getExtras();
                    if (extras != null && extras.getParcelable("data") != null) {
                        Intent bintent = new Intent(this, MainActivityNew.class);
                        bintent.putExtras(extras);
                        setResult(1, bintent);
                        SharedPreferences.Editor _editor3 = App_phms.getInstance().mCurrentloginUserInfo.edit();
                        _editor3.putInt(String.valueOf(App_phms.getInstance().GetUserInfoNAME()) + "selectHeadIconIndex", -2);
                        _editor3.commit();
                        finish();
                        break;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null && extras.getParcelable("data") != null) {
            Bitmap temp = (Bitmap) extras.getParcelable("data");
            Bitmap photo = Bitmap.createScaledBitmap(temp, 200, 200, true);
            temp.recycle();
            String _path = FileOperation.getSdcardUserHeadPath(App_phms.getInstance().GetUserInfoNAME());
            File file = new File(_path);
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                photo.compress(Bitmap.CompressFormat.JPEG, 60, new FileOutputStream(String.valueOf(_path) + this.headName, false));
                photo.compress(Bitmap.CompressFormat.JPEG, 60, new ByteArrayOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
