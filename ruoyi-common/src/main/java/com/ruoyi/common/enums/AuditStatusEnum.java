package com.ruoyi.common.enums;

/**
 * 实名认证状态
 */
public enum AuditStatusEnum {


    /**
     * 审核通过
     */
    EXAMINATION_PASSED("1","审核通过"),

    /**
     * 不通过
     */
    AUDIT_NOT_PASSED("2","不通过"),
    /**
     * 未审核
     */
    NOT_REVIEWED("3","未审核"),




    ;
    private String code;

    private String desc;

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return desc;
    }

    AuditStatusEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
