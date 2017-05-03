package com.yy.demo.Yup.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{
    private final List<Fragment> slidingFragments = new ArrayList<>();
    private final List<String> slidingFragmentTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(Fragment fragment, String title){
        slidingFragments.add(fragment);
        slidingFragmentTitles.add(title);
    }
    @Override
    public Fragment getItem(int position) {
        return slidingFragments.get(position);
    }

    @Override
    public int getCount() {
        return slidingFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return slidingFragmentTitles.get(position);
    }
}
