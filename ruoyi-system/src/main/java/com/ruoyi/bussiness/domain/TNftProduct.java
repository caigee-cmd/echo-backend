package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * nft详情对象 t_nft_product
 *
 * @author ruoyi
 * @date 2023-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_nft_product")
public class TNftProduct extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 合集id
     */
    private Long seriesId;
    /**
     * 图片路径
     */
    private String imgUrl;
    /**
     * 所属id
     */
    private Long userId;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 所属链 -btc -eth -doge
     */
    private String chainType;
    /**
     * 作者
     */
    private String author;
    /**
     * 持有者地址
     */
    private String holdAddress;
    /**
     * 手续费
     */
    private BigDecimal handlingFee;
    /**
     * 版权费
     */
    private BigDecimal copyrightFee;
    /**
     * 描述
     */
    private String des;
    /**
     * 商品状态 1=未上架  2=已上架
     */
    private Integer status;
    /**
     * 销售状态 0=待审核 1=待售 2=持有
     */
    private String saleStatus;
    /**
     * 上架结束日期
     */
    private Date endDate;
    /**
     * $column.columnComment
     */
    private String searchValue;
    /**
     * 0=正常 1=删除
     */
        @TableLogic
    private String delFlag;

}
