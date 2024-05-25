package com.iage.easyAPI.entity;

import com.iage.easyAPI.compositeKeys.CKeyTranCdSrCd;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "enc_mobishoper_trn_items")
@Data
//@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EncMobiShopperTrnItems {

    @EmbeddedId
    private CKeyTranCdSrCd cKeyTranCdSrCd;

    @Column(columnDefinition = "char(4)")
    private String compCd;

    @Column(columnDefinition = "char(4)")
    private String branchCd;

    @Column(columnDefinition = "date")
    private Date docDt;

    @Column(columnDefinition = "varchar(10)")
    private String docSeries;

    @Column(columnDefinition = "varchar(10)")
    private String tranType;

    @Column(columnDefinition = "char(8)")
    private String acctCd;

    @Column(columnDefinition = "char default 'I'")
    private String issRecCd;

    @Column(columnDefinition = "char(10)")
    private String fromLocCd;

    @Column(columnDefinition = "char(10)")
    private String toLocCd;

    @Column(columnDefinition = "char(40)")
    private String itemCd;

    @Column(columnDefinition = "number(12, 2)")
    private BigDecimal qty;

    @Column(columnDefinition = "varchar(16)")
    private String userNm;

    @Column(columnDefinition = "varchar(16)")
    private String machineNm;

    public EncMobiShopperTrnItems(CKeyTranCdSrCd cKeyTranCdSrCd, String compCd, String branchCd, Date docDt, String docSeries, String tranType, String acctCd, String issRecCd, String fromLocCd, String toLocCd, String itemCd, BigDecimal qty, String userNm, String machineNm) {
        this.cKeyTranCdSrCd = cKeyTranCdSrCd;
        this.compCd = compCd;
        this.branchCd = branchCd;
        this.docDt = docDt;
        this.docSeries = docSeries;
        this.tranType = tranType;
        this.acctCd = acctCd;
        this.issRecCd = issRecCd;
        this.fromLocCd = fromLocCd;
        this.toLocCd = toLocCd;
        this.itemCd = itemCd;
        this.qty = qty;
        this.userNm = userNm;
        this.machineNm = machineNm;
    }

    public CKeyTranCdSrCd getcKeyTranCdSrCd() {
        return cKeyTranCdSrCd;
    }

    public void setcKeyTranCdSrCd(CKeyTranCdSrCd cKeyTranCdSrCd) {
        this.cKeyTranCdSrCd = cKeyTranCdSrCd;
    }

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

    public Date getDocDt() {
        return docDt;
    }

    public void setDocDt(Date docDt) {
        this.docDt = docDt;
    }

    public String getDocSeries() {
        return docSeries;
    }

    public void setDocSeries(String docSeries) {
        this.docSeries = docSeries;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getAcctCd() {
        return acctCd;
    }

    public void setAcctCd(String acctCd) {
        this.acctCd = acctCd;
    }

    public String getIssRecCd() {
        return issRecCd;
    }

    public void setIssRecCd(String issRecCd) {
        this.issRecCd = issRecCd;
    }

    public String getFromLocCd() {
        return fromLocCd;
    }

    public void setFromLocCd(String fromLocCd) {
        this.fromLocCd = fromLocCd;
    }

    public String getToLocCd() {
        return toLocCd;
    }

    public void setToLocCd(String toLocCd) {
        this.toLocCd = toLocCd;
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

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getMachineNm() {
        return machineNm;
    }

    public void setMachineNm(String machineNm) {
        this.machineNm = machineNm;
    }
}
