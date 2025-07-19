package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bussiness.domain.TUserCoin;

public interface ITUserCoinService extends IService<TUserCoin> {

    void removeByUserId(Long userId);

}
