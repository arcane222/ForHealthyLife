package com.team_comfortable.forhealthylife.ui.community;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team_comfortable.forhealthylife.R;
import com.team_comfortable.forhealthylife.ui.calendar.fragment.CalendarFragment;
import com.team_comfortable.forhealthylife.ui.calendar.fragment.ScheduleFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommunityFragment extends Fragment {



    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private ArrayList<String> board_titleList = new ArrayList<String>();
    private ArrayList<String> board_contentList = new ArrayList<String>();
    private ArrayList<String> board_writerList = new ArrayList<String>();
    private ArrayList<String> board_timeList = new ArrayList<String>();
    private ArrayList<String> board_keyList = new ArrayList<String>();


    public void initFirebase()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private CustomList adapter;
    private ListView listview;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        listview = view.findViewById(R.id.community_board);
        Button sinupBtn = view.findViewById(R.id.btn_signup);
        sinupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                BoardFragment boardFragment = new BoardFragment();
                transaction.add(R.id.fragment_community, boardFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        adapter = new CustomList((Activity) view.getContext());
        editBoardInDB();
        listview.setAdapter(adapter);
        adapter.setOnClicklistener2(new OnlistClickListener2() {
            @Override
            public void onListClick(String key) {
                Log.i("tag", key);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ContentFragment contentFragment = new ContentFragment();
                contentFragment.getKey(key);
                transaction.replace(R.id.fragment_community, contentFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return view;
    }

    //firebase에서 community list를 받아옵니다.
    public void editBoardInDB() {
        initFirebase();
        DatabaseReference userCommunityDB = mReference.child("Community");
        userCommunityDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final String key12 = data.getKey() + "";
                    final DatabaseReference userCommunityDB = mReference.child("Community").child(key12);
                    board_keyList.add(data.getKey());
                    userCommunityDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String key = data.getKey() + "";
                                switch(key){
                                    case "title":
                                        board_titleList.add(data.getValue().toString());
                                        break;
                                    case "writer":
                                        board_writerList.add(data.getValue().toString());
                                        break;
                                    case "content":
                                        board_contentList.add(data.getValue().toString());

                                        break;
                                    case "date":
                                        board_timeList.add(data.getValue().toString());
                                        break;
                                    default:
                                        break;
                                }
                            }
                            adapter.add(board_titleList.get(0));
                            //adapter.add(board_writerList.get(0));
                            //adapter.add(board_timeList.get(0));

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        /*DatabaseReference userScheduleDB = mReference.child("Community").child("abc");
        Log.i("tag4", String.valueOf(1));
        final int finalI = 0;
        userScheduleDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey() + "";
                    Log.i("tag", key);
                    switch(key){
                        case "title":
                            board_titleList.add(data.getValue().toString());
                            break;
                        case "writer":
                            board_writerList.add(data.getValue().toString());
                            break;
                        case "content":
                            board_contentList.add(data.getValue().toString());

                            break;
                        case "date":
                            board_timeList.add(data.getValue().toString());
                            break;
                        default:
                            break;
                    }
                }
                Log.i("tag", "4");

                adapter.add(board_titleList.get(0));
                //adapter.add(board_writerList.get(0));
                //adapter.add(board_timeList.get(0));
                Log.i("tag", "5");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });*/



    }
    /*
    public interface OnlistClickListener2 {
        public void onListClick(int position);
    }
    */


    public interface OnlistClickListener2 {
        public void onListClick(String key);
    }

    public class CustomList extends ArrayAdapter<String>
    {
        private final Activity context;



        OnlistClickListener2 mClickedlistener2;

        public void setOnClicklistener2(OnlistClickListener2 mClickedlistener){
            this.mClickedlistener2 = mClickedlistener;
        }



        public CustomList(Activity context)
        {
            super(context, R.layout.community_item, new ArrayList<String>());
            this.context = context;
        }

        public View getView(int position, View view, ViewGroup parent)
        {
            final int pos = position;

            if (view == null)
            {
                final Context context = parent.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.community_item, parent, false);
            }
            /*
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.community_item, null, true);*/

            TextView titleList = (TextView) view.findViewById(R.id.communityTitle);
            TextView writerList = (TextView) view.findViewById(R.id.communityWriter);
            TextView timeList = (TextView) view.findViewById(R.id.writeTime);

            titleList.setText(board_titleList.get(position));
            writerList.setText(board_writerList.get(position));
            timeList.setText(board_timeList.get(position));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mClickedlistener2.onListClick(board_keyList.get(pos));
                }
            });
            return view;
        }

    }

}
