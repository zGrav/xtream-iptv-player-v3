package z.xtreamiptv.playerv3.v2api.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class RedirectionInfoDeserializer implements JsonDeserializer<RedirectionInfo> {
    private static final String KEY_METHOD = "httpMethod";
    private static final String KEY_PARAMETERS = "parameters";
    private static final String KEY_URI = "uri";

    RedirectionInfoDeserializer() {
    }

    public RedirectionInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String uri = jsonObject.get(KEY_URI).getAsString();
        String httpMethod = jsonObject.get(KEY_METHOD).getAsString();
        Map<String, String> parameters = readParametersMap(jsonObject);
        RedirectionInfo result = new RedirectionInfo();
        result.setUri(uri);
        result.setHttpMethod(httpMethod);
        result.setParameters(parameters);
        return result;
    }

    @Nullable
    private Map<String, String> readParametersMap(@NonNull JsonObject jsonObject) {
        JsonElement paramsElement = jsonObject.get(KEY_PARAMETERS);
        if (paramsElement == null) {
            return null;
        }
        JsonObject parametersObject = paramsElement.getAsJsonObject();
        Map<String, String> parameters = new HashMap();
        for (Entry<String, JsonElement> entry : parametersObject.entrySet()) {
            parameters.put((String) entry.getKey(), ((JsonElement) entry.getValue()).getAsString());
        }
        return parameters;
    }
}
