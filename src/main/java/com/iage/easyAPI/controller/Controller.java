package com.iage.easyAPI.controller;

import com.iage.easyAPI.entity.EncMobiShopperTrnItems;
import com.iage.easyAPI.service.EncMobiShopperVerificationService;
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


    @Autowired
    EncMobiShopperVerificationService encMobiShopperVerificationService;


    @PostMapping(value = "/insertTrnItemsStaging")
    public Object insertTrnItemsStaging(@RequestBody String params) {
        return encMobiShopperTrnItemsService.insertTrnItemsStaging(params);
    }

    @PostMapping(value = "/deleteTrnItemsFromStaging")
    public Object deleteTrnItemsFromStaging(@RequestBody String params) {
        return encMobiShopperTrnItemsService.deleteTrnItemsFromStaging(params);
    }

    @PostMapping(value = "/locationTransferCommit")
    public Object locationTransferCommit(@RequestBody String params) {
        return encMobiShopperTrnItemsService.insertTrnItemsCommit(params);
    }


    @PostMapping(value = "/login")
    public Object login(@RequestBody String params) {
        return encMobiShopperVerificationService.verifyUserIdAndPassword(params);
    }


}
