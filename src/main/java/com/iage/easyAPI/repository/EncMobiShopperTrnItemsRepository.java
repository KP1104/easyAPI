package com.iage.easyAPI.repository;


import com.iage.easyAPI.model.EncMobiShopperTrnItemsModel;
import com.iage.easyAPI.model.SeriesDetailsModel;
import com.iage.easyAPI.model.StagingDetailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;


/**
 * This repository is used to implement all warehousing sql queries
 */
@Repository
public class EncMobiShopperTrnItemsRepository {
    /**
     * Initializing of jdbc template variable
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    /**
     * creating custom row mapper class to map response to EncMobishopperTrnItemsModel
     *@see EncMobiShopperTrnItemsModel
     */
    private static final class InsertTrnItemsMapper implements RowMapper<EncMobiShopperTrnItemsModel> {
        @Override
        public EncMobiShopperTrnItemsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            EncMobiShopperTrnItemsModel encMobiShopperTrnItemsModel = new EncMobiShopperTrnItemsModel();
            encMobiShopperTrnItemsModel.setTranCd(rs.getString("tranCd"));
            encMobiShopperTrnItemsModel.setRowNum(rs.getRow());
            return encMobiShopperTrnItemsModel;
        }
    }

    /**
     * creating custom row mapper class to map response to staging details model
     * @see StagingDetailModel
     */
    private static final class StagingDetailsMapper implements RowMapper<StagingDetailModel> {
        @Override
        public StagingDetailModel mapRow(ResultSet rs, int rowNum) throws SQLException{
            StagingDetailModel stagingDetailModel = new StagingDetailModel();
            stagingDetailModel.setCompCd(rs.getString("COMP_CD"));
            stagingDetailModel.setBranchCd(rs.getString("BRANCH_CD"));
            stagingDetailModel.setDocSeries(rs.getString("DOC_SERIES"));
            stagingDetailModel.setDocDate(rs.getDate("DOC_DT"));
            stagingDetailModel.setSrCd(rs.getInt("SR_CD"));
            stagingDetailModel.setItemCd(rs.getString("ITEM_CD"));
            stagingDetailModel.setQty(rs.getBigDecimal("QTY"));
            stagingDetailModel.setFromLocationCd(rs.getString("FROM_LOC_CD"));
            stagingDetailModel.setToLocationCd(rs.getString("TO_LOC_CD"));
            stagingDetailModel.setUserName(rs.getString("USER_NM"));
            stagingDetailModel.setMachineName(rs.getString("MACHINE_NM"));
            stagingDetailModel.setRowCount(rs.getInt("ROW_COUNT"));

            return stagingDetailModel;
        }
    }

    /**
     * creating custom row mapper class to map response to series detail model
     * @see SeriesDetailsModel
     */
    private static final class SeriesDetailsMapper implements RowMapper<SeriesDetailsModel> {
        @Override
        public SeriesDetailsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            SeriesDetailsModel seriesDetailsModel = new SeriesDetailsModel();
            seriesDetailsModel.setSeriesCd(rs.getString("SERIES_CD"));
            seriesDetailsModel.setSubTypeCd(rs.getString("TYPE_CD"));
            seriesDetailsModel.setTypeCd(rs.getString("SUBTYPE_CD"));
            seriesDetailsModel.setTranCd(rs.getString("TRAN_CD"));

            return seriesDetailsModel;
        }
    }


    /**
     * sql for selecting tranCd and rowCount to check if user had already
     * left a transaction with a tranCd and had already inserted items in staging
     *
     * @param branchCd unique branch selected by user
     * @param compCd   unique company code selected by user
     * @param tranType transaction type entered by the user
     * @param username username used for logging in by the user
     * @return list of type EncMobishopperTrnItemsModel in particular just tranCd and rowCount
     * @see EncMobiShopperTrnItemsModel
     */
    public List<EncMobiShopperTrnItemsModel> selectTranCdAndRowCount(String compCd, String branchCd, String tranType, String username) {

        String sql = "SELECT TRAN_CD as tranCd ,COUNT(1) as itemCount" +
                "   FROM  ENC_MOBISHOPER_TRN_ITEMS" +
                "   WHERE COMP_CD = :compCd AND" +
                "         BRANCH_CD = :branchCd AND" +
                "         TRAN_TYPE = :tranType AND" +
                "         USER_NM  = :username" +
                "   GROUP BY TRAN_CD";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("compCd", compCd);
        params.addValue("branchCd", branchCd);
        params.addValue("tranType", tranType);
        params.addValue("username", username);

        return namedParameterJdbcTemplate.query(sql, params, new InsertTrnItemsMapper());
    }


    /**
     * sql for inserting values in EncMobishoperTrnItems table after clearing all validations
     *
     * @param compCd         unique company code selected by the user
     * @param branchCd       unique branch code selected by the user
     * @param docSeries      unique document series passed by the user
     * @param tranDate       date of transaction when it was initialised
     * @param username       username used for logging in by the user
     * @param machineName    machine name used by the user for logging in
     * @param fromLocationCd unique location selected by the user to transfer the stock from that particular location
     * @param itemCd         scanned barcode code of the item
     * @param qty            amount of qty passed by the user for that particular scanned item
     * @param tranType       unique transaction type passed by the user
     * @param rowCount       number of rows logged in user had inserted previously
     * @param issueReceiveCd
     * @return row count of the row that is inserted
     */
    public Integer insertTrnItems(String compCd, String branchCd, Long tranCd, String docSeries, Date tranDate, String username, String machineName, String fromLocationCd,
                                  String itemCd, BigDecimal qty, String tranType, Integer rowCount, String issueReceiveCd) {

        String sql = "INSERT INTO ENC_MOBISHOPER_TRN_ITEMS (" +
                "COMP_CD, BRANCH_CD, DOC_DT, DOC_SERIES, TRAN_TYPE, TRAN_CD, " +
                "SR_CD, ISS_REC_CD, FROM_LOC_CD, ITEM_CD, QTY, USER_NM, MACHINE_NM" +
                ") VALUES (" +
                ":compCd, :branchCd, :tranDate, :docSeries, :tranType, :tranCd, " +
                ":rowCount, :issueReceiveCd, :fromLocationCd, :itemCd, :qty, :username, :machineName)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("compCd", compCd)
                .addValue("branchCd", branchCd)
                .addValue("tranCd", tranCd)
                .addValue("docSeries", docSeries)
                .addValue("tranDate", tranDate)
                .addValue("username", username)
                .addValue("machineName", machineName)
                .addValue("fromLocationCd", fromLocationCd)
                .addValue("itemCd", itemCd)
                .addValue("qty", qty)
                .addValue("tranType", tranType)
                .addValue("rowCount", rowCount)
                .addValue("issueReceiveCd", issueReceiveCd);

        return namedParameterJdbcTemplate.update(sql, params);
    }


    /**
     * sql function for checking available live stock for itemCd mentioned in the parameter
     * @implNote it is an sql function call
     * @param compCd   unique company code selected by the user
     * @param branchCd unique branch code selected by the user
     * @param itemCd   scanned barcode code of the item
     * @param locCd    unique from location selected by the user to transfer the stock from that particular location
     * @param asonDate current system date
     * @return avialble stock quantity
     */
    public Integer getAvailableStockQty(String compCd, String branchCd, String itemCd, String locCd, Date asonDate) {
        String sql = "{? = call FUNC_GET_STOCK(?, ?, ?, ?, ?)}";

        return jdbcTemplate.execute(connection -> {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.registerOutParameter(1, Types.NUMERIC);
            callableStatement.setString(2, compCd);
            callableStatement.setString(3, branchCd);
            callableStatement.setString(4, itemCd);
            callableStatement.setString(5, locCd);
            callableStatement.setDate(6, new java.sql.Date(asonDate.getTime()));
            return callableStatement;
        }, (CallableStatement cs) -> {
            cs.execute();
            return cs.getInt(1);
        });
    }

    /**
     * sql for deleting items from staging
     * @param compCd unique company code selected by the user
     * @param branchCd unique branch code selected by the user
     * @param tranType transaction type entered by the user
     * @param username username used for logging in by the user
     */
    public Integer deleteTrnItems(String compCd, String branchCd, String tranType, String username) {
        String sql = "DELETE FROM ENC_MOBISHOPER_TRN_ITEMS " +
                "WHERE COMP_CD = :compCd AND " +
                "BRANCH_CD = :branchCd AND " +
                "TRAN_TYPE = :tranType AND " +
                "USER_NM = :username";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("compCd",compCd);
        params.addValue("branchCd",branchCd);
        params.addValue("tranType",tranType);
        params.addValue("username", username);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    /**
     * sql query to select parameters from staging table based on the required arguments
     * @param compCd unique comp code selected by the user
     * @param branchCd unique branch code selected by the user
     * @param userName unique username used to log in
     * @param tranType unique transaction type passed by the user
     * @return list of type staging details model
     * @see StagingDetailModel
     */
    // remove tran code and add username and tran type as parameters to get details of all staging data
    public List<StagingDetailModel> getStagingDetailsData (String compCd, String branchCd, String userName, String tranType) {
        String sql = "SELECT COMP_CD, " +
                "       BRANCH_CD, " +
                "       DOC_SERIES, " +
                "       DOC_DT, " +
                "       SR_CD, " +
                "       ITEM_CD, " +
                "       QTY, " +
                "       FROM_LOC_CD, " +
                "       TO_LOC_CD, " +
                "       ACCT_CD, " +
                "       USER_NM, " +
                "       MACHINE_NM, " +
                "       ROWNUM AS ROW_COUNT " +
                "  FROM ENC_MOBISHOPER_TRN_ITEMS " +
                " WHERE COMP_CD = :compCd AND " +
                "       BRANCH_CD = :branchCd AND " +
                "       USER_NM = :userName AND " +
                "       TRAN_TYPE = :tranType";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("compCd",compCd);
        params.addValue("branchCd",branchCd);
        params.addValue("userName",userName);
        params.addValue("tranType", tranType);

        return namedParameterJdbcTemplate.query(sql, params, new StagingDetailsMapper());
    }

    /**
     * sql query to get series and tran details from stock branch list
     * @param compCd unique company code slelected by the user
     * @param branchCd unique branch code selected by the user
     * @param docSeries unique document series passed by the user
     * @return list of type Series Details Model
     * @see SeriesDetailsModel
     */
    public List<SeriesDetailsModel> getSeriesDetailData(String compCd, String branchCd, String docSeries) {
        String sql = "SELECT SERIES_CD, TYPE_CD, SUBTYPE_CD, TRAN_CD " +
                "FROM ELG_SERIES_HDR " +
                "WHERE COMP_CD = :compCd AND BRANCH_CD = :branchCd AND SERIES_CD = :docSeries";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("compCd",compCd);
        params.addValue("branchCd",branchCd);
        params.addValue("docSeries",docSeries);

        return namedParameterJdbcTemplate.query(sql, params, new SeriesDetailsMapper());
    }

    /**
     * sql function for getting max document number
     * @implNote it is an sql function call
     * @param compCd unique company code selected by the user
     * @param branchCd unique branch code selected by the user
     * @param tranCd unique tran code fetched from other sql queries
     * @implNote tran code is fetched from getStagingDetailsData
     * @return document number of type long
     */
    public String getMaxDocNumber(String compCd, String branchCd, Long tranCd) {
        String sql = "{? = call GET_MAX_DOC_NO(?, ?, ?)}";

        return jdbcTemplate.execute(connection -> {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.registerOutParameter(1, Types.NUMERIC);
            callableStatement.setString(2, compCd);
            callableStatement.setString(3, branchCd);
            callableStatement.setLong(4, tranCd);
            return callableStatement;
        }, (CallableStatement cs) -> {
            cs.execute();
            return cs.getString(1);
        });
    }

    /**
     * sql fucntion to update document number
     * @param compCd unique company code selected by the user
     * @param branchCd unique branch code selected by the user
     * @param seriesTranCd unique series transaction code fetched from the table
     * @return document number of type Long which is the increment document number
     */
    public Long updateDocumentNumber(String compCd, String branchCd, Long seriesTranCd) {
        String sql = "{? = call P_UPD_DOC_NO(?, ?, ?)";

        return jdbcTemplate.execute(connection -> {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.registerOutParameter(1, Types.NUMERIC);
            callableStatement.setString(2, compCd);
            callableStatement.setString(3, branchCd);
            callableStatement.setLong(4, seriesTranCd);
            return callableStatement;
        }, (CallableStatement cs) -> {
            cs.execute();
            return cs.getLong(1);
        });
    }


    /**
     * sql query for inserting values into header table after clearning all validations
     * @param compCd unique company code selected by the user
     * @param branchCd unique branch code selected by the user
     * @param tranCd unique tran code fetched from sql query
     * @param docSeries unique document series passed by the user
     * @param docCd
     * @param docDate
     * @param fromLocationCd unique location code for the item to transfer from
     * @param toLocationCd unique location code for the item to transfer to
     * @param remarks remarks if any
     * @param docTime
     * @param enteredBy
     * @param machineName machine name from which the transaction is generated
     * @param enteredDate
     * @param docTranCd
     * @param status response status containing response message
     * @param lastEnteredBy
     * @param lastMachineName
     * @param lastModifiedDate
     * @param checkedBy
     * @return row count for insertion of item
     */
    public Integer insertEncLocationHeader(String compCd, String branchCd, Long tranCd, String docSeries,
                                           String docCd, Date docDate, String fromLocationCd, String toLocationCd,
                                           String remarks, Time docTime, String enteredBy, String machineName, Date enteredDate,
                                           Long docTranCd, String status, String lastEnteredBy, String lastMachineName, Date lastModifiedDate,
                                           String checkedBy) {
        String sql = "INSERT INTO ENC_LOCATION_TRF_HDR (" +
                "COMP_CD, BRANCH_CD, TRAN_CD, DOC_SERIES, DOC_CD, DOC_DT, " +
                "FROM_LOC_CD, TO_LOC_CD, REMARKS, DOC_TIME, ENTERED_BY, MACHINE_NM, " +
                "ENTERED_DATE, DOC_TRAN_CD, STATUS, LAST_ENTERED_BY, LAST_MACHINE_NM, " +
                "LAST_MODIFIED_DATE, CHECKED_BY" +
                ") VALUES (" +
                ":compCd, :branchCd, :tranCd, :docSeries, :docCd, :docDate, " +
                ":fromLocationCd, :toLocationCd, :remarks, :docTime, :enteredBy, :machineName, " +
                ":enteredDate, :docTranCd, :status, :lastEnteredBy, :lastMachineName, " +
                ":lastModifiedDate, :checkedBy)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("compCd",compCd)
                .addValue("branchCd", branchCd)
                .addValue("tranCd", tranCd)
                .addValue("docSeries", docSeries)
                .addValue("docCd", docCd)
                .addValue("docDate", docDate)
                .addValue("fromLocationCd", fromLocationCd)
                .addValue("toLocationCd", toLocationCd)
                .addValue("remarks", remarks)
                .addValue("docTime", docTime)
                .addValue("enteredBy", enteredBy)
                .addValue("machineName", machineName)
                .addValue("enteredDate", enteredDate)
                .addValue("docTranCd", docTranCd)
                .addValue("status", status)
                .addValue("lastEnteredBy", lastEnteredBy)
                .addValue("lastMachineName", lastMachineName)
                .addValue("lastModifiedDate", lastModifiedDate)
                .addValue("checkedBy", checkedBy);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    /**
     * sql query for inserting values into detail table
     * @param compCd unique company code selected by the user
     * @param branchCd unique branch code selected by the user
     * @param tranCd unique tran code fetched from sql query
     * @param rowCount
     * @param qty qty entered by the user
     * @param itemCd barcode scanned by the user
     * @param fromLocationCd unique location code for the item to transfer from
     * @param toLocationCd unique location code for the item to transfer to
     */
    public Integer insertEncLocationDetail(String compCd, String branchCd, Long tranCd, Integer rowCount, BigDecimal qty ,
                                           String itemCd, String fromLocationCd, String toLocationCd) {
        String sql = "INSERT INTO ENC_LOCATION_TRF_DTL ( " +
                    "COMP_CD, BRANCH_CD, TRAN_CD, SR_CD, ITEM_CD, QTY," +
                    "FROM_LOC_CD, TO_LOC_CD " +
                    " ) VALUES ( " +
                    " :compCd, :branchCd, :tranCd, :rowCount, :qty, :itemCd, :fromLocationCd, :toLocationCd)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("compCd", compCd)
                .addValue("branchCd", branchCd)
                .addValue("tranCd", tranCd)
                .addValue("rowCount", rowCount)
                .addValue("qty", qty)
                .addValue("itemCd", itemCd)
                .addValue("fromLocationCd", fromLocationCd)
                .addValue("toLocationCd", toLocationCd);

        System.out.println(params.getValues());

        return namedParameterJdbcTemplate.update(sql, params);
    }

}




