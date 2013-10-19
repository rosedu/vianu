package me.znickq.rgtb;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by user on 10/19/13.
 */
public class ControllerFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.controller_fragment, container, false);
        Button buttoncheck= (Button) rootView.findViewById(R.id.checkbutton);
        buttoncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button buttonreport= (Button) rootView.findViewById(R.id.reportbutton);
        buttoncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        getActivity().setTitle("Controllers");
        return rootView;
    }

}