package deeplife.gcme.com.deeplife.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by bengeos on 12/23/16.
 */

public class Logs {
    private int ID;
    private String Type,Task,Value,Service;
    private List<Object> Param;
    public static final String[] Sync_Tasks = {"Send_Log", "Send_Disciples","Remove_Disciple","Update_Disciple","Send_Schedule","Send_Report","Send_Testimony","Update_Schedules"};

    public Logs() {
        Param = new ArrayList<Object>();
        Service = "Update";
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTask() {
        return Task;
    }

    public void setTask(String task) {
        Task = task;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public List<Object> getParam() {
        return Param;
    }

    public void setParam(List<Object> param) {
        Param = param;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

}
