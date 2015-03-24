package com.socialmap.yy.travelbox.settings;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.fragment.Setting31Fragment;
import com.socialmap.yy.travelbox.fragment.Setting32Fragment;


public class Setting3Activity extends PreferenceActivity {
    private static final int COLOR_SELECTED = 0xff0b984c;
    private static final int COLOR_UNSELECTED = 0xff000000;
    private static final int TEXT_SIZE_SELECTED = 18;
    private static final int TEXT_SIZE_UNSELECTED = 16;
    private ImageView mTabCursor;
    private ViewPager mViewPager;
    private TextView mTabGeneral;
    private TextView mTabDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (this.getClass().equals(Setting3Activity.class))
            setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        if (this.getClass().equals(Setting3Activity.class)) {
            setContentView(R.layout.activity_setting3);
            mTabCursor = (ImageView) findViewById(R.id.tab_cursor);
            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));
            viewPager.setOnPageChangeListener(mPageChangeListener);
            mViewPager = viewPager;

            mTabGeneral = (TextView) findViewById(R.id.tab_general);
            mTabDisplay = (TextView) findViewById(R.id.tab_display);
            mTabGeneral.setOnClickListener(mTabOnClickListener);
            mTabDisplay.setOnClickListener(mTabOnClickListener);
            mTabGeneral.setTextColor(COLOR_SELECTED);
            mTabGeneral.setTextSize(TEXT_SIZE_SELECTED);
        }
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                mTabCursor.setX(mViewPager.getCurrentItem()
                        * mTabCursor.getWidth());
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (positionOffset == 0.0f)
                return;
            mTabCursor.setX(positionOffset * mTabCursor.getWidth());
        }

        @Override
        public void onPageSelected(int position) {
            if (0 == position) {
                mTabGeneral.setTextColor(COLOR_SELECTED);
                mTabGeneral.setTextSize(TEXT_SIZE_SELECTED);
                mTabDisplay.setTextColor(COLOR_UNSELECTED);
                mTabDisplay.setTextSize(TEXT_SIZE_UNSELECTED);
            } else if (1 == position) {
                mTabGeneral.setTextColor(COLOR_UNSELECTED);
                mTabGeneral.setTextSize(TEXT_SIZE_UNSELECTED);
                mTabDisplay.setTextColor(COLOR_SELECTED);
                mTabDisplay.setTextSize(TEXT_SIZE_SELECTED);
            }
        }

    };

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Setting31Fragment();

                case 1:
                    return new Setting32Fragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tab_general) {
                mViewPager.setCurrentItem(0);
            } else if (v.getId() == R.id.tab_display) {
                mViewPager.setCurrentItem(1);
            }
        }
    };

}