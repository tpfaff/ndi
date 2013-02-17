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
    
    private boolean createAlbum() {
    	
    	String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
    	File dir=new File(path,"NASA Images");
    	if (!dir.isDirectory()) {
    	        
    	        //Toast.makeText(this,Boolean.toString(created),Toast.LENGTH_SHORT).show();
    	        //Toast.makeText(this, dir.toString(), Toast.LENGTH_SHORT).show();
    	        return dir.mkdirs();//returns false if the directory already exists
    	         
    	}
		return false;
	}

	@Override
    public void onStart(){
    	super.onStart();
    	if(savedInstanceState==null){
    	RssParseSync init=new RssParseSync(this);
    	System.out.println("before it");
		// if(RssParseSync.getFinal_image()!=null){
			// RssParseSync.getFinal_image().recycle();
	//	 }
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

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */
    
   public void onRefreshClicked(View view){
	   Log.d("debug", "in onRefreshClicked");
	   	  // RssParseSync o=new RssParseSync(this);
	   	  // o.getFinalProcessedImage().recycle();
		   FragmentManager fragmentManager=getSupportFragmentManager();
		   NasaDailyImage NasaDailyFragment=(NasaDailyImage)fragmentManager.findFragmentById(R.id.fragment_iotd);//Finds a fragment that was identified by the given id either when inflated from XML or as the container ID when added in a transaction.
	       NasaDailyFragment.onRefresh();
		 }

   }
   



