package me.znickq.rgtb;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by user on 10/19/13.
 */
public class ControllerFragment extends Fragment {
    private static boolean reportedOnce = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.controller_fragment, container, false);
        Button buttonCheck= (Button) rootView.findViewById(R.id.checkbutton);
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) rootView.findViewById(R.id.editText);
                if(et.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "You have to pick a route!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ListView lv = (ListView) rootView.findViewById(R.id.controllerList);
                String[] s = new String[2];
                s[0] = "Piata Victoriei sa moara mama e un prost";
                s[1] = "Piata Romana e d-ala cu mustata de fecior";
                lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, s));
                lv.setVisibility(View.VISIBLE);
                lv.setSelector(android.R.color.transparent);
            }
        });
        Button buttonReport= (Button) rootView.findViewById(R.id.reportbutton);
        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) rootView.findViewById(R.id.editText);
                if(et.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "You have to pick a route!", Toast.LENGTH_SHORT).show();
                    return;
                }
                et = (EditText) rootView.findViewById(R.id.editText2);
                if(et.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "You have to pick an approximate location!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), "Thanks for helping others out :)", Toast.LENGTH_SHORT).show();
                if(reportedOnce) {
                    return;
                }
                reportedOnce = true;
            }
        });
        getActivity().setTitle("Controllers");
        return rootView;
    }

}