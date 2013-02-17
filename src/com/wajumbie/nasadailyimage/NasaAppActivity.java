
/*
 * -------------------
 * Author:Tyler Pfaff
 * App Version: 1.6 
 * Target: 4.0+
 * -------------------
 */

package com.wajumbie.nasadailyimage;

import java.io.File;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



public class NasaAppActivity extends FragmentActivity {
	
    private Bundle savedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAlbum(); 
        setContentView(R.layout.activity_nasa_app);
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
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	    this.savedInstanceState=outState;
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
   
   }
   



