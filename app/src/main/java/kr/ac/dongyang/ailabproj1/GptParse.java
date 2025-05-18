package kr.ac.dongyang.ailabproj1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;


public class GptParse{
    public static ArrayList<Integer> indexes = new ArrayList<>();

    public void runParse() throws JsonProcessingException {
        ObjectMapper objMap = new ObjectMapper();
        JsonNode jsonArray = objMap.readTree(GptUse.gptResponce);

        for (JsonNode node : jsonArray) {
            indexes.add(node.get("index").asInt());
        }
    }
}
