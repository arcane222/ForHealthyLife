package com.example.forhealthylife.ui.running;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    TextView tvStepCount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        runningViewModel =
                ViewModelProviders.of(this).get(RunningViewModel.class);
        View root = inflater.inflate(R.layout.fragment_running, container, false);
        final TextView textView = root.findViewById(R.id.text_community);

        tvStepCount = (TextView) root.findViewById(R.id.tvStepCount);
        AppCompatActivity currentActivity = (MainActivity) getActivity();
        sensorManager = (SensorManager) currentActivity.getSystemService (Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(stepCountSensor == null)
        {
            Toast.makeText(getContext(), "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER)
        {
            tvStepCount.setText("Step Count : " + String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

}
