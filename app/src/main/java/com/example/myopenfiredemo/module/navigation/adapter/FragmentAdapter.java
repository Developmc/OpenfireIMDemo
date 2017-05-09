package com.example.myopenfiredemo.module.navigation.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.example.myopenfiredemo.base.fragment.LazyFragment;

import java.util.List;

/**viewPager fragment adapter
 * Created by clement on 16/12/14.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    
    private List<LazyFragment> fragments ;
    public FragmentAdapter(FragmentManager fm, List<LazyFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments==null||fragments.isEmpty()){
            return null ;
        }
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
