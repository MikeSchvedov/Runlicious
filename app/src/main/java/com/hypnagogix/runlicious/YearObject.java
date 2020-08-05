package com.hypnagogix.runlicious;

public class YearObject {

    int fullYearDuration_In_Minutes = 0;
    int fullYearDistance = 0;
    int fullYearDate = 0;






    public YearObject(int fullYearDate) {
        this.fullYearDistance = fullYearDistance;
        this.fullYearDate = fullYearDate;
        this.fullYearDuration_In_Minutes = fullYearDuration_In_Minutes;

    }


    public int getFullYearDuration_In_Minutes() {
        return fullYearDuration_In_Minutes;
    }

    public void setFullYearDuration_In_Minutes(int fullYearDuration_In_Minutes) {
        this.fullYearDuration_In_Minutes = fullYearDuration_In_Minutes;
    }

    public int getFullYearDistance() {
        return fullYearDistance;
    }

    public void setFullYearDistance(int fullYearDistance) {
        this.fullYearDistance = fullYearDistance;
    }

    public int getFullYearDate() {
        return fullYearDate;
    }

    public void setFullYearDate(int fullYearDate) {
        this.fullYearDate = fullYearDate;
    }

    @Override
    public String toString() {
        return "YEARObject{" +
                "fullYearDuration_In_Minutes=" + fullYearDuration_In_Minutes +
                ", fullYearDistance=" + fullYearDistance +
                ", fullYearDate='" + fullYearDate ;
    }
}
