package com.codomotive.haminibarlogin;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainSettingsLogin extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings_login);

        final EditText username=(EditText)this.findViewById(R.id.admin_user_name);
        final EditText password=(EditText)this.findViewById(R.id.admin_password);
        Button login_button=(Button)this.findViewById(R.id.admin_login);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_val=username.getText().toString();
                String password_val=password.getText().toString();
                if(username_val.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Your Username", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(password_val.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Your Password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    String actual_admin="admin";
                    String actual_password="123456";

                    if(username_val.equals(actual_admin) && password_val.equals(actual_password))
                    {
                        //login success
                        Intent intent=new Intent(MainSettingsLogin.this,MainSettings.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Username or password is incorrect.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_settings_login, menu);
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
