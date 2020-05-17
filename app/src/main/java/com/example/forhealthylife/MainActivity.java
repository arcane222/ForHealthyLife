package com.example.forhealthylife;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.forhealthylife.ui.eating.DrinkFragment;
import com.example.forhealthylife.ui.eating.EatingFragment;
import com.example.forhealthylife.ui.eating.FastFoodFragment;
import com.example.forhealthylife.ui.eating.RiceFragment;
import com.example.forhealthylife.ui.eating.SideDishFragment;
import com.example.forhealthylife.ui.eating.SoupFragment;
import com.example.forhealthylife.ui.exercise.ExerciseFragment;
import com.example.forhealthylife.ui.home.HomeFragment;
import com.example.forhealthylife.ui.running.RunningFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements EatingFragment.OnListSelectedListener, HomeFragment.OnBtnClickListener
{
    private AppBarConfiguration mAppBarConfiguration;
    private String[] REQUEST_PERMISSIONS={ Manifest.permission.ACTIVITY_RECOGNITION };
    private static final int REQUEST_RECOGNITION  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initBottomNavMenu();
        checkRecogPermission();
    }

    public boolean checkRecogPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS,
                    REQUEST_RECOGNITION);
        }
        return true;
    }
    // 상단 툴바 초기설정 메소드
    public void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.side_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.side_menu_profile, R.id.side_menu_analysis, R.id.side_menu_notice,
                R.id.side_menu_settings, R.id.bottom_menu_home, R.id.bottom_menu_schedule,
                R.id.bottom_menu_community).setDrawerLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    // 하단 메뉴 설정 메소드
    public void initBottomNavMenu()
    {
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bottom_menu_home, R.id.bottom_menu_schedule, R.id.bottom_menu_community)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void onListSelected(int position)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position){
            case 0:
                RiceFragment riceFragment = new RiceFragment();
                transaction.replace(R.id.eat_list_layout, riceFragment);
                break;
            case 1:
                SoupFragment soupFragment = new SoupFragment();
                transaction.replace(R.id.eat_list_layout, soupFragment);
                break;
            case 2:
                SideDishFragment sidedishFragment = new SideDishFragment();
                transaction.replace(R.id.eat_list_layout, sidedishFragment);
                break;
           case 3:
                FastFoodFragment fastfoodFragment = new FastFoodFragment();
                transaction.replace(R.id.eat_list_layout, fastfoodFragment);
                break;
            case 4:
                DrinkFragment drinkFragment = new DrinkFragment();
                transaction.replace(R.id.eat_list_layout, drinkFragment);
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* Home Fragment Button Click Listener (Eating, Running, Exercise) */
    public void onBtnClick(View v)
    {
        Fragment eatingFragment = new EatingFragment();
        Fragment runningFragment = new RunningFragment();
        Fragment exerciseFragment = new ExerciseFragment();

        FragmentTransaction menuFt = getSupportFragmentManager().beginTransaction();
        switch(v.getId())
        {
            case R.id.btn_run:
                menuFt.replace(R.id.fragment_home, runningFragment);
                break;
            case R.id.btn_eat:
                menuFt.replace(R.id.fragment_home, eatingFragment);
                break;
            case R.id.btn_exe:
                menuFt.replace(R.id.fragment_home, exerciseFragment);
                break;
            default:
                break;
        }
        menuFt.addToBackStack(null);
        menuFt.commit();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
