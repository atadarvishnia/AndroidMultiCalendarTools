package com.ali.uneversaldatetools.datePicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ali.uneversaldatetools.R;
import com.ali.uneversaldatetools.date.Calendar;
import com.ali.uneversaldatetools.date.DateSystem;
import com.ali.uneversaldatetools.model.Month;
import com.ali.uneversaldatetools.tools.Convert;
import com.ali.uneversaldatetools.tools.DateTools;
import com.ali.uneversaldatetools.tools.ExpandAndCollapseAnimation;
import com.ali.uneversaldatetools.tools.StringGenerator;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;

/**
 * Created by ali on 9/5/18.
 */

public class UDatePicker
        extends
        FrameLayout
        implements
        CalenderViewFragment.CalenderFragmentInterface,
        ViewPager.OnPageChangeListener {

    private OnDateChangedListener listener;
    private Context mContext;
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;
    private TextView topbarMonthText;
    private int currentSelectedYear = 0;

    private DateSystem mDateSystem; // current selected date

    public UDatePicker(Context context) {
        super(context);
        Init(context);
    }

    public UDatePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public UDatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {

        LayoutInflater mInflater = LayoutInflater.from(context);

        this.mContext = context;

        View dayPickerView = mInflater.inflate(R.layout.u_date_picker_days_month, null);
        addView(dayPickerView);

        View yearPickerView = mInflater.inflate(R.layout.u_date_picker_year, null);
        addView(yearPickerView);

    }

    public void ShowDatePicker(FragmentManager appCompatActivity, @NonNull DateSystem defaultDate) {
        mDateSystem = defaultDate;
        SetupDayPicker(appCompatActivity, LoadMonths(mDateSystem));
        SetupYearPicker(mDateSystem.getYear());
    }

    public void ShowDatePicker(FragmentManager appCompatActivity, Calendar calendar) {
        DateSystem defaultDate = new DateSystem(DateTools.getCurrentDate(), calendar); // now
        ShowDatePicker(appCompatActivity, defaultDate);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void SetupDayPicker(FragmentManager appCompatActivity, ArrayList<Month> months) {
        this.getLayoutParams().height = Convert.DpToPixel(350);
        this.getLayoutParams().width = Convert.DpToPixel(300);
        requestLayout();

        SetupDayOfWeek();
        SetupArrowKeys();

        topbarMonthText = findViewById(R.id.text_top_bar_month);
        mViewPager = findViewById(R.id.pager_swipe_layout);

        mViewPager.addOnPageChangeListener(this);

        topbarMonthText.setText(StringGenerator.Month(
                getResources(),
                mDateSystem.getMonth(),
                mDateSystem.getCalendar())
        );

        mViewPagerAdapter = new ViewPagerAdapter(appCompatActivity);
        for (Month month : months) {
            mViewPagerAdapter.AddFragment(new CalenderViewFragment(month, months.indexOf(month), this));
        }
        //for making viewpager swipe circle
        mViewPagerAdapter.AddFragment(new CalenderViewFragment(months.get(0), months.indexOf(months.get(0)), this));
        mViewPagerAdapter.AddFragment(new CalenderViewFragment(months.get(1), months.indexOf(months.get(1)), this));


        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(mDateSystem.getMonth() - 1, false);
    }

    private void SetupYearPicker(int selectedYear) {

        LinearLayout topbarMonth = findViewById(R.id.linear_top_bar_month);
        LinearLayout topbarYear = findViewById(R.id.linear_top_bar_year);
        TextView topbarYearText = findViewById(R.id.text_top_bar_year);
        TextView topbarMonthTitle = findViewById(R.id.text_top_bar_month_title);
        TextView topbarYearTitle = findViewById(R.id.text_top_bar_year_title);
        ListView yearListview = findViewById(R.id.list_year_picker);
        ImageView arrowLeft = findViewById(R.id.img_calender_arrow_left);
        ImageView arrowRight = findViewById(R.id.img_calender_arrow_right);
        View yearPickerView = this.getChildAt(1);


        //top bar init
        topbarYearText.setText(StringGenerator.NumberToString(mContext.getResources(), selectedYear));

        // visible/invisible + effect

        topbarMonth.setAlpha(1);
        topbarYear.setAlpha((float) 0.6);
        topbarMonth.setOnClickListener(v -> {
            if (yearPickerView.getVisibility() == View.GONE) return;

            arrowLeft.animate().alpha(1).setDuration(200).start();
            arrowRight.animate().alpha(1).setDuration(200).start();
            topbarMonth.animate().alpha(1).setDuration(200).start();
            topbarYear.animate().alpha((float) 0.6).setDuration(200).start();
            postDelayed(() -> yearPickerView.setVisibility(GONE), 300);
            yearPickerView.animate().alpha(0).setDuration(300).start();
            ExpandAndCollapseAnimation.Collapse(topbarYearTitle);
            ExpandAndCollapseAnimation.Expand(topbarMonthTitle);

            if (currentSelectedYear == mDateSystem.getYear()) return; //for optimize

            //refresh viewPager
            mDateSystem = new DateSystem(
                    currentSelectedYear,
                    mDateSystem.getMonth(),
                    mDateSystem.getDay(),
                    mDateSystem.getCalendar());

            List<Month> months = LoadMonths(mDateSystem);
            for (Month month : months) {
                ((CalenderViewFragment) mViewPagerAdapter.getItem(months.indexOf(month))).setMonth(month);
            }
            ((CalenderViewFragment) mViewPagerAdapter.getItem(12)).setMonth(months.get(0));
            ((CalenderViewFragment) mViewPagerAdapter.getItem(13)).setMonth(months.get(1));
            mViewPagerAdapter.notifyDataSetChanged();
        });
        topbarYear.setOnClickListener(v -> {
            if (yearPickerView.getVisibility() == VISIBLE) return;

            arrowLeft.animate().alpha((float) 0.6).setDuration(200).start();
            arrowRight.animate().alpha((float) 0.6).setDuration(200).start();
            topbarMonth.animate().alpha((float) 0.6).setDuration(200).start();
            topbarYear.animate().alpha(1).setDuration(200).start();
            yearPickerView.setVisibility(VISIBLE);
            yearPickerView.animate().alpha(1).setDuration(300).start();
            ExpandAndCollapseAnimation.Collapse(topbarMonthTitle);
            ExpandAndCollapseAnimation.Expand(topbarYearTitle);
        });


        //listView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.item_year, R.id.text_year_item, LoadYears());
        yearListview.setAdapter(adapter);
        yearListview.setSelection(selectedYear - 3);
        yearListview.setOnItemClickListener((parent, view, position, id) -> {

            Log.d("selectedCO", String.valueOf(position) + " " + String.valueOf(currentSelectedYear));
            if (position != currentSelectedYear) return; //make on selected items un clickable

            topbarYearText.setText(StringGenerator.NumberToString(mContext.getResources(), position));
            yearListview.setSelection(position - 3);

            topbarMonth.setAlpha(1);
            topbarYear.setAlpha((float) 0.6);
            postDelayed(() -> yearPickerView.setVisibility(GONE), 300);
            yearPickerView.animate().alpha(0).setDuration(300).start();
            ExpandAndCollapseAnimation.Collapse(topbarYearTitle);
            ExpandAndCollapseAnimation.Expand(topbarMonthTitle);

            if (position == mDateSystem.getYear()) return; //for optimize

            //refresh viewPager
            mDateSystem = new DateSystem(
                    position,
                    mDateSystem.getMonth(),
                    mDateSystem.getDay(),
                    mDateSystem.getCalendar());

            List<Month> months = LoadMonths(mDateSystem);
            for (Month month : months) {
                ((CalenderViewFragment) mViewPagerAdapter.getItem(months.indexOf(month))).setMonth(month);
            }
            ((CalenderViewFragment) mViewPagerAdapter.getItem(12)).setMonth(months.get(0));
            ((CalenderViewFragment) mViewPagerAdapter.getItem(13)).setMonth(months.get(1));
            mViewPagerAdapter.notifyDataSetChanged();
        });

        //new

        yearListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == SCROLL_STATE_IDLE) {
                    yearListview.setSelection(currentSelectedYear - 3);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (currentSelectedYear != firstVisibleItem) {
                    Log.d("selectedCO", String.valueOf(firstVisibleItem + 3));
                    topbarYearText.setText(StringGenerator.NumberToString(mContext.getResources(), firstVisibleItem + 3));

                    currentSelectedYear = firstVisibleItem + 3;
                }
            }
        });
    }

    public void setOnDateSelected(OnDateChangedListener listener) {
        this.listener = listener;
    }

    public interface OnDateChangedListener {
        void onDateChange(@Nullable DateSystem dateSystem, @Nullable long unixTime);
    }

    /**
     * @return selected date in selected calender
     * or null if selected day is 31 but this month have no 31
     * you can show message like "plz select day"
     */
    @Nullable
    public DateSystem getSelectedDate() {
        DateSystem dateSystem;
        if (mDateSystem.getMonth() == 13)
            dateSystem = new DateSystem(mDateSystem.getYear(), 1, mDateSystem.getDay(), mDateSystem.getCalendar());
        else
            dateSystem = mDateSystem;

        if (LoadMonths(dateSystem).get(dateSystem.getMonth() - 1).getDaysCount() >= dateSystem.getDay())
            return dateSystem;
        else return null;// when selected day is 31 but month have no 31
    }

    /**
     * @return unix time (seconds from 1970/1/1 in UTC + 00:00)
     * or null if selected day is 31 but this month have no 31
     * you can show message like "plz select day"
     */
    @Nullable
    public Long getSelectedUnixTime() {
        DateSystem dateSystem = getSelectedDate();
        if (dateSystem == null) return null;
        else return dateSystem.getUnixTime();
    }

    private void SetupDayOfWeek() {
        LinearLayout daysOfWeek = findViewById(R.id.linear_day_of_week);

        for (View view : new DaysViewGenerator(mContext).getDayOfWeek()) {
            daysOfWeek.addView(view);
        }
    }

    private void SetupArrowKeys() {

        ImageView arrowLeft = findViewById(R.id.img_calender_arrow_left);
        ImageView arrowRight = findViewById(R.id.img_calender_arrow_right);

        arrowLeft.setOnClickListener(v -> {
            //disable clicks when year picker is visible
            if (this.getChildAt(1).getVisibility() == VISIBLE) return;
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        });

        arrowRight.setOnClickListener(v -> {
            //disable clicks when year picker is visible
            if (this.getChildAt(1).getVisibility() == VISIBLE) return;
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        });
    }

    private ArrayList<Month> LoadMonths(final DateSystem date) {

        ArrayList<Month> list = new ArrayList<>();
        int year = date.getYear();
        Calendar calendar = date.getCalendar();

        for (int i = 1; i <= 12; i++) {

            DateSystem dateLoop = new DateSystem(year, i, 1, calendar);// 1 means first day of month

            Month month = new Month(
                    i,
                    StringGenerator.Month(mContext.getResources(), i, calendar),
                    dateLoop.getDaysOfMonth(),
                    dateLoop.getDayOfWeek()
            );
            list.add(month);
        }
        return list;
    }

    private List<String> LoadYears() {
        List<String> years = new ArrayList<>();
        for (int i = 0; i <= 3000; i++) {
            years.add(StringGenerator.NumberToString(mContext.getResources(), i));
        }
        return years;
    }


    //fragment interface implement
    @Override
    public void onDaySelectListener(int day, int monthPos) {
        mDateSystem = new DateSystem(
                mDateSystem.getYear(),
                mDateSystem.getMonth(),
                day,
                mDateSystem.getCalendar()
        );

        //this part is because last and next viewpager views render before scroll
        try {
            ((CalenderViewFragment) mViewPagerAdapter.getItem(mDateSystem.getMonth() - 2)).Render();
        } catch (ArrayIndexOutOfBoundsException e) {
            //whe we are in first pos (have no last pos)
        }

        try {
            ((CalenderViewFragment) mViewPagerAdapter.getItem(mDateSystem.getMonth())).Render();
        } catch (IndexOutOfBoundsException e) {
            //whe we are in last pos (have no next pos)
        }

        if (listener != null) {
            DateSystem dateSystem = new DateSystem(mDateSystem.getYear(), monthPos, day, mDateSystem.getCalendar());
            listener.onDateChange(dateSystem, dateSystem.getUnixTime());
        }
    }

    @Override
    public int selectedDayProvider() {
        return mDateSystem.getDay();
    }

    @Override
    public int currentMonthProvider() {
        return new DateSystem(DateTools.getCurrentDate(), mDateSystem.getCalendar()).getMonth();
    }

    @Override
    public int currentDayProvider() {
        return new DateSystem(DateTools.getCurrentDate(), mDateSystem.getCalendar()).getDay();
    }


    //view pager listener
    @Override
    public void onPageSelected(int position) {
        Log.d("swipe", "select(" + String.valueOf(position) + ")");

        topbarMonthText.setText(StringGenerator.Month(
                mContext.getResources(),
                getMonth(),
                mDateSystem.getCalendar()));


        mDateSystem = new DateSystem(
                mDateSystem.getYear(),
                position + 1,
                mDateSystem.getDay(),
                mDateSystem.getCalendar());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

        if (state == SCROLL_STATE_IDLE) {
            //on animation stop
            if (mViewPager.getCurrentItem() == 13) {
                mViewPager.setCurrentItem(1, false);
            } else if (mViewPager.getCurrentItem() == 0) {
                mViewPager.setCurrentItem(12, false);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*ignore*/}


    private int getMonth() {
        int current = mViewPager.getCurrentItem() + 1;
        if (current > 12) {
            current -= 12;
        }
        if (current < 0)
            throw new RuntimeException("current is less then 0: " + current);
        return current;
    }
}