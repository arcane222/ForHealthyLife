package com.team_comfortable.forhealthylife.ui.calendar.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


public class EnterFragment extends Fragment {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private EditText EditSch, EditDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initFirebase()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = (View)inflater.inflate(R.layout.fragment_enter, container, false);
        Button EnterBtn = (Button) root.findViewById(R.id.btn_enter);
        Button CancelBtn = (Button) root.findViewById(R.id.btn_cancel);
        EditSch = (EditText) root.findViewById(R.id.edit_sch);
        EditDate = (EditText) root.findViewById(R.id.edit_date);
        initFirebase();
        EnterBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String schedule = EditSch.getText().toString();
                String date = EditDate.getText().toString();
                if(!checkFormat(date)) {
                    Toast.makeText(getContext(), "날짜형식을 맞춰주세요.(YYMMDD)", Toast.LENGTH_SHORT).show();
                }
                else{
                    setScheduleInDB(date, schedule);
                    Toast.makeText(getContext(), "일정이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        CancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CalendarFragment calendarFragment = new CalendarFragment();
                transaction.replace(R.id.fragment_calendar, calendarFragment);
                transaction.addToBackStack(this.getClass().getSimpleName());
                transaction.commit();
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return root;
    }

    public void setScheduleInDB(final String date, final String schedule)
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userSchedule");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                String tmp = schedule;
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String key= data.getKey()+"";
                    if(key.equals(date))
                    {
                        tmp = data.getValue().toString() + "/" + schedule;
                        break;
                    }
                }
                map.put(date, tmp);
                dbReference.updateChildren(map);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CalendarFragment calendarFragment = new CalendarFragment();
                transaction.replace(R.id.fragment_calendar, calendarFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public boolean checkFormat(String data)
    {
        if(data.length() != 6) return false;
        else
        {
            for(int i = 0; i < data.length(); i++)
            {
                if(!Character.isDigit(data.charAt(i))) return false;
            }
        }
        int yymmdd = Integer.valueOf(data);
        yymmdd = yymmdd - (yymmdd / 1000000) * 100000;
        yymmdd = yymmdd - (yymmdd / 10000) * 10000;
        int month = yymmdd / 100;
        yymmdd = yymmdd - (yymmdd / 1000) * 1000;
        yymmdd = yymmdd - (yymmdd / 100) * 100;
        int day = yymmdd;
        Boolean check = ((month>= 1 && month <= 12) && (day >= 1 && day <= 31));
        if(!check) return false;
        return true;
    }
}
