package com.ruoyi.telegrambot;

import com.ruoyi.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@ConditionalOnClass(TelegramBotsApi.class)
@RequiredArgsConstructor
public class TelegramBotConfig{

    private List<BotSession> sessions = new ArrayList<>();

    private final List<TelegramLongPollingBot> pollingBots = new ArrayList<>();


    @Resource
    private MyTelegramBot myTelegramBot;


    public void start() throws TelegramApiException {
        stop();
        myTelegramBot.initMyTelegramBot();
        log.info("Starting auto config for telegram bots");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        pollingBots.clear();
        pollingBots.add(myTelegramBot);
        pollingBots.forEach(bot -> {
            try {
                log.info("Registering polling bot: {}", bot.getBotUsername());
                sessions.add(telegramBotsApi.registerBot(bot));
            } catch (TelegramApiException e) {
                log.error("Failed to register bot {} due to error", bot.getBotUsername(), e);
            }
        });
    }

    public void stop() {
        log.info("STOP ===============:{}",sessions);
        sessions.forEach(session -> {
            if (session != null && session.isRunning()) {
                session.stop();
            }
        });
        sessions.clear();
    }

}
