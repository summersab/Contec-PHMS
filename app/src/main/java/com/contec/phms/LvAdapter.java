package com.contec.phms;

import android.content.Context;
import androidx.core.internal.view.SupportMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.conect.json.CLog;
import com.contec.phms.fragment.LocalDataAdapterData;
import com.contec.phms.db.PedometerSumStepKm;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;
import com.contec.R;

public class LvAdapter extends BaseAdapter {
    Context context;
    List<LocalDataAdapterData> list;
    private LayoutInflater mInflater;

    public LvAdapter(List<LocalDataAdapterData> list2, Context context2) {
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
        int vc;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.lvadapter_item, (ViewGroup) null);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.unit1 = (TextView) convertView.findViewById(R.id.unit1);
            holder.unit2 = (TextView) convertView.findViewById(R.id.unit2);
            holder.unit3 = (TextView) convertView.findViewById(R.id.unit3);
            holder.type1 = (TextView) convertView.findViewById(R.id.type1);
            holder.type2 = (TextView) convertView.findViewById(R.id.type2);
            holder.type3 = (TextView) convertView.findViewById(R.id.type3);
            holder.content1 = (TextView) convertView.findViewById(R.id.content1);
            holder.content2 = (TextView) convertView.findViewById(R.id.content2);
            holder.content3 = (TextView) convertView.findViewById(R.id.content3);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.for_more);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LocalDataAdapterData data = this.list.get(position);
        CLog.e("jflajdflafk", String.valueOf(data.data) + "==" + data.date + "==" + data.type + "==" + data.ivId + "==");
        holder.date.setText(data.date.substring(0, 10));
        holder.time.setText(data.date.substring(11));
        switch (data.ivId) {
            case R.drawable.dia_blood /*2130837548*/:
                float blood_shuge = Float.parseFloat(data.data);
                if (((double) blood_shuge) > 6.1d || ((double) blood_shuge) < 3.6d) {
                    holder.content1.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                holder.content1.setText(new StringBuilder(String.valueOf(blood_shuge)).toString());
                holder.type1.setText(this.context.getResources().getString(R.string.xuetang));
                holder.unit1.setText("mmol/L");
                break;
            case R.drawable.dia_lung /*2130837549*/:
                holder.layout.setVisibility(View.VISIBLE);
                holder.content1.setText(data.data.split(CookieSpec.PATH_DELIM)[0]);
                holder.content2.setText(data.data.split(CookieSpec.PATH_DELIM)[1]);
                holder.content3.setText(data.data.split(CookieSpec.PATH_DELIM)[2]);
                holder.content1.setTextSize(16.0f);
                holder.content2.setTextSize(16.0f);
                holder.content3.setTextSize(16.0f);
                holder.type1.setText("FVC");
                holder.type2.setText("FEV1");
                holder.type3.setText("PEF");
                holder.unit1.setText("L");
                holder.unit2.setText("L");
                holder.unit3.setText("L/s");
                break;
            case R.drawable.dia_ox /*2130837550*/:
                int spot2 = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[0]);
                int mailv = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[1]);
                if (spot2 > 100 || spot2 < 93) {
                    holder.content1.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                if (mailv > 100 || mailv < 60) {
                    holder.content2.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                holder.content1.setText(new StringBuilder(String.valueOf(spot2)).toString());
                holder.content2.setText(data.data.split(CookieSpec.PATH_DELIM)[1]);
                holder.type1.setText(this.context.getResources().getString(R.string.xueyang));
                holder.type2.setText(this.context.getResources().getString(R.string.pulse_rate));
                holder.unit1.setText("%");
                holder.unit2.setText("bpm");
                break;
            case R.drawable.dia_pee /*2130837551*/:
                holder.layout.setVisibility(View.VISIBLE);
                holder.unit4 = (TextView) convertView.findViewById(R.id.unit4);
                holder.unit5 = (TextView) convertView.findViewById(R.id.unit5);
                holder.unit6 = (TextView) convertView.findViewById(R.id.unit6);
                holder.type4 = (TextView) convertView.findViewById(R.id.type4);
                holder.type5 = (TextView) convertView.findViewById(R.id.type5);
                holder.type6 = (TextView) convertView.findViewById(R.id.type6);
                holder.content4 = (TextView) convertView.findViewById(R.id.content4);
                holder.content5 = (TextView) convertView.findViewById(R.id.content5);
                holder.content6 = (TextView) convertView.findViewById(R.id.content6);
                holder.layout1 = (LinearLayout) convertView.findViewById(R.id.for_more1);
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2 = (LinearLayout) convertView.findViewById(R.id.for_more2);
                holder.layout2.setVisibility(View.VISIBLE);
                holder.layout3 = (LinearLayout) convertView.findViewById(R.id.for_more3);
                holder.layout3.setVisibility(View.VISIBLE);
                holder.unit7 = (TextView) convertView.findViewById(R.id.unit7);
                holder.unit8 = (TextView) convertView.findViewById(R.id.unit8);
                holder.unit9 = (TextView) convertView.findViewById(R.id.unit9);
                holder.type7 = (TextView) convertView.findViewById(R.id.type7);
                holder.type8 = (TextView) convertView.findViewById(R.id.type8);
                holder.type9 = (TextView) convertView.findViewById(R.id.type9);
                holder.content7 = (TextView) convertView.findViewById(R.id.content7);
                holder.content8 = (TextView) convertView.findViewById(R.id.content8);
                holder.content9 = (TextView) convertView.findViewById(R.id.content9);
                holder.layout4 = (LinearLayout) convertView.findViewById(R.id.for_more4);
                holder.layout4.setVisibility(View.VISIBLE);
                holder.layout5 = (LinearLayout) convertView.findViewById(R.id.for_more5);
                holder.layout5.setVisibility(View.VISIBLE);
                holder.layout6 = (LinearLayout) convertView.findViewById(R.id.for_more6);
                holder.layout6.setVisibility(View.VISIBLE);
                holder.unit10 = (TextView) convertView.findViewById(R.id.unit10);
                holder.unit11 = (TextView) convertView.findViewById(R.id.unit11);
                holder.unit12 = (TextView) convertView.findViewById(R.id.unit12);
                holder.type10 = (TextView) convertView.findViewById(R.id.type10);
                holder.type11 = (TextView) convertView.findViewById(R.id.type11);
                holder.type12 = (TextView) convertView.findViewById(R.id.type12);
                holder.content10 = (TextView) convertView.findViewById(R.id.content10);
                holder.content11 = (TextView) convertView.findViewById(R.id.content11);
                holder.content12 = (TextView) convertView.findViewById(R.id.content12);
                holder.layout7 = (LinearLayout) convertView.findViewById(R.id.for_more7);
                holder.layout7.setVisibility(View.VISIBLE);
                holder.layout8 = (LinearLayout) convertView.findViewById(R.id.for_more8);
                holder.layout8.setVisibility(View.VISIBLE);
                holder.layout9 = (LinearLayout) convertView.findViewById(R.id.for_more9);
                holder.layout9.setVisibility(View.VISIBLE);
                holder.unit13 = (TextView) convertView.findViewById(R.id.unit13);
                holder.unit14 = (TextView) convertView.findViewById(R.id.unit14);
                holder.unit15 = (TextView) convertView.findViewById(R.id.unit15);
                holder.type13 = (TextView) convertView.findViewById(R.id.type13);
                holder.type14 = (TextView) convertView.findViewById(R.id.type14);
                holder.type15 = (TextView) convertView.findViewById(R.id.type15);
                holder.content13 = (TextView) convertView.findViewById(R.id.content13);
                holder.content14 = (TextView) convertView.findViewById(R.id.content14);
                holder.content15 = (TextView) convertView.findViewById(R.id.content15);
                holder.layout10 = (LinearLayout) convertView.findViewById(R.id.for_more10);
                holder.layout10.setVisibility(View.VISIBLE);
                holder.layout11 = (LinearLayout) convertView.findViewById(R.id.for_more11);
                holder.layout11.setVisibility(View.VISIBLE);
                holder.layout12 = (LinearLayout) convertView.findViewById(R.id.for_more12);
                holder.layout12.setVisibility(View.VISIBLE);
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
                int mal = 0;
                int cr = 0;
                int uca = 0;
                if (data.data.split(CookieSpec.PATH_DELIM).length == 14) {
                    vc = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[10]);
                    mal = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[11]);
                    cr = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[12]);
                    uca = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[13]);
                } else {
                    vc = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[10].substring(0, 1));
                }
                switch (uro) {
                    case 0:
                        holder.content2.setText("Norm");
                        break;
                    case 1:
                        holder.content2.setText("1+");
                        holder.content2.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content2.setText("2+");
                        holder.content2.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content2.setText(">=3+");
                        holder.content2.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                }
                switch (bld) {
                    case 0:
                        holder.content3.setText("-");
                        break;
                    case 1:
                        holder.content3.setText("+-");
                        holder.content3.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content3.setText("1+");
                        holder.content3.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content3.setText("2+");
                        holder.content3.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 4:
                        holder.content3.setText("3+");
                        holder.content3.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                }
                switch (bil) {
                    case 0:
                        holder.content4.setText("-");
                        break;
                    case 1:
                        holder.content4.setText("1+");
                        holder.content4.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content4.setText("2+");
                        holder.content4.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content4.setText("3+");
                        holder.content4.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                }
                switch (ket) {
                    case 0:
                        holder.content5.setText("-");
                        break;
                    case 1:
                        holder.content5.setText("1+");
                        holder.content5.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content5.setText("2+");
                        holder.content5.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content5.setText("3+");
                        holder.content5.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                }
                switch (glu) {
                    case 0:
                        holder.content6.setText("-");
                        break;
                    case 1:
                        holder.content6.setText("+-");
                        holder.content6.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content6.setText("1+");
                        holder.content6.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content6.setText("2+");
                        holder.content6.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 4:
                        holder.content6.setText("3+");
                        holder.content6.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 5:
                        holder.content6.setText("4+");
                        holder.content6.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                }
                switch (pro) {
                    case 0:
                        holder.content7.setText("-");
                        break;
                    case 1:
                        holder.content7.setText("+-");
                        holder.content7.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content7.setText("1+");
                        holder.content7.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content7.setText("2+");
                        holder.content7.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 4:
                        holder.content7.setText(">=3+");
                        holder.content7.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                }
                switch (ph) {
                    case 0:
                        holder.content8.setText("5");
                        break;
                    case 1:
                        holder.content8.setText("6");
                        break;
                    case 2:
                        break;
                    case 3:
                        holder.content8.setText("8");
                        break;
                    case 4:
                        holder.content8.setTextColor(SupportMenu.CATEGORY_MASK);
                        holder.content8.setText("9");
                        break;
                }
                holder.content8.setText("7");
                switch (nit) {
                    case 0:
                        holder.content9.setText("-");
                        break;
                    case 1:
                        holder.content9.setText("1+");
                        holder.content9.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                }
                switch (leu) {
                    case 0:
                        holder.content10.setText("-");
                        break;
                    case 1:
                        holder.content10.setText("+-");
                        holder.content10.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content10.setText("1+");
                        holder.content10.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content10.setText("2+");
                        holder.content10.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 4:
                        holder.content10.setText("3+");
                        holder.content10.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                }
                switch (sg) {
                    case 0:
                        holder.content11.setText("<=1.005");
                        holder.content11.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 1:
                        holder.content11.setText("1.010");
                        break;
                    case 2:
                        holder.content11.setText("1.015");
                        break;
                    case 3:
                        holder.content11.setText("1.020");
                        break;
                    case 4:
                        holder.content11.setText("1.025");
                        break;
                    case 5:
                        holder.content11.setTextColor(SupportMenu.CATEGORY_MASK);
                        holder.content11.setText(">=1.030");
                        break;
                }
                switch (vc) {
                    case 0:
                        holder.content12.setText("-");
                        break;
                    case 1:
                        holder.content12.setText("+-");
                        holder.content12.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content12.setText("1+");
                        holder.content12.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content12.setText("2+");
                        holder.content12.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 4:
                        holder.content12.setText("3+");
                        holder.content12.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                }
                switch (mal) {
                    case 0:
                        holder.content13.setText("-");
                        break;
                    case 1:
                        holder.content13.setText("1+");
                        holder.content13.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content13.setText("2+");
                        holder.content13.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content13.setText("3+");
                        holder.content13.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 9:
                        break;
                }
                holder.content13.setText("-");
                switch (cr) {
                    case 0:
                        holder.content14.setText("-");
                        break;
                    case 1:
                        holder.content14.setText("+-");
                        holder.content14.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content14.setText("1+");
                        holder.content14.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content14.setText("2+");
                        holder.content14.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 4:
                        holder.content14.setText("3+");
                        holder.content14.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 9:
                        holder.content14.setText("-");
                        break;
                }
                switch (uca) {
                    case 0:
                        holder.content15.setText("-");
                        break;
                    case 1:
                        holder.content15.setText("1+");
                        holder.content15.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 2:
                        holder.content15.setText("2+");
                        holder.content15.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 3:
                        holder.content15.setText("3+");
                        holder.content15.setTextColor(SupportMenu.CATEGORY_MASK);
                        break;
                    case 9:
                        holder.content15.setText("-");
                        break;
                }
                holder.type1.setText(this.context.getResources().getString(R.string.name));
                holder.type2.setText(this.context.getResources().getString(R.string.uro));
                holder.type3.setText(this.context.getResources().getString(R.string.bld));
                holder.content1.setText(this.context.getResources().getString(R.string.parmater));
                holder.unit1.setText(this.context.getResources().getString(R.string.reference_value));
                holder.unit2.setText("Norm");
                holder.unit3.setText("-");
                holder.content1.setTextColor(-16777216);
                holder.content1.setTextSize(16.0f);
                holder.content2.setTextSize(16.0f);
                holder.content3.setTextSize(16.0f);
                holder.unit1.setTextSize(16.0f);
                holder.unit2.setTextSize(16.0f);
                holder.unit3.setTextSize(16.0f);
                holder.type4.setText(this.context.getResources().getString(R.string.bil));
                holder.type5.setText(this.context.getResources().getString(R.string.ket));
                holder.type6.setText(this.context.getResources().getString(R.string.glu));
                holder.unit4.setText("-");
                holder.unit5.setText("-");
                holder.unit6.setText("-");
                holder.type7.setText(this.context.getResources().getString(R.string.pro));
                holder.type8.setText("PH");
                holder.type9.setText(this.context.getResources().getString(R.string.nit));
                holder.unit7.setText("-");
                holder.unit8.setText("5~8");
                holder.unit9.setText("-");
                holder.type10.setText(this.context.getResources().getString(R.string.leu));
                holder.type11.setText(this.context.getResources().getString(R.string.sg));
                holder.type12.setText(this.context.getResources().getString(R.string.vc));
                holder.unit10.setText("-");
                holder.unit11.setText("1.010~1.025");
                holder.unit12.setText("-");
                holder.type13.setText(this.context.getResources().getString(R.string.vbdb));
                holder.type14.setText(this.context.getResources().getString(R.string.jg));
                holder.type15.setText(this.context.getResources().getString(R.string.niaog));
                holder.unit13.setText("-");
                holder.unit14.setText("-");
                holder.unit15.setText("-");
                break;
            case R.drawable.dia_press /*2130837552*/:
                int low = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[1]);
                int high = Integer.parseInt(data.data.split(CookieSpec.PATH_DELIM)[0]);
                if (low > 89 || low < 59) {
                    holder.content2.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                if (high > 139 || high < 89) {
                    holder.content1.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                holder.content1.setText(new StringBuilder(String.valueOf(high)).toString());
                holder.content2.setText(new StringBuilder(String.valueOf(low)).toString());
                holder.type1.setText(this.context.getResources().getString(R.string.high_press));
                holder.type2.setText(this.context.getResources().getString(R.string.low_press));
                holder.unit1.setText("mmHg");
                holder.unit2.setText("mmHg");
                break;
            case R.drawable.dia_run /*2130837553*/:
                holder.time.setVisibility(View.GONE);
                holder.content1.setText("0");
                holder.content2.setText("0");
                holder.type1.setText(this.context.getResources().getString(R.string.str_pedometer_history_step));
                holder.type2.setText(this.context.getResources().getString(R.string.cal));
                holder.unit1.setText(this.context.getResources().getString(R.string.str_step));
                holder.unit2.setText(PedometerSumStepKm.Cal);
                break;
            case R.drawable.dia_ter /*2130837554*/:
                float ter = Float.parseFloat(data.data);
                if (ter > 37.0f || ter < 36.0f) {
                    holder.content1.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                holder.content1.setText(new StringBuilder(String.valueOf(ter)).toString());
                holder.content2.setText(bs.b);
                holder.type1.setText(this.context.getResources().getString(R.string.heat));
                holder.type2.setText(bs.b);
                holder.unit1.setText("℃");
                holder.unit2.setText(bs.b);
                break;
            case R.drawable.dia_wt /*2130837555*/:
                holder.content1.setText(data.data);
                holder.content2.setText(bs.b);
                holder.type1.setText(this.context.getResources().getString(R.string.str_user_weight));
                holder.type2.setText(bs.b);
                holder.unit1.setText("Kg");
                holder.unit2.setText(bs.b);
                break;
            case R.drawable.dia_xindian /*2130837556*/:
                int hr = Integer.parseInt(data.data);
                holder.unit2.setTextSize(16.0f);
                holder.content2.setVisibility(View.GONE);
                holder.unit2.setTextColor(-16777216);
                holder.unit2.setText(this.context.getResources().getString(R.string.normal_is));
                if (hr > 100 || hr < 60) {
                    holder.content1.setTextColor(SupportMenu.CATEGORY_MASK);
                    if (hr > 100) {
                        holder.unit2.setText(this.context.getResources().getString(R.string.dance_more));
                    } else if (hr < 60) {
                        holder.unit2.setText(this.context.getResources().getString(R.string.dance_slowly));
                    }
                }
                holder.content1.setText(new StringBuilder(String.valueOf(hr)).toString());
                holder.type1.setText(this.context.getResources().getString(R.string.heart_rate));
                holder.type2.setText(this.context.getResources().getString(R.string.remind));
                holder.unit1.setText("bpm");
                break;
            case R.drawable.dia_xinlv /*2130837557*/:
                int fhr = Integer.parseInt(data.data);
                if (fhr > 160 || fhr < 100) {
                    holder.content1.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                holder.content1.setText(new StringBuilder(String.valueOf(fhr)).toString());
                holder.type1.setText(this.context.getResources().getString(R.string.fetal_rate));
                holder.unit1.setText("bpm");
                break;
        }
        return convertView;
    }

    class ViewHolder {
        public TextView content1;
        public TextView content10;
        public TextView content11;
        public TextView content12;
        public TextView content13;
        public TextView content14;
        public TextView content15;
        public TextView content2;
        public TextView content3;
        public TextView content4;
        public TextView content5;
        public TextView content6;
        public TextView content7;
        public TextView content8;
        public TextView content9;
        public TextView date;
        public LinearLayout layout;
        public LinearLayout layout1;
        public LinearLayout layout10;
        public LinearLayout layout11;
        public LinearLayout layout12;
        public LinearLayout layout2;
        public LinearLayout layout3;
        public LinearLayout layout4;
        public LinearLayout layout5;
        public LinearLayout layout6;
        public LinearLayout layout7;
        public LinearLayout layout8;
        public LinearLayout layout9;
        public TextView time;
        public TextView type1;
        public TextView type10;
        public TextView type11;
        public TextView type12;
        public TextView type13;
        public TextView type14;
        public TextView type15;
        public TextView type2;
        public TextView type3;
        public TextView type4;
        public TextView type5;
        public TextView type6;
        public TextView type7;
        public TextView type8;
        public TextView type9;
        public TextView unit1;
        public TextView unit10;
        public TextView unit11;
        public TextView unit12;
        public TextView unit13;
        public TextView unit14;
        public TextView unit15;
        public TextView unit2;
        public TextView unit3;
        public TextView unit4;
        public TextView unit5;
        public TextView unit6;
        public TextView unit7;
        public TextView unit8;
        public TextView unit9;

        ViewHolder() {
        }
    }
}
