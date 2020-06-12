package com.team_comfortable.forhealthylife.ui.calendar.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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


public class EnterFragment extends Fragment {

    // TODO: Rename and change types and number of parameters
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private EditText EditSch, EditDate;
    private boolean check;
    public String sch,date;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = (View)inflater.inflate(R.layout.fragment_enter, container, false);
        Button EnterBtn = (Button) root.findViewById(R.id.btn_enter);
        EditSch = (EditText) root.findViewById(R.id.edit_sch);
        EditDate = (EditText) root.findViewById(R.id.edit_date);



        EnterBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                sch = EditSch.getText().toString();
                date = EditDate.getText().toString();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();
                mDatabase = FirebaseDatabase.getInstance();
                mReference = mDatabase.getReference();
                DatabaseReference userListDB = mReference.child("UserList").child(user.getUid()).child("userSchedule");
                Map<String, Object> map = new HashMap<String, Object>();
                if(checkSch(user)){
                    map.put(date, sch);
                    userListDB.updateChildren(map);
                }
                else{
                    map.put(date, check);
                    userListDB.updateChildren(map);
                }
            }
        });

        return root;
    }

   /* private void registInDB(final FirebaseUser user)
    {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        final DatabaseReference userListDB = mReference.child("UserList");
        userListDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                boolean isExist = false;
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String value = data.getValue().toString();
                    if(value.equals(user.getUid()))
                    {
                        isExist = true;
                        break;
                    }
                }*/
    public boolean checkSch(final FirebaseUser user){
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        final DatabaseReference userListDB = mReference.child("UserList").child(user.getUid()).child("userSchedule");
        userListDB.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    String value = data.getKey();
                    if(value.equals(date))
                    {
                        check = false;
                        break;
                    }
                    else{
                        check = true;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return check;
    }
}
