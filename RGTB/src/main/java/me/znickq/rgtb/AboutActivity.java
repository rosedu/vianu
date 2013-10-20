package me.znickq.rgtb;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ((Button) findViewById(R.id.sfeedback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"rgtb@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "RGTB Feedback");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
    
}
