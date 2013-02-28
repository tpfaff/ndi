
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
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;



public class NasaAppActivity extends FragmentActivity implements ActionBar.TabListener{
	
    private Bundle savedInstanceState;
    private static View mainView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        createAlbum(); 
        setContentView(R.layout.main_activity);
       
		
		
		
        
       
        this.savedInstanceState=savedInstanceState;
    }
    
    @Override
    public void onStart(){
    	super.onStart(); 	
    	NasaDailyImage NasaDailyFragment=new NasaDailyImage(this);
        getSupportFragmentManager().beginTransaction().add(R.id.focused_view_container,NasaDailyFragment).commit();
    	/*if(savedInstanceState==null){
    		RssParseSync init=new RssParseSync(this);
    		onRefreshClicked(null);
    	}*/
    }
	
	@Override
	public void onResume(){
		super.onResume();
		createAlbum();
	}
	
	 @Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		mainView= super.onCreateView(name, context, attrs);
		return mainView;
	}

	@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       MenuInflater inflater=getMenuInflater();
	       ActionBar actionBar = getActionBar(); 
	       actionBar.setTitle("what what");
	       inflater.inflate(R.menu.action_bar, menu);
	      // actionBar=getActionBar();
	        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.addTab(actionBar.newTab().setText("Image of the day")
					.setTabListener(this));
			actionBar.addTab(actionBar.newTab().setText("Breaking news")
					.setTabListener(this));
	       return true;
	    }

	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		NasaDailyImage NasaDailyFragment=new NasaDailyImage(this);
		switch(item.getItemId()){

		case R.id.content_save:
			NasaDailyFragment.onSaveImage();
			break;
			
		case R.id.content_refresh:
			NasaDailyFragment.onRefresh();
			break;
			
		case R.id.wallpaper_set:
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
	   NasaDailyImage NasaDailyFragment=new NasaDailyImage(this);
	   NasaDailyFragment.onRefresh();
		 }

public void onTabReselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}

public void onTabSelected(Tab tab, FragmentTransaction ft) {
	switch(tab.getPosition()){
	case 0:
		//onRefreshClicked(null);
	case 1:
		BreakingNewsFragment news=new BreakingNewsFragment();
         getFragmentManager().beginTransaction().add(R.id.focused_view_container, news).commit();
         
	}
	
}

public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}
   
   }
   



