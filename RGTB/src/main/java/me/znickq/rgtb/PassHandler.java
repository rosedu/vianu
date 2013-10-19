package me.znickq.rgtb;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by ZNickq on 10/19/13.
 */
public class PassHandler {

    public enum PassType {
        SMS, NET;
    };

    private static Map<PassType, Pass> loaded = new HashMap<PassType, Pass>();

    public static void save(Activity ac) throws Exception {
        DataOutputStream dos = new DataOutputStream(ac.openFileOutput("passes.bin", Context.MODE_PRIVATE));
        dos.writeInt(loaded.size());
        for(PassType pt : loaded.keySet()) {
            Pass ps = loaded.get(pt);
            dos.writeUTF(pt.name());
            dos.writeUTF(ps.date);
            dos.writeUTF(ps.code);
            dos.writeBoolean(ps.wasShared);
        }
        dos.flush();
        dos.close();
    }

    public static void init(Activity ac) {
        //Log.d("rgtb", "Started init");
        try {
            DataInputStream dis = new DataInputStream(ac.openFileInput("passes.bin"));
            int howMany = dis.readInt();
            //Log.d("rgtb", "Got init: "+howMany);

            for(int i=1;i<=howMany;i++) {
                String passTypeName = dis.readUTF();
                String date = dis.readUTF();
                String code = dis.readUTF();
                boolean wasShared = dis.readBoolean();
                PassType passType = PassType.valueOf(passTypeName);
                //Log.d("rgtb", "Putting in passType "+passType);
                loaded.put(passType, new Pass(passType, date, code, wasShared));
            }
            //Log.d("rgtb", "Finished init: "+howMany);
            dis.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        Date today = new Date();
        String todayDate = Util.getTodayDate();
        boolean isOld = false;
        for(PassType p : loaded.keySet()) {
            Pass pp = loaded.get(p);
            Log.d("rgtb", ""+pp);
            if(!todayDate.equals(pp.date)) {
                isOld = true;
            }
        }
        if(isOld) {
            loaded.clear();
        }
    }

    public static void clearPass(PassType pt) {
        loaded.remove(pt);
    }

    public static void obtainPass(Activity ac, PassType pt) {
        obtainPass(ac, pt, false);
    }

    private static final Random ra = new Random();
    public static void obtainPass(final Activity ac, PassType pt, final boolean showDirectly) {
        if(pt == PassType.NET) {
            if(SettingsFragment.points == 0) {
                if(showDirectly) {
                    Intent it = new Intent(ac.getApplicationContext(), ViewPassActivity.class);
                    it.putExtra("grigoras_mode", true);
                    ac.startActivity(it);
                    return;
                }
                Toast.makeText(ac.getApplicationContext(), "You do not have any points!", Toast.LENGTH_SHORT).show();
                return;
            }

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Pass");
            System.out.println("Checking date is "+Util.getTodayDate());
            query.whereEqualTo("date", Util.getTodayDate());
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> passList, com.parse.ParseException e) {
                    if (e == null) {
                        if(passList.isEmpty()) {
                            Toast.makeText(ac.getApplicationContext(), "No shared passes available :(", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int which = ra.nextInt(passList.size());
                        ParseObject chose = passList.get(which);
                        Pass p = new Pass(PassType.NET, chose.getString("date"), chose.getString("code"));
                        loaded.put(PassType.NET, p);

                        if(showDirectly) {
                            Intent it = new Intent(ac.getApplicationContext(), ViewPassActivity.class);
                            it.putExtra("pass_to_show", p);
                            it.putExtra("was_fake", true);
                            ac.startActivity(it);
                            return;
                        }

                        // Create a new fragment and specify the planet to show based on position
                        Fragment fragment = NavHandler.getFragmentForMenu(1);

                        // Insert the fragment by replacing any existing fragment
                        FragmentManager fragmentManager = ac.getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();
                        SettingsFragment.points--;
                        Toast.makeText(ac.getApplicationContext(), "Received shared pass!", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                        Toast.makeText(ac.getApplicationContext(), "Unable to receive pass!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(pt == PassType.SMS) {
            loaded.put(PassType.SMS, new Pass(PassType.SMS, Util.getTodayDate(), "SMSCODE"+ra.nextInt(100)));

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

    public static Pass getPassForToday(PassType toRet) {
        return loaded.get(toRet);
    }


    public static class Pass implements Parcelable{
        private PassType source;
        private String date;
        private String code;
        private boolean wasShared = false;


        public String getCode() {
            return code;
        }

        public boolean wasShared() {
            return wasShared;
        }

        public void share() {
            wasShared = true;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        // write your object's data to the passed-in Parcel
        public void writeToParcel(Parcel out, int flags) {
            out.writeString(source.name());
            out.writeString(date);
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

        public Pass(PassType pt, String date, String code, boolean wasShared) {
            this.date = date;
            this.source = pt;
            this.code = code;
            this.wasShared = wasShared;
        }

        public Pass(PassType pt, String date, String code) {
            this.date = date;
            this.source = pt;
            this.code = code;
        }

        // example constructor that takes a Parcel and gives you an object populated with it's values
        private Pass(Parcel in) {
            source = PassType.valueOf(in.readString());
            date = in.readString();
            code = in.readString();
        }

        public String getDate() {
            return date;
        }
    }
}
