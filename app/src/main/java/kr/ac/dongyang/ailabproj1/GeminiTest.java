package kr.ac.dongyang.ailabproj1;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.ai.client.generativeai.type.SafetySetting;
import com.google.ai.client.generativeai.type.SafetySetting.HarmBlockThreshold;
import com.google.ai.client.generativeai.type.SafetySetting.HarmCategory;
import com.google.ai.client.generativeai.type.content.Content;
import com.google.ai.client.generativeai.type.content.Part;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GeminiTest {

    private static final String API_KEY = "YOUR_API_KEY"; // 여기에 실제 API 키를 넣어주세요.
    private static final String MODEL_NAME = "gemini-pro";

    public static void main(String[] args) {
        GenerativeModel model = new GenerativeModel(MODEL_NAME, API_KEY);
        String promptText = "자바에서 Gemini API를 사용하는 방법에 대한 간단한 예제 코드를 알려줘.";

        Content content = Content.newBuilder()
                .addPart(Part.newBuilder().setText(promptText).build())
                .build();

        // 안전 설정 (선택 사항)
        List<SafetySetting> safetySettings = Arrays.asList(
                new SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE),
                new SafetySetting(HarmCategory.HATE_SPEECH, HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE),
                new SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE),
                new SafetySetting(HarmCategory.DANGEROUS_CONTENT, HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
        );

        // 생성 설정 (선택 사항)
        GenerationConfig generationConfig = GenerationConfig.newBuilder()
                .setMaxOutputTokens(200)
                .setTemperature(0.8f)
                .setTopP(0.9f)
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<GenerateContentResponse> future = GenerativeModelFutures.generateContent(model, content, generationConfig, safetySettings);

        try {
            GenerateContentResponse response = future.get();
            if (response != null && response.getText() != null) {
                System.out.println("Gemini 응답:\n" + response.getText());
            } else {
                System.out.println("Gemini 응답이 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("Gemini API 호출 중 오류 발생: " + e.getMessage());
        } finally {
            executor.shutdown();
            model.close();
        }
    }
}