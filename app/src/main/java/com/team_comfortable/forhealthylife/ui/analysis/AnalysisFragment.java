package com.team_comfortable.forhealthylife.ui.analysis;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team_comfortable.forhealthylife.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AnalysisFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private DatePicker datePicker;
    private TextView tv_title, tv_subtitle_weight, tv_subtitle_stepCount,
            tv_content_weight, tv_content_stepCount;
    private LineChart chartWeight, chartStepCount;
    private Button analysisBtn;
    private String pickerDateValue;
    private ArrayList<HashMap<String, Object>> weightList, stepCountList;

    private class GraphAxisValueFormatter extends ValueFormatter
    {

        private ArrayList<String> values;
        GraphAxisValueFormatter(ArrayList<String> values){
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value)
        {
            return values.get((int) (value));
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_analysis, container, false);
        initFirebase();
        initData(root);
        return root;
    }

    private void initFirebase()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    private void initData(View root)
    {
        datePicker = root.findViewById(R.id.analysis_datePicker);
        String pickerDateValue = getPickerDate_yyMMdd(datePicker);
        tv_title = root.findViewById(R.id.tv_analysis_title);
        tv_subtitle_weight = root.findViewById(R.id.tv_analysis_subtitle_weight);
        tv_subtitle_stepCount = root.findViewById(R.id.tv_analysis_subtitle_stepCount);
        tv_content_weight = root.findViewById(R.id.tv_analysis_content_weight);
        tv_content_stepCount = root.findViewById(R.id.tv_analysis_content_stepCount);

        chartWeight = root.findViewById(R.id.chart_analysis_weight);
        chartStepCount = root.findViewById(R.id.chart_analysis_stepCount);
        analysisBtn = root.findViewById(R.id.btn_analysis);
        analysisBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setDataInList("userWeight");
                setDataInList("userStepCount");
            }
        });
    }

    private String getPickerDate_yyMMdd(DatePicker picker)
    {
        String currYear = String.valueOf(picker.getYear()).substring(2, 4);
        String currMonth = String.valueOf(picker.getMonth() + 1);
        if(currMonth.length() == 1) currMonth = "0" + currMonth;
        String currDay = String.valueOf(picker.getDayOfMonth());
        if(currDay.length() == 1) currDay = "0" + currDay;
        return currYear + currMonth + currDay;
    }

    private ArrayList<String> findWeek(DatePicker picker)
    {
        ArrayList<String> weekDateList = new ArrayList<String>();
        int year = picker.getYear();
        int month = picker.getMonth();
        int day = picker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E");
        //calendar.add(Calendar.DATE, -2);
        String day2 = dateFormat.format(calendar.getTime());
        if(day2.equals("Tue")) calendar.add(Calendar.DATE, -1);
        if(day2.equals("Wed")) calendar.add(Calendar.DATE, -2);
        if(day2.equals("Thu")) calendar.add(Calendar.DATE, -3);
        if(day2.equals("Fri")) calendar.add(Calendar.DATE, -4);
        if(day2.equals("Sat")) calendar.add(Calendar.DATE, -5);
        if(day2.equals("Sun")) calendar.add(Calendar.DATE, -6);
        for(int i = 0; i < 7; i++)
        {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd");
            weekDateList.add(dFormat.format(calendar.getTime()).substring(2, 8));
            calendar.add(Calendar.DATE, 1);
        }
        return weekDateList;
    }


    public void setDataInList(final String key)
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child(key);
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                ArrayList<Entry> entryList = new ArrayList<Entry>();
                ArrayList<String> weekDateList = findWeek(datePicker);
                for(int i = 0; i < weekDateList.size(); i++)
                {
                    boolean isExist = false;
                    DataSnapshot currData = null;
                    for(DataSnapshot data : dataSnapshot.getChildren())
                    {
                        if(weekDateList.get(i).equals(data.getKey()))
                        {
                            isExist = true;
                            currData = data;
                        }
                    }
                    if(isExist) entryList.add(new Entry(i, Float.parseFloat(currData.getValue().toString())));
                    else entryList.add(new Entry(i, 0f));
                }
                if(key.equals("userWeight"))
                    createChart(chartWeight, "날짜별 체중현황 (kg)", weekDateList, entryList);
                else if(key.equals("userStepCount"))
                    createChart(chartStepCount, "날짜별 걸음수 현황 (걸음수)", weekDateList, entryList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void createChart(LineChart chart, String label,
                             ArrayList<String> weekDateList, ArrayList<Entry> entryList)
    {
        int color = 0;
        for(int i = 0; i < weekDateList.size(); i++)
        {
            weekDateList.set(i, weekDateList.get(i).substring(2, 4)
                    + "/" + weekDateList.get(i).substring(4, 6));
        }
        weekDateList.add(" (월/일)");

        if(label.equals("날짜별 체중현황 (kg)"))
            color = Color.rgb(130, 177, 255);
        else if(label.equals("날짜별 걸음수 현황 (걸음수)"))
            color = Color.rgb(67, 160, 71);

        GraphAxisValueFormatter formatter = new GraphAxisValueFormatter(weekDateList);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.disableAxisLineDashedLine();
        xAxis.disableGridDashedLine();
        xAxis.setAxisMaximum(7f);
        xAxis.setLabelCount(7);
        xAxis.setTextSize(12f);
        xAxis.setValueFormatter(formatter);

        LineDataSet dataSet;
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSet = new LineDataSet(entryList, label);
        dataSet.setColor(color);
        dataSet.setCircleColor(color); // set data
        dataSet.setFormSize(10f);
        dataSet.setCircleRadius(8f);
        dataSet.setCircleHoleRadius(4f);
        dataSet.setLineWidth(3f);
        dataSet.setValueTextSize(12f);
        dataSets.add(dataSet);
        LineData data = new LineData(dataSets);

        chart.setData(data);
        chart.invalidate();
    }
}
