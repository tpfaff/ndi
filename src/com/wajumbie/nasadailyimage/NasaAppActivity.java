
/* -------------------
 * Author:Tyler Pfaff
 * 2/19/2013 
 * Target: 4.0+
 * -------------------*/
 

package com.wajumbie.nasadailyimage;

import java.io.File;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;



@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class NasaAppActivity extends Activity implements ActionBar.TabListener{
	
    private Bundle savedInstanceState;
    private static View mainView;
    private FragmentTransaction ft;
    BreakingNewsFragment bnf;
    NasaDailyImage ndi;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        createAlbum(); 
        setContentView(R.layout.main_activity);      
        this.savedInstanceState=savedInstanceState;
    }//
    
    @Override
    public void onStart(){
    	super.onStart(); 
    	if(savedInstanceState==null){
    		ndi=new NasaDailyImage(this);
    		bnf=new BreakingNewsFragment(this);    	
    		ft=getFragmentManager().beginTransaction();
    		ft.add(R.id.focused_view_container,ndi).commit();
    		
    		getFragmentManager().executePendingTransactions();
    		ndi.onRefresh();
    		
    		ft=getFragmentManager().beginTransaction();
    		ft.add(R.id.focused_view_container,bnf).commit();
    		
    	}
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
	       actionBar.setTitle("");
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
		
		switch(item.getItemId()){

		case R.id.content_save:
			ndi.onSaveImage();
			break;
			
		case R.id.content_refresh:
			ndi.onRefresh();
			break;
			
		case R.id.wallpaper_set:
			ndi.onSetWallpaper();
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

 

public void onTabReselected(Tab tab, FragmentTransaction f) {
	// TODO Auto-generated method stub
	
}

public void onTabSelected(Tab tab, FragmentTransaction f) {

	switch(tab.getPosition()){
	case 0:
		ft=getFragmentManager().beginTransaction();
		
		
			ft.hide(bnf);
			ft.show(ndi);
		
			ft.commit();
			
	       getFragmentManager().executePendingTransactions();
		
			 
        break;
	case 1:
			ft=getFragmentManager().beginTransaction();
			ft.hide(ndi);
			ft.show(bnf);
			
			ft.commit();
			bnf.fetchStories();
			//bnf.updateList();
			
	      getFragmentManager().executePendingTransactions();
	      
	     
         break;
	}
	
}

public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}
   
   }
   



