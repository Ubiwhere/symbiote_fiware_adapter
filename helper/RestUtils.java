package eu.h2020.symbiote.fiwarePlugin.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.h2020.symbiote.fiwarePlugin.models.BaseModel;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestUtils <T extends BaseModel> {

    public JSONObject get(String url, Map<String, String> params, HttpHeaders headers ){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params != null ) {
            for (String key : params.keySet()) {
                builder.queryParam(key, params.get(key));
            }
        }

        url = builder.build().encode().toUri().toString();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, params );
        JSONObject jo = null;

        try { jo = new JSONObject(response.getBody().toString());
        } catch (JSONException e) {e.printStackTrace();}

        return jo;
    }

    public List<T> parseJson(JSONArray ja, Class<T> clazz){
        List<T> values = new ArrayList();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T d = null;

        for (int i=0 ; i < ja.length(); i++){
            try {
                d = mapper.readValue(ja.getJSONObject(i).toString(),clazz);
                values.add(d);

            } catch (JSONException e) { e.printStackTrace();
            } catch (IOException e) { e.printStackTrace(); }

        }
        return values;
    }
}
