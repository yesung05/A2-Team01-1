package kr.ac.dongyang.ailabproj1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;


public class GptParse{


    public static ArrayList<Integer> runParse(String respon) throws JsonProcessingException {
        ArrayList<Integer> indexes = new ArrayList<>();
        ObjectMapper objMap = new ObjectMapper();

        JsonNode jsonArray = objMap.readTree(respon);

        for (JsonNode node : jsonArray) {
            indexes.add(node.get("index").asInt());
        }
        System.out.println(indexes.toString());
        return indexes;

    }
}
