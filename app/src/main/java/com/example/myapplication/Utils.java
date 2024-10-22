package com.example.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.*;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Utils {


    public static String date() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int today = cal.get(cal.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 0);

        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd");
        String strDate = mFormat.format(cal.getTime());
        return strDate;
    }

    public static String dateheure() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int today = cal.get(cal.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 0);

        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        String strDate = mFormat.format(cal.getTime());
        return strDate;
    }

    public static String getUUID() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    public static long timestamp_seconde() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int today = cal.get(cal.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 0);


        return cal.getTimeInMillis() / 1000;
    }

    public static long timestamp_milliseconde() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int today = cal.get(cal.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 0);


        return cal.getTimeInMillis();
    }


    public static Button setupBtn(String darkblue, MainActivity mainActivity, String value, String tag, int width) {
        Button b = new Button(mainActivity);
        b.setText(value);
        b.setTag(tag);
        b.setTextSize(13);
        b.setLetterSpacing((float) 0.11);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                width,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 3, 0);
        b.setBackgroundColor(Color.parseColor("#FF6200EE"));
        b.setLayoutParams(params);
        b.setBackgroundResource(R.drawable.rounded_btn);
        b.setTextColor(Color.WHITE);
        return b;
    }

    public static Button setupBtnProduit(String darkblue, MainActivity mainActivity, String value, String tag, int height) {
        Button b = new Button(mainActivity);

        b.setText(value);
        b.setTag(tag);
        b.setTextSize(13);
        //b.setLetterSpacing((float)0.11);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height
        );
        params.weight = 1;
        params.gravity = Gravity.FILL_VERTICAL;
        b.setMinHeight(height);
        params.setMargins(0, 0, 3, 0);
        b.setBackgroundColor(Color.parseColor("#FF6200EE"));
        b.setLayoutParams(params);
        b.setBackgroundResource(R.drawable.rounded_btn);
        b.setTextColor(Color.WHITE);

        return b;
    }
}
