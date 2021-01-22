package com.example.dscveriy;

public class EMPVerifyList {

    String Machinename,Status,Update,LastCheckdate,Empcode,photopath,unitname,remarks,machineip,userverifieddate;

    public EMPVerifyList(String machinename, String status, String update, String lastCheckdate, String empcode, String photopath, String unitname, String remarks, String machineip, String userverifieddate) {
        Machinename = machinename;
        Status = status;
        Update = update;
        LastCheckdate = lastCheckdate;
        Empcode = empcode;
        this.photopath = photopath;
        this.unitname = unitname;
        this.remarks = remarks;
        this.machineip = machineip;
        this.userverifieddate = userverifieddate;
    }

    public String getMachinename() {
        return Machinename;
    }

    public void setMachinename(String machinename) {
        Machinename = machinename;
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

    public String getLastCheckdate() {
        return LastCheckdate;
    }

    public void setLastCheckdate(String lastCheckdate) {
        LastCheckdate = lastCheckdate;
    }

    public String getEmpcode() {
        return Empcode;
    }

    public void setEmpcode(String empcode) {
        Empcode = empcode;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMachineip() {
        return machineip;
    }

    public void setMachineip(String machineip) {
        this.machineip = machineip;
    }

    public String getUserverifieddate() {
        return userverifieddate;
    }

    public void setUserverifieddate(String userverifieddate) {
        this.userverifieddate = userverifieddate;
    }
}
