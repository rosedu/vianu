package me.znickq.rgtb;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by ZNickq on 10/19/13.
 */
public class MainFragment extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        Util.updatePoints(this.getActivity(), rootView);

        TextView tv = (TextView) rootView.findViewById(R.id.textView);

        if(PassHandler.getPassForToday() != null) {
            tv.setText("You're good :)");
            tv.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            Util.disableButton(rootView, R.id.second_button);
            Util.disableButton(rootView, R.id.third_button);
            Util.disableButton(rootView, R.id.forth_button);
            Util.setListenerHere(rootView, R.id.first_button, this);
        } else {
            tv.setText("No pass detected");
            tv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            Util.disableButton(rootView, R.id.first_button);
            Util.setListenerHere(rootView, R.id.second_button, this);
            Util.setListenerHere(rootView, R.id.third_button, this);
            Util.setListenerHere(rootView, R.id.forth_button, this);
        }

        getActivity().setTitle("Main");
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.first_button) {
            PassHandler.Pass p = PassHandler.getPassForToday();
            Intent i = new Intent(v.getContext(), ViewPassActivity.class);
            i.putExtra("pass_to_show",p);
            startActivity(i);
        }
        if(v.getId() == R.id.second_button) {
            PassHandler.obtainPass(getActivity(), PassHandler.PassType.NET);

        }
        if(v.getId() == R.id.third_button) {
            PassHandler.obtainPass(getActivity(), PassHandler.PassType.SMS);
        }
        if(v.getId() == R.id.forth_button) {
            Intent it = new Intent(getActivity().getApplicationContext(), ViewPassActivity.class);
            it.putExtra("pass_to_show", new PassHandler.Pass(PassHandler.PassType.SMS, new Date(), Util.getRandomCode()));
            it.putExtra("fake", true);
            startActivity(it);
        }
    }
}
