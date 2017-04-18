package ru.turpattaya.yandextranslate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SettingsActivity extends AppCompatActivity {

    private ImageView footerTranslateImage, footerFavoriteImage, footerSettingsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        footerTranslateImage = (ImageView) findViewById(R.id.footer_image_translate);
        footerFavoriteImage = (ImageView) findViewById(R.id.footer_image_favorite);
        footerSettingsImage = (ImageView) findViewById(R.id.footer_image_settings);

        footerTranslateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        footerFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HistoryAndFavoriteActivity.class);
                startActivity(intent);
            }
        });
        footerSettingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
