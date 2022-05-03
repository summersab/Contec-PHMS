package com.contec.phms.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import com.contec.phms.util.CLog;

public class RecordHandler {
    protected static final String TAG = RecordHandler.class.getSimpleName();
    public static int mIndex = 0;
    private boolean isAvisiable;
    private RecordAdapterBase mAdapter;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                if (msg.arg1 == 1) {
                    RecordHandler.mIndex = -1;
                    RecordHandler.this.mAdapter.setmIndex(RecordHandler.this.mAdapter.getmIndex() - 1);
                } else if (msg.arg1 == 2) {
                    RecordHandler.mIndex = 1;
                    RecordHandler.this.mAdapter.setmIndex(RecordHandler.this.mAdapter.getmIndex() + 1);
                }
                CLog.i(RecordHandler.TAG, "handler " + Thread.currentThread().getName());
                RecordHandler.this.mAdapter.notifyDataSetChanged();
                RecordHandler.this.mAdapter.toastContent();
                RecordHandler.this.isAvisiable = false;
            } else if (msg.what == 1 && !RecordHandler.this.isAvisiable) {
                RecordHandler.this.isAvisiable = true;
            }
        }
    };
    private ListView mListView;
    private View mListViewFoot;

    public RecordHandler(RecordAdapterBase pAdapter, ListView pListView, View pListViewFoot) {
        this.mAdapter = pAdapter;
        this.mListView = pListView;
        this.mListViewFoot = pListViewFoot;
    }

    public void lastPage() {
        handlerThread(1, 0, false);
        handlerThread(2, 1, true);
    }

    public void nextPage() {
        handlerThread(1, 0, false);
        handlerThread(2, 2, true);
    }

    public void handlerThread(final int pWhat, final int pArg, final boolean pSleep) {
        new Thread(new Runnable() {
            public void run() {
                Message _mess = new Message();
                if (pSleep) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                _mess.what = pWhat;
                _mess.arg1 = pArg;
                CLog.i(RecordHandler.TAG, "handlerThread" + Thread.currentThread().getName());
                RecordHandler.this.mHandler.sendMessage(_mess);
            }
        }).start();
    }
}
