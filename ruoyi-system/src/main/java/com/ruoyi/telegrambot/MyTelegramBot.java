package com.ruoyi.telegrambot;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.ruoyi.bussiness.domain.setting.MarketUrlSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.domain.setting.TgBotSetting;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private String userName;
    private String token;
    private String charId;
    private boolean hasCharId;

    @Resource
    private SettingService settingService;



    public void initMyTelegramBot(){
        this.hasCharId=false;
        Setting setting = settingService.get(SettingEnum.TG_BOT_SETTING.name());
        if(null != setting){
            TgBotSetting tgBotSetting = JSONUtil.toBean(setting.getSettingValue(), TgBotSetting.class);
            this.userName=tgBotSetting.getBotName();
            this.token=tgBotSetting.getBotToken();
            if(StringUtils.isNotEmpty(tgBotSetting.getChatId())){
                this.charId=tgBotSetting.getChatId();
                hasCharId=true;
            }

        }
        log.debug("初始化MyTelegramBot==========：{}",this);
    }


    @Override
    public void onUpdateReceived(Update update) {
        Chat chat = update.getMessage().getChat();
        if(!hasCharId && null==charId &&  null != chat){
            charId = chat.getId()+"";
            Setting setting = settingService.get(SettingEnum.TG_BOT_SETTING.name());
            TgBotSetting tgBotSetting = JSONUtil.toBean(setting.getSettingValue(), TgBotSetting.class);
            tgBotSetting.setChatId(charId);
            setting.setSettingValue(JSONUtil.toJsonStr(tgBotSetting));
            settingService.saveUpdate(setting);
        }
        log.info("update{}:",update);
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }



    public void toSend(SendMessage sendMessage) {
        try {
            if(null!=sendMessage.getText()&&!sendMessage.getText().equals("")){
                sendMessage.setChatId(charId);
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
           log.debug(e.getMessage());
        }
    }
}
