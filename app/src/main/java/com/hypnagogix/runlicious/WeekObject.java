package com.hypnagogix.runlicious;

import android.util.Log;

import java.util.Calendar;

public class WeekObject {

    int fullWeekDistance = 0;
    String fullWeekDates = "";
    String weeksFirstDate ="";
    String weeksLastDate = "";
    int fullWeekCount = 0;
    int fullWeekDuration_In_Minutes = 0;
    int weekNumber= 0;
    int weekYear = 0;
    int fullWeekCalories = 0;
    int weeksMonth = 0;


    public WeekObject(int weekNumber) {
        this.fullWeekDistance = fullWeekDistance;
        this.fullWeekDates = fullWeekDates;
        this.fullWeekCount = fullWeekCount;
        this.fullWeekDuration_In_Minutes = fullWeekDuration_In_Minutes;
        this.weekNumber =weekNumber;
this.weeksMonth = weeksMonth;
        this.fullWeekCalories = fullWeekCalories;

        //breaking the weekNumber into the year and week itself
        int weekNumberWithoutYear = weekNumber%100;

        //setting the year
        this.weekYear = weekNumber/100;;

        Calendar thisCal = Calendar.getInstance();
        thisCal.set(Calendar.WEEK_OF_YEAR, weekNumberWithoutYear );
        thisCal.set(Calendar.YEAR,  this.weekYear );



        thisCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String firstDateLongDescription = thisCal.getTime().toString();

        String[] firstDateValues = firstDateLongDescription.split(" ");
        String monthName1 = firstDateValues[1];
        int dayNumber = Integer.parseInt(firstDateValues[2]);
        this.weeksFirstDate =  dayNumber+"/"+monthName1+"/"+weekYear;


        thisCal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String lastDateLongDescription = thisCal.getTime().toString();
        String[] lastDateValues = lastDateLongDescription.split(" ");
        String monthName2 = lastDateValues[1];
        int dayNumber2 = Integer.parseInt(lastDateValues[2]);
        this.weeksLastDate =  dayNumber2+"/"+monthName2+"/"+weekYear;


    }

    public String getWeeksFirstDate() {
        return weeksFirstDate;
    }

    public void setWeeksFirstDate(String weeksFirstDate) {
        this.weeksFirstDate = weeksFirstDate;
    }

    public String getWeeksLastDate() {
        return weeksLastDate;
    }

    public void setWeeksLastDate(String weeksLastDate) {
        this.weeksLastDate = weeksLastDate;
    }

    public int getFullWeekCalories() {
        return fullWeekCalories;
    }

    public void setFullWeekCalories(int fullWeekCalories) {
        this.fullWeekCalories = fullWeekCalories;
    }

    public int getWeekYear() {
        return weekYear;
    }

    public void setWeekYear(int weekYear) {
        this.weekYear = weekYear;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public int getFullWeekDistance() {
        return fullWeekDistance;
    }

    public void setFullWeekDistance(int fullWeekDistance) {
        this.fullWeekDistance = fullWeekDistance;
    }

    public String getFullWeekDates() {
        return fullWeekDates;
    }

    public void setFullWeekDates(String fullWeekDates) {
        this.fullWeekDates = fullWeekDates;
    }

    public int getFullWeekCount() {
        return fullWeekCount;
    }

    public void setFullWeekCount(int fullWeekCount) {
        this.fullWeekCount = fullWeekCount;
    }

    public int getFullWeekDuration_In_Minutes() {
        return fullWeekDuration_In_Minutes;
    }

    public void setFullWeekDuration_In_Minutes(int fullWeekDuration_In_Minutes) {
        this.fullWeekDuration_In_Minutes = fullWeekDuration_In_Minutes;
    }

    @Override
    public String toString() {
        return "WeekObject{" +
                "fullWeekDistance=" + fullWeekDistance +
                ", fullWeekDates='" + fullWeekDates + '\'' +
                ", weeksFirstDate='" + weeksFirstDate + '\'' +
                ", weeksLastDate='" + weeksLastDate + '\'' +
                ", fullWeekCount=" + fullWeekCount +
                ", fullWeekDuration_In_Minutes=" + fullWeekDuration_In_Minutes +
                ", weekNumber=" + weekNumber +
                ", weekYear=" + weekYear +
                ", fullWeekCalories=" + fullWeekCalories +
                ", weeksMonth=" + weeksMonth +
                '}';
    }
}
