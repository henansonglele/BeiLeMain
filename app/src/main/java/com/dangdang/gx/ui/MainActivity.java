package com.dangdang.gx.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.flutterbase.DDBaseFlutterFragment;
import com.dangdang.gx.ui.home.HomeFragment;
import com.dangdang.gx.ui.html.StoreNormalHtmlFragment;
import com.dangdang.gx.ui.view.CustomBottomLayout;
import java.util.ArrayList;

import static com.dangdang.gx.ui.flutterbase.DDFlutterConst.PAGE_DD_SHELF_MAIN;

public class MainActivity extends AppCompatActivity implements CustomBottomLayout.BottomClickListener {
    ViewPager mViewPager;
    CustomBottomLayout customBottomLayout;
    int currentItem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    };
    void  initView(){
        customBottomLayout = (CustomBottomLayout)findViewById(R.id.bottom_layout);
        customBottomLayout.setListener(this);
        customBottomLayout.setCurrent(currentItem);
        initViewPager();
    }

    void  initViewPager(){
        mViewPager=(ViewPager) findViewById(R.id.mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                customBottomLayout.setCurrent(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String temp = "http://e.dangdang.com/h5/dzssy.html?&deviceType=Android";
        final ArrayList<Fragment> fgLists=new ArrayList<>(5);

        fgLists.add(new HomeFragment());
        fgLists.add( StoreNormalHtmlFragment.getInstance(temp));
        fgLists.add(new DDBaseFlutterFragment(PAGE_DD_SHELF_MAIN,null));
        fgLists.add( new HomeFragment());
        fgLists.add(new HomeFragment());
        FragmentPagerAdapter mPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }

            @Override
            public int getCount() {
                return fgLists.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(currentItem);

    }

    @Override
    public void onItemClick(int i) {
        mViewPager.setCurrentItem(i);
    }
}

