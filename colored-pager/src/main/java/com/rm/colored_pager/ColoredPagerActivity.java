package com.rm.colored_pager;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class ColoredPagerActivity extends AppCompatActivity {

    private static final int DEFAULT_ACTIVE_DOT_COLOR = 0x46ffffff;
    private static final int DEFAULT_INACTIVE_DOT_COLOR = 0x1effffff;
    private static final String DOT_SYMBOL = "&#8226;";
    private static final float DOT_SIZE = 42F;

    private final ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();
    private final ArrayList<Integer> mColors = new ArrayList<>();
    private final List<Fragment> mFragmentList = new ArrayList<>();

    private ViewPager mPager;

    private LinearLayout mDotsLayout;
    private TextView[] mDots;

    private PagerAdapter mPagerAdapter;

    private int mActiveDotColor;
    private int mInactiveDotsColor;
    private int mNumberOfSlides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= 19) {
            //Set status bar to semi-transparent
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_tour);

        mPager = (ViewPager) findViewById(R.id.pager_slides);
        mDotsLayout = (LinearLayout) findViewById(R.id.pager_indicator);

        mActiveDotColor = DEFAULT_ACTIVE_DOT_COLOR;
        mInactiveDotsColor = DEFAULT_INACTIVE_DOT_COLOR;

        //Instantiate the PagerAdapter.
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mFragmentList);
        mPager.setAdapter(mPagerAdapter);

        onCreatePager(savedInstanceState);

        mNumberOfSlides = mFragmentList.size();

        if (mNumberOfSlides > 1) setViewPagerDots();

        setListeners();
    }

    public abstract void onCreatePager(@Nullable Bundle savedInstanceState);

    /**
     * Add a slide to the intro
     *
     * @param fragment Fragment of the slide to be added
     */
    public void addSlide(@NonNull Fragment fragment) {
        mFragmentList.add(fragment);
        addBackgroundColor(Color.TRANSPARENT);
        mPagerAdapter.notifyDataSetChanged();
    }

    /**
     * Add a slide to the intro
     *
     * @param fragment Fragment of the slide to be added
     * @param color    Background color of the fragment
     */
    public void addSlide(@NonNull Fragment fragment, @ColorInt int color) {
        mFragmentList.add(fragment);
        addBackgroundColor(color);
        mPagerAdapter.notifyDataSetChanged();
    }

    /**
     * Return slides
     *
     * @return Return slides
     */
    public List<Fragment> getSlides() {
        return mPagerAdapter.getFragments();
    }

    /**
     * Get which slide is currently active
     *
     * @return Returns the current active slide index
     */
    public int getCurrentSlide() {
        return mPager.getCurrentItem();
    }

    /**
     * Set the currently selected slide
     *
     * @param position Item index to select
     */
    public void setCurrentSlide(int position) {
        mPager.setCurrentItem(position, true);
    }

    /**
     * Set the color of the active dot indicator
     *
     * @param color Color value to set
     */
    public void setActiveDotColor(@ColorInt int color) {
        mActiveDotColor = color;
    }

    /**
     * Set the color of the inactive dot indicator
     *
     * @param color Color value to set
     */
    public void setInactiveDotsColor(@ColorInt int color) {
        mInactiveDotsColor = color;
    }

    /**
     * Show indicator mDots
     */
    public void showIndicatorDots() {
        mDotsLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Hide indicator mDots
     */
    public void hideIndicatorDots() {
        mDotsLayout.setVisibility(View.INVISIBLE);
    }

    protected void addBackgroundColor(@ColorInt int color) {
        mColors.add(color);
    }

    private void setListeners() {
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (mPagerAdapter.getCount() - 1) && position < (mColors.size() - 1)) {
                    int color = (Integer) mArgbEvaluator.evaluate(
                            positionOffset,
                            mColors.get(position),
                            mColors.get(position + 1)
                    );

                    mPager.setBackgroundColor(color);
                } else {
                    int color = mColors.get(mColors.size() - 1);
                    mPager.setBackgroundColor(color);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //Set mDots
                if (mNumberOfSlides > 1) {
                    //Set current inactive mDots color
                    for (int i = 0; i < mNumberOfSlides; i++) {
                        mDots[i].setTextColor(mInactiveDotsColor);
                    }

                    //Set current active dot color
                    mDots[position].setTextColor(mActiveDotColor);
                }
            }
        });
    }

    private void setViewPagerDots() {
        mDots = new TextView[mNumberOfSlides];

        //Set first inactive mDots color
        for (int i = 0; i < mNumberOfSlides; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml(DOT_SYMBOL));
            mDots[i].setTextSize(DOT_SIZE);
            mDots[i].setTextColor(mInactiveDotsColor);
            mDotsLayout.addView(mDots[i]);
        }

        //Set first active dot color
        mDots[0].setTextColor(mActiveDotColor);
    }
}
