package com.team_comfortable.forhealthylife.ui.running;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team_comfortable.forhealthylife.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class RunningFragment extends Fragment implements SensorEventListener
{
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private Button mReset, mSave;
    private TextView mStepCountTextView;
    private SensorManager sensorManager;
    private Sensor stepCountSensor;

    private int mSteps = 0;
    private int mCounterSteps = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_running, container, false);
        initFirebase();
        initData(root);
        initButton();
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
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCountSensor == null)
        {
            Toast.makeText(getContext(),"No Step Detect Sensor",Toast.LENGTH_SHORT).show();
        }
        mReset = (Button)root.findViewById(R.id.btn_resetStepCount);
        mSave = (Button)root.findViewById(R.id.btn_saveStepCount);
        mStepCountTextView = root.findViewById(R.id.tv_running_stepCount);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initButton()
    {
        mReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mSteps = 0;
                mCounterSteps = 0;
                mStepCountTextView.setText(Integer.toString(mSteps));
            }
        });
        mSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                checkSaving();
            }
        });
    }

    public String getCurrentDate()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String currentDate  = dateFormat.format(calendar.getTime());
        return currentDate;
    }

    public void checkSaving()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("걸음 수 저장");
        dialog.setMessage("현재 걸음 수를 저장 및 초기화 하시겠습니까?");
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setPositiveButton("예", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                setStepCountInDB(mSteps);
                Toast.makeText(getContext(), "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
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

    public void setStepCountInDB(final int count)
    {
        final DatabaseReference dbReference = mReference.child("UserList").child(mUser.getUid()).child("userStepCount");
        final String date = getCurrentDate();
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                int tmp = count;
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String key= data.getKey();
                    if(key.equals(date))
                    {
                        tmp = Integer.parseInt(data.getValue().toString()) + count;
                        break;
                    }
                }
                map.put(date, tmp);
                dbReference.updateChildren(map);
                mSteps = 0;
                mCounterSteps = 0;
                mStepCountTextView.setText(Integer.toString(mSteps));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void onStart()
    {
        super.onStart();
        if(stepCountSensor !=null)
        {
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_GAME);
        }
    }
    public void onStop()
    {
        super.onStop();
        if(sensorManager!=null) { sensorManager.unregisterListener(this); }
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER)
        {
            if (mCounterSteps < 1)
            {
                mCounterSteps = (int) event.values[0];
            }
            mSteps = (int) event.values[0] - mCounterSteps;
            mStepCountTextView.setText(Integer.toString(mSteps));
            Log.i("log: ", "New step detected by STEP_COUNTER sensor. Total step count: " + mSteps );
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
