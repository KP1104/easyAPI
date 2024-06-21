package com.iage.easyAPI.service;


import com.iage.easyAPI.model.EncMobiShopperTrnItemsModel;
import com.iage.easyAPI.model.SeriesDetailsModel;
import com.iage.easyAPI.model.StagingDetailModel;
import com.iage.easyAPI.repository.EncMobiShopperTrnItemsRepository;
import com.iage.easyAPI.repository.sequences.Sequences;
import com.iage.easyAPI.utility.CustomJson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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


        Long nextSeqTranCd = sequences.getNextMobiShopperTrnSequence();

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
                                    Integer insertedRowCount = encMobiShopperTrnItemsRepository.insertTrnItems(compCd, branchCd, tranCd, docSeries, sqlDate, username, machineName, fromLocationCd, itemCd, qty, tranType, rowCount, issueReceiveCode);
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
            Integer insertedRowCount = encMobiShopperTrnItemsRepository.insertTrnItems(compCd, branchCd, nextSeqTranCd, docSeries, sqlDate, username, machineName, fromLocationCd, itemCd, qty, tranType, rowCount, issueReceiveCode);
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

    @Transactional
    public String insertTrnItemsCommit(String params) {

        JSONObject jsonObject = new JSONObject(params);
        String compCd = jsonObject.getString("compCd");
        String branchCd = jsonObject.getString("branchCd");
        String userName = jsonObject.getString("userName");
        String tranType = jsonObject.getString("tranType");
        String toLocationCd = jsonObject.getString("toLocationCd");
        String tranCd = "";

        Long nextTranCd = null;

        Time localTime = Time.valueOf(LocalTime.now());

        if(compCd.equals("") || branchCd.equals("") || userName.equals("") || tranType.equals("")) {
            return customJson.RaiseApplicationError("99","Null values found in the request");
        }
            List<StagingDetailModel> stagingDetailModelList = encMobiShopperTrnItemsRepository.getStagingDetailsData(compCd, branchCd, userName, tranType);
            for (StagingDetailModel model :
                 stagingDetailModelList) {
                Integer rowCount = model.getRowCount();
                //System.out.println("Staging detail model row count " + rowCount);
                if(rowCount == 1) {
                    List<SeriesDetailsModel> seriesDetailModelList = encMobiShopperTrnItemsRepository.getSeriesDetailData(compCd, branchCd, model.getDocSeries());
                    if (seriesDetailModelList.isEmpty()) {
                        return customJson.RaiseApplicationError("99","Issues in fetching series data");
                    } else {
                        // to write the conditions after series list
                        for (SeriesDetailsModel seriesModel:
                             seriesDetailModelList) {
                             tranCd = seriesModel.getTranCd();
                        }
                        String maxDocumentCode = encMobiShopperTrnItemsRepository.getMaxDocNumber(compCd, branchCd, Long.valueOf(tranCd));
                        nextTranCd = sequences.getNextTranCdSequence();

                        Integer insertHdr = encMobiShopperTrnItemsRepository.insertEncLocationHeader(compCd, branchCd, nextTranCd, model.getDocSeries(), maxDocumentCode, model.getDocDate(), model.getFromLocationCd(), model.getToLocationCd(), "Data saved from mobile app",
                                localTime, userName, model.getMachineName(), model.getDocDate(), Long.valueOf(tranCd), "N", userName, model.getMachineName(), model.getDocDate(), "Entered by mobile app");
//                        System.out.println("SRCD" + model.);
//                        BigDecimal qty = new BigDecimal(model.getQty());
                        BigDecimal qtyBigDecimal = new BigDecimal(model.getQty());
                        System.out.println(qtyBigDecimal);
                        System.out.println("q " + nextTranCd);
                        Integer insertDetail = encMobiShopperTrnItemsRepository.insertEncLocationDetail(compCd, branchCd, nextTranCd, model.getSrCd(), qtyBigDecimal, model.getItemCd(), model.getFromLocationCd(), toLocationCd);
                        System.out.println("ID" + insertDetail);
                        return customJson.RaiseApplicationMsg("0","Location transfer created with Doc Cd : " + maxDocumentCode);
                    }
                }
            }

        return customJson.RaiseApplicationError("99", "Error!!! Please contact system admin.");
    }
}


