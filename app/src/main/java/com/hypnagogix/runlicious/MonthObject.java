package com.hypnagogix.runlicious;

import java.util.Calendar;

public class MonthObject {

    int fullMonthDuration_In_Minutes = 0;
    int fullMonthDistance = 0;
    String fullMonthDate = "";

    int monthNumberWithoutYear = 0;
    int MonthNumber = 0;





    public MonthObject(int monthNumber) {
        this.fullMonthDistance = fullMonthDistance;
        this.fullMonthDate = fullMonthDate;
        this.fullMonthDuration_In_Minutes = fullMonthDuration_In_Minutes;
        this.MonthNumber = monthNumber;


        //getting the month itself
        this.monthNumberWithoutYear = MonthNumber %100;

    }


    public int getMonthNumber() {
        return MonthNumber;
    }

    public void setMonthNumber(int monthNumber) {
        this.MonthNumber = monthNumber;
    }




    public int getFullMonthDistance() {
        return fullMonthDistance;
    }

    public void setFullMonthDistance(int fullMonthDistance) {
        this.fullMonthDistance = fullMonthDistance;
    }



    public String getFullMonthDate() {
        return fullMonthDate;
    }

    public void setFullMonthDate(String fullMonthDate) {
        this.fullMonthDate = fullMonthDate;
    }




    public int getFullMonthDuration_In_Minutes() {
        return fullMonthDuration_In_Minutes;
    }

    public void setFullMonthDuration_In_Minutes(int fullMonthDuration_In_Minutes) {
        this.fullMonthDuration_In_Minutes = fullMonthDuration_In_Minutes;
    }

    @Override
    public String toString() {
        return "MonthObject{" +
                "fullMonthDuration_In_Minutes=" + fullMonthDuration_In_Minutes +
                ", fullMonthDistance=" + fullMonthDistance +
                ", fullMonthDate='" + fullMonthDate + '\'' +
                ", monthNumberWithoutYear=" + monthNumberWithoutYear +
                ", MonthNumber=" + MonthNumber +
                '}';
    }
}
