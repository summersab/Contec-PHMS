package com.contec.phms.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.contec.phms.R;
import com.contec.phms.domain.CityListItem;
import java.util.List;

public class RegisterPhoneCityAdapter extends BaseAdapter {
    private Context mContext;
    private List<CityListItem> mList;

    public RegisterPhoneCityAdapter(Context mContext2, List<CityListItem> mList2) {
        this.mContext = mContext2;
        this.mList = mList2;
    }

    public int getCount() {
        if (this.mList != null) {
            return this.mList.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return this.mList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return new AdapterView(this.mContext, this.mList.get(position));
    }

    class AdapterView extends LinearLayout {
        public AdapterView(Context context, CityListItem cityListItem) {
            super(context);
            setOrientation(0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1);
            params.gravity = 16;
            TextView name = new TextView(context);
            name.setPadding(10, 10, 10, 10);
            name.setBackgroundColor(getResources().getColor(R.color.white));
            name.setText(cityListItem.getName());
            name.setTextSize(18.0f);
            name.setTextColor(-16777216);
            name.setSingleLine(true);
            addView(name, params);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(120, 50);
            params2.setMargins(1, 1, 1, 1);
            TextView pcode = new TextView(context);
            pcode.setText(cityListItem.getPcode());
            addView(pcode, params2);
            pcode.setVisibility(View.GONE);
        }

        public AdapterView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }
}
