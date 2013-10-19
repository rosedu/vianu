package me.znickq.rgtb;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZNickq on 10/19/13.
 */
public class PassHandler {

    public enum PassType {
        SMS, NET;
    };

    private static Map<PassType, Pass> loaded = new HashMap<PassType, Pass>();

    public static void init() {
        Date today = new Date();
        boolean isOld = false;
        for(PassType p : PassType.values()) {
            Pass pp = loaded.get(p);
            if(!today.equals(pp)) {
                isOld = true;
            }
        }
        if(isOld) {
            loaded.clear();
        }
    }

    public static void obtainPass(Activity ac, PassType pt) {
        if(pt == PassType.NET) {
            loaded.put(PassType.NET, new Pass(PassType.NET, new Date(), "NETCODE"));

            // Create a new fragment and specify the planet to show based on position
            Fragment fragment = NavHandler.getFragmentForMenu(1);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = ac.getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            Toast.makeText(ac.getApplicationContext(), "Received shared pass", Toast.LENGTH_SHORT).show();
        }
        if(pt == PassType.SMS) {
            loaded.put(PassType.SMS, new Pass(PassType.SMS, new Date(), "SMSCODE"));

            // Create a new fragment and specify the planet to show based on position
            Fragment fragment = NavHandler.getFragmentForMenu(2);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = ac.getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            Toast.makeText(ac.getApplicationContext(), "Received sms pass", Toast.LENGTH_SHORT).show();
        }
    }

    public static Pass getPassForToday() {
        if(loaded.containsKey(PassType.NET)) {
            return loaded.get(PassType.NET);
        }
        return loaded.get(PassType.SMS);
    }


    public static class Pass implements Parcelable{
        private PassType source;
        private Date date;
        private String code;


        public String getCode() {
            return code;
        }

        public String getFormattedDate() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.format(date);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        // write your object's data to the passed-in Parcel
        public void writeToParcel(Parcel out, int flags) {
            out.writeString(source.name());
            out.writeLong(date.getTime());
            out.writeString(code);

        }

        // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
        public static final Parcelable.Creator<Pass> CREATOR = new Parcelable.Creator<Pass>() {
            public Pass createFromParcel(Parcel in) {
                return new Pass(in);
            }

            public Pass[] newArray(int size) {
                return new Pass[size];
            }
        };

        public Pass(PassType pt, Date date, String code) {
            this.date = date;
            this.source = pt;
            this.code = code;
        }

        // example constructor that takes a Parcel and gives you an object populated with it's values
        private Pass(Parcel in) {
            source = PassType.valueOf(in.readString());
            date = new Date(in.readLong());
            code = in.readString();
        }

        public Date getDate() {
            return date;
        }
    }
}
