package com.ruoyi.bussiness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.bussiness.domain.TUserCoin;

public interface TUserCoinMapper extends BaseMapper<TUserCoin> {
     void removeByUserId(Long userId);
}
