package me.znickq.rgtb;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by user on 10/19/13.
 */
public class SMSFragment extends Fragment implements View.OnClickListener{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sms_fragment, container, false);

        TextView tv = (TextView) rootView.findViewById(R.id.textView);

        if(PassHandler.getPassForToday(PassHandler.PassType.SMS) != null) {
            tv.setText("You're good :)");
            tv.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            Util.disableButton(rootView, R.id.sms_button);
            Util.setListenerHere(rootView, R.id.sms_share_button, this);
        } else {
            tv.setText("No pass detected");
            tv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            Util.disableButton(rootView, R.id.sms_share_button);
            Util.setListenerHere(rootView, R.id.sms_button, this);
        }

        getActivity().setTitle("SMS");
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sms_button) {
            PassHandler.obtainPass(this.getActivity(), PassHandler.PassType.SMS);
        }
        if(v.getId() == R.id.sms_share_button) {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body", Util.getSmsFor(PassHandler.getPassForToday(PassHandler.PassType.SMS)));
            sendIntent.setData(Uri.parse("sms:"));
            startActivity(sendIntent);
        }
    }
}