package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 通知公告对象 t_notice
 *
 * @author ruoyi
 * @date 2023-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_notice")
public class TNotice extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 公告ID
     */
        @TableId(value = "notice_id",type = IdType.AUTO)
    private Long noticeId;
    /**
     * 标题
     */
    private String noticeTitle;
    /**
     * 公告类型 1=公告信息 2=活动公告 3=首页滚动公告
     */
    private String noticeType;
    /**
             * 模块类型 1=公告信息 2=活动公告 3=首页滚动公告
        1={1=链接弹窗 2=图文弹窗},
        2={1=首页轮播活动 2=Defi挖矿活动图},
        3={1=首页滚动公告}
        注:没有二级的默认给1
    二级联动
     */
    private String modelType;
    /**
     * 内容
     */
    private String noticeContent;
    /**
     * 评论数
     */
    private Long commentsNum;
    /**
     * 图片
     */
    private String cover;
    /**
     * 浏览数
     */
    private Long viewNum;
    /**
     * 公告截止时间
     */
    private Date expireTime;
    /**
     * 图片链接地址
     */
    private String imgUrl;
    /**
     * 链接地址
     */
    private String chainedUrl;
    /**
     * 详情页
     */
    private String detailUrl;
    /**
     * zh:1,cht:2,en:3,pt:4,sa:5,ko:6,ja:7,es:8,th:9,ms:10,id:11,fr:12,ru:13
     */
    private String languageId;
    /**
     * 公告状态（0正常 1关闭）
     */
    private String status;
    /**
     * 排序
     */
    private Long sort;
    /**
     * 展示端1=pc 2=h5
     */
    private String source;


    @TableField(exist = false)
    private String key;

    @TableField(exist = false)
    private String modelKey;

}
