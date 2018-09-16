package com.ali.uneversaldatetools.date;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.ali.uneversaldatetools.tools.DateTools;

import java.util.Date;
import java.util.Objects;

/**
 * Created by ali on 9/5/18.
 */

//this is struct
public class DateSystem implements IDate, Comparable<IDate> {

    private Calendar Calendar;
    private Date Date;
    private IDate Date_SD;

    public DateSystem(Date date, Calendar calendar) {
        Calendar = calendar;

        switch (Calendar) {
            case Jalali: {
                Date_SD = new JalaliDateTime(date);
                break;
            }
            case Gregorian: {
                Date_SD = new GregorianDateTime(date);
                break;
            }
            case Hijri: {
                Date_SD = new HijriDateTime(date);
                break;
            }
            default: {
                throw new RuntimeException("Invalid Calendar Type!");
            }
        }

        Date = date;
    }

    public DateSystem(int year, int month, int day, Calendar calendar) {
        Calendar = calendar;

        switch (Calendar) {
            case Jalali: {
                Date_SD = new JalaliDateTime(year, month, day);
                break;
            }
            case Gregorian: {
                Date_SD = new GregorianDateTime(year, month, day);
                break;
            }
            case Hijri: {
                Date_SD = new HijriDateTime(year, month, day);
                break;
            }
            default: {
                throw new RuntimeException("Invalid Calendar Type!");
            }
        }

        Date = Date_SD.getDate();
    }

    public Calendar getCalendar() {
        return Calendar;
    }

    public Date getDate() {
        return Date;
    }

    public JalaliDateTime getJalaliDateTime() {
        return Date_SD.getJalaliDateTime();
    }

    public GregorianDateTime getGregorianDateTime() {
        return Date_SD.getGregorianDateTime();
    }

    public HijriDateTime getHijriDateTime() {
        return Date_SD.getHijriDateTime();
    }

    public IDate getDate_SD() {
        return Date_SD;
    }

    public int getYear() {
        return Date_SD.getYear();
    }

    public int getMonth() {
        return Date_SD.getMonth();
    }

    public int getDay() {
        return Date_SD.getDay();
    }

    public int getDays() {
        return Date_SD.getDays();
    }

    public Date getFirstDayOfYear() {
        return Date_SD.getFirstDayOfYear();
    }

    public Date getLastDayOfYear() {
        return Date_SD.getLastDayOfYear();
    }

    public Date getFirstDayOfMonth() {
        return Date_SD.getFirstDayOfMonth();
    }

    public Date getLastDayOfMonth() {
        return Date_SD.getLastDayOfMonth();
    }

    public Date FirstDayOfSeason(Season season) {
        return Date_SD.FirstDayOfSeason(season);
    }

    public Date LastDayOfSeason(Season season) {
        return Date_SD.LastDayOfSeason(season);
    }

    public Season getSeason() {
        return Date_SD.getSeason();
    }

    public DayOfWeek getDayOfWeek() {
        return Date_SD.getDayOfWeek();
    }

    public int getDaysOfMonth() {
        return Date_SD.getDaysOfMonth();
    }

    public String getLetters() {
        return Date_SD.getLetters();
    }

    public String getMonthLetters() {
        return Date_SD.getMonthLetters();
    }

    public String getYearMonthLetters() {
        return Date_SD.getYearMonthLetters();
    }

    public Date AddYears(int years) {
        return Date_SD.AddYears(years);
    }

    public Date AddMonths(int months) {
        return Date_SD.AddMonths(months);
    }

    public Date AddDays(int days) {
        return Date_SD.AddDays(days);
    }

    public static DateSystem Now(Calendar calendar) {
        return new DateSystem(DateTools.getCurrentDate(), calendar);
    }

    public static DateSystem Parse(String date, Calendar calendar) {
        switch (calendar) {
            case Jalali: {
                return new DateSystem(JalaliDateTime.Parse(date).getDate(), calendar);
            }
            case Gregorian: {
                return new DateSystem(GregorianDateTime.Parse(date).getDate(), calendar);
            }
            case Hijri: {
                return new DateSystem(HijriDateTime.Parse(date).getDate(), calendar);
            }
            default: {
                throw new RuntimeException("Invalid Calendar Type!");
            }
        }
    }

    public static DateSystem Parse(String yearMonth, int day, Calendar calendar) {
        switch (calendar) {
            case Jalali: {
                return new DateSystem(JalaliDateTime.Parse(yearMonth, day).getDate(), calendar);
            }
            case Gregorian: {
                return new DateSystem(GregorianDateTime.Parse(yearMonth, day).getDate(), calendar);
            }
            case Hijri: {
                return new DateSystem(HijriDateTime.Parse(yearMonth, day).getDate(), calendar);
            }
            default: {
                throw new RuntimeException("Invalid Calendar Type!");
            }
        }
    }

    @Nullable
    public static Date ParseOrNull(String date) {
        try {
            long binary = 0;
            date = date.replace(" ", "");//Trim()
            try {
                Long.parseLong(date, (int) binary);
                return null;                                         //todo last was return java.util.Date. (binary);
            } catch (NumberFormatException e) {
                return new Date(java.util.Date.parse(date));
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String getToYearMonth() {
        return Date_SD.getToYearMonth();
    }

    public String toString() {
        return Date_SD.toString();
    }

    @Override
    public int compareTo(@NonNull IDate o) {
        return CompareTo(o);
    }

    public int CompareTo(IDate t2) {
        return Date.compareTo(t2.getDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean equals(DateSystem other) {
        return Calendar == other.Calendar && Objects.equals(Date_SD, other.Date_SD);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        return obj instanceof DateSystem && equals((DateSystem) obj);
    }

    public int hashCode() {
        return (Calendar.getValue() * 397) ^ (Date_SD.hashCode());
    }

    @Override
    public long getUnixTime() {

        GregorianDateTime g = getGregorianDateTime();
        long from = DateConverter.GregorianToDays(1970, 1, 1);
        long d = g.getDays() - from;
        long s = d * 86400;
        return s;
    }

}
