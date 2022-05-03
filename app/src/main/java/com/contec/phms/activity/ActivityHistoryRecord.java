package com.contec.phms.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.db.HistoryDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.ScreenAdapter;
import com.j256.ormlite.dao.Dao;
//import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityHistoryRecord extends RecordActivityBase {
    private static Dao<HistoryDao, String> mDao = null;
    private ArrayList<HistoryDao> mHistoryList;
    private HistoryRecordAdapter mHitoryAdapter = null;
    private View mListViewFoot;
    private ListView mListViewHistory;
    private AbsListView.OnScrollListener mOnScrollListener;
    private int mPoint;
    private RecordHandler mRecordHandler;
    private TextView mTextViewNoData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history);
        App_phms.getInstance().addActivity(this);
        LinearLayout mlayout_historyrecord_main = (LinearLayout) findViewById(R.id.layout_historyrecord_main);
        Button mback_but = (Button) findViewById(R.id.back_but);
        mback_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ActivityHistoryRecord.this.finish();
            }
        });
        init();
        selectData();
        CLog.e("ActivityHistoryRecord", "mHistoryList.size(): " + this.mHistoryList.size());
        this.mHitoryAdapter = new HistoryRecordAdapter(this);
        this.mHitoryAdapter.setmList(this.mHistoryList);
        this.mHitoryAdapter.listIsNull(this.mHitoryAdapter.getmList());
        TextView tx = new TextView(this);
        tx.setWidth(0);
        tx.setHeight(0);
        this.mListViewHistory.addHeaderView(tx, (Object) null, false);
        this.mListViewHistory.setAdapter(this.mHitoryAdapter);
        this.mListViewHistory.setOnScrollListener(this.mOnScrollListener);
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.titleLayoutToPad(this, (RelativeLayout) findViewById(R.id.linearlayout_title), mback_but);
            ScreenAdapter.changeLayoutTextSize(this, mlayout_historyrecord_main, 10);
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

    private void init() {
        this.mListViewFoot = LayoutInflater.from(this).inflate(R.layout.layout_listview_footerview, (ViewGroup) null);
        this.mTextViewNoData = (TextView) findViewById(R.id.textviewnodatahis);
        this.mListViewHistory = (ListView) findViewById(R.id.list_history);
        this.mOnScrollListener = new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case 0:
                        if (view.getLastVisiblePosition() == view.getCount() - 1 && view.getFirstVisiblePosition() != 0) {
                            ActivityHistoryRecord.this.mPoint = 1;
                            ActivityHistoryRecord.this.mHitoryAdapter.uploadData(ActivityHistoryRecord.this.mPoint);
                            ActivityHistoryRecord.this.mHitoryAdapter.notifyDataSetChanged();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        };
    }

    private List<HistoryDao> selectData() {
        try {
            PageUtil.checkUserinfo(App_phms.getInstance().mUserInfo, this);
            mDao = App_phms.getInstance().mHelper.getHistoryDao();
            this.mHistoryList = new ArrayList<>();
            this.mHistoryList = (ArrayList) mDao.queryForEq(HistoryDao.User, App_phms.getInstance().mUserInfo.mUserID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.mHistoryList == null || this.mHistoryList.size() == 0) {
            this.mTextViewNoData.setVisibility(View.VISIBLE);
            this.mListViewHistory.setVisibility(View.GONE);
        } else {
            Collections.sort(this.mHistoryList, new MyComparator());
            this.mTextViewNoData.setVisibility(View.GONE);
        }
        if (this.mHistoryList == null) {
            return this.mHistoryList;
        }
        return null;
    }

    class MyComparator implements Comparator<HistoryDao> {
        MyComparator() {
        }

        public int compare(HistoryDao lhs, HistoryDao rhs) {
            return rhs.getmId() - lhs.getmId();
        }
    }

    public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        if (arg1.getY() - arg0.getY() > 20.0f) {
            this.mRecordHandler.lastPage();
            return true;
        } else if (arg1.getY() - arg0.getY() >= -20.0f) {
            return true;
        } else {
            this.mRecordHandler.nextPage();
            return true;
        }
    }
}
