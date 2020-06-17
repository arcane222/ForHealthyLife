package com.team_comfortable.forhealthylife.ui.profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team_comfortable.forhealthylife.R;

public class ProfileFragment extends Fragment
{
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private TextView tv_profile_title, tv_profile_uid, tv_profile_name,
            tv_profile_email, tv_profile_weight, tv_profile_stepCount, tv_profile_gender;
    private EditText et_profile_height, et_profile_age;
    private ImageView iv_profile_profileImg;
    private Button genderChangeBtn;
    private String height, age;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
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
        tv_profile_title = root.findViewById(R.id.tv_profile_title);
        tv_profile_uid = root.findViewById(R.id.tv_profile_uid);
        tv_profile_name = root.findViewById(R.id.tv_profile_name);
        tv_profile_email = root.findViewById(R.id.tv_profile_email);
        tv_profile_weight = root.findViewById(R.id.tv_profile_weight);
        tv_profile_stepCount = root.findViewById(R.id.tv_profile_stepCount);
        tv_profile_gender = root.findViewById(R.id.tv_profile_gender);
        et_profile_height = root.findViewById(R.id.et_profile_height);
        et_profile_age = root.findViewById(R.id.et_profile_age);
        iv_profile_profileImg = root.findViewById(R.id.iv_profile_profileImg);
        genderChangeBtn = root.findViewById(R.id.btn_genderChange);

        et_profile_height.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    et_profile_height.setText("");
                else
                    getCurrentWeightFromDB();
            }
        });
        et_profile_age.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    et_profile_age.setText("");
                else
                    getCurrentWeightFromDB();
            }
        });
        et_profile_height.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    String text = et_profile_height.getText().toString();
                    if(text.length() == 0) {
                        Toast.makeText(getContext(), "숫자를 입력해주세요. 예)181, 168", Toast.LENGTH_SHORT).show();
                        et_profile_height.clearFocus();
                        return true;
                    }
                    for(int i = 0; i < text.length(); i++)
                    {
                        if(!Character.isDigit(text.charAt(i)))
                        {
                            // 숫자를 입력한 것이 아닐경우
                            Toast.makeText(getContext(), "숫자를 입력해주세요. 예)181, 168", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                    if(Integer.parseInt(text) < 0 || Integer.parseInt(text) > 250)
                    {
                        Toast.makeText(getContext(), "0 ~ 250사이 값을 입력해주세요. 예)181, 168", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    else
                    {
                        et_profile_height.setText("      키 : " + text + " cm");
                        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userHeight");
                        dbReference.setValue(Integer.parseInt(text));
                        Toast.makeText(getContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                        et_profile_height.clearFocus();
                    }
                }
                return true;
            }
        });

        et_profile_age.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    String text = et_profile_age.getText().toString();
                    if(text.length() == 0) {
                        Toast.makeText(getContext(), "숫자를 입력해주세요. 예)24, 35", Toast.LENGTH_SHORT).show();
                        et_profile_age.clearFocus();
                        return true;
                    }
                    for(int i = 0; i < text.length(); i++)
                    {
                        if(!Character.isDigit(text.charAt(i)))
                        {
                            // 숫자를 입력한 것이 아닐경우
                            Toast.makeText(getContext(), "숫자를 입력해주세요. 예)24, 35", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                    if(Integer.parseInt(text) < 0 || Integer.parseInt(text) > 120)
                    {
                        Toast.makeText(getContext(), "0 ~ 120사이 값을 입력해주세요. 예)24, 35", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    else
                    {
                        et_profile_age.setText("      나이 : " + text);
                        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userAge");
                        dbReference.setValue(Integer.parseInt(text));
                        Toast.makeText(getContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                        et_profile_age.clearFocus();
                    }
                }
                return true;
            }
        });


        genderChangeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeGenderInDB();
            }
        });

        tv_profile_title.setText(mUser.getDisplayName() + " 님의 프로필");
        tv_profile_uid.setText("      UID : " + mUser.getUid());
        tv_profile_name.setText("      닉네임 : " + mUser.getDisplayName());
        tv_profile_email.setText("      E-mail : " + mUser.getEmail());
        Glide.with(getActivity()).load(mUser.getPhotoUrl()).into(iv_profile_profileImg);
        getCurrentWeightFromDB();
        getStepCountSumFromDB();
        getGenderHeightAgeFromDB();
    }

    private void getStepCountSumFromDB()
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userStepCount");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sum = 0;
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    sum = sum + Integer.parseInt(data.getValue().toString());
                }
                tv_profile_stepCount.setText("      총 걸음수 : " + String.valueOf(sum) + " (걸음)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getCurrentWeightFromDB()
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userWeight");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String weight = "";
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    weight = data.getValue().toString();
                }
                tv_profile_weight.setText("      체중 : " + weight + " kg");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getGenderHeightAgeFromDB()
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid());
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    if(data.getKey().equals("userGender"))
                    {
                        tv_profile_gender.setText("      성별 : " + data.getValue().toString());
                    }
                    else if(data.getKey().equals("userHeight"))
                    {
                        et_profile_height.setText("      키 : " + data.getValue().toString() + " cm");
                    }
                    else if(data.getKey().equals("userAge"))
                    {
                        et_profile_age.setText("      나이 : " + data.getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void changeGenderInDB()
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid());
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    if(data.getKey().equals("userGender"))
                    {
                        String gender = "";
                        if(data.getValue().toString().equals("남성")) {
                            gender = "여성";
                        }
                        else if(data.getValue().toString().equals("여성")) {
                            gender = "남성";
                        }
                        else {
                            gender = "남성";
                        }
                        dbReference.child("userGender").setValue(gender);
                        tv_profile_gender.setText("      성별 : " + gender);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
