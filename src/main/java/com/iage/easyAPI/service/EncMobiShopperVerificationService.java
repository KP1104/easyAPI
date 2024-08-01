package com.iage.easyAPI.service;

import com.iage.easyAPI.model.login.BranchDetailsModel;
import com.iage.easyAPI.repository.EncMobiShopperVerificationRepository;
import com.iage.easyAPI.utility.CustomJson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EncMobiShopperVerificationService {

    @Autowired
    CustomJson customJson;

    @Autowired
    private EncMobiShopperVerificationRepository encMobiShopperVerificationRepository;

    String[] keysToRemove = {"userName","srCd"};

    public String verifyUserIdAndPassword (String params) {

        JSONObject requestJson = new JSONObject(params);
        String userName = requestJson.getString("userName");
        String password = requestJson.getString("password");

        Integer rowCount = encMobiShopperVerificationRepository.verifyUserIdAndPassword(userName, password);

        if (rowCount == 0) {
            return customJson.RaiseApplicationError("99","Invalid User Id and Password");
        }
        List<BranchDetailsModel> branchDetailsModelList = encMobiShopperVerificationRepository.getBranchDetails(userName);

        return customJson.CustomJsonImpl(branchDetailsModelList, "0", Optional.ofNullable(keysToRemove), Optional.empty(), Optional.empty());
    }
}
