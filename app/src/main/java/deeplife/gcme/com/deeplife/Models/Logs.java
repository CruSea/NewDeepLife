package deeplife.gcme.com.deeplife.Models;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.SyncService.SyncService;

/**
 * Created by bengeos on 12/23/16.
 */

public class Logs {

    // === Type ===
    public enum TYPE {
        // From apiController.php in Web App:
        DISCIPLE("Disciple"),
        SCHEDULE("Schedule"),
        REMOVE_DISCIPLE("Remove_Disciple"),
        REMOVE_SCHEDULE("Remove_Schedule"),
        NEWS_FEEDS("NewsFeeds"),
        ADD_NEW_ANSWERS("AddNew_Answers"),
        USER("User");


        private final String name;

        private TYPE(String s) {
            this.name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static TYPE fromString(String s) {
            if (s != null) {
                for (TYPE t : TYPE.values()) {
                    if (s.equalsIgnoreCase(t.toString())) {
                        return t;
                    }
                }
            }
            throw new IllegalArgumentException("No constant in <Type> Enum found with text: " + s);
        }
    }

    // === Task ===
    public enum TASK {
        // briggsm: I'm not able to find a list of all the possible different "Tasks". Biniam, can you fine-tune this enum?
        SEND_LOG("Send_Log"),
        SEND_DISCIPLES("Send_Disciples"),
        REMOVE_DISCIPLE("Remove_Disciple"),
        UPDATE_DISCIPLES("Update_Disciples"),
        UPDATE_USER_PROFILE("Update_User_Profile"),
        SEND_SCHEDULE("Send_Schedule"),
        SEND_REPORT("Send_Report"),
        SEND_TESTIMONY("Send_Testimony"),
        UPDATE_SCHEDULES("Update_Schedules"),
        ADD_NEW_DISCIPLES("AddNew_Disciples"),
        SEND_ANSWERS("Send_Answers"),
        ADD_NEW_ANSWERS("AddNew_Answers");


        private final String name;

        private TASK(String s) {
            this.name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static TASK fromString(String s) {
            if (s != null) {
                for (TASK t : TASK.values()) {
                    if (s.equalsIgnoreCase(t.toString())) {
                        return t;
                    }
                }
            }
            throw new IllegalArgumentException("No constant in <Task> Enum found with text: " + s);
        }
    }

    private int ID;
    private String value;

    // Enum's
    private TYPE type;
    private TASK task;
    private SyncService.API_SERVICE Service;

    private List<Object> Param;

    public Logs() {
        Param = new ArrayList<Object>();
        Service = SyncService.API_SERVICE.UPDATE;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public TASK getTask() {
        return task;
    }

    public void setTask(TASK task) {
        this.task = task;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Object> getParam() {
        return Param;
    }

    public void setParam(List<Object> param) {
        Param = param;
    }


    public SyncService.API_SERVICE getService() {
        return Service;
    }

    public void setService(SyncService.API_SERVICE service) {
        Service = service;
    }
}
