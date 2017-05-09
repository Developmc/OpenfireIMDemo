package com.example.myopenfiredemo.module.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.fragment.BaseFragment;
import com.example.myopenfiredemo.base.fragment.LazyFragment;
import com.example.myopenfiredemo.custom.view.BottomNavigationViewEx;
import com.example.myopenfiredemo.module.chats.ChatListFragment;
import com.example.myopenfiredemo.module.navigation.adapter.FragmentAdapter;
import com.example.myopenfiredemo.module.setting.SettingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**NavigationFragment
 * Created by clement on 2017/4/18.
 */

public class NavigationFragment extends BaseFragment{

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationViewEx navigationView;

    private List<LazyFragment> fragments = new ArrayList<>();

    @Override
    public int onBindLayoutID() {
        return R.layout.fragment_navigation;
    }

    @Override
    public void initBehavior(View rootView) {
        initView();
    }

    private void initView(){
        initBottomView();
        initViewPager();
    }
    private void initBottomView(){
        String[] titles = new String[]{"聊天","设置"};
        int[] icons = new int[]{R.drawable.ic_menu_chat,R.drawable.ic_menu_setting};
        for(int i=0;i<navigationView.getMenu().size();i++){
            MenuItem menuItem = navigationView.getMenu().getItem(i);
            menuItem.setTitle(titles[i]);
            menuItem.setIcon(icons[i]);
        }
        navigationView.enableAnimation(false);
        navigationView.enableShiftingMode(false);
        navigationView.enableItemShiftingMode(false);
        //点击选择item
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = 0;
                switch (item.getItemId()) {
                    case R.id.item_chat:
                        position = 0;
                        break;
                    case R.id.item_setting:
                        position = 1;
                        break;
                }
                viewPager.setCurrentItem(position);
                return true;
            }
        });
    }

    private void initViewPager(){
        ChatListFragment chatListFragment = new ChatListFragment();
        SettingFragment settingFragment = new SettingFragment();
        fragments.add(chatListFragment);
        fragments.add(settingFragment);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getFragmentManager(),fragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigationView.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onHide2Show(Bundle bundle) {
        if(bundle!=null){
            //更新chatListFragment
            ChatListFragment chatListFragment = (ChatListFragment)fragments.get(0);
            //通知fragment刷新列表
            chatListFragment.need2Refresh();
        }

    }
}
