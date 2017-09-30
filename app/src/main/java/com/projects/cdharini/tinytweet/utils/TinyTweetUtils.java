package com.projects.cdharini.tinytweet.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dharinic on 9/27/17.
 */

public class TinyTweetUtils {

    /*
     * From sample code on codepath
     * getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
     */
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }

    public static String getDateStringForDetailTweet(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String dateStr = "";
        try {
            Date parsedDate = sf.parse(rawJsonDate);
            sf = new SimpleDateFormat("MM/dd/yyyy, h:mm a zzz", Locale.ENGLISH);
            dateStr = sf.format(parsedDate).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }
}
