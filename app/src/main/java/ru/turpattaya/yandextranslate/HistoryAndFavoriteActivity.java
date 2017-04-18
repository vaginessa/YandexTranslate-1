package ru.turpattaya.yandextranslate;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class HistoryAndFavoriteActivity extends AppCompatActivity implements HistoryFragment.HistoryFragmentHost {


    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ImageView footerTranslateImage, footerFavoriteImage, footerSettingsImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_and_favorite);
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HistoryFragment(), "История");
        adapter.addFragment(new FavoriteFragment(), "Избранное");

        viewPager.setAdapter(adapter);

    }

    @Override
    public void historySelected(int historyId, String historyTextIn, String historyTextOut, String historyTranslateDirection) {

    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>(); //контейнер для фрагментов
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
    }
    @Override
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
    }
}
