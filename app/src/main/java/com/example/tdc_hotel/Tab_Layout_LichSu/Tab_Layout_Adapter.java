package com.example.tdc_hotel.Tab_Layout_LichSu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Tab_Layout_Adapter extends FragmentStateAdapter {
    public Tab_Layout_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment_Dadat();
            case 1:
                return new Fragment_Danghd();
            case 2:
                return new Fragment_HT();
            case 3:
                return new Fragment_Dahuy();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
