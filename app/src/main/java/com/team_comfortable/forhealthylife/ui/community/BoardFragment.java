package com.team_comfortable.forhealthylife.ui.community;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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
import com.team_comfortable.forhealthylife.MainActivity;
import com.team_comfortable.forhealthylife.OnBackPressedListener;
import com.team_comfortable.forhealthylife.R;
import com.team_comfortable.forhealthylife.ui.calendar.fragment.CalendarFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class BoardFragment extends Fragment implements OnBackPressedListener {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private EditText EditBoard, EditBoardTitle;
    MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

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
        View root = (View)inflater.inflate(R.layout.fragment_board, container, false);
        Button EditBtn = (Button) root.findViewById(R.id.btn_edit);
        Button DelBtn = (Button) root.findViewById(R.id.btn_del);
        EditBoardTitle = (EditText) root.findViewById(R.id.edit_boardTitle);
        EditBoard = (EditText) root.findViewById(R.id.edit_board);
        initFirebase();
        EditBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String boardTitle = EditBoardTitle.getText().toString();
                String board = EditBoard.getText().toString();
                if(boardTitle.equals("")){
                    Toast.makeText(getContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(board.equals("")){
                    Toast.makeText(getContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                setBoardInDB();
                Toast.makeText(getContext(), "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });
        DelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CommunityFragment communityFragment = new CommunityFragment();
                transaction.replace(R.id.fragment_community, communityFragment);
                transaction.addToBackStack(null);
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

    //firebase에 글 정보를 입력합니다.
    public void setBoardInDB()
    {
        setTime();
        DatabaseReference communityReference = mReference.child("Community");
        Map<String, Object> boardInfo = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content", EditBoard.getText().toString());
        map.put("date", currentDate);
        map.put("title", EditBoardTitle.getText().toString());
        map.put("writer", mUser.getDisplayName());
        map.put("userUID", mUser.getUid());
        boardInfo.put(timeDate, map);
        communityReference.updateChildren(boardInfo);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        CommunityFragment communityFragment = new CommunityFragment();
        transaction.replace(R.id.fragment_community, communityFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    String currentDate, timeDate;
    //현재의 시간과 날짜를 받아옵니다.
    public void setTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd.");
        timeDate = timeFormat.format(calendar.getTime());
        currentDate  = dateFormat.format(calendar.getTime());

    }



    @Override
    public void onBackPressed() {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        CommunityFragment communityFragment = new CommunityFragment();
        transaction.replace(R.id.fragment_community, communityFragment);

        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setOnBackPressedListener(this);
    }
}
