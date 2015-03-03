package com.example.sea_battle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.sea_battle.fragments.ConnectionFragment;
import com.example.sea_battle.fragments.RedactorFragment;
import com.example.sea_battle.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nixy on 30.01.2015.
 */
public class SimpleFragmentAdapter extends FragmentPagerAdapter {

    List<android.support.v4.app.Fragment> fragments = new ArrayList<Fragment>();

    public static final int SETTINGS_FRAGMENT = 0;
    public static final int CONNECTION_FRAGMENT = 1;
    public static final int REDACTOR_FRAGMENT = 2;

    public SimpleFragmentAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        fragments.add(new SettingsFragment());
        fragments.add(new ConnectionFragment());
        fragments.add(new RedactorFragment());

    }

    @Override
    public android.support.v4.app.Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }





}
