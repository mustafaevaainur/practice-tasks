package com.ifmo.telegram.bot;

import lombok.SneakyThrows;
import org.alicebot.ab.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;

public class TelegramBot extends TelegramLongPollingBot {

    private static final boolean TRACE_MODE = false;
    private static final String botName = "AnnsBot";

    @SneakyThrows
    public static void main(String[] args) throws TelegramApiException {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        botapi.registerBot(new TelegramBot() {
        });
    }

    @Deprecated
    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        String textLine = msg.getText();
        try {
            String resourcesPath = getResourcesPath();
            System.out.println(resourcesPath);
            MagicBooleans.trace_mode = TRACE_MODE;
            Bot bot = new Bot("AnnsBot", resourcesPath);
            Chat chatSession = new Chat(bot);
            bot.brain.nodeStats();

            if (update.hasMessage() && update.getMessage().hasText()) {
                System.out.print(msg.getChat().getFirstName() + ": " + textLine + "\n");
                if ((textLine == null) || (textLine.length() < 1)) {
                    textLine = MagicStrings.null_input;
                } else {
                    String request = textLine;
                    if (MagicBooleans.trace_mode)
                        System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                    String response = chatSession.multisentenceRespond(request);
                    while (response.contains("&lt;"))
                        response = response.replace("&lt;", "<");
                    while (response.contains("&gt;"))
                        response = response.replace("&gt;", ">");
                    sendMsg(msg, response);
                    System.out.println("Robot: " + response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Deprecated
    private void sendMsg(Message msg, String text) throws TelegramApiException {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId());
        s.setText(text);
        sendMessage(s);

    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public String getBotToken() {
        return "545246784:AAG-hd3arb6_qInExqqqlmDlBGX-8E_dcLU";
    }

    private String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath;
    }
}

