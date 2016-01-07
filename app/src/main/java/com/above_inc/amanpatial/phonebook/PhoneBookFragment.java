package com.above_inc.amanpatial.phonebook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhoneBookFragment extends Fragment {

    private static final String FETCH_PHONEBOOKLIST_URL = "http://10.0.2.2:3000/api/phonebooks";
    ArrayAdapter<Contact> phoneBookAdapter;
    ListView lstviewPhoneBook;

    public PhoneBookFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.phonebookfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            FetchPhoneBookTask phonebookTask = new FetchPhoneBookTask();
            phonebookTask.execute();
            return true;
        } else if (id == R.id.action_add) {// Explicit Intent by specifying its class name
            Intent intent = new Intent(getActivity(), PhoneAddActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_phonebook, container, false);
        bindData(rootView);

        return rootView;
    }

    public void bindData(View rootView) {

        List<Contact> phoneContacts = new ArrayList<Contact>();
        phoneContacts.addAll(FetchPhoneBook());

        phoneBookAdapter = new ArrayAdapter<Contact>(
                getActivity(),
                R.layout.list_item_phonebook,
                R.id.list_item_phonebook_textview,
                phoneContacts);

        lstviewPhoneBook = (ListView) rootView.findViewById(R.id.listview_phonebook);
        lstviewPhoneBook.setAdapter(phoneBookAdapter);

        // Add listener on click to each item and way to detail activity
        lstviewPhoneBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phoneBookInfo = phoneBookAdapter.getItem(position).toString();
                Toast.makeText(getActivity(), phoneBookInfo, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private List<Contact> parseJsonToArray(String strJsonPhoneBook) throws JSONException {

        List<Contact> lstPhoneBook;
        lstPhoneBook = new ArrayList<Contact>();

        JSONObject jsonObject = new JSONObject(strJsonPhoneBook);
        JSONArray jsonArray = jsonObject.getJSONArray("phonebooks");

        for (int i = 0; i < jsonArray.length(); i++) {
            lstPhoneBook.add(new Contact(
                    jsonArray.getJSONObject(i).get("firstname").toString(),
                    jsonArray.getJSONObject(i).get("lastname").toString(),
                    jsonArray.getJSONObject(i).get("phonenumber").toString()
            ));
        }
        return lstPhoneBook;
    }

    public List<Contact> FetchPhoneBook() {
        String LOG_TAG = "FetchPhoneBook";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String strJsonPhoneBook = null;

        try {
            // Construct the URL for the Phonebook URL and make sure URL is working
            String baseUrl = FETCH_PHONEBOOKLIST_URL;

            URL url = new URL(baseUrl);

            // Create the request to REST APi, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            strJsonPhoneBook = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the phonebook data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return parseJsonToArray(strJsonPhoneBook);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * populate list in background while showing progress dialog...
     */
    public class FetchPhoneBookTask extends AsyncTask<Void, Void, List<Contact>> {

        private final String LOG_TAG = FetchPhoneBookTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<Contact> doInBackground(Void... params) {

            List<Contact> phoneContacts = new ArrayList<Contact>();

            phoneContacts = FetchPhoneBook();
            return phoneContacts;
        }

        @Override
        protected void onPostExecute(List<Contact> result) {
            if (result != null) {
                phoneBookAdapter.clear();
                phoneBookAdapter.addAll(result);
                lstviewPhoneBook.setAdapter(phoneBookAdapter);
            } else {
                Toast.makeText(getActivity(), "PhoneBook data is not available", Toast.LENGTH_LONG).show();
            }
        }
    }

}
