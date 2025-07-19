package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.domain.setting.ThirdPaySetting;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * 配置业务层
 *
 * @author Chopper
 * @since 2020/11/17 3:46 下午
 */

public interface SettingService extends IService<Setting> {

    /**
     * 通过key获取
     *
     * @param key
     * @return
     */
    Setting get(String key);

    /**
     * 修改
     *
     * @param setting
     * @return
     */
    boolean saveUpdate(Setting setting);

    ThirdPaySetting getThirdPaySetting(String code);

}