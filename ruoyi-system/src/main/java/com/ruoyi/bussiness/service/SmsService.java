package com.ruoyi.bussiness.service;

import com.ruoyi.bussiness.domain.SendPhoneDto;

public interface SmsService {
    String sendMobileCode(SendPhoneDto mobileNumber);
}
