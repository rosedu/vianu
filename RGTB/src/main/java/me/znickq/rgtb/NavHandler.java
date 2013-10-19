package me.znickq.rgtb;

import android.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ZNickq on 10/19/13.
 */
public class NavHandler {
    private static List<String> navTitlesList = Arrays.asList("Main", "Points", "SMS", "Controllers", "Settings");

    public static List<String> getMenusForAdapter() {
        return navTitlesList;
    }


    public static Fragment getFragmentForMenu(int whichMenu) {

        switch(whichMenu) {
            case 0:
                return new MainFragment();
            case 2:
                return new SMSFragment();
            case 3:
                return new ControllerFragment();
            default:
                throw new UnsupportedOperationException("Wat r u doin");

        }
    }

}
