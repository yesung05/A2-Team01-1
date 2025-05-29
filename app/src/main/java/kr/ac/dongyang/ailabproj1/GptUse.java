package kr.ac.dongyang.ailabproj1;

import android.content.Context;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GptUse {

    private static String apiKey = "";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient client;
    private final Gson gson;
    CountDownLatch latch = new CountDownLatch(1);
    String gptResponce;

    public static boolean gpt_wait = false;



    public GptUse() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    public void requestRecommendation() {
        gptResponce = null;
        JsonArray messages = new JsonArray();

        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        StringBuilder content = new StringBuilder();
        content.append("You are a restaurant recommendation assistant.\n\n")
                .append("Here is a list of available restaurants in the form of a list where each item is:\n")
                .append("[index (int), restaurantName (String), rating (double), category (String), subCategory (String), mainMenu1 (String), mainMenu2 (String), mainMenu3 (String)]\n\n")
                .append("If rating is '-1', is null value. Just think as rating 4.85\n")
                .append("Here is the full list:\n")
                .append(RestrauntDatas.RestaurantsJson).append("\n\n")
                .append("Recommend exactly 5 restaurants that best match the user’s preferences.\n")
                .append("If user’s preferences is empty, Recommend exactly 5 restaurants randomly\n")
                .append("Respond in a structured JSON format like this:\n")
                .append("[\n")
                .append("  {\"index\": 1},\n")
                .append("  {\"index\": 2},\n")
                .append("  ...\n")
                .append("]\n\n")
                .append("Don't recommend '주점' to kids\n" +
                        "No duplication on index\n" +
                        "focus on menu and category and subCategory. Don't focus on rating")
                .append("Be concise and only return the list in the specified format. No explanation needed.");

        systemMsg.addProperty("content", content.toString());
        messages.add(systemMsg);

        // user 메시지도 추가
        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", MainActivity.prompt);
        messages.add(userMsg);

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("model", "gpt-4-turbo");
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

                gptResponce = reply.toString();
                System.out.println("리플라이.트림");
                System.out.println(reply.trim());
                System.out.println("지피티리스폰스");
                System.out.println(gptResponce);
                latch.countDown();


//                try {
//                    if(gptResponce != null){
//
//                        GptParse.runParse(gptResponce);
//                    }
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
            }
        });
    }
}