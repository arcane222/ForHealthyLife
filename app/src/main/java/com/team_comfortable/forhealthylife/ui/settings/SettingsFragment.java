package com.team_comfortable.forhealthylife.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class SettingsFragment extends Fragment
{
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private EditText et_setStepCountGoal, et_setWeightGoal;
    private Button setStepCountGoalBtn, setWeightGoalBtn, changeUiColorBtn, removeAccountDataBtn;

    private BottomNavigationView bottomNavView;
    private Toolbar mainToolbar;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        initFirebase();
        initData(root);
        getCurrentStepGoalFromDB();
        getCurrentWeightGoalFromDB();
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
        mainToolbar = ((MainActivity)getActivity()).getMainToolbar();
        bottomNavView = ((MainActivity)getActivity()).getBottomNavView();

        et_setStepCountGoal = root.findViewById(R.id.et_settings_userStepGoal);
        et_setWeightGoal = root.findViewById(R.id.et_settings_userWeightGoal);

        et_setStepCountGoal.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                    et_setStepCountGoal.setText("");
                else
                    getCurrentStepGoalFromDB();
            }
        });
        et_setWeightGoal.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                    et_setWeightGoal.setText("");
                else
                    getCurrentWeightGoalFromDB();
            }
        });

        setStepCountGoalBtn = root.findViewById(R.id.btn_settings_setStepGoal);
        setWeightGoalBtn = root.findViewById(R.id.btn_settings_setWeightGoal);
        changeUiColorBtn = root.findViewById(R.id.btn_settings_changeUiColor);
        removeAccountDataBtn = root.findViewById(R.id.btn_settings_removeData);

        setStepCountGoalBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String textInput = et_setStepCountGoal.getText().toString();
                if(textInput.length()  == 0)
                {
                    Toast.makeText(getContext(), "숫자를 입력해주세요. 예)3000, 1500", Toast.LENGTH_SHORT).show();
                    et_setStepCountGoal.clearFocus();
                    return;
                }
                for(int i = 0; i < textInput.length(); i++)
                {
                    if(!Character.isDigit(textInput.charAt(i)))
                    {
                        Toast.makeText(getContext(), "숫자를 입력해주세요. 예)3000, 1500", Toast.LENGTH_SHORT).show();
                        et_setStepCountGoal.clearFocus();
                        return;
                    }
                }
                if(Integer.parseInt(textInput) < 0 || Integer.parseInt(textInput) > 100000)
                {
                    Toast.makeText(getContext(), "0~10만 사이값을 입력해주세요. 예)3000, 1500", Toast.LENGTH_SHORT).show();
                    et_setStepCountGoal.clearFocus();
                    return;
                }
                else
                {
                    et_setStepCountGoal.setText("이번 주 목표 걸음 수 : " + textInput + " (걸음)");
                    final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userStepGoal");
                    dbReference.setValue(Integer.parseInt(textInput));
                    Toast.makeText(getContext(), "목표 걸음 수가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    et_setStepCountGoal.clearFocus();
                    return;
                }
            }
        });
        setWeightGoalBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String textInput = et_setWeightGoal.getText().toString();
                if(textInput.length()  == 0)
                {
                    Toast.makeText(getContext(), "숫자를 입력해주세요. 예)55, 60", Toast.LENGTH_SHORT).show();
                    et_setWeightGoal.clearFocus();
                    return;
                }
                for(int i = 0; i < textInput.length(); i++)
                {
                    if(!Character.isDigit(textInput.charAt(i)))
                    {
                        Toast.makeText(getContext(), "숫자를 입력해주세요. 예)55, 60", Toast.LENGTH_SHORT).show();
                        et_setWeightGoal.clearFocus();
                        return;
                    }
                }
                if(Integer.parseInt(textInput) < 0 || Integer.parseInt(textInput) > 300)
                {
                    Toast.makeText(getContext(), "0~300 사이값을 입력해주세요. 예)55, 60", Toast.LENGTH_SHORT).show();
                    et_setWeightGoal.clearFocus();
                    return;
                }
                else
                {
                    et_setWeightGoal.setText("이번 주 목표 체중 : " + textInput + " (kg)");
                    final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userWeightGoal");
                    dbReference.setValue(Integer.parseInt(textInput));
                    Toast.makeText(getContext(), "목표 체중이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    et_setWeightGoal.clearFocus();
                    return;
                }
            }
        });
        changeUiColorBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int R = (int) (Math.random() * 256);
                int G = (int) (Math.random() * 256);
                int B = (int) (Math.random() * 256);
                mainToolbar.setBackgroundColor(Color.rgb(R, G, B));
                bottomNavView.setBackgroundColor(Color.rgb(R, G, B));
            }
        });
        removeAccountDataBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("계정 기록 제거");
                dialog.setMessage("(경고) 현재 계정의 정보를 삭제하시겠습니까?\n구글계정 데이터와는 연관되지 않습니다.");
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setPositiveButton("예", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        Toast.makeText(getContext(), "삭제완료하였습니다.", Toast.LENGTH_SHORT).show();
                        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid());
                        dbReference.removeValue();
                        ((MainActivity) getActivity()).LogOut();
                    }
                });
                dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        Toast.makeText(getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
    }

    private void getCurrentStepGoalFromDB()
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid());
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String stepGoal = "";
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    if(data.getKey().equals("userStepGoal"))
                    {
                        stepGoal = data.getValue().toString();
                        break;
                    }
                }
                et_setStepCountGoal.setText("이번 주 목표 걸음 수 : " + stepGoal + " (걸음)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getCurrentWeightGoalFromDB()
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid());
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String weightGoal = "";
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    if(data.getKey().equals("userWeightGoal"))
                    {
                        weightGoal = data.getValue().toString();
                        break;
                    }
                }
                et_setWeightGoal.setText("이번 주 목표 체중 : " + weightGoal + " (kg)");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
