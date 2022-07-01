package com.antique.events.ui.utils.helper

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    const val GOOD_FORMAT_TIME_HM = "hh:mm aa"
    const val GOOD_FORMAT_TIME_HMS = "hh:mm:ss aa"
    const val GOOD_FORMAT_TIME_HM_ONLY = "hh:mm"
    const val GOOD_FORMAT_TIME_ZONE = "aa"
    const val GOOD_FORMAT_DATE = "MMM dd, yyyy"
    const val GOOD_FORMAT_DATE_2 = "MMM dd, yyyy hh:mm aa"
    const val GOOD_FORMAT_MONTH_DAY = "MMM dd"
    const val GOOD_FORMAT_DATE_3 = "dd MMM yyyy"
    const val GOOD_FORMAT_DATE_4 = "MMM yyy"
    const val GOOD_FORMAT_DATE_5 = "EEEE, MMM dd, yyyy"
    const val RAW_FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val RAW_FORMAT_DATE = "yyyy-MM-dd"
    const val RAW_FORMAT_DATE_2 = "yyyy-MM-dd'T'HH:mm:ss.SSS+0000"
    const val RAW_FORMAT_DATE_3 = "yyyy-MM-dd'T'HH:mm:ss"
    const val RAW_FORMAT_DATE_4 = "yyyy-MM-dd HH:mm"
    const val RAW_FORMAT_DATE_5 = "yyyy-MM-dd HH:mm aa"
    const val RAW_FORMAT_DATE_6 = "yyyy-MM-dd hh:mm aa"
    const val API_FORMAT_DATE = "MM/dd/yyyy"
    const val API_FORMAT_DATE_2 = "MM/dd/yyyy HH:mm aa z"
    const val API_FORMAT_DATE_3 = "yyyy-MM-dd HH:mm:ss"
    const val LEAVE_FORMAT_DATE = "MM/dd/yyyy hh:mm aa"
    const val SKILL_FORMAT_DATE = "yyyy-MM-dd'Z'"
    const val GOOD_FORMAT_DAY_NUMBER = "dd"
    const val GOOD_FORMAT_YEAR_NUMBER = "yyyy"
    const val GOOD_FORMAT_DAY_ONLY = "EEE"
    const val GOOD_FORMAT_MONTH_ONLY = "MM"
    const val GOOD_FORMAT_MONTH_NAME = "MMM"
    const val TIME_STAMP_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss aa"
    const val REFERENCE_ID_STAMP_TIME_STAMP_FORMAT = "yyyyMMddHHmmssmmm"
    val MONTHS = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    val WEEKDAYS = arrayOf(
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday"
    )

    fun isSameDay(date1: Date, date2: Date, format: String) : Boolean {
        val date1format = formatDate(date1, format);
        val date2format = formatDate(date2, format);
        return date1format == date2format;
    }

    fun isPastDate(date1: Date, date2: Date) : Long {
        val diff: Long = date1.time - date2.time
        val minutes = diff / 1000 / 60
        return minutes;
    }

    fun isWithinDates(rawDate: Date, start: Date, end: Date) : Boolean {
        val startDifference = isPastDate(rawDate, start);
        val endDifference = isPastDate(rawDate, end);

        // 10:00 - 10:30
        // 9:00 -  -60, -90 : false, true = false
        // 10:20 -  20, -10 : true, true = true
        // 10:45 - 45, 15 : true, false = false

        if(startDifference >= 0 && endDifference <= 0){
            return true;
        }
        return false;
    }

    fun defaultMinDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = 1950
        calendar[Calendar.MONTH] = 0
        calendar[Calendar.DAY_OF_MONTH] = 1
        return calendar
    }

    fun getDateFromTime(date1: Date, time: String): Date {
        val newFormat = formatDate(date1, GOOD_FORMAT_DATE);
        val dateString = "${newFormat} ${time}";
        val newDate = parseDate(dateString, GOOD_FORMAT_DATE_2);
        return newDate
    }

    fun formatDate(date: Date?, format: String?): String {
        val simpleDateFormat = SimpleDateFormat(format)
        return simpleDateFormat.format(date)
    }

    @Throws(ParseException::class)
    fun parseDate(date: String?, format: String?): Date {
        val simpleDateFormat = SimpleDateFormat(format)
        return if (date == null) {
            throw ParseException("Null date", 0)
        } else {
            simpleDateFormat.parse(date)
        }
    }

    @Throws(ParseException::class)
    fun parseDate(date: String?, format: String?, out: String?): String {
        val simpleDateFormat = SimpleDateFormat(format)
        return if (date == null) {
            throw ParseException("Null date", 0)
        } else {
            val fmtOut = SimpleDateFormat(out)
            val date = simpleDateFormat.parse(date);
            fmtOut.format(date);
        }
    }


    /**
     * Set the time to the start time of the day 00:00:00:000
     *
     * @param calendar
     */
    fun toStartTimeOfDay(calendar: Calendar) {
        /* reset from date to 00:00:00:000 */
        calendar[Calendar.HOUR] = 0
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
    }

    /**
     * Set the time to the last time of the day 23:59:59:999
     *
     * @param calendar
     */
    fun toLastTimeOfDay(calendar: Calendar) {
        /* reset from date to 00:00:00:000 */
        calendar[Calendar.HOUR] = 23
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
    }

    fun ago(from: Date, to: Date): String {
        val diff = to.time - from.time
        val diffDays = diff / (24 * 60 * 60 * 1000)
        val diffHours = diff / (60 * 60 * 1000) % 24
        val diffMinutes = diff / (60 * 1000) % 60
        val diffSeconds = diff / 1000 % 60
        if (diffDays >= 365) {
            return String.format("%s year%s ago", diffDays, if (diffDays >= 730) "s" else "")
        }
        if (diffDays >= 30.4167) {
            return String.format(
                "%s month%s ago",
                diffDays,
                if (diffDays >= 60.8334) "s" else ""
            )
        }
        if (diffDays > 1) {
            return String.format("%s day%s ago", diffDays, "s")
        }
        if (diffDays == 1L) {
            return "Yesterday"
        }
        if (diffHours > 0) {
            return String.format("%s hour%s ago", diffHours, if (diffHours > 1) "s" else "")
        }
        if (diffMinutes == 1L) {
            return "A minute ago"
        }
        if (diffMinutes > 1 && diffMinutes < 10) {
            return "Moments ago"
        }
        return if (diffMinutes > 0) {
            String.format("%s minute%s ago", diffMinutes, "s")
        } else "Just now"
    }

    /**
     * source: https://memorynotfound.com/calculate-age-from-date-of-birth-in-java/
     *
     * @param dateOfBirth
     * @return age
     */
    fun getAge(dateOfBirth: Date?): Int {
        val today = Calendar.getInstance()
        val birthDate = Calendar.getInstance()
        birthDate.time = dateOfBirth
        require(!birthDate.after(today)) { "You don't exist yet" }
        val todayYear = today[Calendar.YEAR]
        val birthDateYear = birthDate[Calendar.YEAR]
        val todayDayOfYear = today[Calendar.DAY_OF_YEAR]
        val birthDateDayOfYear = birthDate[Calendar.DAY_OF_YEAR]
        val todayMonth = today[Calendar.MONTH]
        val birthDateMonth = birthDate[Calendar.MONTH]
        val todayDayOfMonth = today[Calendar.DAY_OF_MONTH]
        val birthDateDayOfMonth = birthDate[Calendar.DAY_OF_MONTH]
        var age = todayYear - birthDateYear

        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
        if (birthDateDayOfYear - todayDayOfYear > 3 || birthDateMonth > todayMonth) {
            age--

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        } else if (birthDateMonth == todayMonth && birthDateDayOfMonth > todayDayOfMonth) {
            age--
        }
        return age
    }
}