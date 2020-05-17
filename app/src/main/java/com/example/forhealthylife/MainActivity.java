package com.example.forhealthylife;

import android.os.Bundle;
import android.view.View;

import com.example.forhealthylife.ui.eating.EatingFragment;
import com.example.forhealthylife.ui.eating.RiceFragment;
import com.example.forhealthylife.ui.home.HomeFragment;
import com.example.forhealthylife.ui.running.RunningFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements EatingFragment.OnListSelectedListener, HomeFragment.OnBtnClikListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bottom_menu_home, R.id.bottom_menu_schedule, R.id.button_menu_community)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        getSupportActionBar().hide();



    }

    public void onListSelected(int positon) {
        RiceFragment riceF = new RiceFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_e, riceF);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onBtnClik(View v) {
        Fragment menu = new EatingFragment();
        FragmentTransaction menuFt = getSupportFragmentManager().beginTransaction();
        menuFt.replace(R.id.fragment_home, menu);
        menuFt.addToBackStack(null);
        menuFt.commit();
    }

    /*
    public interface onKeyBackPressedListener { void onBackKey(); }
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }
    //EndToast endToast = new EndToast(this);
    @Override public void onBackPressed() {
        if (mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBackKey();
        } else {
            if(getSupportFragmentManager().getBackStackEntryCount()==0){
                //endToast.showEndToast("종료하려면 한번 더 누르세요.");
            }
            else{ super.onBackPressed();
            }
        }
    }
    */




}
