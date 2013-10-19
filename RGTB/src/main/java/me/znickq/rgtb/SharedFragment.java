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

/**
 * Created by user on 10/19/13.
 */
public class SharedFragment extends Fragment implements View.OnClickListener{


    boolean isGood = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shared_fragment, container, false);

        Util.updatePoints(this.getActivity(), rootView);

        TextView tv = (TextView) rootView.findViewById(R.id.textView);

        if(PassHandler.getPassForToday(PassHandler.PassType.NET) != null) {
            isGood = true;
            tv.setText("You're good :)");
            tv.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            Util.setButtonText(rootView, R.id.get_button, "View");
            Util.setListenerHere(rootView, R.id.get_button, this);
        } else {
            tv.setText("No pass detected");
            tv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            Util.setListenerHere(rootView, R.id.get_button, this);
        }

        getActivity().setTitle("Shared");
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.get_button) {
            if(isGood) {
                Intent i = new Intent(v.getContext(), ViewPassActivity.class);
                i.putExtra("pass_to_show",PassHandler.getPassForToday(PassHandler.PassType.NET));
                startActivity(i);
                return;
            }
            PassHandler.obtainPass(this.getActivity(), PassHandler.PassType.NET);
        }
    }
}