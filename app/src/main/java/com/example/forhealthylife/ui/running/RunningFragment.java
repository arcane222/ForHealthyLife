package com.example.forhealthylife.ui.running;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.forhealthylife.MainActivity;
import com.example.forhealthylife.R;


public class RunningFragment extends Fragment implements SensorEventListener
{

    private RunningViewModel runningViewModel;

    private Button mReset;
    private TextView mStepNum;
    private SensorManager sensorManager;
    private Sensor stepCountSensor;

    private int mSteps = 0;
    private int mCounterSteps = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        runningViewModel = ViewModelProviders.of(this).get(RunningViewModel.class);
        View root = inflater.inflate(R.layout.fragment_running, container, false);

        // Set Step Sensor
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCountSensor == null)
        {
            Toast.makeText(getContext(),"No Step Detect Sensor",Toast.LENGTH_SHORT).show();
        }

        mReset = (Button)root.findViewById(R.id.resetStepBtn);
        mStepNum = root.findViewById(R.id.stepNumView);

        // Initialization Button
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSteps = 0;
                mCounterSteps = 0;
                mStepNum.setText("Step Count : " + Integer.toString(mSteps));
            }
        });

        return root;
    }

    public void onStart() {
        super.onStart();
        if(stepCountSensor !=null){
            //센서의 속도 설정
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void onStop(){
        super.onStop();
        if(sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER)
        {

            // step count Sensor 는 앱이 꺼지더라도 초기화 되지않는다. 그러므로 우리는 초기값을 가지고 있어야한다.
            if (mCounterSteps < 1) {
                // initial value
                mCounterSteps = (int) event.values[0];
            }
            //리셋 안된 값 + 현재값 - 리셋 안된 값
            mSteps = (int) event.values[0] - mCounterSteps;
            mStepNum.setText("Step Count : " + Integer.toString(mSteps));
            Log.i("log: ", "New step detected by STEP_COUNTER sensor. Total step count: " + mSteps );
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
