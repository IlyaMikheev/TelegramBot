package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.methods.send.SendContact;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by USER7 on 02.06.2017.
 */
public class AskFioCommand extends Command {

    private int dialogCounter;
    public ArrayList<Item> dialog = new ArrayList<Item>();

    public final String startMessage;
    public final String finalMessagePart1;
    public final String finalMessagePart2;

    private class Item {
        String quest;
        String unswer;
        public Item(String quest, String unswer) {
            this.quest = quest;
            this.unswer = unswer;
        }
    }

    public AskFioCommand() {
        dialog.add(new Item("Ваша фамилия?",""));
        dialog.add(new Item("Ваше имя?",""));
        dialog.add(new Item("Ваше отчество?",""));
        dialog.add(new Item("Ваш номер телефона? \n (в формате 7хххххххххх)",""));
        this.dialogCounter = 0;
        this.startMessage = "Привет, я робот."+"\n" +
                "Сейчас я спрошу у вас вашу фамилию, имя, отчество, номер телефона.";
        this.finalMessagePart1 = "Итак, вы ввели следующие данные: \n";
        this.finalMessagePart2 = "Если существует контакт с такими данными, вам будет выслана ссылка на него. \n" +
                "Связаться с разработчиком робота можно по ссылке:  https://vk.com/ilmikheev88";
    }

    public int getDialogCounter(){
        return dialogCounter;
    }

    public String getQuest() {
        String quest = dialog.get(dialogCounter).quest;
        dialogCounter++;
        return quest;
    }

    public String getQuest(int counter) {
        return dialog.get(counter).quest;
    }

    public String getUnswer() {
        return dialog.get(dialogCounter).unswer;
    }

    public String getUnswer(int counter) {
        return dialog.get(counter).unswer;
    }

    public void setUnswer(String unswer){
        dialog.get(dialogCounter - 1).unswer = unswer;
    }

    public int size(){
        return dialog.size();
    }

    public boolean hasNextQuest() {
        return dialogCounter < dialog.size();
    }
    private Message message;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        try {
            message = update.getMessage();
            if (message == null) {
                message = update.getCallbackQuery().getMessage();
            }
            if (dialogCounter == 0) {
                // Задаём первый вопрос и выходим из цикла
                bot.sendMessage( new SendMessage()
                        .setChatId(message.getChatId().toString())
                        .setText(getQuest()));
                return false;
            }
            // Запись ответа
            setUnswer(message.getText());
            // Этот вопрос был последним?
            if (!hasNextQuest()){
                sendFinalMessage(bot, message);
                return true;
            }
            // Раз вопрос был не последним, зададим следующий
            bot.sendMessage( new SendMessage()
                    .setChatId(message.getChatId().toString())
                    .setText(getQuest()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendFinalMessage(Bot bot, Message message) throws  TelegramApiException {
        // final message
        bot.sendMessage( new SendMessage()
                .setChatId(message.getChatId().toString())
                .setText(getFinalMessage()));
        // Сформировать контакт на основании введенных данных
        try {
            bot.sendContact(new SendContact()
                    .setChatId(message.getChatId().toString())
                    .setLastName(getUnswer(1))
                    .setFirstName(getUnswer(0))
                    .setPhoneNumber(getUnswer(3)));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getFinalMessage() {
        String result = finalMessagePart1;
        for (Item item:dialog) {
            result +=item.unswer + "\n";
        }
        result += finalMessagePart2;
        return result;
    }
}
