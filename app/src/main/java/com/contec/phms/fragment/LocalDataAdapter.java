package com.contec.phms.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.core.internal.view.SupportMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import com.contec.phms.R;
import com.contec.phms.LocalDataActivity;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;
import com.contec.R;

public class LocalDataAdapter extends BaseAdapter {
    Context context;
    List<LocalDataAdapterData> list;
    private LayoutInflater mInflater;

    public LocalDataAdapter(List<LocalDataAdapterData> list2, Context context2) {
        this.list = list2;
        this.context = context2;
        this.mInflater = LayoutInflater.from(context2);
    }

    public void updataData(List<LocalDataAdapterData> list2) {
        this.list = list2;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.local_data_layout, (ViewGroup) null);
            holder = new ViewHolder();
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.typeIv = (ImageView) convertView.findViewById(R.id.iv);
            holder.data = (TextView) convertView.findViewById(R.id.data);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.unit = (TextView) convertView.findViewById(R.id.unit);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.over_all);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LocalDataAdapterData data = this.list.get(position);
        setDataData(data, holder.data);
        holder.type.setText(data.type);
        holder.time.setText(data.date.substring(0, 10));
        holder.typeIv.setImageResource(data.ivId);
        holder.unit.setText(getUnit(data.ivId));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LocalDataAdapter.this.context, LocalDataActivity.class);
                intent.putExtra("to", data);
                LocalDataAdapter.this.context.startActivity(intent);
            }
        });
        return convertView;
    }

    private void setDataData(LocalDataAdapterData data, TextView data2) {
        String dataContent = data.data;
        data2.setText(dataContent);
        switch (data.ivId) {
            case R.drawable.dia_blood:
                float cmss = Float.parseFloat(dataContent);
                if (((double) cmss) > 6.5d || ((double) cmss) < 3.1d) {
                    data2.setTextColor(SupportMenu.CATEGORY_MASK);
                    return;
                }
                return;
            case R.drawable.dia_ox:
                int spot2 = Integer.parseInt(dataContent.split(CookieSpec.PATH_DELIM)[0]);
                if (spot2 > 100 || spot2 < 93) {
                    data2.setTextColor(SupportMenu.CATEGORY_MASK);
                    return;
                }
                return;
            case R.drawable.dia_pee:
                int uro = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[0]);
                int bld = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[1]);
                int bil = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[2]);
                int ket = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[3]);
                int glu = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[4]);
                int pro = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[5]);
                int ph = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[6]);
                int nit = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[7]);
                int leu = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[8]);
                int sg = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[9]);
                int vc = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[10]);
                if (uro == 0 && bld == 0 && bil == 0 && ket == 0 && glu == 0 && pro == 0 && ph != 0 && ph != 4 && nit == 0 && leu == 0 && vc == 0 && sg != 0 && sg != 1 && sg != 5) {
                    data2.setText(this.context.getResources().getString(R.string.normal));
                    return;
                }
                data2.setTextColor(SupportMenu.CATEGORY_MASK);
                data2.setText(this.context.getResources().getString(R.string.abnormal));
                return;
            case R.drawable.dia_press:
                int low = Integer.parseInt(dataContent.split(CookieSpec.PATH_DELIM)[1]);
                int high = Integer.parseInt(dataContent.split(CookieSpec.PATH_DELIM)[0]);
                if (low > 89 || low < 59 || high > 139 || high < 89) {
                    data2.setTextColor(SupportMenu.CATEGORY_MASK);
                    return;
                }
                return;
            case R.drawable.dia_ter:
                float ter = Float.parseFloat(dataContent);
                if (ter > 37.0f || ter < 36.0f) {
                    data2.setTextColor(SupportMenu.CATEGORY_MASK);
                    return;
                }
                return;
            case R.drawable.dia_xindian:
                int hr = Integer.parseInt(dataContent);
                if (hr > 100 || hr < 59) {
                    data2.setTextColor(SupportMenu.CATEGORY_MASK);
                    return;
                }
                return;
            case R.drawable.dia_xinlv:
                int fhr = Integer.parseInt(dataContent);
                if (fhr > 160 || fhr < 100) {
                    data2.setTextColor(SupportMenu.CATEGORY_MASK);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public String getUnit(int flag) {
        switch (flag) {
            case R.drawable.dia_blood:
                return "mmol/L";
            case R.drawable.dia_lung:
                return "L";
            case R.drawable.dia_ox:
                return "%/bpm";
            case R.drawable.dia_pee:
                return this.context.getResources().getString(R.string.parmater);
            case R.drawable.dia_press:
                return "mmHg";
            case R.drawable.dia_run:
                return this.context.getResources().getString(R.string.str_step);
            case R.drawable.dia_ter:
                return "℃";
            case R.drawable.dia_wt:
                return "Kg";
            case R.drawable.dia_xindian:
                return "bpm";
            case R.drawable.dia_xinlv:
                return "bpm";
            default:
                return bs.b;
        }
    }

    class ViewHolder {
        public TextView data;
        public LinearLayout layout;
        public TextView time;
        public TextView type;
        public ImageView typeIv;
        public TextView unit;

        ViewHolder() {
        }
    }
}
