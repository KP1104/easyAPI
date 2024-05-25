package com.iage.easyAPI.controller;

import com.iage.easyAPI.entity.EncMobiShopperTrnItems;
import com.iage.easyAPI.model.EncMobiShopperTrnItemsModel;
import com.iage.easyAPI.response.CustomJson;
import com.iage.easyAPI.response.CustomResponseObject;
import com.iage.easyAPI.service.EncMobiShopperTrnItemsService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/easyapi")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class Controller {

    @Autowired
    EncMobiShopperTrnItemsService encMobiShopperTrnItemsService;



    EncMobiShopperTrnItems encMobiShopperTrnItems;

//    @Autowired
//    CustomResponseObject customResponseObject;

//    @PostMapping(value = "/findAll")
//    public List testMethod() {
//        List list = (List) encMobiShopperTrnItemsService.findAllRecords();
//        return list;
//    }

    @PostMapping(value = "/getTranCd")
    public Object testMethod2(@RequestBody String params) {

        return encMobiShopperTrnItemsService.getTranCdAndItemCount(params);
    }

    @PostMapping(value = "/json")
    public Object jsonOutput() {
        CustomResponseObject customResponseObject = new CustomResponseObject();
        String[] arr = {"branchCd","compCd","srCd", "tranCd","tranType"};
        return customResponseObject.CustomResponse(EncMobiShopperTrnItems.class, arr, "0");
    }


}
