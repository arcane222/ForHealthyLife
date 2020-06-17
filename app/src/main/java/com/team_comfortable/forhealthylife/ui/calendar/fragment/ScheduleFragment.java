package com.team_comfortable.forhealthylife.ui.calendar.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

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
    private String[] scheduleList;
    private String Date;

    public void initFirebase()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    //클릭한 날짜(일)를 받아오는 코드입니다.
    public void getDate(String date){
        this.Date = date;
    }
    //클릭한 날짜(달,일)를 받아오는 코드입니다.
    public String newDate(String date){
        String MM = date.substring(2, 4);
        if(MM.charAt(0) == '0') {
            MM = MM.substring(1, 2);
        }
        String DD = date.substring(4, 6);
        if(DD.charAt(0) == '0') {
            DD = DD.substring(1, 2);
        }

        return MM+"월 "+DD+"일";
    }

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    private CustomList adapter;
    private ListView listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        listview = view.findViewById(R.id.list_schedule);
        TextView textDate = view.findViewById(R.id.text_date);
        textDate.setText(newDate(Date));
        adapter = new CustomList((Activity) view.getContext());
        editScheduleInDB();
        listview.setAdapter(adapter);
        adapter.setOnClicklistener(new OnClickListener() {
            @Override
            public void onClick(int position) {
                if(scheduleList[0] != "일정 없음") {
                    checkCancel(position);
                }

            }
        });
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



    //firebase에서 schedule list를 받아옵니다.
    public void editScheduleInDB() {
        initFirebase();
        DatabaseReference userScheduleDB = mReference.child("UserList").child(mUser.getUid()).child("userSchedule");

        userScheduleDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = new HashMap<String, Object>();
                String date = Date;

                boolean checkedNull = true;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.i("tag","3");
                    String key = data.getKey() + "";
                    if (date.equals(key)) {
                        schedule = data.getValue().toString();
                        scheduleList = schedule.split("/");
                        checkedNull = false;
                        break;
                    }
                    scheduleList = new String[]{"일정 없음"};
                    checkedNull = false;
                }
                if(checkedNull){
                    scheduleList = new String[]{"일정 없음"};
                }
                adapter.addAll(scheduleList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    //firebase에서 취소한 list 삭제합니다.
    public void cancelScheduleInDB(String list){
        initFirebase();
        DatabaseReference userScheduleDB = mReference.child("UserList").child(mUser.getUid()).child("userSchedule");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Date, list);
        userScheduleDB.updateChildren(map);
        adapter = new CustomList((Activity) getContext());
        editScheduleInDB();
        listview.setAdapter(adapter);
        adapter.setOnClicklistener(new OnClickListener() {
            @Override
            public void onClick(int position) {
                if(scheduleList[0] != "일정 없음") {
                    checkCancel(position);

                }

            }
        });
    }

    //일정이 모두 삭제될 경우 호출합니다.
    public void removeScheduleInDB(){
        initFirebase();
        DatabaseReference userScheduleDB = mReference.child("UserList").child(mUser.getUid()).child("userSchedule");
        userScheduleDB.child(Date).removeValue();
        adapter = new CustomList((Activity) getContext());
        editScheduleInDB();
        listview.setAdapter(adapter);
        adapter.setOnClicklistener(new OnClickListener() {
            @Override
            public void onClick(int position) {
                if(scheduleList[0] != "일정 없음") {
                    checkCancel(position);

                }

            }
        });
    }

    public interface OnClickListener {
        public void onClick(int position);
    }

    public class CustomList extends ArrayAdapter<String>
    {
        private final Activity context;


        OnClickListener mClickedlistener;

        public void setOnClicklistener(OnClickListener mClickedlistener){
            this.mClickedlistener = mClickedlistener;
        }


        public CustomList(Activity context)
        {
            super(context, R.layout.schedule_item, new ArrayList<String>());
            this.context = context;

        }


        public View getView(int position, View view, ViewGroup parent) {
            final int pos = position;
            if (view == null) {
                final Context context = parent.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.schedule_item, parent, false);
            }
            ImageView dot = (ImageView) view.findViewById(R.id.dot);
            TextView schList = (TextView) view.findViewById(R.id.schedule);
            dot.setImageResource(R.drawable.ic_circle_sky);
            schList.setText(scheduleList[position]);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickedlistener.onClick(pos);
                    return true;
                }
            });
            return view;
        }
    }


    public void checkCancel(int position){
        final int pos = position;
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("일정 취소");
        dialog.setMessage("현재 일정을 취소하시겠습니까?");
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setPositiveButton("예", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String list = "";
                for(int i = 0; i<scheduleList.length;i++){
                    if(i != pos){
                        if(list == ""){
                            list = scheduleList[i];
                        }
                        else{
                            list = list + "/" + scheduleList[i];
                        }
                    }
                }
                schedule = list;
                if(list == "") removeScheduleInDB();
                else {
                    cancelScheduleInDB(list);
                }
                Toast.makeText(getContext(), "일정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //Toast.makeText(getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

}
