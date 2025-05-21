package kr.ac.dongyang.ailabproj1;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {

    ImageButton mainBtn, showRecomBtn, showRestBtn, showSettingBtn;

    ImageButton recomBackBtn, recomReBtn;
    TextView recomRslt, recom_text, main_text;
    ConstraintLayout main, showRestMain, showSettingMain, recomRlstMain, loading;

    ScrollView scrollMain, scrollRslt, scrollSetting;
    // 나이대 (CheckBox)
    private CheckBox[] ageCheckBoxes;
    private RadioGroup rgWho, rgCondition, rgWeather;
    private RadioButton[] whoRadioButtons, conditionRadioButtons, weatherRadioButtons;
    private CheckBox[] drinkCheckBoxes;

    private ImageButton [] categorys;
//    private boolean [] buttonSel = {false, false, false, false, false ,false, false, false};
    static String prompt;

    ArrayList<String> categoryList = new ArrayList<String>();
    ArrayList<String> ageList = new ArrayList<String>();
    ArrayList<String> whoList = new ArrayList<String>();
    ArrayList<String> conditionList = new ArrayList<String>();
    ArrayList<String> weatherList = new ArrayList<String>();
    ArrayList<String> drinkList = new ArrayList<String>();

    String [] texts ={
            "빵만 있다면 웬만한 슬픔은 견딜 수 있다. \n -Cervantes",
            "음식으로 못 고치는 병은 의사도 못 고친다. \n -Hippocrates",
            "요리사는 행복을 파는 사람이다 \n -Michel Bras",
            "음식에 대한 사랑처럼 진실된 사랑은 없다. \n -Gerorge Bernard Shaw" ,
            "배가 비어있으면 정신도 빌 수밖에 없다. \n -Honore de Balzac",
            "배고픈 자는 음식을 가리지 않는다 \n -맹자",
            "잘못된 음식이란 것은 없다 \n -Sean Stewart",
            "잘 먹는 것은 결코 하찮은 기술이 아니다 \n -Michel de Montaigne",
            "식욕 이상으로 진실한 신념은 없다 \n -John Cisna"
    };
    ImageView recomRlstImg;
    ArrayList <Integer> indexList;
    RestrauntDatas restrauntReturn = new RestrauntDatas();
    int retryCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        hideSystemUI();
        setContentView(R.layout.activity_main); // 초기 화면 설정
        mainBtn = findViewById(R.id.recommendBtn);
        showRestBtn = findViewById(R.id.viewRest);
        showRecomBtn = findViewById(R.id.viewRecom);
        showSettingBtn = findViewById(R.id.viewSetting);
        main = findViewById(R.id.main);
        showRestMain = findViewById(R.id.seeRestmain);
        showSettingMain = findViewById(R.id.settingMain);
        recomRlstMain = findViewById(R.id.recom_rslt);
//        recom_text = findViewById(R.id.recom_text);
        main_text = findViewById(R.id.Text);
        //추천 결과 페이지 옵젝트
        recomBackBtn = findViewById(R.id.backButton);
        recomReBtn = findViewById(R.id.retryButton);
        recomRslt = findViewById(R.id.rsltText);
        recomRlstImg = findViewById(R.id.rslt_img);
        loading = findViewById(R.id.loading);
        scrollMain = findViewById(R.id.scroll1);
        scrollRslt = findViewById(R.id.scroll2);
        scrollSetting = findViewById(R.id.scroll3);
        ageCheckBoxes = new CheckBox[]{
                findViewById(R.id.checkbox_infant),
                findViewById(R.id.checkbox_child),
                findViewById(R.id.checkbox_teen),
                findViewById(R.id.checkbox_young),
                findViewById(R.id.checkbox_middle)
        };

        rgWho = findViewById(R.id.radioGroup_who);
        whoRadioButtons = new RadioButton[]{
                findViewById(R.id.radio_alone),
                findViewById(R.id.radio_friend),
                findViewById(R.id.radio_company),
                findViewById(R.id.radio_family),
                findViewById(R.id.radio_lover)
        };

        rgCondition = findViewById(R.id.radioGroup_condition);
        conditionRadioButtons = new RadioButton[]{
                findViewById(R.id.radio_happy),
                findViewById(R.id.radio_sad),
                findViewById(R.id.radio_angry),
                findViewById(R.id.radio_depressed),
                findViewById(R.id.radio_tired)
        };

        rgWeather = findViewById(R.id.radioGroup_weather);
        weatherRadioButtons = new RadioButton[]{
                findViewById(R.id.radio_clear),
                findViewById(R.id.radio_snow),
                findViewById(R.id.radio_cloudy),
                findViewById(R.id.radio_hot),
                findViewById(R.id.radio_cold)
        };

        // === 주류 CheckBox 배열 ===
        drinkCheckBoxes = new CheckBox[]{
                findViewById(R.id.checkbox_soju),
                findViewById(R.id.checkbox_beer),
                findViewById(R.id.checkbox_makgeolli),
                findViewById(R.id.checkbox_vodka),
                findViewById(R.id.checkbox_wine)
        };

        categorys = new ImageButton[]{
                findViewById(R.id.category1),
                findViewById(R.id.category2),
                findViewById(R.id.category3),
                findViewById(R.id.category4),
                findViewById(R.id.category5),
                findViewById(R.id.category6),
                findViewById(R.id.category7),
                findViewById(R.id.category8),
        };
        main.setVisibility(View.VISIBLE);

        main_text.setText(texts[(int) (Math.random() * texts.length)]);

        bottomNavBar();
        restrauntList();


        runSettings();



        //추천 버튼 눌렀을시 동작
        mainBtn.setOnClickListener(click -> {
            System.out.println("추천 버튼 눌림");
            // 1. UI 스레드에서 우선 로딩 화면 보여주기
            main.setVisibility(View.GONE);
            showRestMain.setVisibility(View.GONE);
            showSettingMain.setVisibility(View.GONE);
            recomRlstMain.setVisibility(View.GONE);
            scrollMain.setVisibility(View.GONE);
            scrollSetting.setVisibility(View.GONE);
            scrollRslt.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);// <- 로딩 화면 표시
//            main_text.setVisibility(View.VISIBLE);

            // 2. GPT 요청은 별도의 백그라운드 스레드에서 처리
            new Thread(() -> {
                prompt = toScript();
                retryCount = 0;
                GptUse gptSession = new GptUse();
                gptSession.requestRecommendation();

                try {
                    GptUse.latch.await();  // GPT 응답 대기
                    GptParse parse = new GptParse();
                    indexList = parse.runParse();

                    // 3. 결과 도착 후 UI 업데이트는 UI 스레드에서 수행
                    runOnUiThread(() -> {
                        loading.setVisibility(View.GONE);         // 로딩 숨기기
                        recomRlstMain.setVisibility(View.VISIBLE); // 결과 레이아웃 보이기
                        getRslt();
//                            Toast.makeText(getApplicationContext(), indexList.toString(), Toast.LENGTH_SHORT).show();
                    });

                } catch (InterruptedException | JsonProcessingException e) {
                    e.printStackTrace();
//                        runOnUiThread(() ->
//                                Toast.makeText(getApplicationContext(), "추천 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show()
             }

                }).start();  // <- Thread 시작
        });

        // 근처 식당 보기 네비게이션
        recomReBtn.setOnClickListener(click -> {
            if(retryCount < 5){
                getRslt();
            } else {
                Toast.makeText(getApplicationContext(), "시도 가능한 횟수 초과", Toast.LENGTH_SHORT).show();
            }
        });

        recomBackBtn.setOnClickListener(click -> {
            showRestMain.setVisibility(View.GONE);
            showSettingMain.setVisibility(View.GONE);
            recomRlstMain.setVisibility(View.GONE);
            scrollMain.setVisibility(View.VISIBLE);
            scrollSetting.setVisibility(View.GONE);
            scrollRslt.setVisibility(View.GONE);
            main.setVisibility(View.VISIBLE);
            main_text.setText(texts[(int) (Math.random() * texts.length)]);
        });
    }



    public void bottomNavBar() {

        showRestBtn.setOnClickListener(click -> {
            main.setVisibility(View.GONE);
            recomRlstMain.setVisibility(View.GONE);
            showSettingMain.setVisibility(View.GONE);
            scrollMain.setVisibility(View.GONE);
            scrollSetting.setVisibility(View.GONE);
            scrollRslt.setVisibility(View.GONE);
            showRestMain.setVisibility(View.VISIBLE);
            main_text.setVisibility(View.GONE);
            showRestBtn.setAlpha(1f);
            showRecomBtn.setAlpha(0.5f);
            showSettingBtn.setAlpha(0.5f);
            printSettings();
            // RecyclerView 초기화

        });
        // 식당 추천 네비게이션
        showRecomBtn.setOnClickListener(click -> {
            showRestMain.setVisibility(View.GONE);
            showSettingMain.setVisibility(View.GONE);
            recomRlstMain.setVisibility(View.GONE);
            scrollMain.setVisibility(View.VISIBLE);
            scrollSetting.setVisibility(View.GONE);
            scrollRslt.setVisibility(View.GONE);
            main.setVisibility(View.VISIBLE);
            main_text.setVisibility(View.VISIBLE);
            main_text.setText(texts[(int) (Math.random() * texts.length)]);
            showRecomBtn.setAlpha(1f);
            showRestBtn.setAlpha(0.5f);
            showSettingBtn.setAlpha(0.5f);

            toScript();
        });

        showSettingBtn.setOnClickListener(click -> {
            main.setVisibility(View.GONE);
            showRestMain.setVisibility(View.GONE);
            recomRlstMain.setVisibility(View.GONE);
            showSettingMain.setVisibility(View.VISIBLE);
            scrollMain.setVisibility(View.GONE);
            scrollSetting.setVisibility(View.VISIBLE);
            scrollRslt.setVisibility(View.GONE);
            main_text.setVisibility(View.VISIBLE);

            main_text.setText("원하는 느낌을 선택해보세요 \n AI가 식당을 추천해줍니다! 🍽");
            showRecomBtn.setAlpha(0.5f);
            showRestBtn.setAlpha(0.5f);
            showSettingBtn.setAlpha(1f);

        });


    }

    public void restrauntList(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView1);

        // 데이터 준비 (아이콘 배열)
        int[] restaurantIcons = new int[] {
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab,
                R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab, R.drawable.bab
        };// 여러 아이콘을 사용 가능

        String[] restaurantNames = {
                "육회 바른 연어", "백남옥 달인 손만두", "푸라닭", "또봉이 통닭&별난 만두", "본죽&비빔밥",
                "맥주 뚜껑", "동명", "금별맥주", "빙동댕", "동대문 엽기 떡볶이",
                "빡가네 갈비후라이", "no more pizza", "청년다방", "3층인데 괜찮아?", "iOTTOi",
                "GGgo", "베스킨 라빈스", "참이맛 감자탕&순대국", "옛날(꿀) 닭강정", "노랑통닭",
                "주궁", "유가네 닭갈비", "메가커피", "이삭 토스트", "백소정",
                "대보칼국수", "롯데리아", "교촌", "소매점 양꼬치", "맘스터치",
                "비어펍", "투썸플레이", "보드람 치킨", "고척동 삼겹살", "계림원 누릉지 통닭구이",
                "샐러리아", "삼삼 치킨", "리얼펍&살얼음맥주", "호시타코야끼", "고좌리 뼈 칼국수",
                "한라참치", "양떼목장", "중식당 청이", "고척돈까스", "백채 김치찌개",
                "엄마손 전집", "나들이 아구찜", "도담국수", "하우마라", "난연스시",
                "엄마손 생선구이", "꼭지식당", "고척 칼국수", "별이 빛나는 밤에", "써니네 맷돌 빈대떡",
                "발발이 추억", "오복 숯불 닭꼬치", "만포 돼지국밥, 순대국", "도연", "육회지존",
                "한솥", "써브웨이", "매머드익스프레스", "역전우동", "진민네 포차",
                "역전 할머니 맥주", "와플대학", "일품마라탕", "빨봉분식", "홍콩반점",
                "빽다방", "놀부 부대찌개&철판구이", "대왕곱창", "대호네 식당", "돈전성시",
                "오븐마루", "전가복", "청남옥", "에뚜왈", "지니네 밀크빙수",
                "전주식당", "마라공방", "컴포즈커피", "김밥왕국", "포차천국",
                "스위치"
        };
        // 어댑터 설정
        RestaurantAdapter adapter = new RestaurantAdapter(restaurantIcons, restaurantNames);
        recyclerView.setAdapter(adapter);

        // LayoutManager 설정 (가로 스크롤을 위한 설정)
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    public void runSettings(){

        //수정중
        for(ImageButton ib : categorys){
            ib.setOnClickListener(click ->{
                Float alpha = ib.getAlpha();
                if(alpha < 1f){
                    ib.setAlpha(1f);
                    categoryList.remove(ib.getContentDescription().toString());
                } else {
                    ib.setAlpha(0.5f);
                    if(!categoryList.contains(ib.getContentDescription().toString())){
                        categoryList.add(ib.getContentDescription().toString());
                    }
                }
            });
        }



        for(CheckBox cb : ageCheckBoxes){
            CheckBox finalCb = cb;
            cb.setOnClickListener(check -> {
                if(finalCb.isChecked()){
                    if(!ageList.contains(finalCb.getText().toString())){
                        ageList.add(finalCb.getText().toString());
                    }
                } else {
                    ageList.remove(finalCb.getText().toString());
                }
            });
        }
        for(RadioButton rg : whoRadioButtons){
            rg.setOnClickListener(check -> {
                if (rg.isChecked()) {
                    whoList.clear();
                    whoList.add(rg.getText().toString());
                }
            });
        }
        for(RadioButton rg : conditionRadioButtons){
            rg.setOnClickListener(check -> {
                if (rg.isChecked()) {
                    conditionList.clear();
                    conditionList.add(rg.getText().toString());
                }
            });
        }
        for(RadioButton rg : weatherRadioButtons){
            rg.setOnClickListener(check -> {
                if (rg.isChecked()) {
                    weatherList.clear();
                    weatherList.add(rg.getText().toString());
                }
            });
        }
        for(CheckBox cb : drinkCheckBoxes){
            cb.setOnClickListener(check -> {
                if(cb.isChecked()){
                    if(!drinkList.contains(cb.getText().toString())){
                        drinkList.add(cb.getText().toString());
                    }
                } else {
                    drinkList.remove(cb.getText().toString());
                }
            });
        }
    }

    public void printSettings(){
        System.out.println("--------------------");
        System.out.println(categoryList);
        System.out.println(ageList);
        System.out.println(whoList);
        System.out.println(conditionList);
        System.out.println(weatherList);
        System.out.println(drinkList);
    }

    public String toScript(){
        String text = "excludedCategory: " + categoryList.toString() +
                " ageGroup: " + ageList.toString() +
                " withWho: " + whoList.toString() +
                " wether: " + weatherList.toString() +
                " condition: " + conditionList.toString() +
                " drink: " + drinkList.toString();
        System.out.println(text);
        return text;
    }

    public void getRslt(){
        String[] restNames = restrauntReturn.getRestrauntName(indexList.get(retryCount));
        main_text.setVisibility(View.VISIBLE);
        recomRslt.setText(restNames[0]);
        retryCount++;
        main_text.setText("오늘은 " + restNames[0] + "에서\n식사하시는것은 어떤가요? 🍽️");
        imgChange(restNames[1]);
    }

    public void imgChange(String value){
            switch (value) {
                case "비빔밥":
                    recomRlstImg.setImageResource(R.drawable.bibimbap);
                    recomRlstImg.setPadding(48, 48, 48, 48);
                    break;
                case "우동":
                    recomRlstImg.setImageResource(R.drawable.backban);
                    recomRlstImg.setPadding(48, 48, 48, 48);//임시
                    break;
                case "짜장면":
                case "탕수육":
                    recomRlstImg.setImageResource(R.drawable.jajang);
                    recomRlstImg.setPadding(48, 48, 48, 48);
                    break;
                case "스테이크":
                    recomRlstImg.setImageResource(R.drawable.stake);
                    recomRlstImg.setPadding(48, 48, 48, 48);
                    break;
                case "파스타":
                case "스파게티":
                    recomRlstImg.setImageResource(R.drawable.pasta);
                    recomRlstImg.setPadding(48, 48, 48, 48);
                    break;
                case "라멘":
                    recomRlstImg.setImageResource(R.drawable.ramen);
                    recomRlstImg.setPadding(48, 48, 48, 48);
                    break;
                case "피자":
                    break;
                case "국밥":
                    recomRlstImg.setImageResource(R.drawable.gukbap);
                    recomRlstImg.setPadding(48, 48, 48, 48);
                    break;
                case "초밥":
                case "스시":
                    recomRlstImg.setImageResource(R.drawable.susi);
                    recomRlstImg.setPadding(48, 48, 48, 48);
                    break;
                default:
                    recomRlstImg.setImageResource(R.drawable.bab);
                    recomRlstImg.setPadding(48, 48, 48, 48);

            }
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
}