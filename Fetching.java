package com.example.resque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Fetching extends AppCompatActivity {
TextView textView;
TextView textView1;
public static final String lattitude= ("com.example.resque.lattitude");
    public static final String longitude= ("com.example.resque.longitude");
String abc;
private double a,b;
String []store;
Button button;
String latt,longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetching);
        textView=findViewById(R.id.textView4);
        textView1=findViewById(R.id.textView5);
        button=findViewById(R.id.button4);
        new Fetching.SendRequest().execute(abc);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Fetching.this,MapsActivity.class);
                i.putExtra(lattitude,a);
                i.putExtra(longitude,b);
                startActivity(i);

            }
        });
    }
    public class SendRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{

                URL url = new URL("https://helpwomen.000webhostapp.com/fetchdata.php");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("value",abc);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line;

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();
                }
                else {
                    return "false : "+responseCode;
                }
            }
            catch(Exception e){
                return "Exception in scan: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("tag",result);
            store=result.split("/");
            Toast.makeText(Fetching.this,"SUCCESS",Toast.LENGTH_LONG).show();
            Log.e("MyTag",store[0]);
//            a=Double.valueOf(store[0]);
//            b=Double.valueOf(store[1]);

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
    public void activity()
    {

    }
}
