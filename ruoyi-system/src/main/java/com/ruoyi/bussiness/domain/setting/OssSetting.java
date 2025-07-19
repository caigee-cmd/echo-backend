package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

/**
 * 阿里文件服务器
 */
@Data
public class OssSetting {

    /**
     * 存放路径路径
     */
    private String picLocation;

    private String endPoint;
    /**
     * 储存空间
     */
    private String bucketName;
    /**
     * 密钥id
     */
    private String accessKeyId;
    /**
     * 密钥
     */
    private String accessKeySecret;


}
