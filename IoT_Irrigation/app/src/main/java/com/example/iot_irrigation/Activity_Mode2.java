package com.example.iot_irrigation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Activity_Mode2 extends AppCompatActivity {
    private Button retour;
    private Button commande;
    private Switch electovane1;
    private Switch electovane2;
    private Switch electovane3;
    private Switch electovane4;
    private String field1="0",field2="0",field3="0",field4="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__mode2);
        commande = (Button) findViewById(R.id.commande);
        retour = (Button) findViewById(R.id.retour);
        electovane1=(Switch) findViewById(R.id.switch1);
        electovane2=(Switch) findViewById(R.id.switch2);
        electovane3=(Switch) findViewById(R.id.switch3);
        electovane4=(Switch) findViewById(R.id.switch4);

        commande.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                confirmDialogDemo();

            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getVolley(){
        // String URLline = "https://api.thingspeak.com/update?api_key=73GLZ47NJVJRS9BX&field1=1&field2=1&field3=0&field4=1";
        String URLline = "https://api.thingspeak.com/update?api_key=73GLZ47NJVJRS9BX&field1="+field1+"&field2="+field2+"&field3="+field3+"&field4="+field4 ;
        Log.d("getxxx",URLline);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr",">>"+response);
                        if(response!=null){
                            Toast.makeText(getApplicationContext(),"Commandes envoyées en succée!"+response, Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(),"Envoie echouée ,Vérfier connexion! ", Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    private void confirmDialogDemo() {
        if (electovane1.isChecked()){
            field1="1";
        } else field1="0";


        if (electovane2.isChecked()){
            field2="1";
        }else field2="0";

        if (electovane3.isChecked()){
            field3="1";
        }else field3="0";

        if (electovane4.isChecked()){
            field4="1";
        } else field4="0";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Envoi des commandes..");
        builder.setMessage("Vous etes entrain d'ajouter des nouveaux commandes:"+field1+","+field2+","+field3+","+field4+" ! Voulez Vous confirmer? ");
        builder.setCancelable(false);
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Nouveaux commandes seront envoyées!", Toast.LENGTH_SHORT).show();
                getVolley();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Operation d'envoie a été annulée!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }
}

