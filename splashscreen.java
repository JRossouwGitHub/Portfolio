package studio.crunchyiee.degreeprogrammemapper;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class splashscreen extends AppCompatActivity {

    private AlphaAnimation fading;
    private ImageView winteclogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        fading = new AlphaAnimation(0.0f,1.0f);
        winteclogo = findViewById(R.id.imageView_wintecLogo);

        winteclogo.startAnimation(fading);
        fading.setDuration(3000);
        fading.setFillAfter(true);

        final Intent intent = new Intent(this, MainActivity.class);

        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //checkFirstRun();
                startActivity(intent);

            }
        }.start();

    }

    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        int currentVersionCode = BuildConfig.VERSION_CODE;

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);



        if (currentVersionCode == savedVersionCode) {

            //startActivity(intent); use this on final form
            //showDisclaimer();

        } else if (savedVersionCode == DOESNT_EXIST) {

            //showDisclaimer();

        } else if (currentVersionCode > savedVersionCode) {

           // showDisclaimer();
        }

        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }
/*      //MOVED DISCLAIMER TO MAIN PAGE
    public void showDisclaimer(){
        final Dialog disclaimerDialog;
        TextView disclaimerTitle, disclaimerMsg;
        Button dismissDis;
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        disclaimerDialog = new Dialog(this);

        disclaimerDialog.setContentView(R.layout.disclaimer_message);
        dismissDis = disclaimerDialog.findViewById(R.id.btn_i_understand);

        disclaimerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        disclaimerDialog.show();

        dismissDis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                disclaimerDialog.dismiss();
                startActivity(intent);
            }
        });
    }*/
}

