package me.znickq.rgtb;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewPassActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewpass);

        TextView tv = (TextView) findViewById(R.id.passview);

        Bundle extras = getIntent().getExtras();
        PassHandler.Pass pass = (PassHandler.Pass) extras.get("pass_to_show");

        tv.setText("RATB: Abonamentul Dvs de 1 zi a fost activat. Valabil azi "+ pass.getFormattedDate() + " pe toate liniile urbane RATB. Va dorim calatorie placuta! Cod confirmare: "+pass.getCode());


        TextView tv2 = (TextView) findViewById(R.id.receivedText);
        tv2.setText("Received "+getReceivedTime(pass.getDate()));


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
