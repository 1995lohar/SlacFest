package eu.kudan.kudansamples;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//import eu.kudan.ar.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent;
                    intent = new Intent(SplashScreen.this, MainActivity.class);

                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
