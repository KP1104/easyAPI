package com.iage.easyAPI.repository;

import com.iage.easyAPI.compositeKeys.CKeyTranCdSrCd;
import com.iage.easyAPI.entity.EncMobiShopperTrnItems;

import com.iage.easyAPI.model.EncMobiShopperTrnItemsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EncMobiShopperTrnItemsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //List<EncMobiShopperTrnItems> findAll();

    // List<Object[]> countByCompCdAndBranchCdAndTranTypeUAndUserNmGroupByTranCd();

    //    @Query(
//            nativeQuery = true,
//            value = "SELECT TRAN_CD as tranCd ,COUNT(1) as itemCount" +
//                    "   FROM  ENC_MOBISHOPER_TRN_ITEMS" +
//                    "   WHERE COMP_CD = ?1 AND" +
//                    "         BRANCH_CD = ?2 AND" +
//                    "         TRAN_TYPE = ?3 AND" +
//                    "         USER_NM  = ?4" +
//                    "   GROUP BY TRAN_CD;"
//    )
    public List<EncMobiShopperTrnItemsModel> getTranCdAndItemCount(String compCd, String branchCd, String tranType, String username) {
        String sql = "SELECT TRAN_CD as tranCd ,COUNT(1) as itemCount" +
                "   FROM  ENC_MOBISHOPER_TRN_ITEMS" +
                "   WHERE COMP_CD = ? AND" +
                "         BRANCH_CD = ? AND" +
                "         TRAN_TYPE = ? AND" +
                "         USER_NM  = ?" +
                "   GROUP BY TRAN_CD";

        return jdbcTemplate.query(sql, new Object[]{compCd, branchCd, tranType, username}, (resultSet, rowNum) -> {
            EncMobiShopperTrnItemsModel model = new EncMobiShopperTrnItemsModel();
            //System.out.println("rn" + rowNum);
            model.setTranCd(resultSet.getString("tranCd"));
            model.setItemCount(resultSet.getInt("itemCount"));
            model.setRowNum(rowNum);
            return model;

        });
    }

}


