package deeplife.gcme.com.deeplife.Models;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.SyncService.SyncService;

/**
 * Created by bengeos on 12/23/16.
 */

public class Logs {

    // === Type ===
    public enum Type {
        SEND_LOG ("Send_Log"),
        SEND_DISCIPLES ("Send_Disciples"),
        REMOVE_DISCIPLE ("Remove_Disciple"),
        UPDATE_DISCIPLE ("Update_Disciple"),
        SEND_SCHEDULE ("Send_Schedule"),
        SEND_REPORT ("Send_Report"),
        SEND_TESTIMONY ("Send_Testimony"),
        UPDATE_SCHEDULES ("Update_Schedules"),
        ADD_NEW_DISCIPLES ("AddNew_Disciples"),
        UPDATE_DISCIPLES ("Update_Disciples"),
        SEND_ANSWERS ("Send_Answers"),
        ADD_NEW_ANSWERS ("AddNew_Answers"),

        DISCIPLE ("Disciple");


        private final String name;
        private Type(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
        public static Type fromString(String s) {
            if (s != null) {
                for (Type t : Type.values()) {
                    if (s.equalsIgnoreCase(t.toString())) {
                        return t;
                    }
                }
            }
            //return null;
            throw new IllegalArgumentException("No constant in Enum found with text: " + s);
        }
    }

    // === Task ===
    public enum Task {
        SEND_LOG("Send_Log"),
        SEND_DISCIPLES("Send_Disciples"),
        REMOVE_DISCIPLE("Remove_Disciple"),
        UPDATE_DISCIPLE("Update_Disciple"),
        SEND_SCHEDULE("Send_Schedule"),
        SEND_REPORT("Send_Report"),
        SEND_TESTIMONY("Send_Testimony"),
        UPDATE_SCHEDULES("Update_Schedules"),
        ADD_NEW_DISCIPLES("AddNew_Disciples"),
        UPDATE_DISCIPLES("Update_Disciples"),
        SEND_ANSWERS("Send_Answers"),
        ADD_NEW_ANSWERS("AddNew_Answers");


        private final String name;
        private Task(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
        public static Task fromString(String s) {
            if (s != null) {
                for (Task t : Task.values()) {
                    if (s.equalsIgnoreCase(t.toString())) {
                        return t;
                    }
                }
            }
            //return null;
            throw new IllegalArgumentException("No constant in Enum found with text: " + s);
        }
    }

    private int ID;
//    private String type,task,value,Service;
//    private String type,task,value;
    private String value;

    // Enum's
    private Type type;
    private Task task;
    private SyncService.ApiService Service;

    private List<Object> Param;

    public Logs() {
        Param = new ArrayList<Object>();
        Service = SyncService.ApiService.UPDATE;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
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


    public SyncService.ApiService getService() {
        return Service;
    }

    public void setService(SyncService.ApiService service) {
        Service = service;
    }
}
