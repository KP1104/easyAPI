package com.iage.easyAPI.repository;

import com.iage.easyAPI.model.login.BranchDetailsModel;
import com.iage.easyAPI.utility.MD5HashingImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EncMobiShopperVerificationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    private static final class BranchDetailsRowMapper implements RowMapper<BranchDetailsModel> {
        @Override
        public BranchDetailsModel mapRow(ResultSet rs, int rowNumber)throws SQLException {
            BranchDetailsModel branchDetailsModel = new BranchDetailsModel();
            branchDetailsModel.setBranchCd(rs.getString("BRANCH_CD"));
            branchDetailsModel.setCompCd(rs.getString("COMP_CD"));
            branchDetailsModel.setSrCd(rs.getInt("SR_CD"));
            branchDetailsModel.setUserName(rs.getString("USER_NAME"));

            return branchDetailsModel;
        }

    }

    public Integer verifyUserIdAndPassword(String userName, String password) {
        String userPassword = MD5HashingImpl.getMD5(password);
        //System.out.println(userPassword);
         String sql = "SELECT COUNT(1) as count " +
                 "FROM SECURITY_USERS M " +
                 "WHERE M.USER_NAME = :userName " +
                 "AND M.USER_PASSWORD = PACK_ENCR_DCR.PROC_ENCR_MD5(:password)";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("userName", userName);
        params.addValue("password", password);

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<BranchDetailsModel> getBranchDetails(String userName) {
        String sql = "SELECT E.USER_NAME, E.SR_CD, E.COMP_CD, E.BRANCH_CD, " +
                "(SELECT B.BRANCH_NM FROM ELF_BRANCH_MST B " +
                "WHERE B.COMP_CD = E.COMP_CD AND B.BRANCH_CD = E.BRANCH_CD) AS BRANCH_NM, " +
                "FUNC_COMP_NM(E.COMP_CD) AS COMP_NM " +
                "FROM ENC_COMPANY_ACCESS_DTL E " +
                "WHERE TRIM(UPPER(E.USER_NAME)) = TRIM(UPPER(:userName)) " +
                "UNION ALL " +
                "SELECT 'admin' AS USER_NAME, ROW_NUMBER() OVER (ORDER BY E.COMP_CD) AS SR_CD, " +
                "E.COMP_CD, E.BRANCH_CD, E.BRANCH_NM, " +
                "FUNC_COMP_NM(E.COMP_CD) AS COMP_NM " +
                "FROM ELF_BRANCH_MST E " +
                "WHERE E.COMP_CD = '0001' " +
                "AND TRIM(UPPER(:userName)) = TRIM(UPPER('admin'))";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userName", userName);

        return namedParameterJdbcTemplate.query(sql, params, new BranchDetailsRowMapper());
    }

}
