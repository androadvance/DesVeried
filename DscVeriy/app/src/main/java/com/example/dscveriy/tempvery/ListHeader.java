package com.example.dscveriy;

public class ListHeader {

    String MachineName,Status,Update,LastcheckDate;

    public ListHeader(String machineName, String status, String update, String lastcheckDate) {
        MachineName = machineName;
        Status = status;
        Update = update;
        LastcheckDate = lastcheckDate;
    }

    public String getMachineName() {
        return MachineName;
    }

    public void setMachineName(String machineName) {
        MachineName = machineName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUpdate() {
        return Update;
    }

    public void setUpdate(String update) {
        Update = update;
    }

    public String getLastcheckDate() {
        return LastcheckDate;
    }

    public void setLastcheckDate(String lastcheckDate) {
        LastcheckDate = lastcheckDate;
    }
}
