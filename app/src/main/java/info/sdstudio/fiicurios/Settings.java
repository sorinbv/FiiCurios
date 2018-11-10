package info.sdstudio.fiicurios;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Switch swNotificari = findViewById(R.id.swNotification);
        boolean notif = MainActivity.sharedPref.getBoolean("notificari", true);
        if (notif) {
            swNotificari.setChecked(true);
        } else {
            swNotificari.setChecked(false);
        }

        swNotificari.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = MainActivity.sharedPref.edit();
                if (isChecked) {
                    editor.putBoolean("notificari", true);
                } else {
                    editor.putBoolean("notificari", false);
                }
                editor.apply();
            }
        });


        Switch swRandom = findViewById(R.id.swRandom);
        boolean aleatoriu = MainActivity.sharedPref.getBoolean("aleatoriu", true);
        if (aleatoriu) {
            swRandom.setChecked(true);
        } else {
            swRandom.setChecked(false);
        }

        swRandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = MainActivity.sharedPref.edit();
                if (isChecked) {
                    editor.putBoolean("aleatoriu", true);
                } else {
                    editor.putBoolean("aleatoriu", false);
                }
                editor.apply();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
