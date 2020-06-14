package com.team_comfortable.forhealthylife.ui.calendar.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team_comfortable.forhealthylife.R;
import com.team_comfortable.forhealthylife.ui.eating.RiceFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ScheduleFragment extends Fragment{

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String schedule;
    private String[] scheduleList = {"1", "2","12"};

    public void initFirebase()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    private CustomList adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //editScheduleInDB();
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ListView listview = view.findViewById(R.id.list_schedule);
        adapter = new CustomList((Activity) view.getContext());
        editScheduleInDB();
        listview.setAdapter(adapter);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CalendarFragment calendarFragment = new CalendarFragment();
                transaction.replace(R.id.fragment_calendar, calendarFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    public void editScheduleInDB() {
        initFirebase();
        DatabaseReference userScheduleDB = mReference.child("UserList").child(mUser.getUid()).child("userSchedule");
        userScheduleDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = new HashMap<String, Object>();
                String date = "200613";
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey() + "";
                    if (date.equals(key)) {
                        schedule = data.getValue().toString();
                        Log.i("tag2", "78979879");
                        scheduleList = schedule.split("/");
                        Log.i("tag1", scheduleList[0]);
                        Log.i("tag1", scheduleList[1]);
                        Log.i("tag1", scheduleList[2]);
                        adapter.addAll(scheduleList);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    String[] riceName = {"1","2","3"};

    public class CustomList extends ArrayAdapter<String>
    {
        private final Activity context;


        public CustomList(Activity context)
        {
            super(context, R.layout.schedule_item, new ArrayList<String>());
            this.context = context;

        }

        public View getView(int position, View view, ViewGroup parent)
        {
            final int pos = position;

            if (view == null)
            {
                final Context context = parent.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.schedule_item, parent, false);
            }
            ImageView dot = (ImageView) view.findViewById(R.id.dot);
            TextView schList = (TextView) view.findViewById(R.id.schedule);
            Log.i("tag4", "44");
            dot.setImageResource(R.drawable.ic_circle_sky);
            schList.setText(scheduleList[position]);
            return view;
        }



    }



}
