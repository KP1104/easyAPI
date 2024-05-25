package com.iage.easyAPI.response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CustomJson {

    public String CustomJsonImpl(List<?> response, String responseCode, String[] keysToRemove, Optional<String> errorMsg, Optional<String> errorDetail) {

        // Creating responseJsonObject for holding the responseData
        JSONObject responseJsonObject = new JSONObject();

        // Passing response code to validate the response
        responseJsonObject.put("status", responseCode);

        // Create a JSON array to hold the filtered response objects and remove unwanted values
        JSONArray jsonArray = new JSONArray();

        // Checking if the response code is 0 for success and then Iterate through the list and filter out the unwanted fields
        if (responseCode.equals("0")) {
            for (Object item : response) {
                JSONObject jsonObject = new JSONObject(item);
                for (String key : keysToRemove) {
                    jsonObject.remove(key);
                }
                jsonArray.put(jsonObject);

            }
            responseJsonObject.put("responseData", jsonArray);
        } else {
            // Initiating same workflow for error response
            JSONObject errorJsonObject = new JSONObject();
            JSONArray errorJsonArray = new JSONArray();
            errorJsonObject.put("errorCd", "-99");
            errorJsonObject.put("errorTitle", "ERROR");
            errorJsonObject.put("errorMsg", errorMsg.orElse("There is an error!"));
            errorJsonObject.put("errorDetail", errorDetail.orElse("Contact system admin!!"));
            responseJsonObject.put("status", responseCode);
            responseJsonObject.put("errorData", errorJsonArray.put(errorJsonObject));

        }
        // Return the response JSON object as a string
        return responseJsonObject.toString();
    }
}
