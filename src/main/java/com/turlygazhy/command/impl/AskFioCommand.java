package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.WaitingType;
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

    private WaitingType waitingType;
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

    public void setUnswer(int counter, String unswer) {  dialog.get(counter).unswer = unswer; }

    public int size(){
        return dialog.size();
    }

    public boolean hasNextQuest() {
        return dialogCounter < dialog.size();
    }
    private Message message;
    /*

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
*/
    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {

        message = update.getMessage();
        if (message == null) {
            message = update.getCallbackQuery().getMessage();
        }
        Long chatId = message.getChatId();
        // Бот получает первое сообщение "Ввод персональнх данных" Запоминать еще нечего, witingType == null;
        if (waitingType == null) {
            sendMessage("Ваша фамилия?", chatId, bot); // Задаём первый вопрос - запрашиваем Фамилию
            waitingType = WaitingType.GOAL_NAME;     // Пусть GOAL_NAME соответствует второму шагу (первому в switch)
            return false;
        }

        switch (waitingType) {
            case GOAL_NAME:
                setUnswer(0, message.getText()); // Запись фамилии
                sendMessage("Ваше имя?", chatId, bot); // Запрос имени
                waitingType = WaitingType.GOAL_NEW_NAME; // Опять же знаяение WaitingType выбрано случайно
                return false;
            case GOAL_NEW_NAME:
                setUnswer(1, message.getText()); // Запись имени
                sendMessage("Ваше отчество?", chatId, bot); // Запрос отчества
                waitingType = WaitingType.NAVIKI;
                return false;
            case NAVIKI:
                setUnswer(2, message.getText()); // Запись отчества
                sendMessage("Ваш номер телефона?", chatId, bot); // Запрос номера телефона
                waitingType = WaitingType.CONTACT;
                return false;
            case CONTACT:
                setUnswer(3, message.getText()); // Запись номера телефона
                sendFinalMessage(bot, message);
                return true;
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
