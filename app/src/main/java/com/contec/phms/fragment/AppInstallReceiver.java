package com.contec.phms.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import java.io.File;

public class AppInstallReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = intent.getData().getSchemeSpecificPart();
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED") && packageName != null && "com.adobe.reader".equals(packageName) && FragmentWebViewBase.savefile != null) {
            Intent openfileintent = getFileIntent(FragmentWebViewBase.savefile);
            openfileintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openfileintent);
        }
        intent.getAction().equals("android.intent.action.PACKAGE_REMOVED");
        intent.getAction().equals("android.intent.action.PACKAGE_REPLACED");
    }

    public Intent getFileIntent(File file) {
        Uri fromFile = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type=" + type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse("file://" + file.toString()), type);
        return intent;
    }

    private String getMIMEType(File f) {
        String fName = f.getName();
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end.equals("pdf")) {
            return "application/pdf";
        }
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return "audio/*";
        }
        if (end.equals("3gp") || end.equals("mp4")) {
            return "video/*";
        }
        if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return "image/*";
        }
        if (end.equalsIgnoreCase("apk")) {
            return "application/vnd.android.package-archive";
        }
        return "*/*";
    }
}
