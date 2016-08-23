package com.example.mychessgame;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;




public class MainActivity extends ActionBarActivity {
	
	public static final int NUM_ROWS = 8;
	public static final int NUM_COLS = 8;
	public static int SQUARE_SIZE = 82; // size tbd in pixels based on screen size
	public static final int OFFSET = 15;
	
	// screen size of the phone to be initialized arbitrarily
	int width = 100; 
	int height = 100;
	
	
	public static SharedPreferences data = null;
	public static SharedPreferences.Editor editor = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        
        SQUARE_SIZE = (width - 20) / 8 - 5; // all tiles should be displayed on screen
        Log.e("DEBUG", String.format("%d", SQUARE_SIZE));
        
        if (savedInstanceState == null) {
            /*
        	getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
            */
            getSupportFragmentManager().beginTransaction()
            .add(R.id.container, new MenuFragment())
            .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	// I don't need this
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /*
     * Creates the display for the main menu, two game modes are single player and two playerd
     * 
     */
    public class MenuFragment extends Fragment{
    	
  	  @Override
  	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
  	        Bundle savedInstanceState) {
  		  
  		  View rootView = inflater.inflate(R.layout.menu_fragment, container, false);
  		  ImageView frontImg = new ImageView(rootView.getContext());
  		  frontImg.setImageResource(R.drawable.menu1);
  		  LayoutParams params = new LayoutParams(width, (int) (height*0.7));
  		  frontImg.setLayoutParams(params);
  		  frontImg.setScaleType(ImageView.ScaleType.FIT_XY);
  		  frontImg.setAdjustViewBounds(false);	
  		
  		  LinearLayout l = (LinearLayout)rootView.findViewById(R.id.testLayout);
  		  
  		  Button twoPlayersButton = new Button(rootView.getContext());
  		  twoPlayersButton.setText("Two Players");
  		
  		  Button singlePlayerButton = new Button(rootView.getContext());
  		  singlePlayerButton.setText("Single Player");
  		
  		  l.addView(frontImg);
  		  l.addView(singlePlayerButton);
  		  l.addView(twoPlayersButton);
  		  
  		  singlePlayerButton.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            	 FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	            	 transaction.replace(R.id.container, GameFragment.newInstance(true));  // change this line
	            	 transaction.addToBackStack(null);
	            	 transaction.commit(); 
	             }
		  });
  		  
  		  twoPlayersButton.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click

	            	 FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

	            	 // Replace whatever is in the fragment_container view with this fragment,
	            	 // and add the transaction to the back stack so the user can navigate back
	            	 transaction.replace(R.id.container, GameFragment.newInstance(false));  
	            	 transaction.addToBackStack(null);
	            	 transaction.commit(); 
	             }
  		  });
			
  		  return rootView;
  	    }
    }       
}
