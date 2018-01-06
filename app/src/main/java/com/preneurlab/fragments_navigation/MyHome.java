package com.preneurlab.fragments_navigation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.preneurlab.fragments_tab.MyFragmentPagerAdapter;
import com.preneurlab.comjagatmagazin.R;
import com.preneurlab.fragments_tab.Fragment2008;
import com.preneurlab.fragments_tab.Fragment2009;
import com.preneurlab.fragments_tab.Fragment2010;
import com.preneurlab.fragments_tab.Fragment2011;
import com.preneurlab.fragments_tab.Fragment2012;
import com.preneurlab.fragments_tab.Fragment2013;
import com.preneurlab.fragments_tab.Fragment2014;
import com.preneurlab.fragments_tab.Fragment2015;
import com.preneurlab.fragments_tab.Fragment2016;
import com.preneurlab.fragments_tab.begum;

import java.util.List;
import java.util.Vector;

public class MyHome extends Fragment implements OnTabChangeListener,
        OnPageChangeListener {

    int i = 0;
    View v;
    private TabHost tabHost;
    private ViewPager viewPager;
    private MyFragmentPagerAdapter myViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.tabs_viewpager_layout, container, false);
        i++;
        // init tabhost
        this.initializeTabHost(savedInstanceState);
        // init ViewPager
        this.initializeViewPager();
        return v;
    }

    private void initializeViewPager() {
        List<Fragment> fragments = new Vector<Fragment>();

//        fragments.add(new begum());
        fragments.add(new Fragment2016());
        fragments.add(new Fragment2015());
        fragments.add(new Fragment2014());
        fragments.add(new Fragment2013());
        fragments.add(new Fragment2012());
        fragments.add(new Fragment2011());
        fragments.add(new Fragment2010());
        fragments.add(new Fragment2009());
        fragments.add(new Fragment2008());

        this.myViewPagerAdapter = new MyFragmentPagerAdapter(
                getChildFragmentManager(), fragments);
        this.viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        this.viewPager.setAdapter(this.myViewPagerAdapter);
        this.viewPager.setOnPageChangeListener(this);

    }

    private void initializeTabHost(Bundle args) {

        tabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        tabHost.setup();

        String[] tabNames = {"২০১৬", "২০১৫", "২০১৪", "২০১৩", "২০১২", "২০১১", "২০১০", "২০০৯", "২০০৮"};

        //for (int i = 2016; i >= 2010; i--) {
        for (int i = 0; i < tabNames.length; i++) {

            TabHost.TabSpec tabSpec;
            //tabSpec = tabHost.newTabSpec("Tab " + i);
            //tabSpec.setIndicator("ম্যাগাজিন " + i);
            tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(getActivity()));
            tabHost.addTab(tabSpec);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(16);
            tv.setTextColor(Color.WHITE);
            tv.setTypeface(null, Typeface.NORMAL);
        }
        tabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        int pos = this.tabHost.getCurrentTab();
        this.viewPager.setCurrentItem(pos);

        HorizontalScrollView hScrollView = (HorizontalScrollView) v
                .findViewById(R.id.hScrollView);
        View tabView = tabHost.getCurrentTabView();
        int scrollPos = tabView.getLeft()
                - (hScrollView.getWidth() - tabView.getWidth()) / 2;
        hScrollView.smoothScrollTo(scrollPos, 0);
        //tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#405159"));

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        this.tabHost.setCurrentTab(position);
    }

    // fake content for tabhost
    class FakeContent implements TabContentFactory {
        private final Context mContext;

        public FakeContent(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }
    }

}
