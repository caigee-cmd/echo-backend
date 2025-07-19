package com.ruoyi.common.enums;


import java.util.*;
import java.util.stream.Collectors;

/**
 * 公告管理枚举
 *
 */
public enum NoticeTypeEnum {
    INFORMATION_NOTICE("1","公告信息"),
    ACTIVITY_NOTICE("2","活动公告"),
    ROLL_NOTICE("3","首页滚动公告"),
    POP_UPS_NOTICE("4","弹窗公告"),
    REGISTER_WELFARE("5","注册福利");
//    WHITE_PAPER("4","白皮书");


    NoticeTypeEnum(String code, String value) {
         this.code = code;
         this.value = value;
    }

    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 公告管理二级
     */
     public enum ChildrenEnum {
        /**
         * 公告信息二级
         */
        LINK_INFORMATION("1","链接弹窗",NoticeTypeEnum.INFORMATION_NOTICE),
        TEXT_INFORMATION("2","图文弹窗",NoticeTypeEnum.INFORMATION_NOTICE),
        /**
         * 活动公告二级
         */
        HOME_ACTIVITY("1","首页轮播活动",NoticeTypeEnum.ACTIVITY_NOTICE),
        MINING_ACTIVITY("2","Defi挖矿活动图",NoticeTypeEnum.ACTIVITY_NOTICE),
        FINANCE_ACTIVITY("3","理财活动图",NoticeTypeEnum.ACTIVITY_NOTICE),
        POP_UPS_NOTICE("4","首页弹窗公告图",NoticeTypeEnum.POP_UPS_NOTICE),
        DEFI_POP_UPS_NOTICE("5","DEFI弹窗公告图",NoticeTypeEnum.POP_UPS_NOTICE),
        REGISTER_WELFARE_NOTICE("6","新手注册活动",NoticeTypeEnum.REGISTER_WELFARE);

        private String code;
        private String value;
        private NoticeTypeEnum prent;

        ChildrenEnum(String code, String value, NoticeTypeEnum prent) {
            this.code = code;
            this.value = value;
            this.prent = prent;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public NoticeTypeEnum getPrent() {
            return prent;
        }

        public void setPrent(NoticeTypeEnum prent) {
            this.prent = prent;
        }

        public static List<Map<String, Object>> getChildrenEnum(NoticeTypeEnum parent) {
            ChildrenEnum[] values = ChildrenEnum.values();
            List<Map<String, Object>> list = new ArrayList<>();
            for (ChildrenEnum noticeType :values) {
                if (noticeType.getPrent().equals(parent)){
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("key",noticeType.name());
                    resultMap.put("code",noticeType.getCode());
                    resultMap.put("value",noticeType.getValue());
                    list.add(resultMap);
                }
            }
            return list;
        }

    }

    public static List<Map<String, Object>> getEnum(){
        NoticeTypeEnum[] values = NoticeTypeEnum.values();
        List<Map<String, Object>> list = new ArrayList<>();
        for (NoticeTypeEnum noticeType :values) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("key",noticeType.name());
            resultMap.put("code",noticeType.getCode());
            resultMap.put("value",noticeType.getValue());
            resultMap.put("obj",ChildrenEnum.getChildrenEnum(noticeType));
            list.add(resultMap);
        }
        return list;
    }
}
