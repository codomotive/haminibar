package com.codomotive.haminibarlogin;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static android.widget.Toast.*;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        //login button code
       final Button login_button=(Button)findViewById(R.id.login);
        final EditText login_text=(EditText)findViewById(R.id.login_phone);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phn_no = login_text.getText().toString();
                if (phn_no.matches("")) {
                    makeText(getApplicationContext(), "Please Enter Your Phone Number", LENGTH_SHORT).show();
                } else {
                    //attempt login
                    Toast.makeText(getApplicationContext(), phn_no, Toast.LENGTH_SHORT);
                    new login().execute(phn_no);
                }


            }
        });
    }


    /*Login FUnction*/
    private class login extends AsyncTask<String, Integer, JSONObject> {

        public AsyncTask delegate=null;
        JSONObject user_info;

        @Override
        protected JSONObject doInBackground(String... phn_no) {
            try{
                String data="phn="+ phn_no[0];
                byte[] postData= data.getBytes( StandardCharsets.UTF_8 );
                int    data_length = postData.length;
                URL url = new URL("http://haminibar.co.il/app_req/login.php");
                HttpURLConnection con=(HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestProperty("charset", "utf-8");
                con.setRequestProperty("Content-Length", Integer.toString(data_length));
                con.setUseCaches(false);
                DataOutputStream ds= null;
                ds = new DataOutputStream(con.getOutputStream());
                ds.write(postData);
                String line = "";
                InputStream res=null;
                try {
                    res = con.getInputStream();
                }
                catch(IOException exception)
                {
                    res=con.getErrorStream();
                }
                InputStreamReader isr = new InputStreamReader(res);
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
                // Response from server after login process will be stored in response variable.
                String response = sb.toString();
                try {
                    user_info=new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isr.close();
                reader.close();
            }
            catch (IOException e)
            {

            }
            return user_info;
        }

        protected void onProgressUpdate(Integer... progress) {
        }


        protected void onPostExecute(JSONObject result) {
            /*onPostExecute runs on activity thread. Now we deal with the message from server.*/
            String user_name="";
            String user_id="";
            String balance="";
            String due="";
            String error= null;
            try {
                error = result.getString("error");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(error.equals("1"))
            {
                try {
                    Toast.makeText(getApplicationContext(),result.getString("error_message"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    user_name=result.getString("name");
                    user_id=result.getString("user_id");
                    balance=result.getString("balance");
                    due=result.getString("due");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"Welcome "+user_name,Toast.LENGTH_SHORT).show();
                Intent intent;
                intent=new Intent(MainActivity.this,menu_grid.class);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_id",user_id);
                intent.putExtra("balance",balance);
                intent.putExtra("due",due);
                startActivity(intent);
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}