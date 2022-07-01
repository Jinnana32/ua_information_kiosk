package com.antique.events.ui.utils.helper

import android.util.SparseArray
import androidx.fragment.app.FragmentManager
import com.codetroopers.betterpickers.Utils
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment
import java.util.*

class DatePickerHelper {

    companion object {
        private val dateToday = Calendar.getInstance()
        fun showDatePicker(
            fragmentManager: FragmentManager,
            tag: String,
            disabledDays: SparseArray<MonthAdapter.CalendarDay>? = SparseArray(),
            disabledWeekdays: List<Int> = listOf(),
            isMinimun: Boolean = true,
            onDatePicked: (m: Calendar) -> Unit
        ) {
            val calendarMinDate = Calendar.getInstance()
            var minDate: MonthAdapter.CalendarDay? = MonthAdapter.CalendarDay(
                calendarMinDate.get(Calendar.YEAR),
                calendarMinDate.get(Calendar.MONTH), calendarMinDate.get(Calendar.DAY_OF_MONTH)
            )

            val calendarMaxDate = Calendar.getInstance()
            calendarMaxDate[Calendar.YEAR] = calendarMinDate.get(Calendar.YEAR) + 2
            var maxDate: MonthAdapter.CalendarDay? = MonthAdapter.CalendarDay(
                calendarMaxDate.get(Calendar.YEAR),
                calendarMaxDate.get(Calendar.MONTH), calendarMaxDate.get(Calendar.DAY_OF_MONTH)
            )

            if(!disabledWeekdays.isNullOrEmpty()){
                val loopdate: Calendar = calendarMinDate
                while (calendarMinDate.before(calendarMaxDate)) {
                    val dayOfWeek = loopdate[Calendar.DAY_OF_WEEK]
                    if (!disabledWeekdays.contains(dayOfWeek)) {
                        val disabledDay: Pair<Int, MonthAdapter.CalendarDay> = parseSingleDisabledDay(loopdate.time);
                        disabledDays!!.put(disabledDay.first, disabledDay.second);
                    }
                    loopdate.add(Calendar.DATE, 1)
                }
            }



            if(!isMinimun){
                minDate = null;
                maxDate = null;
            }

            val calendar = Calendar.getInstance()
            val calendarDatePickerDialogFragment = CalendarDatePickerDialogFragment()
                .setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    onDatePicked(calendar);
                }
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDateRange(minDate, maxDate)
                .setDoneText("Ok")
                .setCancelText("Cancel")
                .setThemeLight()
                .setDisabledDays(disabledDays!!)

            calendarDatePickerDialogFragment.isCancelable = false
            calendarDatePickerDialogFragment.show(fragmentManager, tag)
        }

        fun showTimePicker(
            fragmentManager: FragmentManager,
            tag: String,
            ontimePicked: (calendar: Calendar) -> Unit
        ) {
            val calendar: Calendar? = Calendar.getInstance();
            val radialTimePicker = RadialTimePickerDialogFragment()
                .setOnTimeSetListener { dialog: RadialTimePickerDialogFragment?, hourOfDay: Int, minute: Int ->
                    calendar!!.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    ontimePicked(calendar);
                }
                .setStartTime(calendar!!.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
                .setDoneText("Ok")
                .setCancelText("Cancel")
                .setThemeLight()

            radialTimePicker.show(
                fragmentManager,
                tag
            )
        }

        fun parseDisabledDays(dates: List<Date>) : SparseArray<MonthAdapter.CalendarDay> {
            val disabledDays = SparseArray<MonthAdapter.CalendarDay>()
            for(date in dates) {
                val tempCal = Calendar.getInstance()
                val calendar: Calendar = GregorianCalendar()
                calendar.time = date
                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH]
                val dayOfWeek = calendar[Calendar.DATE]
                tempCal.set(year, month, dayOfWeek);
                val key: Int = Utils.formatDisabledDayForKey(
                    tempCal[Calendar.YEAR],
                    tempCal[Calendar.MONTH], tempCal[Calendar.DAY_OF_MONTH]
                )
                disabledDays.put(key, MonthAdapter.CalendarDay(tempCal))
            }
            return disabledDays;
        }

        fun parseSingleDisabledDay(date: Date) : Pair<Int, MonthAdapter.CalendarDay> {
            val tempCal = Calendar.getInstance()
            val calendar: Calendar = GregorianCalendar()
            calendar.time = date
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val dayOfWeek = calendar[Calendar.DATE]
            tempCal.set(year, month, dayOfWeek);
            val key: Int = Utils.formatDisabledDayForKey(
                tempCal[Calendar.YEAR],
                tempCal[Calendar.MONTH], tempCal[Calendar.DAY_OF_MONTH]
            )

            return Pair(first = key, second = MonthAdapter.CalendarDay(tempCal));
        }

    }
}