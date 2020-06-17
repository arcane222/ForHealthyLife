package com.team_comfortable.forhealthylife.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class SettingsFragment extends Fragment
{
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private void initFirebase()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }




    private EditText editStepGoal,editWeightGoal;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        initFirebase();
        editStepGoal = root.findViewById(R.id.userStepGoal);
        editWeightGoal = root.findViewById(R.id.userWeightGoal);
        editStepGoal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editStepGoal.setText("");
                }
                else
                    getStepWeightGoalFromDB();
            }
        });
        editWeightGoal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editWeightGoal.setText("");
                }
                else
                    getStepWeightGoalFromDB();
            }
        });

        editStepGoal.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                Log.i("tag", "12");
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {

                    String text =  editStepGoal.getText().toString();
                    if(text.length() == 0) {
                        Toast.makeText(getContext(), "숫자를 입력해주세요. 예)1400, 2500", Toast.LENGTH_SHORT).show();
                        editStepGoal.clearFocus();
                        return true;
                    }
                    for(int i = 0; i < text.length(); i++)
                    {
                        if(!Character.isDigit(text.charAt(i)))
                        {
                            // 숫자를 입력한 것이 아닐경우
                            Toast.makeText(getContext(), "숫자를 입력해주세요. 예)1400, 2500", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                    if(Integer.parseInt(text) < 0)
                    {
                        Toast.makeText(getContext(), "0 보다 큰 값을 입력해주세요. 예)1400, 2500", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    else
                    {
                        editStepGoal.setText("      목표 걸음 수 : " + text + " 걸음");
                        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userStepGoal");
                        dbReference.setValue(Integer.parseInt(text));
                        Toast.makeText(getContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                        editStepGoal.clearFocus();
                    }
                }
                return true;
            }
        });
        editWeightGoal.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    String text =  editWeightGoal.getText().toString();
                    if(text.length() == 0) {
                        Toast.makeText(getContext(), "숫자를 입력해주세요. 예)56, 68", Toast.LENGTH_SHORT).show();
                        editWeightGoal.clearFocus();
                        return true;
                    }
                    for(int i = 0; i < text.length(); i++)
                    {
                        if(!Character.isDigit(text.charAt(i)))
                        {
                            // 숫자를 입력한 것이 아닐경우
                            Toast.makeText(getContext(), "숫자를 입력해주세요. 예)56, 68", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                    if(Integer.parseInt(text) < 0 || Integer.parseInt(text) > 200)
                    {
                        Toast.makeText(getContext(), "0 ~ 200 사이 값을 입력해주세요. 예)56, 68", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    else
                    {
                        editWeightGoal.setText("      목표 체중 : " + text + " kg");
                        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userWeightGoal");
                        dbReference.setValue(Integer.parseInt(text));
                        Toast.makeText(getContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                        editWeightGoal.clearFocus();
                    }
                }
                return true;
            }
        });
        getStepWeightGoalFromDB();




        return root;
    }

    private void getStepWeightGoalFromDB()
    {

        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid());
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    if(data.getKey().equals("userStepGoal"))
                    {
                        editStepGoal.setText("      목표 걸음 수 : " + data.getValue().toString()+ " 걸음");
                    }
                    else if(data.getKey().equals("userWeightGoal"))
                    {
                        editWeightGoal.setText("      목표 체중 : " + data.getValue().toString() + " kg");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }











}
