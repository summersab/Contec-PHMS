package com.contec.phms.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.contec.phms.R;
import com.contec.phms.domain.HospitalBean;
import java.util.List;

public class HospitalAdapter extends BaseAdapter {
    private Context mContext;
    private List<HospitalBean> mList;
    private boolean mResponse;
    private int selectedPosition = -1;

    public HospitalAdapter(Context mContext2, List<HospitalBean> mList2, boolean mResponse2) {
        this.mContext = mContext2;
        this.mList = mList2;
        this.mResponse = mResponse2;
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return this.mList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View convertView2 = LayoutInflater.from(this.mContext).inflate(R.layout.layout_hospital_item, (ViewGroup) null);
        ((TextView) convertView2.findViewById(R.id.hospital_item_tv)).setText(this.mList.get(position).getHospitalName());
        return convertView2;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    class Holder {
        TextView content;

        Holder() {
        }
    }
}
