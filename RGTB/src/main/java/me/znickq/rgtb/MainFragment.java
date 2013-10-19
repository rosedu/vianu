package me.znickq.rgtb;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by ZNickq on 10/19/13.
 */
public class MainFragment extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        if(PassHandler.getPassForToday() != null) {
            disableButton(rootView, R.id.second_button);
            disableButton(rootView, R.id.third_button);
            setListenerHere(rootView, R.id.first_button);
        } else {
            disableButton(rootView, R.id.first_button);
            setListenerHere(rootView, R.id.second_button);
            setListenerHere(rootView, R.id.third_button);
        }

        getActivity().setTitle("Main");
        return rootView;
    }

    private void setListenerHere(View rootView, int id) {
        Button b = (Button) rootView.findViewById(id);
        b.setOnClickListener(this);
    }

    private void disableButton(View rootView, int id) {
        Button oneButton = (Button) rootView.findViewById(id);
        oneButton.setClickable(false);
        oneButton.setVisibility(View.GONE);
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

        }
        if(v.getId() == R.id.second_button) {

        }
    }
}
