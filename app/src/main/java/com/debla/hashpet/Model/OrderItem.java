package com.debla.hashpet.Model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItem implements Serializable{
    private Integer ordItemId;

    private Integer ordId;

    private Integer ordProId;

    private Integer ordNum;

    private BigDecimal ordSingleP;

    public Integer getOrdItemId() {
        return ordItemId;
    }

    public void setOrdItemId(Integer ordItemId) {
        this.ordItemId = ordItemId;
    }

    public Integer getOrdId() {
        return ordId;
    }

    public void setOrdId(Integer ordId) {
        this.ordId = ordId;
    }

    public Integer getOrdProId() {
        return ordProId;
    }

    public void setOrdProId(Integer ordProId) {
        this.ordProId = ordProId;
    }

    public Integer getOrdNum() {
        return ordNum;
    }

    public void setOrdNum(Integer ordNum) {
        this.ordNum = ordNum;
    }

    public BigDecimal getOrdSingleP() {
        return ordSingleP;
    }

    public void setOrdSingleP(BigDecimal ordSingleP) {
        this.ordSingleP = ordSingleP;
    }
}