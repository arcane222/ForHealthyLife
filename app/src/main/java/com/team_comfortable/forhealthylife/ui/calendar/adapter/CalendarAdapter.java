package com.team_comfortable.forhealthylife.ui.calendar.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team_comfortable.forhealthylife.LoginActivity;
import com.team_comfortable.forhealthylife.MainActivity;
import com.team_comfortable.forhealthylife.R;
import com.team_comfortable.forhealthylife.ui.calendar.fragment.CalendarFragment;
import com.team_comfortable.forhealthylife.ui.calendar.fragment.EnterFragment;
import com.team_comfortable.forhealthylife.ui.calendar.model.CalendarHeader;
import com.team_comfortable.forhealthylife.ui.calendar.model.Day;
import com.team_comfortable.forhealthylife.ui.calendar.model.EmptyDay;
import com.team_comfortable.forhealthylife.ui.calendar.model.ViewModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarAdapter extends RecyclerView.Adapter
{
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private List<Object> mCalendarList;


    public interface OnItemClickListener {
        public void onItemClick(String date);
    }


    OnItemClickListener mClicklistener;

    public void setOnItemClicklistener(OnItemClickListener mClicklistener){
        this.mClicklistener = mClicklistener;
    }




    public CalendarAdapter(List<Object> calendarList) {
        mCalendarList = calendarList;
    }

    public void setCalendarList(List<Object> calendarList)
    {
        mCalendarList = calendarList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    { //뷰타입 나누기
        Object item = mCalendarList.get(position);
        if (item instanceof Long) {
            return HEADER_TYPE; //날짜 타입
        }
        else if (item instanceof String){
            return EMPTY_TYPE; // 비어있는 일자 타입
        }
        else {
            return DAY_TYPE; // 일자 타입
        }
    }


    // viewHolder 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 날짜 타입
        if (viewType == HEADER_TYPE)
        {
                HeaderViewHolder viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.fragment_calendar_header, parent, false));

                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)viewHolder.itemView.getLayoutParams();
                params.setFullSpan(true); //Span을 하나로 통합하기
                viewHolder.itemView.setLayoutParams(params);

                return viewHolder;
        } else if (viewType == EMPTY_TYPE) {  //비어있는 일자 타입
            return new EmptyViewHolder(inflater.inflate(R.layout.fragment_calendar_day_empty, parent, false));
        }
        else {  // 일자 타입
            return new DayViewHolder(inflater.inflate(R.layout.fragment_calendar_day, parent, false));
        }
    }

    // 데이터 넣어서 완성시키는것
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position)
    {
        int viewType = getItemViewType(position);

        /**날짜 타입 꾸미기*/
        /** EX : 2018년 8월*/
        if (viewType == HEADER_TYPE)
        {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;

            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            // long type의 현재시간
            if (item instanceof Long) {
                // 현재시간 넣으면, 2017년 7월 같이 패턴에 맞게 model에 데이터들어감.
                model.setHeader((Long) item);
            }
            // view에 표시하기
            holder.bind(model);
        }
        /** 비어있는 날짜 타입 꾸미기 */
        /** EX : empty */
        else if (viewType == EMPTY_TYPE)
        {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            EmptyDay model = new EmptyDay();
            holder.bind(model);
        }
        /** 일자 타입 꾸미기 */
        /** EX : 22 */
        else if (viewType == DAY_TYPE)
        {

            DayViewHolder holder = (DayViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            Day model = new Day();

            if (item instanceof Calendar) {
                // Model에 Calendar값을 넣어서 몇일인지 데이터 넣기
                model.setCalendar((Calendar) item);
            }
            // Model의 데이터를 View에 표현하기

            holder.bind(model);

        }
    }

    // 개수구하기
    @Override
    public int getItemCount()
    {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }

    /** viewHolder */
    private class HeaderViewHolder extends RecyclerView.ViewHolder
    { //날짜 타입 ViewHolder

        TextView itemHeaderTitle;

        public HeaderViewHolder(@NonNull View itemView)
        {
            super(itemView);
            initView(itemView);
        }


        public void initView(View v){
            itemHeaderTitle = (TextView)v.findViewById(R.id.item_header_title);
        }

        public void bind(ViewModel model)
        {
            // 일자 값 가져오기
            String header = ((CalendarHeader)model).getHeader();

            // header에 표시하기, ex : 2018년 8월
            itemHeaderTitle.setText(header);
        };

    }


    private class EmptyViewHolder extends RecyclerView.ViewHolder
    {  // 비어있는 요일 타입 ViewHolder


        public EmptyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            initView(itemView);
        }
        public void initView(View v)
        {
        }

        public void bind(ViewModel model)
        {
        };
    }



    // TODO : item_day와 매칭
    private class DayViewHolder extends RecyclerView.ViewHolder
    {// 요일 입 ViewHolder

        TextView itemDay, dayText;
        ImageView imgDay;
        String month;


        public DayViewHolder(@NonNull View itemView)
        {
            super(itemView);
            initView(itemView);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    mClicklistener.onItemClick(month);
                }
            });
        }

        public void initView(View v){
            itemDay = (TextView)v.findViewById(R.id.item_day);
            imgDay = (ImageView)v.findViewById(R.id.item_img);
        }

        public void bind(final ViewModel model)
        {
            final ViewModel vmodel = model;
            // 일자 값 가져오기
            initFirebase();
            DatabaseReference userScheduleDB = mReference.child("UserList").child(mUser.getUid()).child("userSchedule");
            userScheduleDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String tmp = "000000";
                    for(DataSnapshot data : dataSnapshot.getChildren())
                    {
                        String key= data.getKey()+"";
                        tmp = tmp + "/" + data.getKey();
                    }
                    mDayList = tmp.split("/");


                    String day = ((Day)vmodel).getDay();
                    month = ((Day)vmodel).getDate();
                    itemDay.setText(day);
                    for(int i =0; i<mDayList.length;i++){
                        if(month.equals(mDayList[i])) {
                            imgDay.setImageResource(R.drawable.ic_calendar);
                            break;
                        }
                        else{
                            imgDay.setImageResource(R.drawable.ic_background_white);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



           // dayText.setBackgroundColor(Color.RED);
           // dayText.setText(day);
        };

        public String getMonth(){

            return month;
        }
    }


    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String Date;
    private String[] mDayList = {};
    public void initFirebase()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }
/*
    public void checkInDB(){
        Log.i("tag","3");
        initFirebase();
        Log.i("tag","4");
        DatabaseReference userScheduleDB = mReference.child("UserList").child(mUser.getUid()).child("userSchedule");
        userScheduleDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tmp = "000000";
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String key= data.getKey()+"";
                    tmp = tmp + "/" + data.getKey();
                }
                mDayList = tmp.split("/");
                Log.i("tag",mDayList[0]+mDayList[1]+mDayList[2]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
*/



}