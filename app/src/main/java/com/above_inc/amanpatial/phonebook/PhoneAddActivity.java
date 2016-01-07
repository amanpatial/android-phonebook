package com.above_inc.amanpatial.phonebook;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


//Add phone number
public class PhoneAddActivity extends Activity {
    private static final String PHONE_ADD_API_URL = "http://10.0.2.2:3000/api/phonebooks";
    EditText firstNameET;
    EditText lastNameET;
    EditText phoneNumberET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_phonebook);

        firstNameET = (EditText) findViewById(R.id.txtFirstName);
        lastNameET = (EditText) findViewById(R.id.txtLastName);
        phoneNumberET = (EditText) findViewById(R.id.txtPhoneNumber);
    }

    public void saveToPhoneBook(View view) {
        //Phonebook will receive input data
        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        String phonebook = phoneNumberET.getText().toString();

        if (this.isValid()) {
            //Spawn async background thread to call Rest API
            AddPhoneBookTask addPhonebook;
            addPhonebook = new AddPhoneBookTask();
            addPhonebook.execute(firstName, lastName, phonebook);
        }
    }

    public boolean isValid() {
        //validation of input values
        if (!Utility.hasText(firstNameET)) return false;
        if (!Utility.hasText(lastNameET)) return false;
        return Utility.hasText(phoneNumberET);
    }

    public void setDefaultValues() {
        //Clear all inputs and set to default values
        firstNameET.setText("");
        lastNameET.setText("");
        phoneNumberET.setText("");
    }

    public class AddPhoneBookTask extends AsyncTask<String, Void, String> {
        //Push data to API asynchronously using android background service

        private final String LOG_TAG = AddPhoneBookTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Saving...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONObject jsonBody;
            String requestBody;
            HttpURLConnection urlConnection = null;
            try {

                /* Create request body */
                JSONObject jsonParam = new JSONObject();

                jsonParam.put("firstname", params[0]);
                jsonParam.put("lastname", params[1]);
                jsonParam.put("phonenumber", params[2]);

                jsonBody = new JSONObject();
                jsonBody.put("phonebook", jsonParam);

                requestBody = Utility.buildPostParameters(jsonBody);

                urlConnection = (HttpURLConnection) Utility.makeRequest("POST", PHONE_ADD_API_URL, null, "application/json", requestBody);

                InputStream inputStream;
                // get stream
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }

                // parse stream
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp, response = "";

                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }
                return response;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Phone entry is saved successfully!", Toast.LENGTH_LONG).show();
            setDefaultValues();
        }
    }
}
