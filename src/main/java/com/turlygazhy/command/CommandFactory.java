package com.turlygazhy.command;

import com.turlygazhy.command.impl.*;
import com.turlygazhy.exception.NotRealizedMethodException;

/**
 * Created by user on 1/2/17.
 */
public class CommandFactory {
    public static Command getCommand(long id) {
        CommandType type = CommandType.getType(id);
        switch (type) {
            case ORDER_CAKE:
                return new OrderCakeCommand();
            case CHANGE_GOAL:
                return new ChangeGoalCommand();
            case SHOW_CHART:
                return new ShowChartCommand();
            case SHOW_ALL_THESIS:
                return new ShowAllThesisCommand();
            case SHOW_THESIS:
                return new ShowThesisCommand();
            case SHOW_ALL_GOALS:
                return new ShowAllGoalsCommand();
            case SHOW_GOAL:
                return new ShowGoalCommand();
            case ADD_GOAL:
                return new AddGoalCommand();
            case ASK_ACCESS:
                return new AskAccessCommand();
            case READING:
                return new ReadingCommand();
            case SHARE_ACHIEVEMENTS:
                return new ShareAchievmentsCommand();
            case ADD_PLAN:
                return new AddPlanCommand();
            case RESERVATION:
                return new ReservationCommand();
            case SHOW_INFO:
                return new ShowInfoCommand();
            case CHANGE_INFO:
                return new ChangeInfoCommand();
            case ADD_TO_LIST:
                return new AddToListCommand();
            case SHOW_ALL_LIST:
                return new ShowAllListCommand();
            case DELETE_FROM_LIST:
                return new DeleteFromListCommand();
            case INFORM_ADMIN:
                return new InformAdminCommand();
            case REQUEST_CALL:
                return new RequestCallCommand();
            case PUT_TEXT_INSTEAD_BUTTON:
                return new PutTextInsteadButtonCommand();
            case COLLECT_INFO_COMMAND:
                return new CollectInfoCommand();
            case SHOW_INFO_ABOUT_MEMBER:
                return new ShowInfoAboutMemberCommand();
            case CHANGE_NISHA:
                return new ChangeNishaCommand();
            case CHANGE_NAVIKI:
                return new ChangeNavikiCommand();
            case KEY_WORDS:
                return new KeyWordsCommand();
            case SEARCH:
                return new SearchCommand();
            case ASK_FIO:
                return new AskFioCommand();
            default:
                throw new NotRealizedMethodException("Not realized for type: " + type);
        }
    }
}
