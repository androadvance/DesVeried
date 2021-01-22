package com.example.dscveriy;

public class PhotoVerifyList {

    String EmpNo,Name,Photoverify,AadharVerify,photourl,aadharurl,AadharNo;

    public PhotoVerifyList(String empNo, String name, String photoverify, String aadharVerify, String photourl, String aadharurl, String aadharNo) {
        EmpNo = empNo;
        Name = name;
        Photoverify = photoverify;
        AadharVerify = aadharVerify;
        this.photourl = photourl;
        this.aadharurl = aadharurl;
        AadharNo = aadharNo;
    }

    public String getEmpNo() {
        return EmpNo;
    }

    public void setEmpNo(String empNo) {
        EmpNo = empNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhotoverify() {
        return Photoverify;
    }

    public void setPhotoverify(String photoverify) {
        Photoverify = photoverify;
    }

    public String getAadharVerify() {
        return AadharVerify;
    }

    public void setAadharVerify(String aadharVerify) {
        AadharVerify = aadharVerify;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getAadharurl() {
        return aadharurl;
    }

    public void setAadharurl(String aadharurl) {
        this.aadharurl = aadharurl;
    }

    public String getAadharNo() {
        return AadharNo;
    }

    public void setAadharNo(String aadharNo) {
        AadharNo = aadharNo;
    }
}
