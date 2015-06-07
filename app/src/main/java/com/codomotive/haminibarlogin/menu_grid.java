package com.codomotive.haminibarlogin;

import com.codomotive.haminibarlogin.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;


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
        /*TableLayout invoice=(TableLayout)this.findViewById(R.id.invoice_table);
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
                TableRow.LayoutParams.WRAP_CONTENT));*/


        //Creating gridlayout and inflating the scroll view with it

        /*stub module for json. using hardcoded image urls to fill up the gridlayout*/
        String[] urls = {"http://images.bigoven.com/image/upload/t_recipe-256/hummus-9.jpg",
                "http://www.inisrael.com/news/wp-content/uploads/2008/06/steak.jpg",
                "http://images.bigoven.com/image/upload/t_recipe-256/hummus-9.jpg",
                "http://www.fabulousfingerfood.com.au/italian1.jpg",
                "https://s-media-cache-ak0.pinimg.com/236x/34/7b/74/347b745ef9f0e392ada61fe17ad8aa5f.jpg",
                "http://images.bigoven.com/image/upload/t_recipe-256/hummus-9.jpg"


        };

        ScrollView foodscroll = (ScrollView)findViewById(R.id.foodscroll);
        GridLayout gl; //defining a gridlayout object
        gl = new GridLayout(menu_grid.this);
        gl.setLayoutParams(new LayoutParams
                (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        gl.setOrientation(0);
        gl.setColumnCount(3);
        int k=Math.round((urls.length) / 3);
        int total=urls.length;
        DisplayMetrics displayMetrics = menu_grid.this.getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels;// displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels; // displayMetrics.density;
        int grid_wd=Math.round((dpWidth-20)/3);
        gl.setRowCount(k);
        //ImageView[] food_images= new ImageView[urls.length];
        for(int i =0, c = 0, r = 0; i < total; i++, c++)
        {
            if(c == 3)//number of coloumns
            {
                c = 0;
                r++;
            }
            /*
            * FOOD MENU UI DESIGN
            *
            * (imageView + textView)--> LinearLayout --> LinearLayout --> gridlayout
            *
            * */
            LinearLayout super_food_container = new LinearLayout(this); // Linear layout parent so that margin can be set to child Linear layout
            super_food_container.setOrientation(LinearLayout.VERTICAL);
            //setting layoutparams
            LayoutParams super_cont_params= new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            super_food_container.setLayoutParams(super_cont_params);

            //creating child linearlayout

            LinearLayout food_container = new LinearLayout(this);
            food_container.setOrientation(LinearLayout.VERTICAL);
            // Setting Params. Using LinearLayout.layoutparams against only layoutparams for additional  options
            LinearLayout.LayoutParams cont_params= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            if(c==0)
                cont_params.setMargins(5,2,2,3);
            else if(c==1)
                cont_params.setMargins(3,2,3,3);
            else if(c==2)
                cont_params.setMargins(2,2,5,3);
            food_container.setLayoutParams(cont_params);
            TextView food_name= new TextView(this);
            LayoutParams food_desc_params= new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            food_name.setText("Long name of food :" + i);
            food_name.setLayoutParams(food_desc_params);
            ImageView food_images = new ImageView(this);
            //food_images[i].setImageResource(R.drawable.ic_launcher);
            new ImageDownloader(food_images).execute(urls[0]);
            GridLayout.LayoutParams param =new GridLayout.LayoutParams();
            param.height = LayoutParams.WRAP_CONTENT;
            param.width = grid_wd; //LayoutParams.WRAP_CONTENT;
            param.rightMargin = 5;
            param.topMargin = 5;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            food_images.setLayoutParams(param);
            food_container.addView(food_images);
            food_container.addView(food_name);
            super_food_container.addView(food_container);
            gl.addView(super_food_container);
        }

        //View child = getLayoutInflater().inflate(R.layout.child, null);
        foodscroll.addView(gl);



    }
    //Image downloader class
    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            //bmImage.setImageBitmap(scaleImage(result));
            bmImage.setImageDrawable(scaleImage(result));
        }

        private BitmapDrawable scaleImage(Bitmap bitmap)
        {


            // Get current dimensions AND the desired bounding box
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int bounding = dpToPx(250);
            Log.i("Test", "original width = " + Integer.toString(width));
            Log.i("Test", "original height = " + Integer.toString(height));
            Log.i("Test", "bounding = " + Integer.toString(bounding));

            // Determine how much to scale: the dimension requiring less scaling is
            // closer to the its side. This way the image always stays inside your
            // bounding box AND either x/y axis touches it.
            float xScale = ((float) bounding) / width;
            float yScale = ((float) bounding) / height;
            float scale = (xScale <= yScale) ? xScale : yScale;
            Log.i("Test", "xScale = " + Float.toString(xScale));
            Log.i("Test", "yScale = " + Float.toString(yScale));
            Log.i("Test", "scale = " + Float.toString(scale));

            // Create a matrix for the scaling and add the scaling data
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            // Create a new bitmap and convert it to a format understood by the ImageView
            Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            width = scaledBitmap.getWidth(); // re-use
            height = scaledBitmap.getHeight(); // re-use
            BitmapDrawable result = new BitmapDrawable(scaledBitmap);
            Log.i("Test", "scaled width = " + Integer.toString(width));
            Log.i("Test", "scaled height = " + Integer.toString(height));


            return (result);


        }

        private int dpToPx(int dp)
        {
            float density = getApplicationContext().getResources().getDisplayMetrics().density;
            return Math.round((float)dp * density);
        }
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
