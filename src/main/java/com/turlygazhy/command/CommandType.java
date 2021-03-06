package com.turlygazhy.command;

import com.turlygazhy.exception.NotRealizedMethodException;

/**
 * Created by user on 1/1/17.
 */
public enum CommandType {
    SHOW_INFO(1),
    CHANGE_INFO(2),
    ADD_TO_LIST(3),
    DELETE_FROM_LIST(4),
    SHOW_ALL_LIST(5),
    INFORM_ADMIN(6),
    REQUEST_CALL(7),
    PUT_TEXT_INSTEAD_BUTTON(8),
    COLLECT_INFO_COMMAND(9),
    SHOW_INFO_ABOUT_MEMBER(10),
    CHANGE_NISHA(11),
    CHANGE_NAVIKI(12),
    KEY_WORDS(13),
    SEARCH(14),
    RESERVATION(15),
    ADD_PLAN(16),
    SHARE_ACHIEVEMENTS(17),
    READING(18),
    ASK_ACCESS(19),
    SHOW_GOAL(20),
    ADD_GOAL(21),
    SHOW_ALL_GOALS(22),
    SHOW_THESIS(23),
    SHOW_ALL_THESIS(24),
    SHOW_CHART(25),
    CHANGE_GOAL(26),
    ORDER_CAKE(27),
    ASK_FIO(28);

    private final int id;

    CommandType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static CommandType getType(long id) {
        for (CommandType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new NotRealizedMethodException("There are no type for id: " + id);
    }
}
