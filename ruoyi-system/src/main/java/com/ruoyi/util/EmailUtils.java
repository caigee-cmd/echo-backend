package com.ruoyi.util;

import cn.hutool.json.JSONUtil;
import com.ruoyi.bussiness.domain.setting.EmailSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.bussiness.service.impl.TAppUserServiceImpl;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.enums.UserCodeTypeEnum;
import com.ruoyi.common.utils.SpringContextUtil;
import com.ruoyi.common.utils.sms.SmsSenderUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @packageName:com.zebra.common.utils
 * @className:EmailUtils
 * @description:邮箱工具类
 * @version:1.0.0
 * @author:xuanyihun
 * @createDate:2022-05-06 11:07
 */
public class EmailUtils {


    /**
     * @description 验证邮箱
     * @param email(邮箱)
     * @version 1.0.0
     * @author xuanyihun
     * @createDate 2022-05-06 11:23
     * @return boolean
    */
    public static boolean checkEmail(String email) {

        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception exception) {
            flag = false;
        }
        return flag;

    }
    /**
     * 组成邮箱发送公共方法
     * @return
     */
//    public static void formMail(String email,String type) {
//        RedisCache redisCache = SpringContextUtil.getBean(RedisCache.class);
//        SettingService settingService = SpringContextUtil.getBean(SettingService.class);
//        TAppUserServiceImpl bean = SpringContextUtil.getBean(TAppUserServiceImpl.class);
//        String randomCode = String.valueOf(SmsSenderUtil.getRandomNumber(100000, 999999));
//        Setting setting = settingService.get(SettingEnum.EMAIL_SETTING.name());
//        EmailSetting emailSetting = JSONUtil.toBean(setting.getSettingValue(), EmailSetting.class);
//        String appName = emailSetting.getMailAppName();
//        String host = emailSetting.getMailHost();
//        int port = Integer.parseInt(emailSetting.getMailPort());
//        String username = emailSetting.getMailUsername();
//        String password = emailSetting.getMailPassword();
//        String templateCode = emailSetting.getMailTemplate();
//        JavaMailSenderImpl jms = new JavaMailSenderImpl();
//        jms.setHost(host);
//        jms.setPort(port);
//        jms.setUsername(username);
//        jms.setPassword(password);
//        jms.setDefaultEncoding("Utf-8");
//        Properties p = new Properties();
//        p.setProperty("mail.smtp.auth", "true");
//        p.setProperty("mail.smtp.starttls.enable", "true");
//        p.setProperty("mail.smtp.starttls.required", "true");
//        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        p.setProperty("mail.smtp.ssl.enable", "true");
//        jms.setJavaMailProperties(p);
//        MimeMessage mimeMessage = jms.createMimeMessage();
//        MimeMessageHelper helper = null;
//        try {
//            helper = new MimeMessageHelper(mimeMessage, true);
//            helper.setFrom(username);
//            helper.setTo(email);
//            helper.setSubject(appName);
//            Map<String, Object> model = new HashMap<>(16);
//            model.put("code", randomCode);
//            Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
//            cfg.setClassForTemplateLoading(bean.getClass(), "/templates");
//            Template template = cfg.getTemplate(templateCode);
//            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//            helper.setText(html, true);
//            //发送邮件
//            jms.send(mimeMessage);
//            //绑定邮箱
//        } catch (Exception  e) {
//            e.printStackTrace();
//        }
//        redisCache.setCacheObject(CachePrefix.EMAIL_CODE.getPrefix()+ UserCodeTypeEnum.valueOf(type)+ email, randomCode, CacheConstants.REGISTER_CODE_TIME, TimeUnit.SECONDS);
//
//    }


    public static void formMail(String email,String type) {
        RedisCache redisCache = SpringContextUtil.getBean(RedisCache.class);
        SettingService settingService = SpringContextUtil.getBean(SettingService.class);
        String randomCode = String.valueOf(SmsSenderUtil.getRandomNumber(100000, 999999));
        TAppUserServiceImpl bean = SpringContextUtil.getBean(TAppUserServiceImpl.class);
        Setting setting = settingService.get(SettingEnum.EMAIL_SETTING.name());
        EmailSetting emailSetting = JSONUtil.toBean(setting.getSettingValue(), EmailSetting.class);
        String appName = emailSetting.getMailAppName();
        String host = emailSetting.getMailHost();
        String port = emailSetting.getMailPort();
        String username = emailSetting.getMailUsername();
        String password = emailSetting.getMailPassword();
        String templateCode = emailSetting.getMailTemplate();
        // 1.创建连接对象javax.mail.Session
        // 2.创建邮件对象 javax.mail.Message
        // 3.发送一封激活邮件
        Properties properties = System.getProperties();// 获取系统属性

        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.transport.protocol","smtp");
        properties.setProperty("mail.smtp.port", port);  //不需要 默认端口
        properties.setProperty("mail.smtp.auth", "true");// 打开认证
        //  properties.put("mail.smtp.socketFactory", "javax.net.ssl.SSLSocketFactory");
        try {
            // QQ邮箱需要下面这段代码，163邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2"); //加上这句解决问题

            // 1.获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            session.setDebug(true);

            //2、通过session得到transport对象
            Transport transport = session.getTransport();
            //3、使用用户名和授权码连上邮件服务器
            transport.connect(host,username,password);


            // 4.创建邮件对象
            Message message = new MimeMessage(session);
            // 4.1设置发件人
            message.setFrom(new InternetAddress(username));
            // 4.2设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 4.3设置邮件主题
            String title = appName;
            message.setSubject(title);

            Map<String, Object> model = new HashMap<>(16);
            model.put("code", randomCode);
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
            cfg.setClassForTemplateLoading(bean.getClass(), "/templates");
            Template template = cfg.getTemplate(templateCode);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            // 2.4设置邮件内容
            message.setContent(html, "text/html;charset=UTF-8");
            //5、发送邮件
            transport.sendMessage(message,message.getAllRecipients());
            //6、关闭连接
            transport.close();
            System.out.println("邮件成功发送!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //绑定邮箱
        redisCache.setCacheObject(CachePrefix.EMAIL_CODE.getPrefix()+ UserCodeTypeEnum.valueOf(type)+ email, randomCode, CacheConstants.REGISTER_CODE_TIME, TimeUnit.SECONDS);

    }
}
