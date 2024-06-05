package com.iage.easyAPI.model;

import java.sql.Date;

/**
 * model class to get only required number of parameters from select query
 */
public class EncMobiShopperTrnItemsModel {

     String tranCd;
     Integer qty;

     Integer rowNum;
     String docSeries;
     String compCd;
     String branchCd;



     public Integer getQty() {
          return qty;
     }

     public void setQty(Integer qty) {
          this.qty = qty;
     }

     public String getDocSeries() {
          return docSeries;
     }

     public void setDocSeries(String docSeries) {
          this.docSeries = docSeries;
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

     public String getTranCd() {
          return tranCd;
     }

     public void setTranCd(String tranCd) {
          this.tranCd = tranCd;
     }

     public Integer getItemCount() {
          return qty;
     }

     public void setItemCount(Integer itemCount) {
          this.qty = itemCount;
     }


     public Integer getRowNum() {
          return rowNum;
     }

     public void setRowNum(Integer rowNum) {
          this.rowNum = rowNum;
     }
}
