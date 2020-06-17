package com.team_comfortable.forhealthylife.ui.exercise;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.team_comfortable.forhealthylife.MainActivity;
import com.team_comfortable.forhealthylife.R;
import com.team_comfortable.forhealthylife.ui.eating.EatingFragment;
import com.team_comfortable.forhealthylife.ui.eating.EatingViewModel;
import com.team_comfortable.forhealthylife.ui.exercise.DataExercise;
import com.team_comfortable.forhealthylife.ui.exercise.ExerciseViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExerciseFragment extends Fragment {

    private ExerciseViewModel exerciseViewModel;
    private int pos;
    AdapterViewFlipper v_fllipper;
    List<Integer> imageId = new ArrayList<Integer>();
    private TextView TitleText,LevelText,FocusText,UsingText,EquipmentText,Level,Focus,Using,Equipment;

    EatingFragment.OnListSelectedListener mListSelListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exerciseViewModel =
                ViewModelProviders.of(this).get(ExerciseViewModel.class);

        View root = inflater.inflate(R.layout.fragment_exercise, container, false);
        ListView listview = root.findViewById(R.id.exercise_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, DataExercise.exercise);
        listview.setAdapter(adapter);
        TitleText = root.findViewById(R.id.exeTitleText);
        LevelText = root.findViewById(R.id.exeLevelText);
        FocusText = root.findViewById(R.id.exeFocusText);
        UsingText = root.findViewById(R.id.exeUsingText);
        Level = root.findViewById(R.id.levelTitle);
        Focus = root.findViewById(R.id.focusTitle);
        Using = root.findViewById(R.id.usingTitle);
        Equipment = root.findViewById(R.id.equipmentTitle);





        EquipmentText = root.findViewById(R.id.exeEquipmentText);
        v_fllipper = (AdapterViewFlipper) root.findViewById(R.id.exerciseViewFlipper);

        mListSelListener = (MainActivity) getActivity();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListSelListener.onListSelected(position+5);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        exerciseViewModel = ViewModelProviders.of(getActivity()).get(ExerciseViewModel.class);
        exerciseViewModel.setInteger(15);
        exerciseViewModel.getInteger().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                pos = integer;
                onImage(pos);

           }
        });

    }
    public void setTextInfo(int position){
        TitleText.setText(DataExercise.exeTitle[position]);
        Level.setVisibility(View.VISIBLE);
        LevelText.setText(DataExercise.exeLevel[position]);
        Focus.setVisibility(View.VISIBLE);
        FocusText.setText(DataExercise.exeFocus[position]);
        Using.setVisibility(View.VISIBLE);
        UsingText.setText(DataExercise.exeUsing[position]);
        Equipment.setVisibility(View.VISIBLE);
        EquipmentText.setText(DataExercise.exeEquipment[position]);
    }

    public void onImage(Integer position){
        imageId.clear();
        switch(position){
            case 0:
                for(int i =0; i < DataExercise.bowImage.length; i++){
                    imageId.add(DataExercise.bowImage[i]);
                    setTextInfo(position);
                }
                break;
            case 1:
                for(int i =0; i < DataExercise.childeImage.length; i++){
                    imageId.add(DataExercise.childeImage[i]);
                    setTextInfo(position);
                }
                break;
            case 2:
                for(int i =0; i < DataExercise.twistImage.length; i++){
                    imageId.add(DataExercise.twistImage[i]);
                    setTextInfo(position);
                }
                break;
            case 3:
                for(int i =0; i < DataExercise.benchImage.length; i++){
                    imageId.add(DataExercise.benchImage[i]);
                    setTextInfo(position);
                }
                break;
            case 4:
                for(int i =0; i < DataExercise.dumbbellImage.length; i++){
                    imageId.add(DataExercise.dumbbellImage[i]);
                    setTextInfo(position);
                }
                break;
            case 5:
                for(int i =0; i < DataExercise.dipsImage.length; i++){
                    imageId.add(DataExercise.dipsImage[i]);
                    setTextInfo(position);
                }
                break;
            case 6:
                for(int i =0; i < DataExercise.pullupImage.length; i++){
                    imageId.add(DataExercise.pullupImage[i]);
                    setTextInfo(position);
                }
                break;
            case 7:
                for(int i =0; i < DataExercise.latImage.length; i++){
                    imageId.add(DataExercise.latImage[i]);
                    setTextInfo(position);
                }
                break;
            case 8:
                for(int i =0; i < DataExercise.barbellrowImage.length; i++){
                    imageId.add(DataExercise.barbellrowImage[i]);
                    setTextInfo(position);
                }
                break;
            case 9:
                for(int i =0; i < DataExercise.situpImage.length; i++){
                    imageId.add(DataExercise.situpImage[i]);
                    setTextInfo(position);
                }
                break;
            case 10:
                for(int i =0; i < DataExercise.kneeImage.length; i++){
                    imageId.add(DataExercise.kneeImage[i]);
                    setTextInfo(position);
                }
                break;
            case 11:
                for(int i =0; i < DataExercise.climberImage.length; i++){
                    imageId.add(DataExercise.climberImage[i]);
                    setTextInfo(position);
                }
                break;
            case 12:
                for(int i =0; i < DataExercise.shoulderImage.length; i++){
                    imageId.add(DataExercise.shoulderImage[i]);
                    setTextInfo(position);
                }
                break;
            case 13:
                for(int i =0; i < DataExercise.lateralImage.length; i++){
                    imageId.add(DataExercise.lateralImage[i]);
                    setTextInfo(position);
                }
                break;
            case 14:
                for(int i =0; i < DataExercise.vraiseImage.length; i++){
                    imageId.add(DataExercise.vraiseImage[i]);
                    setTextInfo(position);
                }
                break;
            default:
                return;
        }

        v_fllipper.setAdapter(new galleryAdapter(getActivity()));

        v_fllipper.setFlipInterval(1000);

        v_fllipper.startFlipping();

    }




    public class galleryAdapter extends BaseAdapter {

        private final Context mContext;
        LayoutInflater inflater;

        public galleryAdapter(Context c) {
            mContext = c;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return imageId.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.exercise_item, parent, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.exerciseView);
            imageView.setImageResource(imageId.get(position));
            return convertView;
        }
    }








}
