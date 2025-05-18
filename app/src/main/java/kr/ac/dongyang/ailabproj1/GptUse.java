package kr.ac.dongyang.ailabproj1;

import android.content.Context;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GptUse {

    private static String apiKey = "API_KEYS";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient client;
    private final Gson gson;
    String[] mainMenus = {
            "Gukbap",
            "Stews",
            "Grilled Meat",
            "Noodles",
            "Rice",
            "Bunsik",
            "Sushi",
            "Japanese Noodles",
            "Tempura",
            "Donburi",
            "Korean-Chinese Dishes",
            "Lamb Skewers (Yangkochi)",
            "Pasta",
            "Pizza",
            "Steak"
    };

    String RestaurantsJson = "[\n" +
            "  [1, \"맛있는 비빔밥집\", 4.3, \"한식\", \"비빔밥\", \"돌솥비빔밥\", \"불고기비빔밥\", \"김치찌개\"],\n" +
            "  [2, \"우동나라\", 4.7, \"일식\", \"우동\", \"카케우동\", \"튀김우동\", \"냉우동\"],\n" +
            "  [3, \"짜장명가\", 3.9, \"중식\", \"짜장면\", \"짜장면\", \"짬뽕\", \"탕수육\"],\n" +
            "  [4, \"스테이크 하우스\", 4.8, \"양식\", \"스테이크\", \"립아이 스테이크\", \"필레 미뇽\", \"시저 샐러드\"],\n" +
            "  [5, \"한식정식\", 4.1, \"한식\", \"한정식\", \"불고기\", \"된장찌개\", \"잡채\"],\n" +
            "  [6, \"초밥천국\", 4.6, \"일식\", \"초밥\", \"연어초밥\", \"광어초밥\", \"참치초밥\"],\n" +
            "  [7, \"탕수육달인\", 4.0, \"중식\", \"탕수육\", \"탕수육\", \"짜장면\", \"깐풍기\"],\n" +
            "  [8, \"파스타앤피자\", 4.5, \"양식\", \"파스타\", \"까르보나라\", \"마르게리타 피자\", \"볼로네제\"],\n" +
            "  [9, \"순대국밥집\", 3.8, \"한식\", \"국밥\", \"순대국밥\", \"내장탕\", \"공기밥\"],\n" +
            "  [10, \"텐동명가\", 4.4, \"일식\", \"덴푸라\", \"새우텐동\", \"야채텐동\", \"튀김우동\"],\n" +
            "  [11, \"중화요리집\", 4.2, \"중식\", \"중화요리\", \"마파두부\", \"고추잡채\", \"짜장면\"],\n" +
            "  [12, \"양식레스토랑\", 4.7, \"양식\", \"양식\", \"립아이 스테이크\", \"치킨 파마산\", \"시저 샐러드\"],\n" +
            "  [13, \"돌솥비빔밥집\", 4.3, \"한식\", \"비빔밥\", \"돌솥비빔밥\", \"된장찌개\", \"김치\"],\n" +
            "  [14, \"우동천국\", 4.5, \"일식\", \"우동\", \"카케우동\", \"튀김우동\", \"냉우동\"],\n" +
            "  [15, \"중식당\", 4.0, \"중식\", \"중식\", \"짬뽕\", \"짜장면\", \"탕수육\"],\n" +
            "  [16, \"파스타가게\", 4.6, \"양식\", \"파스타\", \"알리오 올리오\", \"까르보나라\", \"볼로네제\"],\n" +
            "  [17, \"김치찌개 전문점\", 4.1, \"한식\", \"찌개\", \"김치찌개\", \"된장찌개\", \"순두부찌개\"],\n" +
            "  [18, \"초밥집\", 4.7, \"일식\", \"초밥\", \"연어초밥\", \"광어초밥\", \"참치초밥\"],\n" +
            "  [19, \"탕수육맛집\", 4.3, \"중식\", \"탕수육\", \"탕수육\", \"깐풍기\", \"짜장면\"],\n" +
            "  [20, \"피자헛\", 4.5, \"양식\", \"피자\", \"페퍼로니 피자\", \"마르게리타\", \"고르곤졸라\"],\n" +
            "  [21, \"콩나물국밥\", 4.0, \"한식\", \"국밥\", \"콩나물국밥\", \"순두부찌개\", \"김치\"],\n" +
            "  [22, \"덴푸라집\", 4.4, \"일식\", \"덴푸라\", \"새우덴푸라\", \"야채덴푸라\", \"튀김우동\"],\n" +
            "  [23, \"중화요리 전문점\", 4.2, \"중식\", \"중화요리\", \"마파두부\", \"고추잡채\", \"짜장면\"],\n" +
            "  [24, \"서양식당\", 4.6, \"양식\", \"양식\", \"스테이크\", \"파스타\", \"샐러드\"],\n" +
            "  [25, \"불고기집\", 4.3, \"한식\", \"불고기\", \"불고기\", \"비빔밥\", \"된장찌개\"],\n" +
            "  [26, \"일본라멘집\", 4.5, \"일식\", \"라멘\", \"돈코츠라멘\", \"쇼유라멘\", \"미소라멘\"],\n" +
            "  [27, \"중국집\", 4.1, \"중식\", \"중식\", \"짜장면\", \"짬뽕\", \"탕수육\"],\n" +
            "  [28, \"파스타집\", 4.7, \"양식\", \"파스타\", \"볼로네제\", \"알리오 올리오\", \"까르보나라\"],\n" +
            "  [29, \"찌개전문점\", 4.0, \"한식\", \"찌개\", \"된장찌개\", \"김치찌개\", \"순두부찌개\"],\n" +
            "  [30, \"스시집\", 4.6, \"일식\", \"초밥\", \"연어초밥\", \"참치초밥\", \"광어초밥\"],\n" +
            "  [31, \"중식당2\", 4.2, \"중식\", \"중식\", \"고추잡채\", \"마파두부\", \"탕수육\"],\n" +
            "  [32, \"스테이크하우스\", 4.7, \"양식\", \"스테이크\", \"립아이\", \"필레미뇽\", \"치킨 파마산\"],\n" +
            "  [33, \"김치찌개집\", 4.1, \"한식\", \"찌개\", \"김치찌개\", \"된장찌개\", \"순두부찌개\"],\n" +
            "  [34, \"라멘집\", 4.5, \"일식\", \"라멘\", \"돈코츠라멘\", \"쇼유라멘\", \"미소라멘\"],\n" +
            "  [35, \"중식전문점\", 4.3, \"중식\", \"중식\", \"짜장면\", \"짬뽕\", \"탕수육\"],\n" +
            "  [36, \"피자가게\", 4.6, \"양식\", \"피자\", \"마르게리타\", \"페퍼로니\", \"고르곤졸라\"],\n" +
            "  [37, \"국밥집\", 4.0, \"한식\", \"국밥\", \"순대국밥\", \"내장탕\", \"공기밥\"],\n" +
            "  [38, \"덴푸라집2\", 4.4, \"일식\", \"덴푸라\", \"새우덴푸라\", \"야채덴푸라\", \"튀김우동\"],\n" +
            "  [39, \"중화요리집2\", 4.3, \"중식\", \"중식\", \"마파두부\", \"고추잡채\", \"탕수육\"],\n" +
            "  [40, \"서양요리집\", 4.7, \"양식\", \"양식\", \"스테이크\", \"파스타\", \"샐러드\"]\n" +
            "]";


    public GptUse() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    public void requestRecommendation() {
        JsonArray messages = new JsonArray();

        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        StringBuilder content = new StringBuilder();
        content.append("You are a restaurant recommendation assistant.\n\n")
                .append("Here is a list of available restaurants in the form of a list where each item is:\n")
                .append("[index (int), restaurantName (String), rating (double), category (String), subCategory (String), mainMenu1 (String), mainMenu2 (String), mainMenu3 (String)]\n\n")
                .append("For example:\n")
                .append("[1, \"맛있는 비빔밥집\", 4.3, \"한식\", \"비빔밥\", \"돌솥비빔밥\", \"불고기비빔밥\", \"김치찌개\"]\n\n")
                .append("Here is the full list:\n")
                .append(RestaurantsJson).append("\n\n")
                .append("Recommend exactly 5 restaurants that best match the user’s preferences.\n")
                .append("Respond in a structured JSON format like this:\n")
                .append("[\n")
                .append("  {index: 1, restaurantName: ..., category: ..., mainTheme: ...},\n")
                .append("  {index: 2, restaurantName: ..., category: ..., mainTheme: ...},\n")
                .append("  ...\n")
                .append("]\n\n")
                .append("Use one of the following for the 'mainTheme' field:\n")
                .append(new Gson().toJson(mainMenus)).append("\n\n")
                .append("Be concise and only return the list in the specified format. No explanation needed.");

        systemMsg.addProperty("content", content.toString());
        messages.add(systemMsg);

        // user 메시지도 추가
        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", MainActivity.prompt);
        messages.add(userMsg);

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("model", "gpt-3.5-turbo");
        jsonBody.add("messages", messages);

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("GPT 요청 실패: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("GPT 응답 실패: " + response.code());
                    return;
                }

                String resultStr = response.body().string();
                JsonObject resultJson = JsonParser.parseString(resultStr).getAsJsonObject();
                String reply = resultJson
                        .getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();

                System.out.println(reply.trim());;
            }
        });
    }
}