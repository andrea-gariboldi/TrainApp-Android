package com.example.maledettatreest;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
@Entity
public class UserEntity {
    @PrimaryKey @NonNull
    private String uid;

    @ColumnInfo(name = "sid")
    private String sid;

    @ColumnInfo(name = "pversion")
    private String pversion;

    @ColumnInfo(name = "upicture")
    private String upicture;

    @ColumnInfo(name = "uname")
    private String uname;

    public String getUid() {
        return uid;
    }

    public String getSid() {
        return sid;
    }

    public String getPversion() {
        return pversion;
    }

    public String getUpicture() {
        return upicture;
    }

    public String getUname() {
        return uname;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setPversion(String pversion) {
        this.pversion = pversion;
    }

    public void setUpicture(String upicture) {
        this.upicture = upicture;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public UserEntity(User user, String sid) {
        this.uid = user.uid;
        this.sid = sid;
        this.pversion = user.pversion;
        this.upicture = user.upicture;
        this.uname = user.uname;
    }


    public UserEntity() {
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "uid='" + uid + '\'' +
                ", sid='" + sid + '\'' +
                ", pversion='" + pversion + '\'' +
                ", upicture='" + upicture + '\'' +
                ", uname='" + uname + '\'' +
                '}';
    }
}
