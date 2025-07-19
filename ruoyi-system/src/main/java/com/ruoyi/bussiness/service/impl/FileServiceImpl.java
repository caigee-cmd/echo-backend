package com.ruoyi.bussiness.service.impl;

import cn.hutool.json.JSONUtil;
import com.ruoyi.bussiness.domain.setting.OssSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.service.FileService;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.AliOssCloudUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileServiceImpl implements FileService {


    @Resource
    private SettingService settingService;


    @Override
    public String uploadFileOSS(MultipartFile file, String name) throws IOException {
        Setting setting = settingService.get(SettingEnum.OSS_SETTING.name());
        OssSetting ossSetting = JSONUtil.toBean(setting.getSettingValue(), OssSetting.class);
        InputStream inputStream = null;
        inputStream = file.getInputStream();
        //阿里云的endpoint
        String endpoint =  ossSetting.getEndPoint();
        //阿里云的accessKeyId
        String accessKeyId = ossSetting.getAccessKeyId();
        //阿里云的accessKeySecret
        String accessKeySecret = ossSetting.getAccessKeySecret();
        //空间
        String bucketName =ossSetting.getBucketName();
        //文件存储目录
        String filedir = ossSetting.getPicLocation();

        AliOssCloudUtil util = new AliOssCloudUtil(endpoint,accessKeyId,accessKeySecret,bucketName,filedir);
        //上传成功返回完整路径的url
        return util.uploadFile2OSS(inputStream, name);
    }
}
