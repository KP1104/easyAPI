package com.iage.easyAPI.utility;

import com.iage.easyAPI.compositeKeys.CKeyTranCdSrCd;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.Arrays;


public class CustomResponseObject {


    public String CustomResponse(Class<?> requestClass, String[] strArray, String responseStatus) {

        //creating main response data json array
        JSONArray responseJsonArray = new JSONArray();

        if(responseStatus.equals("0")) {
            try {
                // Creating an instance of the provided class so that we can access all the fields using getter setter methods
                Object instance = requestClass.getDeclaredConstructor().newInstance();

                // Getting all fields of the class, including private ones
                Field[] fields = requestClass.getDeclaredFields();

                // JsonObject to store field names and values
                JSONObject fieldsJson = new JSONObject();


                /*
                * Adding fields and values to Json Object
                * Accessing fields from Class variable
                */
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object fieldValue = field.get(instance);

                    // Checking if the field is in the strArray which passed as parameters to make it more dynamic when all the params are not needed
                    if (Arrays.asList(strArray).contains(field.getName())) {
                        // Handling object fields recursively if there are any null values
                        if (fieldValue != null) {
                            // Accessing field if there is a composite key
                            JSONObject objectFieldsJson = null;
                            if (field.getType().equals(CKeyTranCdSrCd.class)) {
                                objectFieldsJson = new JSONObject();
                                // getting all the declared fields of the composite keys
                                for (Field objField : field.getType().getDeclaredFields()) {
                                    objField.setAccessible(true);
                                    Object objFieldValue = objField.get(fieldValue);
                                    // Iterating through array of fields values as mentioned in the parameter
                                    if (Arrays.asList(strArray).contains(objField.getName())) {
                                        // Populating json with key value pairs and checking if there are any null values
                                        objectFieldsJson.put(objField.getName().toUpperCase(), objFieldValue != null ? objFieldValue : "");
                                    }
                                }
                                fieldsJson.put(field.getName().toUpperCase(), responseJsonArray.put(objectFieldsJson));
                            } else {
                                fieldsJson.put(field.getName().toUpperCase(), responseJsonArray.put(objectFieldsJson));
                            }
                        } else {
                            fieldsJson.put(field.getName().toUpperCase(), "");
                        }
                    }
                }

                // Constructing the response JSON object
                JSONObject responseJson = new JSONObject();
                responseJson.put("status", "0");
                responseJson.put("responseData", responseJsonArray.put(fieldsJson));

                // Returning the json
                return responseJson.toString();
            } catch (Exception e) {
                // Returning the response message if there is an exception
                return e.getMessage();
            }
        } else {
            // Constructing the Json Objects if the response code is 99
            JSONArray jsonArray = new JSONArray();
            JSONObject responseJsonObject2 = new JSONObject();
            JSONObject responseJsonObject3 = new JSONObject();
            responseJsonObject3.put("errorCd","-99");
            responseJsonObject3.put("errorTitle","ERROR");
            responseJsonObject3.put("errorMsg","There is an error!!");
            responseJsonObject3.put("errorDetail","Contact system admin!");
            responseJsonObject2.put("status", responseStatus);
            responseJsonObject2.put("errorData",jsonArray.put(responseJsonObject3));

            return responseJsonObject2.toString();

        }
    }
}



