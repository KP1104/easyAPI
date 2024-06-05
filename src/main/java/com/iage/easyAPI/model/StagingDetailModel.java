package com.iage.easyAPI.model;

import java.math.BigDecimal;
import java.sql.Date;

public class StagingDetailModel {

    String compCd;
    String branchCd;
    String docSeries;
    Date docDate;
    Integer srCd;
    String itemCd;
    BigDecimal qty;
    String fromLocationCd;
    String toLocationCd;
    String userName;
    String machineName;
    Integer rowCount;

    public String getCompCd() {
        return compCd;
    }

    public void setCompCd(String compCd) {
        this.compCd = compCd;
    }

    public String getBranchCd() {
        return branchCd;
    }

    public void setBranchCd(String branchCd) {
        this.branchCd = branchCd;
    }

    public String getDocSeries() {
        return docSeries;
    }

    public void setDocSeries(String docSeries) {
        this.docSeries = docSeries;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public Integer getSrCd() {
        return srCd;
    }

    public void setSrCd(Integer srCd) {
        this.srCd = srCd;
    }

    public String getItemCd() {
        return itemCd;
    }

    public void setItemCd(String itemCd) {
        this.itemCd = itemCd;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getFromLocationCd() {
        return fromLocationCd;
    }

    public void setFromLocationCd(String fromLocationCd) {
        this.fromLocationCd = fromLocationCd;
    }

    public String getToLocationCd() {
        return toLocationCd;
    }

    public void setToLocationCd(String toLocationCd) {
        this.toLocationCd = toLocationCd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
}
