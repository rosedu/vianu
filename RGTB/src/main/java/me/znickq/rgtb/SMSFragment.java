package me.znickq.rgtb;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by user on 10/19/13.
 */
public class SMSFragment extends Fragment implements View.OnClickListener{


    boolean isGood = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sms_fragment, container, false);

        TextView tv = (TextView) rootView.findViewById(R.id.textView);

        if(PassHandler.getPassForToday(PassHandler.PassType.SMS) != null) {
            isGood = true;
            tv.setText("You're good :)");
            tv.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            Util.setButtonText(rootView, R.id.sms_button, "View");
            Util.setListenerHere(rootView, R.id.sms_button, this);
            Util.setListenerHere(rootView, R.id.net_share_button, this);
            Util.setListenerHere(rootView, R.id.sms_share_button, this);
        } else {
            tv.setText("No pass detected");
            tv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            Util.disableButton(rootView, R.id.sms_share_button);
            Util.disableButton(rootView, R.id.net_share_button);
            Util.setListenerHere(rootView, R.id.sms_button, this);
        }

        getActivity().setTitle("SMS");
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.net_share_button) {

            final PassHandler.Pass p = PassHandler.getPassForToday(PassHandler.PassType.SMS);
            if(p.wasShared()) {
                Toast.makeText(getActivity().getApplicationContext(), "You have already shared this pass!", Toast.LENGTH_SHORT).show();
                return;
            }
            ParseObject po = new ParseObject("Pass");
            po.put("code", p.getCode());
            po.put("date", p.getDate());
            po.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        p.share();
                        SettingsFragment.points += 2;
                        Toast.makeText(getActivity().getApplicationContext(), "Thanks for sharing :)", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Error sharing: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(v.getId() == R.id.sms_button) {
            if(isGood) {
                Intent i = new Intent(v.getContext(), ViewPassActivity.class);
                i.putExtra("pass_to_show",PassHandler.getPassForToday(PassHandler.PassType.SMS));
                startActivity(i);
                return;
            }
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