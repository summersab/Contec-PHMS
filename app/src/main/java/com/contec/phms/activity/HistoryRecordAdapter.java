package com.contec.phms.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.conect.json.CLog;
import com.contec.phms.R;
import com.contec.phms.db.HistoryDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.ScreenAdapter;
import java.util.ArrayList;
import java.util.Locale;

public class HistoryRecordAdapter extends RecordAdapterBase {
    private Context mContext;
    private HistoryDao mHistoryDao;
    private String mLanguage;
    public ArrayList<HistoryDao> mList = new ArrayList<>();
    public ArrayList<HistoryDao> mListSrc = new ArrayList<>();

    public HistoryRecordAdapter(Context pContext) {
        super(pContext, 100);
        this.mContext = pContext;
        this.mLanguage = Constants.Language;
        if (this.mLanguage.equals("1zh")) {
            this.mLanguage = Locale.getDefault().getLanguage();
        } else if (this.mLanguage.equals("1en")) {
            this.mLanguage = Locale.getDefault().getLanguage();
        }
    }

    private class Holder {
        TextView _Date;
        TextView _contec;
        TextView _number;
        LinearLayout mLayout;

        private Holder() {
        }

        /* synthetic */ Holder(HistoryRecordAdapter historyRecordAdapter, Holder holder) {
            this();
        }
    }

    public void uploadData(int index) {
        if (index != 0) {
            if ((this.mIndex + 1) * this.mCount < this.mList.size()) {
                this.mPageCount = this.mCount;
            } else {
                initData(this.mIndex + 1);
            }
            this.mIndex++;
        } else if (this.mIndex >= 1) {
            this.mIndex--;
        }
        notifyDataSetChanged();
    }

    public void initData(int point) {
        int _count = 0;
        for (int i = point * this.mCount; i < this.mListSrc.size() && _count < this.mCount; i++) {
            this.mList.add(this.mListSrc.get(i));
            _count++;
        }
        if (_count != this.mCount && _count != 0) {
            this.mPageCount = _count;
        } else if (_count == 0) {
            this.mIndex--;
        } else {
            this.mPageCount = this.mCount;
        }
    }

    public ArrayList<?> getmList() {
        return this.mList;
    }

    public void setmList(ArrayList<HistoryDao> pList) {
        this.mListSrc = pList;
        initData(0);
    }

    public int getLength() {
        return this.mList.size();
    }

    public int getCount() {
        if (this.mList == null) {
            return 0;
        }
        return this.mList.size();
    }

    public Object getItem(int arg0) {
        return this.mList.get(arg0);
    }

    public View getView(int arg0, View arg1, ViewGroup arg2) {
        Holder _holder = new Holder(this, (Holder) null);
        if (arg1 == null) {
            arg1 = getmLayoutInflater().inflate(R.layout.layout_show_history_item, (ViewGroup) null);
            _holder._number = (TextView) arg1.findViewById(R.id.textviewhistorynum);
            _holder._Date = (TextView) arg1.findViewById(R.id.textviewhistorydata);
            _holder._contec = (TextView) arg1.findViewById(R.id.textviewhistorycontec);
            _holder.mLayout = (LinearLayout) arg1.findViewById(R.id.linearhistory);
            LinearLayout mlinearhistory = (LinearLayout) arg1.findViewById(R.id.linearhistory);
            if (Constants.IS_PAD_NEW) {
                ScreenAdapter.changeLayoutTextSize(this.mContext, mlinearhistory, 10);
                ScreenAdapter.changeLayoutSize(this.mContext, mlinearhistory, 0, 10);
            }
            arg1.setTag(_holder);
        } else {
            _holder = (Holder) arg1.getTag();
        }
        this.mHistoryDao = (HistoryDao) getmList().get((int) getItemId(arg0));
        String _content = this.mHistoryDao.getContent().trim();
        if (this.mLanguage.contains("zh")) {
            if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_ear_temp_en).trim())) {
                _content = this.mContext.getResources().getString(R.string.upload_base_ear_temp);
            } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_8000gw_en).trim())) {
                _content = this.mContext.getResources().getString(R.string.upload_base_8000gw);
            } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_50D_en).trim())) {
                _content = this.mContext.getResources().getString(R.string.upload_base_50D_cn);
            } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_50EW_en).trim())) {
                _content = this.mContext.getResources().getString(R.string.upload_base_50EW);
            } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_10W_en).trim())) {
                _content = this.mContext.getResources().getString(R.string.upload_base_10W);
            } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_SXT_en).trim())) {
                _content = this.mContext.getResources().getString(R.string.upload_base_SXT);
            } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_50W_en).trim())) {
                _content = this.mContext.getResources().getString(R.string.upload_base_50W);
            } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_8AW_en).trim())) {
                _content = this.mContext.getResources().getString(R.string.upload_base_8AW);
            } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_FHR01_en).trim())) {
                _content = this.mContext.getResources().getString(R.string.upload_base_FHR01);
            } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_WT_en).trim())) {
                _content = this.mContext.getResources().getString(R.string.upload_base_WT);
            } else if (_content.equals(this.mContext.getResources().getString(R.string.str_upload_pedometer_content_en))) {
                _content = this.mContext.getResources().getString(R.string.str_upload_pedometer_content);
            }
        } else if (!this.mLanguage.contains("en")) {
            CLog.e("HistoryRecordAdapter", "It is not equals!");
        } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_ear_temp).trim())) {
            _content = this.mContext.getResources().getString(R.string.upload_base_ear_temp_en);
        } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_8000gw).trim())) {
            _content = this.mContext.getResources().getString(R.string.upload_base_8000gw_en);
        } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_50D_cn).trim())) {
            _content = this.mContext.getResources().getString(R.string.upload_base_50D_en);
        } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_50EW_cn).trim())) {
            _content = this.mContext.getResources().getString(R.string.upload_base_50EW);
        } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_10W_cn).trim())) {
            _content = this.mContext.getResources().getString(R.string.upload_base_10W);
        } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_SXT_cn).trim())) {
            _content = this.mContext.getResources().getString(R.string.upload_base_SXT);
        } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_50W_cn).trim())) {
            _content = this.mContext.getResources().getString(R.string.upload_base_50W);
        } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_8AW_cn).trim())) {
            _content = this.mContext.getResources().getString(R.string.upload_base_8AW);
        } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_FHR01_cn).trim())) {
            _content = this.mContext.getResources().getString(R.string.upload_base_FHR01);
        } else if (_content.equals(this.mContext.getResources().getString(R.string.upload_base_WT_cn).trim())) {
            _content = this.mContext.getResources().getString(R.string.upload_base_WT);
        } else if (_content.equals(this.mContext.getResources().getString(R.string.str_upload_pedometer_content_ch))) {
            _content = this.mContext.getResources().getString(R.string.str_upload_pedometer_content);
        }
        _holder._number.setText(new StringBuilder().append(arg0 + 1).toString());
        _holder._Date.setText(this.mHistoryDao.getDate());
        _holder._contec.setText(_content);
        return arg1;
    }
}
