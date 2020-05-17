package com.example.forhealthylife;

import android.os.Bundle;
import android.view.View;

import com.example.forhealthylife.ui.eating.EatingFragment;
import com.example.forhealthylife.ui.eating.RiceFragment;
import com.example.forhealthylife.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements EatingFragment.OnListSelectedListener, HomeFragment.OnBtnClikListener
{
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initBottomNavMenu();
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
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void onListSelected(int position)
    {
        RiceFragment riceF = new RiceFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_e, riceF);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onBtnClik(View v)
    {
        Fragment menu = new EatingFragment();
        FragmentTransaction menuFt = getSupportFragmentManager().beginTransaction();
        menuFt.replace(R.id.fragment_home, menu);
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
