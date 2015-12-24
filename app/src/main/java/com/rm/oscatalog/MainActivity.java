package com.rm.oscatalog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rm.colored_pager.ColoredPagerActivity;

public class MainActivity extends ColoredPagerActivity {

    @Override
    public void onCreatePager(@Nullable Bundle savedInstanceState) {
        int firstColor = Color.parseColor("#0097A7");
        int secondColor = Color.parseColor("#FFA000");
        int thirdColor = Color.parseColor("#FF5722");
        int fourthColor = Color.parseColor("#673AB7");

        addSlide(new CustomSlide(), firstColor);
        addSlide(new CustomSlide(), secondColor);
        addSlide(new CustomSlide(), thirdColor);
        addSlide(new CustomSlide(), fourthColor);
    }

    public static class CustomSlide extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_custom_slide, container, false);
        }
    }

}
