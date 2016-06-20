package com.company;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by otovstiuk on 14.06.2016.
 */
public class Player {

    //private enum Positions {Goalkeeper, Defender, Midfielder, Forward};

    private  String country;
    private  String name;
    private  String bio;
    private  Boolean photoDone;
    private  String  specialPlayer;
    private  String  position;
    private  String number;
    private  Integer caps;
    private  Integer goalsForCountry;
    private  String  club;
    private  String  league;
    private  Date    dateOfBirth;
    //        "": "",
    private  String ratingMatch1;
    private  String ratingMatch2;
    private  String ratingMatch3;

    private static final String SQL = "insert into euro2016 (Country, Name, Bio, Photodone, Specialplayer, Position, Num, Caps, Goalsforcountry, Club, League, Dateofbirth, Ratingmatch1, Ratingmatch2, Ratingmatch3) " +
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

    public String getPhotoDone() {
        return String.valueOf(photoDone);
    }

    public void setPhotoDone(Boolean photoDone) {
        this.photoDone = photoDone;
    }

    public void setPhotoDone(String photoDone) {
        this.photoDone = photoDone.equalsIgnoreCase("true") ||
                         photoDone.equalsIgnoreCase("yes") ||
                         photoDone.equalsIgnoreCase("y");
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRatingMatch1() {
        return ratingMatch1;
    }

    public void setRatingMatch1(String ratingMatch1) {
        this.ratingMatch1 = ratingMatch1;
    }

    public String getRatingMatch2() {
        return ratingMatch2;
    }

    public void setRatingMatch2(String ratingMatch2) {
        this.ratingMatch2 = ratingMatch2;
    }

    public String getRatingMatch3() {
        return ratingMatch3;
    }

    public void setRatingMatch3(String ratingMatch3) {
        this.ratingMatch3 = ratingMatch3;
    }

    public Player (String country, JSONObject obj) throws JSONException, ParseException {
        super();

        setCountry(country);
        setName(obj.getString("name"));
        setBio(obj.getString("bio"));
        setPhotoDone(obj.getString("photo done?"));
        setSpecialPlayer(obj.getString("special player? (eg. key player, promising talent, etc)"));
        setPosition(obj.getString("position"));
        setNumber(obj.getString("number"));
        setCaps(obj.getInt("caps"));
        setGoalsForCountry(obj.getInt("goals for country"));
        setClub(obj.getString("club"));
        setLeague(obj.getString("league"));
        setDateOfBirth(Main.dtformatter.parse(obj.getString("date of birth")));
        setRatingMatch1(obj.getString("rating_match1"));
        setRatingMatch2(obj.getString("rating_match2"));
        setRatingMatch3(obj.getString("rating_match3"));

    }


    @Override
    public String toString(){

        String separator = "|";
        return
        new StringBuilder()
                .append(getCountry()).append(separator)
                .append(getName()).append(separator)
                //.append(getBio()).append(separator)
                .append(getPhotoDone()).append(separator)
                .append(getSpecialPlayer()).append(separator)
                .append(getPosition()).append(separator)
                .append(getNumber()).append(separator)
                .append(getCaps()).append(separator)
                .append(getGoalsForCountry()).append(separator)
                .append(getClub()).append(separator)
                .append(getLeague()).append(separator)
                .append(getDateOfBirth()).append(separator)
                .append(getRatingMatch1()).append(separator)
                .append(getRatingMatch2()).append(separator)
                .append(getRatingMatch3()).append(separator)
                .toString();
    }

    public static Comparator<Player> compareByAge = new Comparator<Player>() {
        public int compare(Player player1, Player player2) {
            return player1.getDateOfBirth().compareTo(player2.getDateOfBirth());
        }
    };

}
