package com.ruoyi.common.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class AliOssCloudUtil {
    private String endpoint =  "";

    //阿里云的accessKeyId
    private String accessKeyId = "";

    //阿里云的accessKeySecret
    private String accessKeySecret = "";

    //空间
    private String bucketName = "";

    //文件存储目录
    private String filedir = "";

    private OSSClient ossClient;

    public AliOssCloudUtil(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String filedir) {
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
        this.filedir = filedir;
        this.ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public AliOssCloudUtil() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public String getFiledir() {
        return this.filedir;
    }

    //自定义上传文件夹
    public AliOssCloudUtil(String filedir) {
        this.filedir = filedir;
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }


    /**
     * 初始化
     */
    public void init() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }


    /**
     * 上传到OSS服务器
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        // 判断bucket是否已经存在,不存在进行创建
        if (!doesBucketExist()) {
            createBucket();
        }
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf(".")+1)));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);

            // 指定上传文件操作时是否覆盖同名Object。
            // 不指定x-oss-forbid-overwrite时，默认覆盖同名Object。
            // 指定x-oss-forbid-overwrite为false时，表示允许覆盖同名Object。
            // 指定x-oss-forbid-overwrite为true时，表示禁止覆盖同名Object，如果同名Object已存在，程序将报错。
            objectMetadata.setHeader("x-oss-forbid-overwrite", "false");

            String objectName = filedir + fileName;

            //上传文件
            ossClient.putObject(bucketName, objectName, instream, objectMetadata);
            // 封装  url 路径
            String url = "https://" + bucketName + "." + endpoint + "/" + objectName;
            System.out.println(objectName);
            ret = url;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            ossClient.shutdown();
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    /**
     * 判断文件是否存在。doesObjectExist还有一个参数isOnlyInOSS，
     * 如果为true则忽略302重定向或镜像；如果为false，则考虑302重定向或镜像。
     * yourObjectName 表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
     *
     * @return 存在返回true
     */
    public boolean doesObjectExist(String objectName) {
        boolean exists = ossClient.doesObjectExist(bucketName, filedir + objectName);
        return exists;
    }

    /**
     * 判断Bucket是否存在
     *
     * @return 存在返回true
     */
    public boolean doesBucketExist() {
        boolean exists = ossClient.doesBucketExist(bucketName);
        return exists;
    }

    /**
     * 创建Bucket
     */
    public void createBucket() {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        // 设置bucket权限为公共读，默认是私有读写
        createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
        // 设置bucket存储类型为低频访问类型，默认是标准类型
        createBucketRequest.setStorageClass(StorageClass.IA);
        boolean exists = ossClient.doesBucketExist(bucketName);
        if (!exists) {
            try {
                ossClient.createBucket(createBucketRequest);
                // 关闭client
                ossClient.shutdown();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if ("bmp".equalsIgnoreCase(FilenameExtension)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(FilenameExtension)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(FilenameExtension) ||
                "jpg".equalsIgnoreCase(FilenameExtension) ||
                "png".equalsIgnoreCase(FilenameExtension)) {
            return "image/jpeg";
        }
        if ("html".equalsIgnoreCase(FilenameExtension)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(FilenameExtension)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(FilenameExtension)) {
            return "application/vnd.visio";
        }
        if ("pptx".equalsIgnoreCase(FilenameExtension) ||
                "ppt".equalsIgnoreCase(FilenameExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("docx".equalsIgnoreCase(FilenameExtension) ||
                "doc".equalsIgnoreCase(FilenameExtension)) {
            return "application/msword";
        }
        if ("xml".equalsIgnoreCase(FilenameExtension)) {
            return "text/xml";
        }
        if ("pdf".equalsIgnoreCase(FilenameExtension)) {
            return "application/pdf";
        }
        return "image/jpeg";
    }


    /**
     * @param fileName
     * @return
     * @Title: getInputStreamByFileUrl
     * @Description: 根据文件路径获取InputStream流
     * @return: InputStream
     */
    public InputStream getInputStreamByFileUrl(String fileName) {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, fileName);
        return ossObject.getObjectContent();
    }


    /**
     * @desc 删除文件
     * @date 2022-10-12
     */
    public  void delete(String objectName) {
        // 根据BucketName,objectName删除文件
        ossClient.deleteObject(bucketName, objectName);
        ossClient.shutdown();
    }
}
