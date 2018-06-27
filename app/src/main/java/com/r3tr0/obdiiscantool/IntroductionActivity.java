package com.r3tr0.obdiiscantool;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import adapters.IntroductionPagerAdapter;
import fragments.IntroductionFragment;

public class IntroductionActivity extends AppCompatActivity {

    IntroductionPagerAdapter adapter;
    int selectedTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        initPager();

    }

    void initPager() {
        ViewPager pager = findViewById(R.id.introductionPager);
        final TabLayout tabLayout = findViewById(R.id.introductionTabs);

        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new IntroductionFragment(
                Color.parseColor("#378f76")
                , R.drawable.general_info
                , "Our app supports the ability to check some general information about your vehicle that helps you on the road.\nThe data examples presented in this prototype are Speed and fuel level.\nFututre values can be added like RPM, Oxygen level, Temperature, and much more! "));

        fragments.add(new IntroductionFragment(
                Color.parseColor("#1a3d49")
                , R.drawable.diagnosis
                , "The app also supports the diagnosis functions.\nThis means that the application can read diagnosis errors from your vehicle and update your list on the fly!\nThis helps you to be aware of your car's problems right away!"
        ));

        fragments.add(new IntroductionFragment(
                Color.parseColor("#185c54")
                , R.drawable.trip
                , "The last feature in this prototype is the ability to record your trips using your smart phone's built-in GPS.\n also identifying the shortest way to your destination!, then you can review your previously finished trips later in the application if you want to."));

        adapter = new IntroductionPagerAdapter(getSupportFragmentManager(), fragments);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(selectedTab).setIcon(R.drawable.unselected);
                tabLayout.getTabAt(position).setIcon(R.drawable.selected);
                selectedTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i == 0)
                tabLayout.getTabAt(i).setIcon(R.drawable.selected);
            else
                tabLayout.getTabAt(i).setIcon(R.drawable.unselected);

        }
    }
}
