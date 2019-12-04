package com.example.droidbarv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {



    private final int DURACION_SPLASH = 1000; // 1 segundos

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tenemos una plantilla llamada splash.xml donde mostraremos la información que queramos (logotipo, etc.)
        setContentView(R.layout.activity_splash);

        /*Drawable originalDrawable = getResources().getDrawable(R.drawable.ic_android_splash);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());

        ImageView imageView = findViewById(R.id.ivSplash);
        imageView.setImageDrawable(roundedDrawable);*/



        new Handler().postDelayed(new Runnable(){
            public void run(){
                // Cuando pasen los x segundos de la constante, pasamos a la actividad principal de la aplicación
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            };
        }, DURACION_SPLASH);
    }
}


