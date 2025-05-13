package kr.ac.dongyang.ailabproj1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Loading extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2500; // 3초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 3초 후 메인 액티비티로 전환
                Intent intent = new Intent(Loading.this, MainActivity.class);
                startActivity(intent);
                finish(); // 스플래시 액티비티 종료
            }
        }, SPLASH_DELAY);
    }
}