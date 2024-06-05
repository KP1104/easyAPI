package com.iage.easyAPI.repository.sequences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sequences {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Long getNextMobiShoppertrnSequence() {
        String sql = "SELECT SEQ_MOBISHOPER_TRN_ITEMS.NEXTVAL FROM dual";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Long getNextTranCdSequence() {
        String sql = "SELECT SEQ_TRAN_CD.NEXTVAL FROM dual";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
