package com.codomotive.haminibarlogin;

import com.codomotive.haminibarlogin.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class menu_grid extends Activity {

    Boolean ham_open=false;
    RelativeLayout ham_layout;
    ImageButton ham_button;
    ImageButton ham_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu_grid);

        Bundle extras=getIntent().getExtras();
        String welcomeuser="Hi "+extras.getString("user_name");
        String due=extras.getString("due");
        String balance=extras.getString("balance");
        String userbal;
        if(due.equals("0"))
            userbal=balance;
        else
            userbal="-"+due;
        String userbalance="Your Balance is:"+userbal;

        TextView menu_welcome_user=(TextView)this.findViewById(R.id.menu_welcome_user);
        menu_welcome_user.setText(welcomeuser);

        TextView menu_balance=(TextView)this.findViewById(R.id.menu_balance);
        menu_balance.setText(userbalance);

        ham_layout= (RelativeLayout) this.findViewById(R.id.ham_drawer);
        ham_layout.setVisibility(RelativeLayout.GONE);

        ham_button=(ImageButton)this.findViewById(R.id.ham_button);
        ham_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ham_layout.setVisibility(RelativeLayout.VISIBLE);
                if(ham_open)
                    ham_drawer(false);
                else
                    ham_drawer(true);

            }
        });

        ham_back=(ImageButton)this.findViewById(R.id.ham_back);
        ham_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ham_open)
                    ham_drawer(false);
            }
        });

        //dynamic table row adding
        TableLayout invoice=(TableLayout)this.findViewById(R.id.invoice_table);
        TableRow trow= new TableRow(this);
        trow.setId(10);
        trow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        TextView row_no=new TextView(this);
        row_no.setId(11);
        row_no.setText("11");
        row_no.setHeight(40);
        row_no.setWidth(30);
        row_no.setTextSize(19);
        row_no.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        row_no.setTextColor(Color.BLACK);

        TextView name=new TextView(this);
        name.setId(12);
        name.setText("Food Name");
        name.setHeight(40);
        name.setWidth(130);
        name.setTextSize(19);
        name.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        name.setTextColor(Color.BLACK);

        TextView quantity=new TextView(this);
        quantity.setId(13);
        quantity.setText("2");
        quantity.setHeight(40);
        quantity.setWidth(50);
        quantity.setTextSize(19);
        quantity.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        quantity.setTextColor(Color.BLACK);

        TextView price=new TextView(this);
        price.setId(14);
        price.setText("25$");
        price.setHeight(40);
        price.setWidth(70);
        price.setTextSize(19);
        price.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        price.setTextColor(Color.BLACK);

        trow.addView(row_no);
        trow.addView(name);
        trow.addView(quantity);
        trow.addView(price);

        invoice.addView(trow, new TableLayout.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));

    }

    public void ham_drawer(Boolean x)
    {
        if(x)
        {
            ham_layout.setVisibility(View.VISIBLE);
            ham_open=true;
        }
        else
        {
            ham_layout.setVisibility(RelativeLayout.GONE);
            ham_open=false;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //swipe detection code
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 > x1)
                    {
                        //Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show();
                        ham_drawer(true);
                    }

                    // Right to left swipe action
                    else
                    {
                       // Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show();
                        ham_drawer(false);
                    }

                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }


}
