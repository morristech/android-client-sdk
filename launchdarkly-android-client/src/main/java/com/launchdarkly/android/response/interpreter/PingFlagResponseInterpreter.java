package com.launchdarkly.android.response.interpreter;

import android.support.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.launchdarkly.android.response.FlagResponse;
import com.launchdarkly.android.response.UserFlagResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Farhan
 * 2018-01-30
 */
public class PingFlagResponseInterpreter implements FlagResponseInterpreter<List<FlagResponse>> {

    @NonNull
    @Override
    public List<FlagResponse> apply(@Nullable JsonObject input) {
        List<FlagResponse> flagResponseList = new ArrayList<>();
        if (input != null) {
            for (Map.Entry<String, JsonElement> entry : input.entrySet()) {
                String key = entry.getKey();
                JsonElement v = entry.getValue();

                if (isValueInsideObject(v)) {
                    JsonObject asJsonObject = v.getAsJsonObject();

                    flagResponseList.add(UserFlagResponseParser.parseFlag(asJsonObject, key));
                } else {
                    flagResponseList.add(new UserFlagResponse(key, v));
                }
            }
        }
        return flagResponseList;
    }

    protected boolean isValueInsideObject(JsonElement element) {
        return !element.isJsonNull() && element.isJsonObject() && element.getAsJsonObject().has("value");
    }
}
