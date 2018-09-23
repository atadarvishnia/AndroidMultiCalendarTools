package com.ali.uneversaldatetools;

import com.ali.uneversaldatetools.date.Calendar;
import com.ali.uneversaldatetools.date.DateConverter;
import com.ali.uneversaldatetools.date.DateSystem;
import com.ali.uneversaldatetools.date.GregorianDateTime;
import com.ali.uneversaldatetools.date.HijriDateTime;
import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.ali.uneversaldatetools.model.DateModel;
import com.ali.uneversaldatetools.tools.DateTools;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void CurrentTimeTest(){
        DateModel date = DateTools.getCurrentDate();
        Log(date.year);
        Log(date.month);
        Log(date.day);
    }


    @Test
    public void Test() {
        DateModel date = DateTools.getCurrentDate();
        DateSystem dateSystem = new DateSystem(date,Calendar.Jalali);
        Log(dateSystem.getYear() + "/" + dateSystem.getMonth() + "/" + dateSystem.getDate());
    }

    @Test
    public void MiladiTest() {

        DateModel date = DateTools.getCurrentDate();
        date.setYear(date.getYear());
        GregorianDateTime gregorianDateTime = new GregorianDateTime(date);
        HijriDateTime hijriDateTime = gregorianDateTime.getHijriDateTime();
        JalaliDateTime jalaliDateTime = gregorianDateTime.getJalaliDateTime();

        Log("------date-------");
        Log("year: " + date.getYear());
        Log("day: " + date.getDay());
        Log("month: " + date.getMonth());
        Log("str: " + date.toString());

        Log("------miladi-------");
        Log("year: " + gregorianDateTime.getYear());
        Log("day: " + gregorianDateTime.getDate().getDay());
        Log("month: " + gregorianDateTime.getMonth());
        Log("str: " + gregorianDateTime.toString());

        Log("------hijri-------");
        Log("year: " + hijriDateTime.getYear());
        Log("day: " + hijriDateTime.getDay());
        Log("month: " + hijriDateTime.getMonth());
        Log("str: " + hijriDateTime.toString());

        Log("------jalali-------");
        Log("year: " + jalaliDateTime.getYear());
        Log("day: " + jalaliDateTime.getDay());
        Log("month: " + jalaliDateTime.getMonth());
        Log("str: " + jalaliDateTime.toString());
    }

    @Test
    public void JalaliTest() {
        HijriDateTime mDateTime = new HijriDateTime(DateTools.getCurrentDate());
    }

    @Test
    public void HijriTest() {
        JalaliDateTime mDateTime = new JalaliDateTime(DateTools.getCurrentDate());
        Log(DateConverter.IsHijriLeap(1439));
    }

    @Test
    public void SystemDateTest() {
        DateSystem mDateTime = new DateSystem(DateTools.getCurrentDate(), Calendar.Hijri);
    }

    private void Log(Object s) {
        System.out.println(s);
    }
}