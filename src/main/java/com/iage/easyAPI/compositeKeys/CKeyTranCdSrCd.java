package com.iage.easyAPI.compositeKeys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CKeyTranCdSrCd implements Serializable {

   /*
   * GeneratedValue is an annotation used that the field should be automatically generated when
   * entity is persisted. Strategy parameter is used to mention what kind of strategy to use for
   * sequence generation
   */
    @Column(name = "tran_cd", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tran_cd_generator")
    @SequenceGenerator(name = "tran_cd_generator", sequenceName = "enc_mobishoper_trn_items_tran_cd_seq", allocationSize = 1)
    private Long tranCd;

    // Integer data type can hold bull value where int data type cannot hold null value
    @Column(name = "sr_cd", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sr_cd_generator")
    @SequenceGenerator(name = "sr_cd_generator", sequenceName = "enc_mobishoper_trn_items_sr_cd_seq", allocationSize = 1)
    private Integer srCd;

 public Long getTranCd() {
  return tranCd;
 }

 public void setTranCd(Long tranCd) {
  this.tranCd = tranCd;
 }

 public Integer getSrCd() {
  return srCd;
 }

 public void setSrCd(Integer srCd) {
  this.srCd = srCd;
 }

 @Override
 public boolean equals(Object o) {
  if (this == o) return true;
  if (o == null || getClass() != o.getClass()) return false;
  CKeyTranCdSrCd that = (CKeyTranCdSrCd) o;
  return Objects.equals(tranCd, that.tranCd) && Objects.equals(srCd, that.srCd);
 }

 @Override
 public int hashCode() {
  return Objects.hash(tranCd, srCd);
 }
}
