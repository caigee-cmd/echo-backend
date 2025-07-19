package com.ruoyi.bussiness.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 支持币种对象 t_symbols
 * 
 * @author ruoyi
 * @date 2023-06-26
 */
public class TSymbols extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 币种名称（ID） */
    private String slug;

    /** 币种符号 */
    @Excel(name = "币种符号")
    private String symbol;

    /** 币种全称 */
    @Excel(name = "币种全称")
    private String fullname;

    /** 图标链接 */
    @Excel(name = "图标链接")
    private String logoUrl;

    /** 是否法定货币 */
    @Excel(name = "是否法定货币")
    private Boolean fiat;

    public void setSlug(String slug) 
    {
        this.slug = slug;
    }

    public String getSlug() 
    {
        return slug;
    }
    public void setSymbol(String symbol) 
    {
        this.symbol = symbol;
    }

    public String getSymbol() 
    {
        return symbol;
    }
    public void setFullname(String fullname) 
    {
        this.fullname = fullname;
    }

    public String getFullname() 
    {
        return fullname;
    }
    public void setLogoUrl(String logoUrl) 
    {
        this.logoUrl = logoUrl;
    }

    public String getLogoUrl() 
    {
        return logoUrl;
    }
    public void setFiat(Boolean fiat)
    {
        this.fiat = fiat;
    }

    public Boolean getFiat()
    {
        return fiat;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("slug", getSlug())
            .append("symbol", getSymbol())
            .append("fullname", getFullname())
            .append("logoUrl", getLogoUrl())
            .append("fiat", getFiat())
            .toString();
    }
}
