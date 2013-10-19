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

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

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


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Controller");
                query.whereEqualTo("route", et.getText().toString());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if(e != null) {
                            Toast.makeText(getActivity().getApplicationContext(), "An error occured!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(parseObjects.isEmpty()) {
                            Toast.makeText(getActivity().getApplicationContext(), "No controllers reported!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<ParseObject> toRemove = new ArrayList<ParseObject>();
                        List<ParseObject> toShow = new ArrayList<ParseObject>();

                        java.util.Date d = new java.util.Date();
                        for(ParseObject po : parseObjects) {
                            java.util.Date d2 = po.getCreatedAt();
                            if(d.getDay() == d2.getDay() && d.getMonth() == d2.getMonth() && d.getYear() == d2.getYear()) {
                                toShow.add(po);
                            } else {
                                toRemove.add(po);
                            }
                        }

                        if(!toRemove.isEmpty()) {
                            MassParseDeleter mpd = new MassParseDeleter(toRemove);
                            mpd.start();
                        }

                        ListView lv = (ListView) rootView.findViewById(R.id.controllerList);
                        String[] toPutInListView = new String[toShow.size()];

                        int which = 0;
                        for(ParseObject po : toShow) {
                            toPutInListView[which] = po.getString("location") + " at "+Util.getHourForDate(po.getCreatedAt());
                            which++;
                        }
                        lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, toPutInListView));
                        lv.setVisibility(View.VISIBLE);
                        lv.setSelector(android.R.color.transparent);

                    }
                });
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
                EditText et2 = (EditText) rootView.findViewById(R.id.editText2);
                if(et2.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "You have to pick an approximate location!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), "Thanks for helping others out :)", Toast.LENGTH_SHORT).show();
                if(reportedOnce) {
                }
                reportedOnce = true;

                ParseObject po = new ParseObject("Controller");
                po.put("route",et.getText().toString());
                po.put("location", et2.getText().toString());
                po.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Toast.makeText(getActivity().getApplicationContext(), "Error occured!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        getActivity().setTitle("Controllers");
        return rootView;
    }

    private class MassParseDeleter extends DeleteCallback {

        private List<ParseObject> toRemove;
        private int current = 0;

        public MassParseDeleter(List<ParseObject> toRemove) {
            this.toRemove = toRemove;
        }

        public void start() {
            toRemove.get(0).deleteInBackground(this);
        }

        @Override
        public void done(ParseException e) {
            current++;
            if(current < toRemove.size()) {
                toRemove.get(current).deleteInBackground(this);
            }
        }
    }
}