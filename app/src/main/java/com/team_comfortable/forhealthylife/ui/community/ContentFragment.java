package com.team_comfortable.forhealthylife.ui.community;

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
import com.team_comfortable.forhealthylife.MainActivity;
import com.team_comfortable.forhealthylife.OnBackPressedListener;
import com.team_comfortable.forhealthylife.R;
import com.team_comfortable.forhealthylife.ui.calendar.fragment.CalendarFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ContentFragment extends Fragment implements OnBackPressedListener {

    private int position;
    private String key, commentKey;
    MainActivity activity;
    private ArrayList<String> comment_textList = new ArrayList<String>();
    private ArrayList<String> comment_writerList = new ArrayList<String>();
    private ArrayList<String> comment_timeList = new ArrayList<String>();
    private CustomList adapter;
    private ListView listview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }
/*
    public void getPosition(int position){
        this.position = position;
    }*/
    public void getKey(String key){
        this.key = key;
    }

    private TextView titleText,writerText,dateText,contentText;
    private EditText commentText;
    private Button btnContentRemove;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        titleText = view.findViewById(R.id.content_title);
        writerText = view.findViewById(R.id.content_writer);
        dateText = view.findViewById(R.id.content_date);
        contentText = view.findViewById(R.id.content_board);
        listview = view.findViewById(R.id.comment_list);

        commentText = view.findViewById(R.id.commentText);

        Button btnBack = view.findViewById(R.id.btn_contentBack);
        Button btnCommentEdit = view.findViewById(R.id.btn_commentEdit);
        btnContentRemove = view.findViewById(R.id.btn_contentRemove);


        adapter = new CustomList((Activity) getContext());

        editCommentListInDB();
        listview.setAdapter(adapter);
        adapter.setOnClicklistener3(new OnlistClickListener3() {
            @Override
            public void onListClick(int position) {
                checkRemove2(position);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CommunityFragment communityFragment = new CommunityFragment();
                transaction.replace(R.id.fragment_community, communityFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btnContentRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRemove();

            }
        });
        btnCommentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCommentInDB();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setContentInDB();
        checkContentInDB();

        return view;
    }

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;


    public void initFirebase()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    //firebase에서 게시물의 정보를 받아옵니다.
    public void setContentInDB(){
        initFirebase();
        final DatabaseReference communityContentDB = mReference.child("Community").child(key);
        communityContentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey() + "";
                    switch(key){
                        case "title":
                            titleText.setText(data.getValue().toString());
                            break;
                        case "writer":
                            writerText.setText(data.getValue().toString());
                            break;
                        case "content":
                            contentText.setText(data.getValue().toString());
                            break;
                        case "date":
                            dateText.setText(data.getValue().toString());
                            break;
                        default:
                            break;
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    //사용자가 글 삭제 권한이 있는지를 체크합니다.
    public void checkContentInDB(){
        initFirebase();
        final DatabaseReference communityContentDB = mReference.child("Community").child(key);
        communityContentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey() + "";
                    switch(key){
                        case "userUID":
                            if(data.getValue().toString().equals(mUser.getUid())){
                                btnContentRemove.setVisibility(View.VISIBLE);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void removeContentInDB(){
        initFirebase();
        DatabaseReference communityContentDB = mReference.child("Community").child(key);
        communityContentDB.removeValue();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        CommunityFragment communityFragment = new CommunityFragment();
        transaction.replace(R.id.fragment_community, communityFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void checkRemove(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("게시물 삭제");
        dialog.setMessage("게시물을 삭제하시겠습니까?");
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setPositiveButton("예", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                removeContentInDB();
                Toast.makeText(getContext(), "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("아니오.", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //Toast.makeText(getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();

    }

    //firebase에 댓글 내용을 입력합니다.
    public void editCommentInDB(){

        setTime();
        DatabaseReference communityReference = mReference.child("Community").child(key).child("comment");
        Map<String, Object> commentInfo = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content", commentText.getText().toString());
        map.put("date", currentDate);
        map.put("writer", mUser.getDisplayName());
        map.put("userUID", mUser.getUid());
        commentInfo.put(timeDate, map);
        communityReference.updateChildren(commentInfo);
        commentText.setText(null);
        Toast.makeText(getContext(), "댓글을 등록했습니다.", Toast.LENGTH_SHORT).show();
        adapter = new CustomList((Activity) getContext());
        editCommentListInDB();
        listview.setAdapter(adapter);
        adapter.setOnClicklistener3(new OnlistClickListener3() {
            @Override
            public void onListClick(int position) {
                checkRemove2(position);
            }
        });

    }



    //firebase에 댓글 data를 받아와 list를 작성합니다.
    public void editCommentListInDB(){
        comment_textList = new ArrayList<String>();
        comment_writerList = new ArrayList<String>();
        comment_timeList = new ArrayList<String>();
        checkCommentInDB();
        initFirebase();
        DatabaseReference commentListDB = mReference.child("Community").child(key).child("comment");
        commentListDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    String key2 = data.getKey() + "";
                    DatabaseReference commentListDB2 = mReference.child("Community").child(key).child("comment").child(key2);
                    commentListDB2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String key = data.getKey() + "";
                                switch (key) {
                                    case "content":
                                        comment_textList.add(data.getValue().toString());

                                        break;
                                    case "writer":
                                        comment_writerList.add(data.getValue().toString());
                                        break;
                                    case "date":
                                        comment_timeList.add(data.getValue().toString());
                                        break;
                                    default:
                                        break;
                                }
                            }
                            adapter.add(comment_textList.get(0));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                //adapter.addAll(comment_textList);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public ArrayList<Integer> checkPosition = new ArrayList<Integer>();

    //사용자가 댓글 삭제 권한이 있는지를 체크합니다
    public void checkCommentInDB(){
        checkPosition = new ArrayList<Integer>();
        initFirebase();
        final DatabaseReference communityContentDB = mReference.child("Community").child(key).child("comment");
        communityContentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key2 = data.getKey() + "";
                    final int cun = count;
                    DatabaseReference commentListDB2 = mReference.child("Community").child(key).child("comment").child(key2);
                    commentListDB2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String key = data.getKey() + "";
                                switch(key){
                                    case "userUID":
                                        if(data.getValue().toString().equals(mUser.getUid())){
                                            checkPosition.add(cun);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    count++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    //
    public void checkRemove2(int position){
        final int pos = position;
        DatabaseReference commentDB = mReference.child("Community").child(key).child("comment");
        commentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey() + "";
                    if(pos == count){
                        commentKey = key;
                        Log.i("tag",commentKey);
                    }
                    count++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("댓글 삭제");
        dialog.setMessage("댓글을 삭제하시겠습니까?");
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setPositiveButton("예", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                removeCommentInDB(pos);
                Toast.makeText(getContext(), "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("아니오.", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //Toast.makeText(getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();

    }

    //firebase에서 댓글 정보를 삭제합니다.
    public void removeCommentInDB(int position){
        final int pos = position;
        initFirebase();
        DatabaseReference commentDB = mReference.child("Community").child(key).child("comment");
        commentDB.child(commentKey).removeValue();
        adapter = new CustomList((Activity) getContext());
        editCommentListInDB();
        listview.setAdapter(adapter);
        adapter.setOnClicklistener3(new OnlistClickListener3() {
            @Override
            public void onListClick(int position) {
                checkRemove2(position);
            }
        });

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
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setOnBackPressedListener(this);
    }

    public interface OnlistClickListener3 {
        public void onListClick(int position);
    }

    public class CustomList extends ArrayAdapter<String>
    {
        private final Activity context;

        OnlistClickListener3 mClickedlistener3;

        public void setOnClicklistener3(OnlistClickListener3 mClickedlistener){
            this.mClickedlistener3 = mClickedlistener;
        }

        public CustomList(Activity context)
        {
            super(context, R.layout.comment_item, new ArrayList<String>());
            this.context = context;
        }

        public View getView(int position, View view, ViewGroup parent)
        {
            final int pos = position;

            if (view == null)
            {
                final Context context = parent.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.comment_item, parent, false);
            }
            /*
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.community_item, null, true);*/
            TextView textList = (TextView) view.findViewById(R.id.commentText);
            TextView writerList = (TextView) view.findViewById(R.id.commentWriter);
            TextView timeList = (TextView) view.findViewById(R.id.commentDate);
            Button btncommentRemove = (Button) view.findViewById(R.id.btn_commentRemove);
            Log.i("tag",checkPosition+"");
            for(int i =0; i < checkPosition.size();i++){
                if(checkPosition.get(i) == pos){
                    btncommentRemove.setVisibility(View.VISIBLE);
                }
            }
            textList.setText(comment_textList.get(position));
            writerList.setText(comment_writerList.get(position));
            timeList.setText(comment_timeList.get(position));

            btncommentRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickedlistener3.onListClick(pos);
                }
            });
            return view;
        }

    }

}
