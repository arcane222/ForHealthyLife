package com.team_comfortable.forhealthylife.ui.analysis;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<HashMap<String, Object>> weightList, stepCountList;
    private int stepGoal = -1, weightGoal = -1;

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
        stepCountList = new ArrayList<HashMap<String, Object>>();
        weightList = new ArrayList<HashMap<String, Object>>();
        getDataFromDB();
        getDataFromDB2();

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
                if(stepGoal == -1 || weightGoal == -1)
                {
                    Toast.makeText(getContext(), "데이터 로딩중입니다. 잠시 기다려주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                setDataAndCreateChart("weight");
                setDataAndCreateChart("stepCount");
            }
        });
    }

    private String findToday(String key, ArrayList<HashMap<String, Object>> list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).get(key) != null)
            {
                return list.get(i).get(key).toString();
            }
        }
        return null;
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
        String day2 = dateFormat.format(calendar.getTime());
        if(day2.equals("Tue") || day2.equals("화")) calendar.add(Calendar.DATE, -1);
        if(day2.equals("Wed") || day2.equals("수")) calendar.add(Calendar.DATE, -2);
        if(day2.equals("Thu") || day2.equals("목")) calendar.add(Calendar.DATE, -3);
        if(day2.equals("Fri") || day2.equals("금")) calendar.add(Calendar.DATE, -4);
        if(day2.equals("Sat") || day2.equals("토")) calendar.add(Calendar.DATE, -5);
        if(day2.equals("Sun") || day2.equals("일")) calendar.add(Calendar.DATE, -6);
        for(int i = 0; i < 7; i++)
        {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd");
            weekDateList.add(dFormat.format(calendar.getTime()).substring(2, 8));
            calendar.add(Calendar.DATE, 1);
        }
        return weekDateList;
    }

    private void getDataFromDB2()
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userStepCount");
        final DatabaseReference dbReference2 = mReference.child("UserList").child(mUser.getUid()).child("userWeight");

        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    HashMap<String, Object> hMap = new HashMap<String, Object>();
                    hMap.put(data.getKey(), data.getValue().toString());
                    stepCountList.add(hMap);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        dbReference2.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    HashMap<String, Object> hMap = new HashMap<String, Object>();
                    hMap.put(data.getKey(), data.getValue().toString());
                    weightList.add(hMap);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setDataAndCreateChart(String type)
    {
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        ArrayList<String> weekDateList = findWeek(datePicker);

        if(type.equals("weight"))
        {
            for(int i = 0; i < weekDateList.size(); i++)
            {
                float entryVal = 0f;
                for(int j = 0; j < weightList.size(); j++)
                {
                    if(weightList.get(j).get(weekDateList.get(i)) != null)
                    {
                        entryVal = Float.parseFloat(weightList.get(j).get(weekDateList.get(i)).toString());
                    }
                }
                entryList.add(new Entry(i, entryVal));
            }
            drawChart(chartWeight, "날짜별 체중현황 (kg)", weekDateList, entryList);
            analyzeData(entryList, type);
        }
        else if(type.equals("stepCount"))
        {
            for(int i = 0; i < weekDateList.size(); i++)
            {
                float entryVal = 0f;
                for(int j = 0; j < stepCountList.size(); j++)
                {
                    if(stepCountList.get(j).get(weekDateList.get(i)) != null)
                    {
                        entryVal = Float.parseFloat(stepCountList.get(j).get(weekDateList.get(i)).toString());
                    }
                }
                entryList.add(new Entry(i, entryVal));
            }
            drawChart(chartStepCount, "날짜별 걸음수 현황 (걸음수)", weekDateList, entryList);
            analyzeData(entryList, type);
        }
    }


    private void drawChart(LineChart chart, String label,
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

    private void getDataFromDB()
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid());
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    String dataKey = data.getKey();
                    if (dataKey.equals("userStepGoal"))
                        stepGoal = Integer.parseInt(data.getValue().toString());
                    else if (dataKey.equals("userWeightGoal"))
                        weightGoal = Integer.parseInt(data.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void analyzeData(ArrayList<Entry> entry, String type)
    {
        String weightToday = findToday(getPickerDate_yyMMdd(datePicker), weightList);
        String stepToday = findToday(getPickerDate_yyMMdd(datePicker), stepCountList);
        float weightToday_float = 0f;
        float stepToday_float = 0f;
        if(weightToday != null) weightToday_float = Float.parseFloat(weightToday);
        if(stepToday != null) stepToday_float = Float.parseFloat(stepToday);

        float weekDataSum = 0f;
        for(int i = 0; i < entry.size(); i++)
        {
            weekDataSum = weekDataSum + entry.get(i).getY();
        }
        float avg = weekDataSum / 7;

        if(type.equals("weight"))
        {
            if(weightGoal == 0)
            {
                tv_content_weight.setText("목표 체중 값이 0입니다.\n목표 체중값을 설정해주세요.");
                return;
            }
            else
            {
                String day = "( " + getPickerDate_yyMMdd(datePicker) + " )";
                String today = "오늘의 체중 : " + weightToday_float;
                String avg_str = "주간 체중 평균 : " + avg + " kg";
                String goal = "주간 목표 체중 : " + weightGoal + " kg";
                String analysis_str = "\n";
                if(weightToday_float > weightGoal * 1.5) analysis_str += "목표체중까지 많이 남았군요.\n더욱 노력하세요.";
                else if(weightToday_float > weightGoal * 1.2) analysis_str += "목표체중까지 많이 도달했습니다.\n조금더 노력해봐요!";
                else if(weightToday_float > weightGoal) analysis_str += "고지가 코앞입니다\n 조금만 더 힘내세요!";
                else if(weightToday_float < weightGoal) analysis_str += "축하드립니다! 목표체중를 달성하셨네요!";
                tv_content_weight.setText(day + "\n" + today + "\n" + avg_str + "\n" + goal + "\n" + analysis_str);
            }
        }
        else if(type.equals("stepCount"))
        {
            if(stepGoal == 0)
            {
                tv_content_stepCount.setText("목표 걸음수 값이 0입니다.\n목표 걸음수를 설정해주세요.");
                return;
            }
            else
            {
                String day = "( " + getPickerDate_yyMMdd(datePicker) + " )";
                String today = "오늘의 걸음 수 : "  + stepToday_float;
                String sum_str = "주간 걸음수 합 : " + weekDataSum;
                String avg_str = "주간 평균 걸음수 : " + avg;
                String goal = "주간 목표 총걸음수 : " + stepGoal;
                String analysis_str = "\n";
                if(weekDataSum < stepGoal / 2) analysis_str += "걸음수가 많이 부족해요! \n조금 더 열심히 걸어보는건 어떨까요?";
                else if(weekDataSum < stepGoal) analysis_str += "열심히 걷고 있군요? \n조금 더 걸어보아요!";
                else if(weekDataSum > stepGoal) analysis_str += "목표치를 달성하였군요. \n건강한 삶을 살고 계시네요!";
                tv_content_stepCount.setText(day + "\n" + today + "\n" + sum_str + "\n" + avg_str + "\n" + goal + "\n" + analysis_str);
            }
        }
    }
}
