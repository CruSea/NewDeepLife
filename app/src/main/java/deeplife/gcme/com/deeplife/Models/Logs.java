package deeplife.gcme.com.deeplife.Models;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.SyncService.SyncService;

/**
 * Created by bengeos on 12/23/16.
 */

public class Logs {
    private int ID;
    //private String Type,Task,Value,Service;
    private String Type,Task,Value;
    private SyncService.SyncServ Service;
    private List<Object> Param;

    public Logs() {
        Param = new ArrayList<Object>();
        //Service = SendParam.Service.UPDATE;
        Service = SyncService.SyncServ.UPDATE;
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

    public SyncService.SyncServ getService() {
        return Service;
    }

    public void setService(SyncService.SyncServ service) {
        Service = service;
    }

}
