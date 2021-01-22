package com.example.dscveriy;

public class WorkStationList {

    String unitlineno,bkworkstation,breakdowntype,date,issueraisedby,search,sno,workcompleted,qcverify,isresponse;

    public WorkStationList(String unitlineno, String bkworkstation, String breakdowntype, String date, String issueraisedby, String search, String sno, String workcompleted, String qcverify, String isresponse) {
        this.unitlineno = unitlineno;
        this.bkworkstation = bkworkstation;
        this.breakdowntype = breakdowntype;
        this.date = date;
        this.issueraisedby = issueraisedby;
        this.search = search;
        this.sno = sno;
        this.workcompleted = workcompleted;
        this.qcverify = qcverify;
        this.isresponse = isresponse;
    }

    public String getUnitlineno() {
        return unitlineno;
    }

    public void setUnitlineno(String unitlineno) {
        this.unitlineno = unitlineno;
    }

    public String getBkworkstation() {
        return bkworkstation;
    }

    public void setBkworkstation(String bkworkstation) {
        this.bkworkstation = bkworkstation;
    }

    public String getBreakdowntype() {
        return breakdowntype;
    }

    public void setBreakdowntype(String breakdowntype) {
        this.breakdowntype = breakdowntype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIssueraisedby() {
        return issueraisedby;
    }

    public void setIssueraisedby(String issueraisedby) {
        this.issueraisedby = issueraisedby;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getWorkcompleted() {
        return workcompleted;
    }

    public void setWorkcompleted(String workcompleted) {
        this.workcompleted = workcompleted;
    }

    public String getQcverify() {
        return qcverify;
    }

    public void setQcverify(String qcverify) {
        this.qcverify = qcverify;
    }

    public String getIsresponse() {
        return isresponse;
    }

    public void setIsresponse(String isresponse) {
        this.isresponse = isresponse;
    }
}
