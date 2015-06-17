package com.codomotive.haminibarlogin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainSettings extends ActionBarActivity {

    HashMap<String,String> ref_map=new HashMap<String,String>();
    Button ref_save;
    SQLiteDatabase db;
    String selected_ref_id;
    String selected_ref_name;
    TextView current_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);
        ref_save=(Button)findViewById(R.id.ref_save);
        ref_save.setVisibility(Button.GONE);
        db = openOrCreateDatabase("haminibar", this.MODE_PRIVATE, null);
        current_ref=(TextView)findViewById(R.id.current_ref);
        Cursor cursor=db.rawQuery("SELECT value FROM settings WHERE name='refrigerator_name'",null);
        String refrigerator_name=null;
        if(cursor.moveToFirst()) {
            refrigerator_name = cursor.getString(0);
            if(refrigerator_name.equals("0"))
            {
                current_ref.setText("None");
            }
            else
            {
                current_ref.setText(refrigerator_name);
            }
        }
        new get_refrigerators().execute(4);

        final Spinner ref_spinner=(Spinner)findViewById(R.id.ref_spinner);
        ref_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_ref_id=ref_map.get(ref_spinner.getSelectedItem().toString());
                selected_ref_name=ref_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ref_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("UPDATE settings SET value='"+selected_ref_id+"' WHERE name='refrigerator_id'");
                db.execSQL("UPDATE settings SET value='"+selected_ref_name+"' WHERE name='refrigerator_name'");
                Toast.makeText(getApplicationContext(),"Saved.",Toast.LENGTH_SHORT).show();
                current_ref.setText(selected_ref_name);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_settings, menu);
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

    public class get_refrigerators extends AsyncTask<Integer,Integer,JSONObject>{

        JSONObject refrigerator_info;
        @Override
        protected JSONObject doInBackground(Integer... params) {

            try {
                URL url=new URL("http://haminibar.co.il/app_req/get_refrigerators.php");
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                InputStream res=null;
                res=con.getInputStream();
                InputStreamReader isr = new InputStreamReader(res);
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
                // Response from server after login process will be stored in response variable.
                String response = sb.toString();
                try {
                    refrigerator_info=new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isr.close();
                reader.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return refrigerator_info;
        }

        protected void onPostExecute(JSONObject result) {
            ArrayList<String> ref_list = new ArrayList<String>();
            try {

                JSONArray refrigerators=result.getJSONArray("refrigerators");
                for(int i=0;i<refrigerators.length();i++)
                {
                    JSONObject refrigerator=refrigerators.getJSONObject(i);
                    String name=refrigerator.getString("name");
                   ref_list.add(name);
                    ref_map.put(name,refrigerator.getString("id"));
                }
                Spinner ref_spinner=(Spinner)findViewById(R.id.ref_spinner);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_original_layout,ref_list);
                adapter.setDropDownViewResource(R.layout.spinner_layout);
                ref_spinner.setAdapter(adapter);
                ref_save.setVisibility(Button.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
