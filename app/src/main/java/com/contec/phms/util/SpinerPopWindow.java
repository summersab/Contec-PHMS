package com.contec.phms.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.contec.phms.R;
import com.contec.phms.domain.HospitalBean;
import java.util.List;

public class SpinerPopWindow<T> extends PopupWindow {
    private LayoutInflater inflater;
    private List<HospitalBean> list;
    private SpinerPopWindow<T>.MyAdapter mAdapter;
    private ListView mListView;

    public SpinerPopWindow(Context context, List<HospitalBean> list2, AdapterView.OnItemClickListener clickListener) {
        super(context);
        this.inflater = LayoutInflater.from(context);
        this.list = list2;
        init(clickListener);
    }

    private void init(AdapterView.OnItemClickListener clickListener) {
        View view = this.inflater.inflate(R.layout.spiner_window_layout, (ViewGroup) null);
        setContentView(view);
        setWidth(-2);
        setHeight(-2);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        this.mListView = (ListView) view.findViewById(R.id.listview);
        ListView listView = this.mListView;
        SpinerPopWindow<T>.MyAdapter myAdapter = new MyAdapter(this, (MyAdapter) null);
        this.mAdapter = myAdapter;
        listView.setAdapter(myAdapter);
        this.mListView.setOnItemClickListener(clickListener);
    }

    private class MyAdapter extends BaseAdapter {
        private MyAdapter() {
        }

        /* synthetic */ MyAdapter(SpinerPopWindow spinerPopWindow, MyAdapter myAdapter) {
            this();
        }

        public int getCount() {
            return SpinerPopWindow.this.list.size();
        }

        public Object getItem(int position) {
            return SpinerPopWindow.this.list.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            SpinerPopWindow<T>.ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder(SpinerPopWindow.this, (ViewHolder) null);
                convertView = SpinerPopWindow.this.inflater.inflate(R.layout.spiner_item_layout, (ViewGroup) null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(((HospitalBean) SpinerPopWindow.this.list.get(position)).getHospitalName());
            return convertView;
        }
    }

    private class ViewHolder {
        private TextView tvName;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder(SpinerPopWindow spinerPopWindow, ViewHolder viewHolder) {
            this();
        }
    }
}
