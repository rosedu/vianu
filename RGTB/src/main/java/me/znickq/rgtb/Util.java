package me.znickq.rgtb;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by ZNickq on 10/19/13.
 */
public class Util {

    public static void setListenerHere(View rootView, int id, View.OnClickListener toSet) {
        Button b = (Button) rootView.findViewById(id);
        b.setOnClickListener(toSet);
    }

    public static void disableButton(View rootView, int id) {
        Button oneButton = (Button) rootView.findViewById(id);
        oneButton.setClickable(false);
        oneButton.setVisibility(View.GONE);
    }

    public static String getSmsFor(PassHandler.Pass pass) {
        return "RATB: Abonamentul Dvs de 1 zi a fost activat. Valabil azi "+ pass.getDate() + " pe toate liniile urbane RATB. Va dorim calatorie placuta! Cod confirmare: "+pass.getCode();
    }

    public static void setButtonText(View rootView, int id, String text) {
        Button oneButton = (Button) rootView.findViewById(id);
        oneButton.setText(text);
    }

    public static void updatePoints(Activity ac, View rootView) {
        TextView tv = (TextView) rootView.findViewById(R.id.pointsLabel);
        tv.setText(SettingsFragment.points+" points");

        if(SettingsFragment.points == 0) {
            tv.setTextColor(ac.getResources().getColor(android.R.color.holo_red_light));
            return;
        }
        if(SettingsFragment.points <= 4) {
            tv.setTextColor(ac.getResources().getColor(android.R.color.holo_orange_light));
            return;
        }
        tv.setTextColor(ac.getResources().getColor(android.R.color.holo_green_light));
    }

    public static String getRandomCode() {
        CharSequence cs = "LLLLCC"; //MADJ57
        StringBuilder sb = new StringBuilder();
        Random r = new Random();

        for(int i=0;i<cs.length();i++) {
            if(cs.charAt(i) == 'L') {
                char c = (char)(r.nextInt(26) + 'a');
                sb.append(c);
            } else {
                sb.append(""+ r.nextInt(10));
            }
        }

        return sb.toString().toUpperCase(Locale.ENGLISH);
    }

    public static String getTodayTime() {
            Date d = new Date();
            return ""+d.getHours()+":"+d.getMinutes();

    }

    public static String getTodayDate() {
        SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        return sdt.format(d);
    }

    public static String getHourForDate(Date createdAt) {
        SimpleDateFormat sdt = new SimpleDateFormat("HH:mm");
        //Log.d("rgtb", "Date is "+createdAt);
        return sdt.format(createdAt);
    }
}
