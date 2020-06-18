package com.team_comfortable.forhealthylife.ui.calendar.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team_comfortable.forhealthylife.R;
import com.team_comfortable.forhealthylife.ui.calendar.model.CalendarHeader;
import com.team_comfortable.forhealthylife.ui.calendar.model.Day;
import com.team_comfortable.forhealthylife.ui.calendar.model.EmptyDay;
import com.team_comfortable.forhealthylife.ui.calendar.model.ViewModel;

import java.util.Calendar;
import java.util.List;


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
    {
        Object item = mCalendarList.get(position);
        if (item instanceof Long) {
            return HEADER_TYPE;
        }
        else if (item instanceof String){
            return EMPTY_TYPE;
        }
        else {
            return DAY_TYPE;
        }
    }

    // viewHolder 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == HEADER_TYPE)
        {
                HeaderViewHolder viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.fragment_calendar_header, parent, false));
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)viewHolder.itemView.getLayoutParams();
                params.setFullSpan(true);
                viewHolder.itemView.setLayoutParams(params);
                return viewHolder;
        } else if (viewType == EMPTY_TYPE) {
            return new EmptyViewHolder(inflater.inflate(R.layout.fragment_calendar_day_empty, parent, false));
        }
        else {
            return new DayViewHolder(inflater.inflate(R.layout.fragment_calendar_day, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position)
    {
        int viewType = getItemViewType(position);

        if (viewType == HEADER_TYPE)
        {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;

            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            if (item instanceof Long) {
                model.setHeader((Long) item);
            }
            holder.bind(model);
        }

        else if (viewType == EMPTY_TYPE)
        {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            EmptyDay model = new EmptyDay();
            holder.bind(model);
        }

        else if (viewType == DAY_TYPE)
        {
            DayViewHolder holder = (DayViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            Day model = new Day();
            if (item instanceof Calendar) {
                model.setCalendar((Calendar) item);
            }
            holder.bind(model);
        }
    }

    @Override
    public int getItemCount()
    {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder
    {

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
            String header = ((CalendarHeader)model).getHeader();
            itemHeaderTitle.setText(header);
        };

    }


    private class EmptyViewHolder extends RecyclerView.ViewHolder
    {
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

    private class DayViewHolder extends RecyclerView.ViewHolder
    {
        TextView itemDay;
        ImageView imgDay;
        String month;

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
        };
    }
}