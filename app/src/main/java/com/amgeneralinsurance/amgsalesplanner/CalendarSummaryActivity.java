package com.amgeneralinsurance.amgsalesplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.utils.ReuseableClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarSummaryActivity extends AppCompatActivity implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EmptyViewClickListener{

        private static final int TYPE_DAY_VIEW = 1;
        private static final int TYPE_THREE_DAY_VIEW = 2;
        private static final int TYPE_WEEK_VIEW = 3;
        private int mWeekViewType = TYPE_THREE_DAY_VIEW;
        private WeekView mWeekView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_calendar_summary);

                // Get a reference for the week view in the layout.
                mWeekView = (WeekView) findViewById(R.id.weekView);
                mWeekView.goToHour(9);

                mWeekView.setOnEventClickListener(this);
                mWeekView.setMonthChangeListener(this);
                mWeekView.setEmptyViewClickListener(this);
                setupDateTimeInterpreter(false);
        }

        @Override
        public void onBackPressed() {
                Intent i = new Intent(this, DashBoardActivity.class);
                finish();
                startActivity(i);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();
                setupDateTimeInterpreter(id == R.id.action_week_view);
                switch (id){
                        case R.id.action_today:
                                mWeekView.goToToday();
                                return true;
                        case R.id.action_day_view:
                                if (mWeekViewType != TYPE_DAY_VIEW) {
                                        item.setChecked(!item.isChecked());
                                        mWeekViewType = TYPE_DAY_VIEW;
                                        mWeekView.setNumberOfVisibleDays(1);

                                        // Lets change some dimensions to best fit the view.
                                        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                                        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                                        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                                }
                                return true;
                        case R.id.action_three_day_view:
                                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                                        item.setChecked(!item.isChecked());
                                        mWeekViewType = TYPE_THREE_DAY_VIEW;
                                        mWeekView.setNumberOfVisibleDays(3);

                                        // Lets change some dimensions to best fit the view.
                                        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                                        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                                        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                                }
                                return true;
                        case R.id.action_week_view:
                                if (mWeekViewType != TYPE_WEEK_VIEW) {
                                        item.setChecked(!item.isChecked());
                                        mWeekViewType = TYPE_WEEK_VIEW;
                                        mWeekView.setNumberOfVisibleDays(7);

                                        // Lets change some dimensions to best fit the view.
                                        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                                        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                                        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                                }
                                return true;
                }

                return super.onOptionsItemSelected(item);
        }

        /**
         * Set up a date time interpreter which will show short date values when in week view and long
         * date values otherwise.
         * @param shortDate True if the date values should be short.
         */
        private void setupDateTimeInterpreter(final boolean shortDate) {
                mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
                        @Override
                        public String interpretDate(Calendar date) {
                                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                                String weekday = weekdayNameFormat.format(date.getTime());
                                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                                // All android api level do not have a standard way of getting the first letter of
                                // the week day name. Hence we get the first char programmatically.
                                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                                if (shortDate)
                                        weekday = String.valueOf(weekday.charAt(0));
                                return weekday.toUpperCase() + format.format(date.getTime());
                        }

                        @Override
                        public String interpretTime(int hour) {
                                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
                        }
                });
        }

        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

                SQLiteDatabase db = ReuseableClass.createAndOpenDb(this);
                String sql = "SELECT * FROM plan_tbl WHERE meeting_date_modified >='"+ newYear+String.format("%02d", (newMonth-1))+"01' AND " +
                        "meeting_date_modified <='" + newYear+String.format("%02d", (newMonth-1))+31+"'";
                Log.i("TAG", sql);
                Cursor cur = db.rawQuery(sql, null);

                String idArray[]                = new String[cur.getCount()];
                String DateArray[]              = new String[cur.getCount()];
                String MonthArray[]             = new String[cur.getCount()];
                String YearArray[]              = new String[cur.getCount()];
                String StartTimeHourArray[]     = new String[cur.getCount()];
                String StartTimeMinArray[]      = new String[cur.getCount()];
                String EndTimeHourArray[]       = new String[cur.getCount()];
                String EndTimeMinArray[]        = new String[cur.getCount()];
                String ObjArray[]               = new String[cur.getCount()];
                String CompletedArray[]         = new String[cur.getCount()];

                if(cur.moveToNext()) {
                        int i = 0;
                        do {
                                idArray[i]              = cur.getString(1);
                                DateArray[i]            = cur.getString(6).substring(0,2);
                                MonthArray[i]           = cur.getString(6).substring(3,5);
                                YearArray[i]            = cur.getString(6).substring(6,10);
                                StartTimeHourArray[i]   = cur.getString(7).substring(0,2);
                                StartTimeMinArray[i]    = cur.getString(7).substring(3,5);
                                EndTimeHourArray[i]     = cur.getString(8).substring(0,2);
                                EndTimeMinArray[i]      = cur.getString(8).substring(3,5);
                                ObjArray[i]             = cur.getString(10);
                                CompletedArray[i]       = cur.getString(12);

                                Log.i("TAG", cur.getString(0));
                                Log.i("TAG", cur.getString(6).substring(3,5));
                                Log.i("TAG", cur.getString(6).substring(0,2));
                                Log.i("TAG", cur.getString(6).substring(3,5));
                                Log.i("TAG", cur.getString(6).substring(6,10));
                                Log.i("TAG", cur.getString(7).substring(0,2));
                                Log.i("TAG", cur.getString(7).substring(3,5));
                                Log.i("TAG", cur.getString(8).substring(0,2));
                                Log.i("TAG", cur.getString(8).substring(3,5));
                                Log.i("TAG", cur.getString(10));
                                Log.i("TAG", cur.getString(12));

                                Calendar startTime = Calendar.getInstance();
                                startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(StartTimeHourArray[i]));
                                startTime.set(Calendar.MINUTE, Integer.parseInt(StartTimeMinArray[i]));
                                startTime.set(Calendar.DATE, Integer.parseInt(DateArray[i]));
                                startTime.set(Calendar.MONTH, (Integer.parseInt(MonthArray[i])-1));
                                startTime.set(Calendar.YEAR, newYear);
                                Calendar endTime = (Calendar) startTime.clone();
                                endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(EndTimeHourArray[i]));
                                endTime.set(Calendar.MINUTE, Integer.parseInt(EndTimeMinArray[i]));
                                endTime.set(Calendar.DATE, Integer.parseInt(DateArray[i]));
                                endTime.set(Calendar.MONTH, (Integer.parseInt(MonthArray[i])-1));
                                endTime.set(Calendar.YEAR, newYear);
                                WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
                                if(CompletedArray[i].equalsIgnoreCase("1"))
                                        event.setColor(getResources().getColor(R.color.event_color_green));
                                else
                                        event.setColor(getResources().getColor(R.color.event_color_red));
                                event.setName(ObjArray[i]);
                                event.setId(Long.parseLong(idArray[i]));
                                events.add(event);

                                i++;
                        }while (cur.moveToNext());
                }
                cur.close();
                db.close();

                return events;
        }

        private String getEventTitle(Calendar time) {
                return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public void onEventClick(WeekViewEvent event, RectF eventRect) {
//                Toast.makeText(CalendarSummaryActivity.this, "Clicked " + event.getId(), Toast.LENGTH_SHORT).show();

                SQLiteDatabase db = ReuseableClass.createAndOpenDb(this);
                Cursor cur = db.rawQuery("SELECT * FROM plan_tbl WHERE id='"+event.getId()+"'", null);

                if(cur.moveToNext()) {
                        do {
                                Intent i = new Intent(CalendarSummaryActivity.this, ModifyAgentActivity.class);
                                i.putExtra("plan_id", cur.getString(1));
                                i.putExtra("agency_name", cur.getString(2));
                                i.putExtra("date", cur.getString(6));
                                i.putExtra("start_time", cur.getString(7));
                                i.putExtra("end_time", cur.getString(8));
                                i.putExtra("purpose", cur.getString(9));
                                i.putExtra("objective", cur.getString(10));
                                i.putExtra("outcome", cur.getString(11));
                                i.putExtra("completed", cur.getString(12));
                                i.putExtra("action_required", cur.getString(13));
                                i.putExtra("lat", cur.getString(4));
                                i.putExtra("lng", cur.getString(5));
                                i.putExtra("coming_from", "calender_activity");
                                finish();
                                startActivity(i);

                        }while (cur.moveToNext());
                }
                cur.close();
                db.close();
        }

        @Override
        public void onEmptyViewClicked(Calendar time) {
                Log.d("TAG","onEmptyViewClicked");
                /*
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE),
                time.get(Calendar.DAY_OF_MONTH)
                time.get(Calendar.MONTH)+1,
                time.get(Calendar.YEAR),
                */

                Intent i = new Intent(CalendarSummaryActivity.this, AddAgentActivity.class);
                i.putExtra("Date",String.format("%02d", time.get(Calendar.DAY_OF_MONTH))+"-"+
                                  String.format("%02d", time.get(Calendar.MONTH) + 1)+"-"+
                                  String.format("%02d", time.get(Calendar.YEAR) ));
                i.putExtra("StartTime",String.format("%02d", time.get(Calendar.HOUR_OF_DAY))+":"+
                                  String.format("%02d", time.get(Calendar.MINUTE)));
                i.putExtra("EndTime",String.format("%02d", time.get(Calendar.HOUR_OF_DAY) + 1)+":"+
                        String.format("%02d", time.get(Calendar.MINUTE)));
                i.putExtra("coming_from", "calender_activity");
                finish();
                startActivity(i);
        }
}
