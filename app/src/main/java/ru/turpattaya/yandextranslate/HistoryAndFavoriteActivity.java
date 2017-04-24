package ru.turpattaya.yandextranslate;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;
import java.util.List;

import ru.turpattaya.yandextranslate.fragments.FavoriteFragment;
import ru.turpattaya.yandextranslate.fragments.HistoryFragment;


public class HistoryAndFavoriteActivity extends AppCompatActivity implements HistoryFragment.HistoryFragmentAddFavoriteHost{

    HistoryFragment historyFragment;
    FavoriteFragment favoriteFragment;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ImageView footerTranslateImage, footerFavoriteImage, footerSettingsImage;
    boolean isFavorite = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_history_and_favorite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        {
            ActionBar bar = getSupportActionBar();
            if (bar != null) {
                bar.setDisplayHomeAsUpEnabled(false);
                bar.setDisplayShowTitleEnabled(false);
                bar.setHomeButtonEnabled(false);
            }


        }

        footerTranslateImage = (ImageView) findViewById(R.id.footer_image_translate);
        footerFavoriteImage = (ImageView) findViewById(R.id.footer_image_favorite);
        footerSettingsImage = (ImageView) findViewById(R.id.footer_image_settings);


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));

            footerTranslateImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistoryAndFavoriteActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            footerFavoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistoryAndFavoriteActivity.this, HistoryAndFavoriteActivity.class);
                    startActivity(intent);
                }
            });
            footerSettingsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistoryAndFavoriteActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            });
        }

        tabLayout = (TabLayout) findViewById (R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        setupViewPager(viewPager);// будет добавлять объекты в адаптер
        tabLayout.setupWithViewPager(viewPager);


    }

    ViewPager.OnPageChangeListener myOnPageChangeListener =
            new ViewPager.OnPageChangeListener(){

                @Override
                public void onPageScrollStateChanged(int state) {

                }

                @Override
                public void onPageScrolled(int position,
                                           float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //This method will be invoked when a new page becomes selected.

                }};



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HistoryFragment(), "История");
        adapter.addFragment(new FavoriteFragment(), "Избранное");
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(myOnPageChangeListener);
    }

    @Override
    public void addToFavorite(int historyId, String historyTextIn, String historyTextOut, String historyTranslateDirection, int imageFavorite) {

    }



     private static class ViewPagerAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener {

        TabHost mTabHost;
        ViewPager mViewPager;

         final List<Fragment> fragmentList = new ArrayList<>(); //контейнер для фрагментов
        private  final List<String> titleList = new ArrayList<>(); // контейнер для заголовков Tabs

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }



        void addFragment(Fragment fragment, String title) {// метод, который в этот адаптер добавит fragment and title
            fragmentList.add(fragment);
            titleList.add(title);
        }

         void refreshPage(int position) {
            Fragment fragment = getItem(position);

            switch (position) {
                case 0:
                    ((HistoryFragment) fragment).refreshView();
                    break;
                case 1:
                    ((FavoriteFragment) fragment).refreshView();
                    break;
            }
        }


        @Override
        public void onTabChanged(String tabId) {
            int postion = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(postion, true);
        }
    }
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history_and_favorite, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_delete:
//TODO дописать после добавления бд
                break;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
