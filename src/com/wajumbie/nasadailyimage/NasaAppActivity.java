
/* -------------------
 * Author:Tyler Pfaff
 * 2/19/2013 
 * Target: 4.0+
 * -------------------*/
 

package com.wajumbie.nasadailyimage;

import java.io.File;



import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;



public class NasaAppActivity extends FragmentActivity implements ActionBar.TabListener{
	
    private Bundle savedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAlbum(); 
        setContentView(R.layout.activity_nasa_app);
        final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("Image of the day")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Breaking news")
				.setTabListener(this));
        NasaDailyImage NasaDailyFragment=(NasaDailyImage)getSupportFragmentManager().findFragmentById(R.id.fragment_iotd);
        this.savedInstanceState=savedInstanceState;
    }
    
    @Override
    public void onStart(){
    	super.onStart(); 	
    	if(savedInstanceState==null){
    		RssParseSync init=new RssParseSync(this);
    		onRefreshClicked(null);
    	}
    }
	
	@Override
	public void onResume(){
		super.onResume();
		createAlbum();
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       MenuInflater inflater=getMenuInflater();
	       ActionBar actionBar = getActionBar(); 
	       actionBar.setTitle("");
	       inflater.inflate(R.menu.action_bar, menu);
	      
	       return true;
	    }
	 
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		FragmentManager fragmentManager=getSupportFragmentManager();
		NasaDailyImage NasaDailyFragment;
		switch(item.getItemId()){
		
		case R.id.content_save:
			NasaDailyFragment=(NasaDailyImage)fragmentManager.findFragmentById(R.id.fragment_iotd);  
			NasaDailyFragment.onSaveImage();
			break;
			
		case R.id.content_refresh:
			NasaDailyFragment=(NasaDailyImage)fragmentManager.findFragmentById(R.id.fragment_iotd);
			NasaDailyFragment.onRefresh();
			break;
			
		case R.id.wallpaper_set:
			NasaDailyFragment=(NasaDailyImage)fragmentManager.findFragmentById(R.id.fragment_iotd);
			NasaDailyFragment.onSetWallpaper();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	    this.savedInstanceState=outState;
	    }
	@Override
	protected void onDestroy(){
		super.onDestroy();
		savedInstanceState=null;
	}
	
    private boolean createAlbum() {
    	//Creates an album named NASA images in the gallery if it does not already exist
    	String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
    	File dir=new File(path,"NASA Images");
    	
    	if (!dir.isDirectory()){
    		return dir.mkdirs();//returns false if the directory already exists       
    	}
    	
		return false;
	}

   public void onRefreshClicked(View view){
	   //Refreshes all views and information
	   Log.d("debug", "in onRefreshClicked");
	   FragmentManager fragmentManager=getSupportFragmentManager();
	   NasaDailyImage NasaDailyFragment=(NasaDailyImage)fragmentManager.findFragmentById(R.id.fragment_iotd); 
	   NasaDailyFragment.onRefresh();
		 }

public void onTabReselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}

public void onTabSelected(Tab tab, FragmentTransaction ft) {
	switch(tab.getPosition()){
	case 0:
		onRefreshClicked(null);
	case 1:
	
	}
	
}

public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}
   
   }
   



