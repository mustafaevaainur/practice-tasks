
import lombok.SneakyThrows;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.util.Random;


public class Bot extends TelegramLongPollingBot{
    @SneakyThrows
    public static void main(String[] args) throws TelegramApiException {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        botapi.registerBot(new Bot() {});
    }
    @Override
    public String getBotUsername() {
        return "AnnsBot";
    }

    @Override
    public void onUpdateReceived(Update e) {

        Message msg = e.getMessage();
        String txt = msg.getText();

        // Тут будет то, что выполняется при получении сообщения
        if (txt.equals("/start")) {
            sendMsg(msg, "Привет, " + msg.getChat().getFirstName() + ")");
        } else {
            String[] answerArray = {"Я не знаю, что ответить",
                    "Ты уверен, что я знаю ответ?",
                    "Давай лучше помолчим, я люблю тишину"};
            sendMsg(msg, answerArray[new Random().nextInt(answerArray.length)]);
        }
    }


    @Override
    public String getBotToken() {
        return "545246784:AAG-hd3arb6_qInExqqqlmDlBGX-8E_dcLU";
    }

    @SuppressWarnings("deprecation")
    private void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId());
        s.setText(text);
        try {
            sendMessage(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

}
