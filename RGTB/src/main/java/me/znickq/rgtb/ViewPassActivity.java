package me.znickq.rgtb;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewPassActivity extends Activity {

    boolean isFake = false;
    int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewpass);

        TextView tv = (TextView) findViewById(R.id.passview);
        TextView tv2 = (TextView) findViewById(R.id.receivedText);
        EditText et = (EditText) findViewById(R.id.editText);

        Button sButton = (Button) findViewById(R.id.send_button);

        Bundle extras = getIntent().getExtras();
        PassHandler.Pass pass = (PassHandler.Pass) extras.get("pass_to_show");

        if(extras.containsKey("grigoras_mode")) {
            tv.setText("Nu poate cumva domnul Grigorescu sa va faca sa va razganditi asupra amenzii?");
            tv2.setVisibility(View.GONE);
            sButton.setVisibility(View.GONE);
            et.setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.domnulgrigorescu)).setVisibility(View.VISIBLE);
            return;
        }

        if(extras.containsKey("fake")) {
            isFake = true;
        }

        tv.setText(Util.getSmsFor(pass));


        tv2.setText("Received "+getReceivedTime(pass.getDate()));
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SettingsFragment.doubleClickEnabled) {
                    return;
                }
                clicks++;
                if(clicks == 2) {
                    PassHandler.obtainPass(ViewPassActivity.this, SettingsFragment.pt, true);
                }
            }
        });
        if(extras.containsKey("was_fake")) {
            et.setHint("Answer ;)");
        }


    }

    private String getReceivedTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        return dateFormat.format(date);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_pass, menu);
        return true;
    }
    
}
