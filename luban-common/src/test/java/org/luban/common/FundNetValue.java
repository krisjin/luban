package org.luban.common;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * User:krisjin
 * Date:2019/2/22
 *  
 */
public class FundNetValue {

    /**
     * 净值日期
     */
    private Date netValueDate;
    /**
     * 净值
     */
    private BigDecimal netValue;

    /**
     * 构造函数
     */
    public FundNetValue(Date netValueDate, BigDecimal netValue) {
        this.netValueDate = netValueDate;
        this.netValue = netValue;
    }

    /**
     * setter for netValueDate
     */
    public void setNetValueDate(Date netValueDate) {
        this.netValueDate = netValueDate;
    }

    /**
     * setter for netValue
     */
    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

    /**
     * getter for netValueDate
     */
    public Date getNetValueDate() {
        return this.netValueDate;
    }

    /**
     * getter for netValue
     */
    public BigDecimal getNetValue() {
        return this.netValue;
    }
}

