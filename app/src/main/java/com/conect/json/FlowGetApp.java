package com.conect.json;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.widget.Toast;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FlowGetApp {
    private AppFlowInfo mApp = null;
    private Drawable mAppIcon;
    private int mAppId;
    private String mAppNameRemote;
    private Context mContext;
    private SqliteDataBaseOperation mDataBase;
    private long mDownLoadFlow;
    private List<AppFlowInfo> mListInfo;
    private PackageManager mPackage;
    private List<PackageInfo> mPackageInfo;
    private boolean mSupport;
    private long mUpLoadFlow;

    public FlowGetApp(String pAppName, Context pContext) {
        this.mAppNameRemote = pAppName;
        this.mContext = pContext;
        getFlow();
    }

    public void getFlow() {
        PackageInfo p = null;
        String mAppName = null;
        this.mDataBase = new SqliteDataBaseOperation(this.mContext);
        this.mPackage = this.mContext.getPackageManager();
        this.mPackageInfo = this.mPackage.getInstalledPackages(0);
        Iterator<PackageInfo> it = this.mPackageInfo.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            p = it.next();
            mAppName = p.applicationInfo.loadLabel(this.mContext.getPackageManager()).toString();
            if (mAppName == null || mAppName.length() == 0) {
                Toast.makeText(this.mContext, "no app", 1).show();
            } else if (mAppName.trim().equalsIgnoreCase(this.mAppNameRemote)) {
                CLog.e("FlowGetApp", "找到应用程序！");
                this.mAppId = p.applicationInfo.uid;
                long _rxData = TrafficStats.getUidRxBytes(this.mAppId);
                this.mDownLoadFlow = _rxData / 1024;
                long _txData = TrafficStats.getUidTxBytes(this.mAppId);
                this.mUpLoadFlow = _txData / 1024;
                if (_txData == -1 && _rxData == -1) {
                    this.mSupport = false;
                } else {
                    this.mSupport = true;
                    this.mAppIcon = p.applicationInfo.loadIcon(this.mContext.getPackageManager());
                    this.mApp = new AppFlowInfo(mAppName, this.mUpLoadFlow, this.mDownLoadFlow, this.mAppIcon);
                }
            } else {
                continue;
            }
        }
        this.mSupport = true;
        this.mAppIcon = p.applicationInfo.loadIcon(this.mContext.getPackageManager());
        this.mApp = new AppFlowInfo(mAppName, this.mUpLoadFlow, this.mDownLoadFlow, this.mAppIcon);
        if (this.mApp != null && this.mSupport) {
            CLog.e("FlowGetApp", "mDownLoadFlow: " + this.mDownLoadFlow + " mUpLoadFlow: " + this.mUpLoadFlow);
            CLog.e("FlowGetApp", "Insert into database: " + this.mDataBase.insertData(this.mApp));
        }
    }

    public List<AppFlowInfo> selectDataFromDataBase() {
        if (this.mSupport) {
            this.mListInfo = this.mDataBase.selectDataForAll();
            if (!(this.mListInfo == null || this.mListInfo.size() == 0)) {
                Collections.sort(this.mListInfo, new MyComparator());
            }
        }
        return this.mListInfo;
    }

    public List<AppFlowInfo> select(int start, int count) {
        if (this.mSupport) {
            this.mListInfo = this.mDataBase.select(start, count);
        }
        return this.mListInfo;
    }

    public boolean isSupport() {
        return this.mSupport;
    }

    class MyComparator implements Comparator<AppFlowInfo> {
        MyComparator() {
        }

        public int compare(AppFlowInfo lhs, AppFlowInfo rhs) {
            return lhs.getIntDate() - rhs.getIntDate();
        }
    }
}
