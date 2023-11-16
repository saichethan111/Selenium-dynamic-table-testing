package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import requests.Person;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonReader {

    public List<Person> readTestData(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Read JSON data directly from file into a List<Person>
            return Arrays.asList(objectMapper.readValue(new File(filePath), Person[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JsonNode convertStringToJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception (e.g., log it or throw a custom exception)
        }
        return null;
    }
    public List<Map<String, Object>> convertJsonToHashMap(JsonNode jsonArray) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        if (jsonArray != null && jsonArray.isArray()) {
            for (JsonNode jsonNode : jsonArray) {
                Map<String, Object> resultMap = new HashMap<>();
                Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    resultMap.put(entry.getKey(), entry.getValue().asText());
                }
                resultList.add(resultMap);
            }
        }

        return resultList;
    }
}

