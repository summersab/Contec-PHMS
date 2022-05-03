package com.contec.phms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.conect.json.CLog;
import com.contec.phms.fragment.LocalDataAdapterData;
import com.contec.phms.db.localdata.BloodDDataDao;
import com.contec.phms.db.localdata.CmssxtDataDao;
import com.contec.phms.db.localdata.FetalHeartDataDao;
import com.contec.phms.db.localdata.FvcDataDao;
import com.contec.phms.db.localdata.PedometerDayDataDao;
import com.contec.phms.db.localdata.PluseDataDao;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.db.localdata.TempertureDataDao;
import com.contec.phms.db.localdata.UrineDataDao;
import com.contec.phms.db.localdata.WeightDataDao;
import com.contec.phms.db.localdata.opration.BloodDDataDaoOperation;
import com.contec.phms.db.localdata.opration.CmssxtDataDaoOperation;
import com.contec.phms.db.localdata.opration.FeltalHeartDataDaoOperation;
import com.contec.phms.db.localdata.opration.FvcDataDaoOperation;
import com.contec.phms.db.localdata.opration.PedometerDayDataDaoOperation;
import com.contec.phms.db.localdata.opration.PluseDataDaoOperation;
import com.contec.phms.db.localdata.opration.Spo2DataDaoOperation;
import com.contec.phms.db.localdata.opration.TempertureDataDaoOperation;
import com.contec.phms.db.localdata.opration.UrineDataDaoOperation;
import com.contec.phms.db.localdata.opration.WeightDataDaoOperation;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class LocalDataActivity extends Activity {
    Button back;
    List<LocalDataAdapterData> list;

    ListView lv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_data);
        initView();
    }

    private void initView() {
        this.lv = (ListView) findViewById(R.id.content);
        this.list = new ArrayList();
        this.back = (Button) findViewById(R.id.back);
        this.back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LocalDataActivity.this.finish();
            }
        });
        LocalDataAdapterData data = (LocalDataAdapterData) getIntent().getSerializableExtra("to");
        CLog.e("LocalDataActivity", new StringBuilder(String.valueOf(data.ivId)).toString());
        switch (data.ivId) {
            case R.drawable.dia_blood:
                this.list.clear();
                List<CmssxtDataDao> CmssxtdaoList = CmssxtDataDaoOperation.getInstance(this).queryAll();
                com.contec.phms.util.CLog.e("dkfjaljdflkjdlf", new StringBuilder(String.valueOf(CmssxtdaoList.size())).toString());
                for (CmssxtDataDao dao : CmssxtdaoList) {
                    LocalDataAdapterData data2 = new LocalDataAdapterData();
                    data2.type = getResources().getString(R.string.xuetang);
                    data2.data = dao.mData;
                    data2.date = dao.mTime;
                    data2.ivId = R.drawable.dia_blood;
                    this.list.add(data2);
                }
                break;
            case R.drawable.dia_lung:
                this.list.clear();
                List<FvcDataDao> FvcdaoList = FvcDataDaoOperation.getInstance(this).queryAll();
                com.contec.phms.util.CLog.e("dkfjaljdflkjdlf", new StringBuilder(String.valueOf(FvcdaoList.size())).toString());
                for (FvcDataDao dao2 : FvcdaoList) {
                    LocalDataAdapterData data4 = new LocalDataAdapterData();
                    data4.data = String.valueOf(dao2.mFvc) + CookieSpec.PATH_DELIM + dao2.mFev1 + CookieSpec.PATH_DELIM + dao2.mPef;
                    data4.type = getResources().getString(R.string.str_fgn);
                    data4.date = dao2.mTime;
                    data4.ivId = R.drawable.dia_lung;
                    this.list.add(data4);
                }
                break;
            case R.drawable.dia_ox:
                this.list.clear();
                List<Spo2DataDao> Spo2daoList = Spo2DataDaoOperation.getInstance(this).queryAll();
                com.contec.phms.util.CLog.e("dkfjaljdflkjdlf", new StringBuilder(String.valueOf(Spo2daoList.size())).toString());
                for (Spo2DataDao dao3 : Spo2daoList) {
                    LocalDataAdapterData data10 = new LocalDataAdapterData();
                    data10.type = getResources().getString(R.string.xueyang);
                    data10.data = String.valueOf(dao3.mSpo2) + CookieSpec.PATH_DELIM + dao3.mPr;
                    data10.date = dao3.mTime;
                    data10.ivId = R.drawable.dia_ox;
                    CLog.e("LocalDataActivity", String.valueOf(dao3.mTime) + "==" + dao3.mSpo2 + "==" + dao3.mPr);
                    this.list.add(data10);
                }
                break;
            case R.drawable.dia_pee:
                this.list.clear();
                List<UrineDataDao> UrinedaoList = UrineDataDaoOperation.getInstance(this).queryAll();
                com.contec.phms.util.CLog.e("dkfjaljdflkjdlf", new StringBuilder(String.valueOf(UrinedaoList.size())).toString());
                for (UrineDataDao dao4 : UrinedaoList) {
                    LocalDataAdapterData data8 = new LocalDataAdapterData();
                    data8.type = getResources().getString(R.string.pee_nomal);
                    Log.e("UrinedaoList", String.valueOf(dao4.mValue) + "----" + dao4.mValuedd);
                    data8.data = String.valueOf(dao4.mValue) + dao4.mValuedd;
                    data8.date = dao4.mTime;
                    data8.ivId = R.drawable.dia_pee;
                    this.list.add(data8);
                }
                break;
            case R.drawable.dia_press:
                this.list.clear();
                List<BloodDDataDao> BlooddaoList = BloodDDataDaoOperation.getInstance(this).queryAll();
                com.contec.phms.util.CLog.e("dkfjaljdflkjdlf", new StringBuilder(String.valueOf(BlooddaoList.size())).toString());
                for (BloodDDataDao dao5 : BlooddaoList) {
                    LocalDataAdapterData data1 = new LocalDataAdapterData();
                    data1.type = getResources().getString(R.string.xueya);
                    data1.data = String.valueOf(dao5.mHigh) + CookieSpec.PATH_DELIM + dao5.mLow;
                    data1.date = dao5.mTime;
                    data1.ivId = R.drawable.dia_press;
                    this.list.add(data1);
                }
                break;
            case R.drawable.dia_run:
                this.list.clear();
                List<PedometerDayDataDao> DaydaoList = PedometerDayDataDaoOperation.getInstance(this).queryAll();
                com.contec.phms.util.CLog.e("dkfjaljdflkjdlf", new StringBuilder(String.valueOf(DaydaoList.size())).toString());
                for (PedometerDayDataDao dao6 : DaydaoList) {
                    LocalDataAdapterData data5 = new LocalDataAdapterData();
                    data5.type = getResources().getString(R.string.today_steps);
                    data5.data = dao6.mSteps;
                    data5.date = dao6.mTime;
                    data5.ivId = R.drawable.dia_run;
                    this.list.add(data5);
                }
                break;
            case R.drawable.dia_ter:
                this.list.clear();
                List<TempertureDataDao> TemperturedaoList = TempertureDataDaoOperation.getInstance(this).queryAll();
                com.contec.phms.util.CLog.e("dkfjaljdflkjdlf", new StringBuilder(String.valueOf(TemperturedaoList.size())).toString());
                for (TempertureDataDao dao7 : TemperturedaoList) {
                    LocalDataAdapterData data7 = new LocalDataAdapterData();
                    data7.type = getResources().getString(R.string.heat);
                    data7.data = dao7.mTemperture;
                    data7.date = dao7.mTime;
                    com.contec.phms.util.CLog.e("initViewAfterLoadError", String.valueOf(data7.data) + "===" + data7.date);
                    data7.ivId = R.drawable.dia_ter;
                    this.list.add(data7);
                }
                break;
            case R.drawable.dia_wt:
                this.list.clear();
                List<WeightDataDao> WeightdaoList = WeightDataDaoOperation.getInstance(this).queryAll();
                com.contec.phms.util.CLog.e("dkfjaljdflkjdlf", new StringBuilder(String.valueOf(WeightdaoList.size())).toString());
                for (WeightDataDao dao8 : WeightdaoList) {
                    LocalDataAdapterData data9 = new LocalDataAdapterData();
                    data9.type = getResources().getString(R.string.str_user_weight);
                    data9.data = dao8.mWeight;
                    data9.date = dao8.mTime;
                    data9.ivId = R.drawable.dia_wt;
                    this.list.add(data9);
                }
                break;
            case R.drawable.dia_xindian:
                this.list.clear();
                List<PluseDataDao> PlusedaoList = PluseDataDaoOperation.getInstance(this).queryAll();
                com.contec.phms.util.CLog.e("dkfjaljdflkjdlf", new StringBuilder(String.valueOf(PlusedaoList.size())).toString());
                for (PluseDataDao dao9 : PlusedaoList) {
                    LocalDataAdapterData data6 = new LocalDataAdapterData();
                    data6.type = getResources().getString(R.string.heart_rate);
                    data6.data = dao9.mPluse;
                    data6.date = dao9.mTime;
                    data6.ivId = R.drawable.dia_xindian;
                    this.list.add(data6);
                }
                break;
            case R.drawable.dia_xinlv:
                this.list.clear();
                List<FetalHeartDataDao> FetalHeartdaoList = FeltalHeartDataDaoOperation.getInstance(this).queryAll();
                com.contec.phms.util.CLog.e("dkfjaljdflkjdlf", new StringBuilder(String.valueOf(FetalHeartdaoList.size())).toString());
                for (FetalHeartDataDao dao10 : FetalHeartdaoList) {
                    LocalDataAdapterData data3 = new LocalDataAdapterData();
                    data3.type = getResources().getString(R.string.fetal_rate);
                    data3.data = dao10.mFetalHeartRate;
                    data3.date = dao10.mTime;
                    data3.ivId = R.drawable.dia_xinlv;
                    this.list.add(data3);
                }
                break;
        }
        this.lv.setAdapter(new LvAdapter(this.list, this));
        Toast.makeText(this, getResources().getString(R.string.offline_advice), Toast.LENGTH_LONG).show();
    }
}
