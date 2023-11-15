package com.example.tdc_hotel.Fragment_Menu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Menu_Adapter extends FragmentStateAdapter {


    public Menu_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment_TimKiem();
            case 1:
                return new Fragment_DaLuu();
            case 2:
                return new Fragment_DatCho();
            case 3:
                return new Fragment_TaiKhoan();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
