package com.iage.easyAPI.service;


import com.iage.easyAPI.model.EncMobiShopperTrnItemsModel;
import com.iage.easyAPI.repository.EncMobiShopperTrnItemsRepository;
import com.iage.easyAPI.repository.sequences.Sequences;
import com.iage.easyAPI.utility.CustomJson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Service
public class EncMobiShopperTrnItemsService {

    @Autowired
    private EncMobiShopperTrnItemsRepository encMobiShopperTrnItemsRepository;

    @Autowired
    CustomJson customJson;

    @Autowired
    Sequences sequences;

    /**
     *
     */
    @Transactional
    public String insertTrnItemsStaging(String params) {
        /*
         * Creating json object and extracting values from json object
         */
        JSONObject jsonParams = new JSONObject(params);
        String compCd = jsonParams.getString("compCd");
        String branchCd = jsonParams.getString("branchCd");
        String tranType = jsonParams.getString("tranType");
        String username = jsonParams.getString("username");
        String fromLocationCd = jsonParams.getString("fromLocCd");
        String tranStatus = jsonParams.getString("tranStatus");
        String docSeries = jsonParams.getString("docSeries");
        String machineName = jsonParams.getString("machineNm");
        Object items = jsonParams.get("items");

        // initializing variables
        Integer rowCount = 0;
        Long tranCd = null;
        String strQty = "";
        String itemCd = "";
        String issueReceiveCode = "";

        /**
         * iterating over items json array in order to directly insert if it is a new user and there
         * exists no tranCd for that user by generating new tranCd using the sequence
         */
        if (items instanceof JSONArray) {
            JSONArray jsonItemArray = jsonParams.getJSONArray("items");
            for (Object jsonItemsObject :
                    jsonItemArray) {
                if (jsonItemsObject instanceof JSONObject) {
                    itemCd = ((JSONObject) jsonItemsObject).getString("itemCd");
                    strQty = ((JSONObject) jsonItemsObject).getString("qty");
                } if (strQty.equals("") || strQty.equals("0")) {
                    strQty = "1";
                }
            }
        }



        // getting current date in the local time zone
        LocalDate localDate = LocalDate.now();

        //converting local date into sql time zone
        Date sqlDate = Date.valueOf(localDate);

        String[] tranTypearr = {"PUR_IN", "CRN", "RADS", "CARG", "STK_REC"};


        Long nextSeqTranCd = sequences.getNextMobiShoppertrnSequence();

        /*
         * Calling repository procedure to get tranCd and rowCount for existing user who already has a tranCd generated
         */
        List<EncMobiShopperTrnItemsModel> response = encMobiShopperTrnItemsRepository.selectTranCdAndRowCount(compCd, branchCd, tranType, username);
        if (Arrays.toString(tranTypearr).contains(tranType)) {
            issueReceiveCode = "R";
        } else {
            issueReceiveCode = "I";
        }
        if (!response.isEmpty()) {
            for (EncMobiShopperTrnItemsModel model : response) {
                // getting tranCd and rowCount to use it for further validations or incrementing the values for inserting
                rowCount = model.getRowNum() + 1;
                tranCd = Long.valueOf(model.getTranCd());
                if (jsonParams.has("items")) {
                    if (jsonParams.get("items") instanceof JSONArray) {
                        JSONArray jsonItems = jsonParams.getJSONArray("items");
                        for (Object obj :
                                jsonItems) {
                            if (obj instanceof JSONObject) {
                                itemCd = ((JSONObject) obj).getString("itemCd");
                                strQty = ((JSONObject) obj).getString("qty");
                                if (itemCd.equals("") || itemCd.trim().length() == 0) {
                                    return customJson.RaiseApplicationError("99", "No ItemCd found!");
                                }
                                Integer availableStockQty = encMobiShopperTrnItemsRepository.getAvailableStockQty(compCd, branchCd, itemCd, fromLocationCd, sqlDate);

                                BigDecimal qty = new BigDecimal(strQty);

                                if(fromLocationCd.equals("")) {
                                    return customJson.RaiseApplicationError("99", "From location code is required. Null values not allowed.");
                                }
                                if (availableStockQty < Integer.parseInt(strQty)) {
                                    return customJson.RaiseApplicationError("99", "Stock goes negative. Available qty is : " + availableStockQty);
                                } else {
                                    Integer x = encMobiShopperTrnItemsRepository.insertTrnItems(compCd, branchCd, tranCd, docSeries, sqlDate, username, machineName, fromLocationCd, itemCd, qty, tranType, rowCount, issueReceiveCode);
                                }
                                return customJson.RaiseApplicationMsg("0", "Item inserted successfully with Tran Code : " + tranCd);
                            }
                        }
                    }
                }
            }
        }
        // inserting for first time user
        rowCount = rowCount + 1;
        Integer availableStockQty = encMobiShopperTrnItemsRepository.getAvailableStockQty(compCd, branchCd, itemCd, fromLocationCd, sqlDate);

        BigDecimal qty = new BigDecimal(strQty);
        if(fromLocationCd.equals("")) {
            return customJson.RaiseApplicationError("99", "From location code is required. Null values not allowed.");
        }
        if (availableStockQty < Integer.parseInt(strQty)) {

            return customJson.RaiseApplicationError("-1", "Stock goes negative. Available qty is : " + availableStockQty);
        } else {
            Integer x = encMobiShopperTrnItemsRepository.insertTrnItems(compCd, branchCd, nextSeqTranCd, docSeries, sqlDate, username, machineName, fromLocationCd, itemCd, qty, tranType, rowCount, issueReceiveCode);
        }
        return customJson.RaiseApplicationMsg("0", "Item inserted successfully with Tran Code : " + nextSeqTranCd);

    }

    public String deleteTrnItemsFromStaging(String params) {

        JSONObject jsonParams = new JSONObject(params);
        String compCd = jsonParams.getString("compCd");
        String branchCd = jsonParams.getString("branchCd");
        String tranType = jsonParams.getString("tranType");
        String username = jsonParams.getString("username");

        Integer deletedRows = 0;

        deletedRows = encMobiShopperTrnItemsRepository.deleteTrnItems(compCd, branchCd, tranType, username);
        if(deletedRows == 0) {
            return customJson.RaiseApplicationError("99","No items found for deletion");
        } else {
            return customJson.RaiseApplicationMsg("0",deletedRows + " rows deleted");
        }
    }
}


