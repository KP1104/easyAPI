package com.iage.easyAPI.model.login;

public class BranchDetailsModel {

    String branchCd;

    String compCd;

    Integer srCd;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String userName;

    public String getBranchCd() {
        return branchCd;
    }

    public void setBranchCd(String branchCd) {
        this.branchCd = branchCd;
    }

    public String getCompCd() {
        return compCd;
    }

    public void setCompCd(String compCd) {
        this.compCd = compCd;
    }

    public Integer getSrCd() {
        return srCd;
    }

    public void setSrCd(Integer srCd) {
        this.srCd = srCd;
    }
}
