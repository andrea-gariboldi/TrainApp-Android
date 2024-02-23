package com.example.maledettatreest;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface userDao {

    @Query("SELECT COUNT(*) FROM userentity")
    int getCount();

    @Query("SELECT * FROM userentity")
    List<UserEntity> StampaContenutoDatabase();

    @Query("SELECT uname FROM userentity WHERE sid !='' ")
    String restituisciUname();

    @Query("SELECT sid FROM userentity WHERE sid !='' ")
    String restituisciSid();

    @Query("UPDATE userentity SET uname = :uname WHERE sid = :sid")
    void inserisciUname(String uname, String sid);

    @Query("UPDATE userentity SET upicture = :upicture WHERE sid = :sid")
    void inserisciUPicture(String upicture, String sid);

    @Query("SELECT upicture FROM userentity WHERE sid = :sid")
    String restituisciUPicture( String sid);

    @Query("SELECT pversion FROM userentity WHERE uid = :uid")
    String restituisciPversion( String uid);

    @Query("SELECT upicture FROM userentity WHERE uid = :uid")
    String restituisciUPictureByUid( String uid);

    @Query("UPDATE userentity  SET upicture = :upicture , pversion = :pversion WHERE uid = :uid")
    void inserisciUPicturePversionByUid(String uid, String upicture, String pversion);

    @Query("SELECT COUNT(*) FROM userentity WHERE uid = :uid")
    int restituisciUid(String uid);

    @Insert
    void insertUser(UserEntity newUser);


    //GET-UPICTURE
    //GET-SID
    // GET-UID
    // GET-PVERSION
}
