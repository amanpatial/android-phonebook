package com.above_inc.amanpatial.phonebook;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class PhoneBookDisplayActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook_display);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PhoneBookFragment())
                    .commit();
        }
    }
}
