package com.team_comfortable.forhealthylife.ui.weight;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team_comfortable.forhealthylife.R;

import java.util.HashMap;
import java.util.Map;

public class WeightFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private DatePicker datePicker;
    private EditText inputWeight;
    private TextView displayWeight;
    private Button deleteWeightBtn, registerWeightBtn;
    private String pickerDate, findWeight;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_weight, container, false);
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

        // Initialize View
        datePicker = (DatePicker) root.findViewById(R.id.datePicker_weight);
        inputWeight = (EditText) root.findViewById(R.id.editText_inputWeight);
        displayWeight = (TextView) root.findViewById(R.id.textView_displayWeight);
        deleteWeightBtn = (Button) root.findViewById(R.id.btn_weight_delete);
        registerWeightBtn = (Button) root.findViewById(R.id.btn_weight_register);

        // Initialize Picker's Date (초기화 하지 않으면 pickerDate는 null 상태)
        String currYear = String.valueOf(datePicker.getYear()).substring(2, 4);
        String currMonth = String.valueOf(datePicker.getMonth() + 1);
        if(currMonth.length() == 1) currMonth = "0" + currMonth;
        String currDay = String.valueOf(datePicker.getDayOfMonth());
        if(currDay.length() == 1) currDay = "0" + currDay;
        pickerDate = currYear + currMonth + currDay;

        // API 26(O) 버전부터 지원, Picker Listener
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener()
            {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    pickerDate = getYYMMDDValue(year, monthOfYear+1, dayOfMonth);
                    handleWeightInDB(pickerDate, "", "find");
                }
            });
        }

        // 삭제, 추가 버튼 Click Listener
        deleteWeightBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(pickerDate != null)
                {
                    handleWeightInDB(pickerDate, "", "remove");
                    displayWeight.setText("0" + " (kg)");
                }
            }
        });
        registerWeightBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(pickerDate != null)
                {
                    String input = String.valueOf(inputWeight.getText());
                    handleWeightInDB(pickerDate, input, "add");
                    displayWeight.setText(input + " (kg)");
                }
            }
        });
    }

    // Date Picker 의 날짜를 yyMMdd 포맷으로 반환
    private String getYYMMDDValue(int year, int monOfYear, int dayOfMonth)
    {
        String year2 = String.valueOf(year);
        String monthOfYear2 = String.valueOf(monOfYear);
        String dayOfMonth2 = String.valueOf(dayOfMonth);

        year2 = year2.substring(2, 4);
        if(monthOfYear2.length() == 1) monthOfYear2 = "0" + monthOfYear2;
        if(dayOfMonth2.length() == 1) dayOfMonth2= "0" + dayOfMonth2;

        return year2 + monthOfYear2 + dayOfMonth2;
    }

    // Firebase DB에 날짜 별 Weight 데이터 추가, 제거, 찾기
    public void handleWeightInDB(final String date, final String weight, final String handleMode)
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userWeight");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                if(handleMode.equals("add"))
                {
                        map.put(date, String.valueOf(weight));
                        dbReference.updateChildren(map);
                }
                else if(handleMode.equals("remove"))
                {
                    for(DataSnapshot data : dataSnapshot.getChildren())
                    {
                        String key = data.getKey();
                        if(key.equals(date))
                        {
                            dbReference.child(date).removeValue();
                        }
                    }
                }
                else if(handleMode.equals("find"))
                {
                    findWeight = "0 (KG)";
                    for(DataSnapshot data : dataSnapshot.getChildren())
                    {
                        String key = data.getKey();
                        if(key.equals(date))
                        {
                            findWeight = data.getValue().toString() + " (kg)";
                            break;
                        }
                    }
                    displayWeight.setText(findWeight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
