package com.iage.easyAPI.service;


import com.iage.easyAPI.model.EncMobiShopperTrnItemsModel;
import com.iage.easyAPI.repository.EncMobiShopperTrnItemsRepository;
import com.iage.easyAPI.response.CustomJson;
import org.glassfish.jersey.internal.inject.Custom;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class EncMobiShopperTrnItemsService {

    @Autowired
    private EncMobiShopperTrnItemsRepository encMobiShopperTrnItemsRepository;

    @Autowired
    CustomJson customJson;

//    public Object findAllRecords() {
//        List list = encMobiShopperTrnItemsRepository.findAll();
//        return list;
//    }

    /*
     * username, activityCd, moduleCd, machineNm, {compCd, branchCd, tranType, fromLocation,} barcode, itemQty --> insert into database
     */


    public String getTranCdAndItemCount(String params) {
        /*
         * Creating json object and extracting values from json object
         */
        JSONObject jsonParams = new JSONObject(params);

        String compCd = jsonParams.getString("compCd");
        String branchCd = jsonParams.getString("branchCd");
        String tranType = jsonParams.getString("tranType");
        String username = jsonParams.getString("username");

        Integer vTranCd;
        Integer vItemCount;
        Integer vRowCount;
        String[] keysToRemove = {"rowNum"};

        /*
         * Calling repository procedure with the required arguments
         */
        List<EncMobiShopperTrnItemsModel> response = encMobiShopperTrnItemsRepository.getTranCdAndItemCount(compCd, branchCd, tranType, username);
        if (!response.isEmpty()) {
            for (EncMobiShopperTrnItemsModel model : response) {
                vRowCount = model.getRowNum();
            }
            return customJson.CustomJsonImpl(response, "0", keysToRemove, Optional.empty(), Optional.empty());
        } else {
            return customJson.CustomJsonImpl(response, "99", keysToRemove,Optional.of("No data found!"), Optional.of("No data found!"));
        }


    }
}
