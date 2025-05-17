package kr.ac.dongyang.ailabproj1;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    ImageButton mainBtn, showRecomBtn, showRestBtn, showSettingBtn;

    ImageButton recomBackBtn, recomReBtn;
    TextView recomRslt;
    ConstraintLayout main, showRestMain, showSettingMain, recomRlstMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main); // 초기 화면 설정
        mainBtn = findViewById(R.id.recommendBtn);
        showRestBtn = findViewById(R.id.viewRest);
        showRecomBtn = findViewById(R.id.viewRecom);
        showSettingBtn = findViewById(R.id.viewSetting);
        main = findViewById(R.id.main);
        showRestMain = findViewById(R.id.seeRestmain);
        showSettingMain = findViewById(R.id.settingMain);
        recomRlstMain = findViewById(R.id.recom_rslt);


        //추천 결과 페이지 옵젝트
        recomBackBtn = findViewById(R.id.backButton);
        recomReBtn = findViewById(R.id.retryButton);
        recomRslt = findViewById(R.id.rsltText);


        main.setVisibility(View.VISIBLE);

        // 근처 식당 보기 네비게이션
        showRestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 레이아웃 변경
                // 버튼 id 재설정
                main.setVisibility(View.GONE);
                recomRlstMain.setVisibility(View.GONE);
                showSettingMain.setVisibility(View.GONE);

                showRestMain.setVisibility(View.VISIBLE);
                showRestBtn.setAlpha(1f);
                showRecomBtn.setAlpha(0.5f);
                showSettingBtn.setAlpha(0.5f);
                // RecyclerView 초기화
                RecyclerView recyclerView = findViewById(R.id.recyclerView1);

                // 데이터 준비 (아이콘 배열)
                int[] restaurantIcons = {R.drawable.bab, R.drawable.bab, R.drawable.bab}; // 여러 아이콘을 사용 가능

                // 어댑터 설정
                RestaurantAdapter adapter = new RestaurantAdapter(restaurantIcons);
                recyclerView.setAdapter(adapter);

                // LayoutManager 설정 (가로 스크롤을 위한 설정)
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

            }
        });
        // 식당 추천 네비게이션
        showRecomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRestMain.setVisibility(View.GONE);
                showSettingMain.setVisibility(View.GONE);
                recomRlstMain.setVisibility(View.GONE);
                main.setVisibility(View.VISIBLE);


                showRecomBtn.setAlpha(1f);
                showRestBtn.setAlpha(0.5f);
                showSettingBtn.setAlpha(0.5f);
            }
        });

        //추천 버튼 눌렀을시 동작
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다른 레이아웃 전환
                main.setVisibility(View.GONE);
                showRestMain.setVisibility(View.GONE);
                showSettingMain.setVisibility(View.GONE);
                recomRlstMain.setVisibility(View.VISIBLE);


            }
        });

        showSettingBtn.setOnClickListener(click -> {
            main.setVisibility(View.GONE);
            showRestMain.setVisibility(View.GONE);
            recomRlstMain.setVisibility(View.GONE);
            showSettingMain.setVisibility(View.VISIBLE);

        });

    }
}
