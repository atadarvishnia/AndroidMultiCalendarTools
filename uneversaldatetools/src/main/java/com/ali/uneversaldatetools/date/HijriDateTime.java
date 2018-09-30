package com.ali.uneversaldatetools.date;

import android.support.annotation.NonNull;

import com.ali.uneversaldatetools.model.DateModel;
import com.ali.uneversaldatetools.tools.DateTools;

/**
 * Created by ali on 9/5/18.
 */

public class HijriDateTime implements IDate, Comparable<HijriDateTime> {

    private int Year;
    private int Month;
    private int Day;
    private int Hour;
    private int Min;
    private int Sec;

    private static final int[] DaysInMonth = {
            0,
            30,
            29,
            30,
            29,
            30,
            29,
            30,
            29,
            30,
            29,
            30,
            30
    };

    public static GregorianDateTime FromUnixTime(int unixTime) {
        DateModel dateModel = DateConverter.UnixToHijri(unixTime);
        Validate(dateModel);
        return new GregorianDateTime(dateModel);
    }

    public HijriDateTime(DateModel date) {
        DateModel sd = DateConverter.GregorianToHijri(date.year, date.month, date.day);

        Validate(sd);

        Year = sd.year;
        Month = sd.month;
        Day = sd.day;
        Hour = sd.hour;
        Min = sd.min;
        Sec = sd.sec;
    }

    public HijriDateTime(int year, int month, int day) {

        Validate(new DateModel(year, month, day));

        Year = year;
        Month = month;
        Day = day;
    }

    public HijriDateTime(int year, int month, int day, int hour, int min, int sec) {

        Validate(new DateModel(year, month, day, hour, min, sec));

        Year = year;
        Month = month;
        Day = day;
        Hour = hour;
        Min = min;
        Sec = sec;
    }

    public HijriDateTime(int days) {
        DateModel sd = DateConverter.DaysToHijri(days);
        Validate(sd);
        Year = sd.year;
        Month = sd.month;
        Day = sd.day;
    }

    public static HijriDateTime Parse(String date) {
        int y = Integer.valueOf(date.substring(0, 4));
        int m = Integer.valueOf(date.substring(5, 7));
        int d = Integer.valueOf(date.substring(8, 10));
        return new HijriDateTime(y, m, d);
    }

    public static HijriDateTime Parse(String yearMonth, int day) {
        int y = Integer.valueOf(yearMonth.substring(0, 4));
        int m = Integer.valueOf(yearMonth.substring(5, 7));
        return new HijriDateTime(y, m, day);
    }

    public static HijriDateTime ParseYearMonth(String yearMonth) {
        int y = Integer.valueOf(yearMonth.substring(0, 4));
        int m = Integer.valueOf(yearMonth.substring(5, 7));
        return new HijriDateTime(y, m, 1);
    }

    public static HijriDateTime Now() {
        return new HijriDateTime(DateTools.getCurrentDate());
    }

    public DateModel getDate() {
        DateModel md = DateConverter.HijriToGregorian(Year, Month, Day);
        return new DateModel(md.year, md.month, md.day);
    }

    public DateModel getFirstDayOfYear() {
        DateModel md = DateConverter.HijriToGregorian(Year, 1, 1);
        return new DateModel(md.year, md.month, md.day);
    }

    public DateModel getLastDayOfYear() {
        DateModel md = DateConverter.HijriToGregorian(Year, 12, DaysInMonth[12] - (!DateConverter.IsHijriLeap(Year) ? 1 : 0));
        return new DateModel(md.year, md.month, md.day, 23, 59, 59);
    }

    public DateModel getFirstDayOfMonth() {
        DateModel md = DateConverter.HijriToGregorian(Year, Month, 1);
        return new DateModel(md.year, md.month, md.day);
    }

    public DateModel getLastDayOfMonth() {
        DateModel md = DateConverter.HijriToGregorian(Year, Month, DaysInMonth[Month] - (Month == 12 && !DateConverter.IsHijriLeap(Year) ? 1 : 0));
        return new DateModel(md.year, md.month, md.day, 23, 59, 59);
    }

    public DateModel FirstDayOfSeason(Season season) {
        DateModel mFirstDayOfSeason = new GregorianDateTime(getDate()).FirstDayOfSeason(season);
        HijriDateTime firstDayOfSeason = new HijriDateTime(mFirstDayOfSeason);

        if (firstDayOfSeason.Year == Year) {
            if (firstDayOfSeason.Month < 11 || firstDayOfSeason.Month == 11 && firstDayOfSeason.Day <= 15) {
                return mFirstDayOfSeason;
            }

            getDate().month = getDate().month - 6;
            return new HijriDateTime(getDate()).FirstDayOfSeason(season);
        }


        if (firstDayOfSeason.Year == Year - 1) {
            if (firstDayOfSeason.Month == 12 || firstDayOfSeason.Month == 11 && firstDayOfSeason.Day >= 15) {
                return mFirstDayOfSeason;
            }
        }

        getDate().month = getDate().month - (firstDayOfSeason.Year < Year ? 6 : -6);
        return new HijriDateTime(getDate()).FirstDayOfSeason(season);
    }


    public DateModel LastDayOfSeason(Season season) {
        return new GregorianDateTime(FirstDayOfSeason(season)).LastDayOfSeason(season);
    }


    public Season getSeason() {
        switch (getDate().month) {
            case 1:
            case 2:
            case 3:
                return Season.Winter;
            case 4:
            case 5:
            case 6:
                return Season.Spring;
            case 7:
            case 8:
            case 9:
                return Season.Summer;
            case 10:
            case 11:
            case 12:
                return Season.Autumn;
        }
        throw new IndexOutOfBoundsException("month is out of bound");
    }


    public DateModel AddYears(int years) {
        int y = Year + years;
        int m = Month;
        int maxd = DaysInMonth[m];
        maxd -= (m == 12 && !DateConverter.IsHijriLeap(y) ? 1 : 0);
        int d = Day > maxd ? maxd : Day;
        return new HijriDateTime(y, m, d).getDate();
    }


    public DateModel AddMonths(int months) {
        int m = Month - 1 + months;
        int y = Year + (m / 12);
        m = (m % 12) + 1;
        int maxd = DaysInMonth[m];
        maxd -= (m == 12 && !DateConverter.IsHijriLeap(y) ? 1 : 0);
        int d = Day > maxd ? maxd : Day;
        return new HijriDateTime(y, m, d).getDate();
    }


    public DateModel AddDays(int days) {
        return new HijriDateTime(getDays() + days).getDate();
    }


    public JalaliDateTime getJalaliDateTime() {
        return new JalaliDateTime(getDate());
    }


    public GregorianDateTime getGregorianDateTime() {
        return new GregorianDateTime(getDate());
    }


    public HijriDateTime getHijriDateTime() {
        return this;
    }


    public String getLetters() {
        return "";
    }


    public String getMonthLetters() {
        return "";
    }


    public String getYearMonthLetters() {
        return "";
    }


    public int getYear() {
        return Year;
    }


    public int getMonth() {
        return Month;
    }


    public int getDay() {
        return Day;
    }

    @Override
    public int getHour() {
        return Hour;
    }

    @Override
    public int getMin() {
        return Min;
    }

    @Override
    public int getSec() {
        return Sec;
    }

    public int getDays() {
        return DateConverter.HijriToDays(Year, Month, Day);
    }

    @Override
    public int getDaysOfMonth() {
        return DaysInMonth[Month] - (Month == 12 & !DateConverter.IsHijriLeap(Year) ? 1 : 0);
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.ToDayOfWeek((getDays() + 5) % 7);
    }


    public String getToYearMonth() {
        return String.format("%04d/%02d", Year, Month); //tested
    }

    public String toString() {
        return String.format("%04d/%02d/%02d", Year, Month, Day); //tested
    }

    public String toLongString() {
        return String.format("%04d/%02d/%02d/%02d/%02d/%02d", Year, Month, Day, Hour, Min, Sec);
    }

    @Override
    public int compareTo(@NonNull HijriDateTime o) {
        return CompareTo(o);
    }

    public int CompareTo(IDate t2) {
        if (t2.getYear() > Year) return -1;
        if (t2.getYear() < Year) return 1;
        if (t2.getMonth() > Month) return -1;
        if (t2.getMonth() < Month) return 1;
        if (t2.getDay() > Day) return -1;
        return t2.getDay() == Day ? 0 : 1;
    }

    public boolean equals(HijriDateTime other) {
        return Year == other.Year && Month == other.Month && Day == other.Day;
    }

    public boolean equals(Object obj) {
        if (null == obj) { // C# :ReferenceEquals(null, obj)

            return false;
        }

        return obj instanceof HijriDateTime && equals((HijriDateTime) obj);
    }

    public int hashCode() {
        int hashCode = Year;
        hashCode = (hashCode * 397) ^ Month;
        hashCode = (hashCode * 397) ^ Day;
        return hashCode;
    }

    private static void Validate(DateModel dateModel) {
        //year
        if (dateModel.year < 0)
            throw new IllegalArgumentException("invalid date");

        //month
        if (dateModel.month < 1 | dateModel.month > 12)
            throw new IllegalArgumentException("invalid date");

        //day
        if (DateConverter.IsGregorianLeap(dateModel.year) & dateModel.month == 12) {
            if (dateModel.day < 1 | dateModel.day > DaysInMonth[dateModel.month] + 1)
                throw new IllegalArgumentException("invalid date");
        } else {
            if (dateModel.day < 1 | dateModel.day > DaysInMonth[dateModel.month])
                throw new IllegalArgumentException("invalid date");
        }

        //hour
        if (dateModel.month < 0 | dateModel.month > 23)
            throw new IllegalArgumentException("invalid date");

        //min
        if (dateModel.month < 0 | dateModel.month > 60)
            throw new IllegalArgumentException("invalid date");

        //sec
        if (dateModel.month < 0 | dateModel.month > 60)
            throw new IllegalArgumentException("invalid date");

    }
}
