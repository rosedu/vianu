package me.znickq.rgtb;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by ZNickq on 10/19/13.
 */
public class SettingsFragment extends Fragment {

    public static int points = 2;
    public static boolean doubleClickEnabled = true;
    public static PassHandler.PassType pt = PassHandler.PassType.NET;

    public Spinner fakeSource;
    public CheckBox fakeEnabled;

    private EditText debug;

    @Override
    public void onPause() {
        super.onPause();

        doubleClickEnabled = fakeEnabled.isChecked();

        if(!(debug.getText().toString().isEmpty())) {
            points = Integer.parseInt(debug.getText().toString());
        }

        if(fakeSource.getSelectedItemPosition() == 0) {
            pt = PassHandler.PassType.SMS;
        } else {
            pt = PassHandler.PassType.NET;
        }


        try {
        saveData(getActivity());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);


        fakeEnabled = (CheckBox) rootView.findViewById(R.id.doubletapcheckbox);

        fakeEnabled.setChecked(doubleClickEnabled);

        fakeSource = (Spinner) rootView.findViewById(R.id.spinner);

        debug = (EditText) rootView.findViewById(R.id.pointsDebug);


        String[] toPut = new String[2];
        toPut[0] = "SMS";
        toPut[1] = "Net";
        ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, toPut);
        adapter0.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        fakeSource.setAdapter(adapter0);

        if(pt == PassHandler.PassType.NET) {
            fakeSource.setSelection(1);
        }

        getActivity().setTitle("Settings");
        return rootView;
    }

    public static void loadData(Activity ac) throws Exception{
        FileInputStream fis = ac.openFileInput("data.bin");
        DataInputStream dis = new DataInputStream(fis);

        points = dis.readInt();
        doubleClickEnabled = dis.readBoolean();
        pt = PassHandler.PassType.valueOf(dis.readUTF());
        dis.close();
    }

    public static void saveData(Activity ac) throws Exception{
        FileOutputStream fos = ac.openFileOutput("data.bin", Context.MODE_PRIVATE);
        DataOutputStream dos = new DataOutputStream(fos);

        dos.writeInt(points);
        dos.writeBoolean(doubleClickEnabled);
        dos.writeUTF(pt.name());
        dos.flush();
        dos.close();
    }
}
