package com.example.iot_irrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Activity_Mode1 extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] sensors = { "Capteur DHT/DHT11","Capteur Lumiere(TEMT6000)","Capteur Niveau d'eau", "Capteur Sol"};
    private RelativeLayout mRelative;
    private EditText status;
    private EditText temperature;
    private EditText humidity,Number_ent,Created_at;
    private EditText light;
    private EditText watter;
    private EditText sol;
    private ProgressDialog pd;
    private Button refresh;
    private Button retour;
    private View childView;
    private String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__mode1);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        // temperature=(EditText)findViewById(R.id.temperature);
       /* EditText humidity=(EditText)findViewById(R.id.humidity);
        EditText Number_ent=(EditText)findViewById(R.id.Number_ent);
        EditText created_at=(EditText)findViewById(R.id.created_at);*/


        // temperature.setText("15c");
        //mRelative.addView(childView);

        // new JsonTask().execute("https://api.thingspeak.com/channels/829296/feeds.json?api_key=XHA1BB868NHQE9SN&results=2");
        //new GetContacts().execute();
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        mRelative = (RelativeLayout) findViewById(R.id.sensor_layout_container);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sensors);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

    }



    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),sensors[position] , Toast.LENGTH_LONG).show();

        // get the current spinner value
        choice = sensors[position].toString();

        // get and clear our sub-menu container
// get and clear our sub-menu container

        if (choice.equals("Capteur DHT/DHT11")) {
            mRelative.removeAllViews();
            childView = getLayoutInflater().inflate(R.layout.sensor_dht11, null);
            status = childView.findViewById(R.id.status_val);
            temperature = childView.findViewById(R.id.temperature_val);
            humidity = childView.findViewById(R.id.himidité_val);
            Number_ent = childView.findViewById(R.id.number_val);
            Created_at = childView.findViewById(R.id.date_val);
            refresh=childView.findViewById(R.id.refresh);
            mRelative.addView(childView);

            // Log.i("sensor", "DHT sensor");
            //new JsonTask(this).execute("https://api.thingspeak.com/channels/829296/feeds.json?api_key=XHA1BB868NHQE9SN&results=2");
            new Activity_Mode1.GetSensors(this).execute();
        }
        if (choice.equals("Capteur Lumiere(TEMT6000)")) {

            mRelative.removeAllViews();
            childView = getLayoutInflater().inflate(R.layout.sensor_light, null);
            status = childView.findViewById(R.id.status_val);
            light = childView.findViewById(R.id.light_val);
            Number_ent = childView.findViewById(R.id.number_val);
            Created_at = childView.findViewById(R.id.date_val);
            refresh=childView.findViewById(R.id.refresh);
            retour=childView.findViewById(R.id.retour);
            mRelative.addView(childView);
            new Activity_Mode1.GetSensors(this).execute();
        }
        if (choice.equals("Capteur Niveau d'eau")) {
            mRelative.removeAllViews();
            childView = getLayoutInflater().inflate(R.layout.sensor_watter, null);
            status = childView.findViewById(R.id.status_val);
            watter = childView.findViewById(R.id.watter_val);
            Number_ent = childView.findViewById(R.id.number_val);
            Created_at = childView.findViewById(R.id.date_val);
            refresh=childView.findViewById(R.id.refresh);
            retour=childView.findViewById(R.id.retour);
            mRelative.addView(childView);
            new Activity_Mode1.GetSensors(this).execute();
        }
        if (choice.equals("Capteur Sol")) {
            mRelative.removeAllViews();
            childView = getLayoutInflater().inflate(R.layout.sensor_soil, null);
            status = childView.findViewById(R.id.status_val);
            sol = childView.findViewById(R.id.sol_val);
            Number_ent = childView.findViewById(R.id.number_val);
            Created_at = childView.findViewById(R.id.date_val);
            refresh=childView.findViewById(R.id.refresh);
            retour=childView.findViewById(R.id.retour);
            mRelative.addView(childView);
            new Activity_Mode1.GetSensors(this).execute();
        }
// inflate the determined layout to a view, and add it to our container
        //container.addView(LayoutInflater.from(this).inflate(layout, null, false));

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    private class GetSensors extends AsyncTask<Void, Void, Void> {

        protected Activity_Mode1 context;

        public GetSensors(Context context) {
            this.context = (Activity_Mode1) context;
        }
        /* protected void onPreExecute() {
             super.onPreExecute();
             Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

         }*/
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Activity_Mode1.this);
            pd.setMessage("Collection des données en cours...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.thingspeak.com/channels/829296/feeds.json?api_key=XHA1BB868NHQE9SN&results=2";
            String jsonStr = sh.makeServiceCall(url);

            Log.e("sensor", "Response from url: " + jsonStr);
            retour=childView.findViewById(R.id.retour);
            refresh=childView.findViewById(R.id.refresh);

            refresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    //new JsonTask(context).execute("https://api.thingspeak.com/channels/829296/feeds.json?api_key=XHA1BB868NHQE9SN&results=2");
                    Toast.makeText(getApplicationContext(),
                            " Operation echouée,vérifiez connexion internet!",
                            Toast.LENGTH_LONG).show();
                }
            });

            retour.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            });

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray feeds = jsonObj.getJSONArray("feeds");

                    // looping through All Contacts
                    for (int i = 0; i < feeds.length(); i++) {
                        JSONObject c = feeds.getJSONObject(i);
                        final String created_at = c.getString("created_at");
                        final String entry_id = c.getString("entry_id");
                        final String field1 = c.getString("field1");
                        final String field2 = c.getString("field2");
                        final String field3 = c.getString("field3");
                        final String field4 = c.getString("field4");
                        final String field5 = c.getString("field5");


                        // Feeds node is JSON Object
                        // tmp hash map for single contact
                        HashMap<String, String> feed = new HashMap<>();

                        // adding each child node to HashMap key => value
                        feed.put("field1", field1);
                        feed.put("field2", field2);
                        feed.put("field3", field3);
                        feed.put("field4", field4);
                        feed.put("field5", field5);
                        feed.put("entry_id",entry_id);
                        feed.put("created_at",created_at);
                        Log.d("field1: ",field1+"\n" );
                        Log.d("field2: ",field2+"\n" );
                        Log.d("field3: ",field3+"\n" );
                        Log.d("field4: ",field4+"\n" );
                        Log.d("field5: ",field5+"\n" );
                        Log.d("entry_id: ",entry_id+"\n" );
                        Log.d("created_at: ",created_at+"\n" );

                        context.runOnUiThread(new

                                                      Runnable() {
                                                          @Override
                                                          public void run () {

                                                              temperature = childView.findViewById(R.id.temperature_val);
                                                              humidity = childView.findViewById(R.id.himidité_val);
                                                              light = childView.findViewById(R.id.light_val);
                                                              watter = childView.findViewById(R.id.watter_val);
                                                              sol = childView.findViewById(R.id.sol_val);
                                                              // field commune
                                                              status = childView.findViewById(R.id.status_val);
                                                              Number_ent = childView.findViewById(R.id.number_val);
                                                              Created_at = childView.findViewById(R.id.date_val);

                                                              if (choice.equals("Capteur DHT/DHT11")) {
                                                                  status.setText("Marche");
                                                                  status.setTextColor(Color.GREEN);
                                                                  temperature.setText(field1+"C°");
                                                                  humidity.setText(field2+"%");
                                                                  Number_ent.setText(entry_id);
                                                                  Created_at.setText(created_at);}

                                                              if (choice.equals("Capteur Lumiere(TEMT6000)")) {
                                                                  status.setText("Marche");
                                                                  status.setTextColor(Color.GREEN);
                                                                  light.setText(field3);
                                                                  Number_ent.setText(entry_id);
                                                                  Created_at.setText(created_at);}

                                                              if (choice.equals("Capteur Niveau d'eau")) {
                                                                  status.setText("Marche");
                                                                  status.setTextColor(Color.GREEN);
                                                                  watter.setText(field4);
                                                                  Number_ent.setText(entry_id);
                                                                  Created_at.setText(created_at);}

                                                              if (choice.equals("Capteur Sol")) {
                                                                  status.setText("Marche");
                                                                  status.setTextColor(Color.GREEN);
                                                                  sol.setText(field5+"%");
                                                                  Number_ent.setText(entry_id);
                                                                  Created_at.setText(created_at);}

                                                              refresh.setOnClickListener(new View.OnClickListener() {
                                                                  public void onClick(View v) {
                                                                      // Perform action on click
                                                                      //new JsonTask(context).execute("https://api.thingspeak.com/channels/829296/feeds.json?api_key=XHA1BB868NHQE9SN&results=2");
                                                                      new Activity_Mode1.GetSensors(context).execute();
                                                                  }
                                                              });



                                                          }
                                                      });

                        // adding contact to contact list
                        //contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e("sensor", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e("sensor", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Connexion au serveur echouée! Vérfier connexion internet!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           /* ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
                    R.layout.list_item, new String[]{ "email","mobile"},
                    new int[]{R.id.email, R.id.mobile});
            lv.setAdapter(adapter);*/

            if (pd.isShowing()){
                pd.dismiss();
            }
            //txtJson.setText(result);
            Log.i("sensor", "result:"+result);

        }
    }
}

