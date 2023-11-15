package com.example.tdc_hotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.tdc_hotel.Fragment_Menu.Menu_Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main_Activity extends AppCompatActivity {

ViewPager2 vp_bottomNavigation;
BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setControl();
        Initialization();

    }

    private void Initialization() {
        vp_bottomNavigation.setUserInputEnabled(false);
        Menu_Adapter menu_adapter = new Menu_Adapter(this);
        vp_bottomNavigation.setAdapter(menu_adapter);
        vp_bottomNavigation.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigation.getMenu().findItem(R.id.menu_timkiem).setChecked(true);
                        break;
                    case 1:
                        bottomNavigation.getMenu().findItem(R.id.menu_daluu).setChecked(true);
                        break;
                    case 2:
                        bottomNavigation.getMenu().findItem(R.id.menu_datcho).setChecked(true);
                        break;
                    case 3:
                        bottomNavigation.getMenu().findItem(R.id.menu_taikhoan).setChecked(true);
                        break;
                }
            }
        });
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_timkiem) {
                vp_bottomNavigation.setCurrentItem(0);
                overridePendingTransition(0, 0);
            } else if (item.getItemId() == R.id.menu_daluu) {
                vp_bottomNavigation.setCurrentItem(1);
                overridePendingTransition(0, 0);
            } else if (item.getItemId() == R.id.menu_datcho) {
                vp_bottomNavigation.setCurrentItem(2);
                overridePendingTransition(0, 0);
            } else if (item.getItemId() == R.id.menu_taikhoan) {
                vp_bottomNavigation.setCurrentItem(3);

                overridePendingTransition(0, 0);
            }
            return true;
        });

    }

    private void setControl() {
        vp_bottomNavigation = findViewById(R.id.vp_bottomNavigation);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }
}