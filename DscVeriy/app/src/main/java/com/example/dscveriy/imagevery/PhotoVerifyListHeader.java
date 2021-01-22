package com.example.dscveriy;

public class PhotoVerifyListHeader {

    String EmpNo,Name,PhotoVerifyStatus,AdharverifyStatus;

    public PhotoVerifyListHeader(String empNo, String name, String photoVerifyStatus, String adharverifyStatus) {
        EmpNo = empNo;
        Name = name;
        PhotoVerifyStatus = photoVerifyStatus;
        AdharverifyStatus = adharverifyStatus;
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

    public String getPhotoVerifyStatus() {
        return PhotoVerifyStatus;
    }

    public void setPhotoVerifyStatus(String photoVerifyStatus) {
        PhotoVerifyStatus = photoVerifyStatus;
    }

    public String getAdharverifyStatus() {
        return AdharverifyStatus;
    }

    public void setAdharverifyStatus(String adharverifyStatus) {
        AdharverifyStatus = adharverifyStatus;
    }
}
