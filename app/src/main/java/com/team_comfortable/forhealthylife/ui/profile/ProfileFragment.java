package com.team_comfortable.forhealthylife.ui.profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment
{
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private TextView tv_profile_title, tv_profile_uid, tv_profile_name,
            tv_profile_email, tv_profile_weight, tv_profile_stepCount,
            tv_profile_gender, tv_profile_height, tv_profile_age;
    private ImageView iv_profile_profileImg;
    private Button genderChangeBtn;

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
        tv_profile_height = root.findViewById(R.id.tv_profile_height);
        tv_profile_age = root.findViewById(R.id.tv_profile_age);
        iv_profile_profileImg = root.findViewById(R.id.iv_profile_profileImg);
        genderChangeBtn = root.findViewById(R.id.btn_genderChange);

        genderChangeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeGenderInDB();
            }
        });

        tv_profile_title.setText(mUser.getDisplayName() + " 님의 프로필");
        tv_profile_uid.setText("   UID : " + mUser.getUid());
        tv_profile_name.setText("   닉네임 : " + mUser.getDisplayName());
        tv_profile_email.setText("   E-mail : " + mUser.getEmail());
        Glide.with(getActivity()).load(mUser.getPhotoUrl()).into(iv_profile_profileImg);
        getDataInDB();
    }
    private void getDataInDB()
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid());
        final DatabaseReference dbReference2 = mReference.child("UserList").child(mUser.getUid()).child("userStepCount");
        final DatabaseReference dbReference3 = mReference.child("UserList").child(mUser.getUid()).child("userWeight");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    if(data.getKey().equals("userGender"))
                    {
                        tv_profile_gender.setText("   성별 : " + data.getValue().toString());
                    }
                    else if(data.getKey().equals("userAge"))
                    {
                        tv_profile_age.setText("   나이 : " + data.getValue().toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        dbReference2.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sum = 0;
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    sum = sum + Integer.parseInt(data.getValue().toString());
                }
                tv_profile_stepCount.setText("   총 걸음수 : " + String.valueOf(sum) + " (걸음)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        dbReference3.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String weight = "";
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    weight = data.getValue().toString();
                }
                tv_profile_weight.setText("   체중 : " + weight + " (kg)");
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
                        tv_profile_gender.setText("   성별 : " + gender);
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
