package me.znickq.rgtb;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

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
        return "RATB: Abonamentul Dvs de 1 zi a fost activat. Valabil azi "+ pass.getFormattedDate() + " pe toate liniile urbane RATB. Va dorim calatorie placuta! Cod confirmare: "+pass.getCode();
    }
}
