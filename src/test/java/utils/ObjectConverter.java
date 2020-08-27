package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class ObjectConverter {
    public static MultiValueMap<String, String> toParams (Object object, ObjectMapper mapper){
        Map<String, String> sObject = mapper.convertValue(object, Map.class);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for (String key : sObject.keySet()
        ) {
            params.add(key, String.valueOf(sObject.get(key)));
        }
        return params;
    }
}
