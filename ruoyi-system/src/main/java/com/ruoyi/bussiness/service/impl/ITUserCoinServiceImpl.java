package com.ruoyi.bussiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bussiness.domain.TUserCoin;
import com.ruoyi.bussiness.mapper.TUserCoinMapper;
import com.ruoyi.bussiness.service.ITUserCoinService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ITUserCoinServiceImpl extends ServiceImpl<TUserCoinMapper, TUserCoin> implements ITUserCoinService {


    @Resource
    TUserCoinMapper tUserCoinMapper;

    @Override
    public void removeByUserId(Long userId) {
        tUserCoinMapper.removeByUserId(userId);
    }
}
