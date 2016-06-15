package com.company;

/**
 * Created by otovstiuk on 14.06.2016.
 */
public class Player {

    private  String country;
    private  String name;
    private  String bio;
    private  Boolean photoDone;
    private  String  specialPlayer;
    private  String  position;
    private  Integer number;
    private  Integer caps;
    private  Integer goalsForCountry;
    private  String  club;
    private  String  league;
    private  String  dateOfBirth;
    //        "": "",
    private  Integer ratingMatch1;
    private  Integer ratingMatch2;
    private  Integer ratingMatch3;

    private static final String SQL = "insert into Players (Country, Name, Bio, Photodone, Specialplayer, Position, Num, Caps, Goalsforcountry, Club, League, Dateofbirth, Ratingmatch1, Ratingmatch2, Ratingmatch3) " +
                                      " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    public static String getSQL(){
        return SQL;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Boolean getPhotoDone() {
        return photoDone;
    }

    public void setPhotoDone(Boolean photoDone) {
        this.photoDone = photoDone;
    }

    public String getSpecialPlayer() {
        return specialPlayer;
    }

    public void setSpecialPlayer(String specialPlayer) {
        this.specialPlayer = specialPlayer;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCaps() {
        return caps;
    }

    public void setCaps(Integer caps) {
        this.caps = caps;
    }

    public Integer getGoalsForCountry() {
        return goalsForCountry;
    }

    public void setGoalsForCountry(Integer goalsForCountry) {
        this.goalsForCountry = goalsForCountry;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getRatingMatch1() {
        return ratingMatch1;
    }

    public void setRatingMatch1(Integer ratingMatch1) {
        this.ratingMatch1 = ratingMatch1;
    }

    public Integer getRatingMatch2() {
        return ratingMatch2;
    }

    public void setRatingMatch2(Integer ratingMatch2) {
        this.ratingMatch2 = ratingMatch2;
    }

    public Integer getRatingMatch3() {
        return ratingMatch3;
    }

    public void setRatingMatch3(Integer ratingMatch3) {
        this.ratingMatch3 = ratingMatch3;
    }




}
