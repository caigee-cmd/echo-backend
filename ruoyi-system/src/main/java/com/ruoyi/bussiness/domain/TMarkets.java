package com.ruoyi.bussiness.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Id;

/**
 * 支持交易所对象 t_markets
 * 
 * @author ruoyi
 * @date 2023-06-26
 */
public class TMarkets extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 交易所名称(ID) */
    @Id
    private String slug;

    /** 交易所全称 */
    @Excel(name = "交易所全称")
    private String fullname;

    /** 交易所官网链接 */
    @Excel(name = "交易所官网链接")
    private String websiteUrl;

    /** 状态: [enable, disable]. disable为停止更新数据 */
    @Excel(name = "状态: [enable, disable]. disable为停止更新数据")
    private String status;

    /** 是否接入K线数据 */
    @Excel(name = "是否接入K线数据")
    private Boolean kline;

    /** 是否支持现货 */
    @Excel(name = "是否支持现货")
    private Boolean spot;

    /** 是否支持期货 */
    @Excel(name = "是否支持期货")
    private Boolean futures;

    public void setSlug(String slug) 
    {
        this.slug = slug;
    }

    public String getSlug() 
    {
        return slug;
    }
    public void setFullname(String fullname) 
    {
        this.fullname = fullname;
    }

    public String getFullname() 
    {
        return fullname;
    }
    public void setWebsiteUrl(String websiteUrl) 
    {
        this.websiteUrl = websiteUrl;
    }

    public String getWebsiteUrl() 
    {
        return websiteUrl;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setKline(Boolean kline)
    {
        this.kline = kline;
    }

    public Boolean getKline()
    {
        return kline;
    }
    public void setSpot(Boolean spot)
    {
        this.spot = spot;
    }

    public Boolean getSpot()
    {
        return spot;
    }
    public void setFutures(Boolean futures)
    {
        this.futures = futures;
    }

    public Boolean getFutures()
    {
        return futures;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("slug", getSlug())
            .append("fullname", getFullname())
            .append("websiteUrl", getWebsiteUrl())
            .append("status", getStatus())
            .append("kline", getKline())
            .append("spot", getSpot())
            .append("futures", getFutures())
            .toString();
    }
}
