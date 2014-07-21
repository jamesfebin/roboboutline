package com.boutline.sports.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 18/07/14.
 */
public class FormateTime {

    public FormateTime()
    {

    }

   public String formatUnixtime(String unixtime,String format)
   {


       Date date = new Date ();
       date.setTime(Long.parseLong(unixtime)*1000);
       SimpleDateFormat sdf = new SimpleDateFormat(format); // the format of your date
       String formattedTime = sdf.format(date);


       return formattedTime;


   }


}
