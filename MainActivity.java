package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText name,email,address,username,password;
    String name1,address1,email1,username1,password1;
    InputStream is=null;
    String result=null;
    String line=null;
    Button insert;
    int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=findViewById(R.id.editText);
        address=findViewById(R.id.editText2);
        email=findViewById(R.id.editText3);
        username=findViewById(R.id.editText4);
        password=findViewById(R.id.editText5);
        insert=findViewById(R.id.button);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name1=name.getText().toString();
                address1=address.getText().toString();
                email1=email.getText().toString();
                username1=username.getText().toString();
                password1=password.getText().toString();
                if(TextUtils.isEmpty(name1))
                {
                    Toast.makeText(MainActivity.this, "Please Enter the Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(address1))
                {
                    Toast.makeText(MainActivity.this, "Please Enter the Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email1))
                {
                    Toast.makeText(MainActivity.this, "Please Enter the Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(username1))
                {
                    Toast.makeText(MainActivity.this, "Please Enter the Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password1))
                {
                    Toast.makeText(MainActivity.this, "Please Enter the Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                new SendRequest().execute();
                Intent i=new Intent(MainActivity.this,Track.class);
                startActivity(i);
            }
        });
    }
    public class SendRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{

                URL url = new URL("https://helpwomen.000webhostapp.com/index.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name",name1);
                postDataParams.put("address",address1);
                postDataParams.put("email",email1);
                postDataParams.put("username",username1);
                postDataParams.put("password",password1);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
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
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
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
}
