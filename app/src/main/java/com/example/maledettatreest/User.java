package com.example.maledettatreest;

public class User {
   String  uname;
   String uid;
   String pversion;
   String upicture;

   public User(String uname, String uid, String pversion, String upicture) {
      this.uname = uname;
      this.uid = uid;
      this.pversion = pversion;
      this.upicture = upicture;
   }

   @Override
   public String toString() {
      return "User{" +
              "uname='" + uname + '\'' +
              ", uid='" + uid + '\'' +
              ", pversion=" + pversion +
              ", upicture='" + upicture + '\'' +
              '}';
   }
}
