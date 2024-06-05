package com.iage.easyAPI.controller;

import com.iage.easyAPI.entity.EncMobiShopperTrnItems;
import com.iage.easyAPI.utility.CustomResponseObject;
import com.iage.easyAPI.service.EncMobiShopperTrnItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/easyapi")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class Controller {

    @Autowired
    EncMobiShopperTrnItemsService encMobiShopperTrnItemsService;



    EncMobiShopperTrnItems encMobiShopperTrnItems;


    @PostMapping(value = "/insertTrnItemsStaging")
    public Object insertTrnItemsStaging(@RequestBody String params) {
        return encMobiShopperTrnItemsService.insertTrnItemsStaging(params);
    }

    @PostMapping(value = "/deleteTrnItemsFromStaging")
    public Object deleteTrnItemsFromStaging(@RequestBody String params) {
        return encMobiShopperTrnItemsService.deleteTrnItemsFromStaging(params);
    }

    @PostMapping(value = "/json")
    public Object jsonOutput() {
        CustomResponseObject customResponseObject = new CustomResponseObject();
        String[] arr = {"branchCd","compCd","srCd", "tranCd","tranType"};
        return customResponseObject.CustomResponse(EncMobiShopperTrnItems.class, arr, "0");
    }


}
