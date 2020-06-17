package com.team_comfortable.forhealthylife.ui.eating;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.team_comfortable.forhealthylife.R;

import java.util.HashMap;
import java.util.Map;


public class EatingFragment extends Fragment
{


    private EatingViewModel eatingViewModel;
    private TextView kcalView;
    private int kcalSum, ver;

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


    public interface OnListSelectedListener
    {
        public void onListSelected(int position);
    }
    OnListSelectedListener mListSelListener;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragment_eating, container, false);
        ListView listview = root.findViewById(R.id.eat_list);
        kcalView = root.findViewById(R.id.calculKcal);
        Button btn_kcalreset = root.findViewById(R.id.btn_kcalReset);
        btn_kcalreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetKcal();
            }
        });
        getKcal();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Data.eats);
        listview.setAdapter(adapter);
        mListSelListener = (MainActivity) getActivity();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListSelListener.onListSelected(position);
            }
        });
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {

        super.onActivityCreated(savedInstanceState);
        eatingViewModel =  ViewModelProviders.of(getActivity()).get(EatingViewModel.class);

        eatingViewModel.getInteger().observe(this, new Observer<Integer>()
        {
            @Override
            public void onChanged(Integer integer)
            {
                setKcal();
            }
        });
    }

    public void setKcal(){
        initFirebase();
        DatabaseReference kcalInDB = mReference.child("UserList").child(mUser.getUid());
        kcalInDB.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey() + "";
                    if(key.equals("userKcal")){
                        kcalSum = Integer.parseInt(data.getValue().toString());
                        kcalView.setText(String.valueOf(kcalSum));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getKcal(){
        initFirebase();
        DatabaseReference kcalInDB = mReference.child("UserList").child(mUser.getUid());
        kcalInDB.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey() + "";
                    if(key.equals("userKcal")){
                        kcalSum = Integer.parseInt(data.getValue().toString());
                        kcalView.setText(String.valueOf(kcalSum));
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void resetKcal(){
        initFirebase();
        DatabaseReference kcalInDB = mReference.child("UserList").child(mUser.getUid()).child("userKcal");
        kcalInDB.setValue("0");
        setKcal();
    }


}
