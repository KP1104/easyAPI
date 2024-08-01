package com.iage.easyAPI.utility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomJson {

    /**
     *
     * @param response : The response object that needs to be displayed in the responseData
     * @param responseCode : Response code to check whether the data fetched is a success or failure or there are any exceptions
     * @param keysToRemove : Keys to remove from the json as the json is made from the model class.
     * @param errorMsg : Optional parameter which is similar to conditional parameter.
     *                   It means that if the value is passed that that value will be passed else default value will be passed as parameter
     * @param errorDetail : Optional parameter which is similar to conditional parameter.
     *                      It means that if the value is passed that that value will be passed else default value will be passed as parameter
     * @return : Json object converted to string is returned
     */
    public String CustomJsonImpl(List<?> response, String responseCode, Optional<String[]> keysToRemove, Optional<String> errorMsg, Optional<String> errorDetail) {

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
//                for (String key : keysToRemove) {
//                    jsonObject.remove(key);
//                }
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

    public String RaiseApplicationError(String responseCode, String responseError) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("responseCode",responseCode);
        jsonObject1.put("error", responseError);
        jsonArray.put(jsonObject1);
        return jsonArray.toString();
    }

    public String RaiseApplicationMsg(String status, String responseMsg) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("status",status);
        jsonObject1.put("responseMsg", responseMsg);
        jsonArray.put(jsonObject1);
        return jsonArray.toString();
    }
}

