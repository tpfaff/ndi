/* -------------------
 * Author:Tyler Pfaff
 * 2/19/2013 
 * Target: 4.0+
 * -------------------*/
package com.wajumbie.nasadailyimage;

import java.io.File;
import java.io.FileOutputStream;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("ValidFragment")
public class NasaDailyImage extends Fragment{
    /** Called when the activity is first created. */
	private String title="";
	private String link="";
	private String description="";
	private String date="";
	private ProgressDialog dog;
	private static Bundle savedInstanceState;
	private Handler handler=new Handler();
	private static View ndiView;
	private static Activity mainActivity;
	
	
	
	public NasaDailyImage(Activity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public NasaDailyImage() {
		
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    //  String what= mainActivity.toString();
    } 
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		ndiView= inflater.inflate(R.layout.nasa_daily_image_layout, container,false);
		return ndiView;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
	}
	
	
    public void onRefresh(){
    	System.out.println("in onRefresh");
    		
	    	
			
		String result=mainActivity.toString();
		RssParseSync rps=new RssParseSync(ndiView,mainActivity);
    	rps.execute(title,description,date,link);
    	
    }

    
    public void onSaveImage() {
    	final Bitmap img;
    	final String imageName;
    	final File dir;
    	boolean imageAdded;
    	RssParseSync rps=new RssParseSync();
    	
    	imageName=rps.getImageName();
    	img=rps.getFinalProcessedImage();
		String path=(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)).toString();
		dir=new File(path,"NASA Images");
		Thread th=new Thread(){
    		public void run(){
		if(galleryAddImg(imageName,img,dir)){
			handler.post(new Runnable(){
				public void run(){
					Toast.makeText(mainActivity, "Saved", Toast.LENGTH_SHORT).show();
					
				}});
			
		}else{
			handler.post(new Runnable(){
				public void run(){
					Toast.makeText(mainActivity, "Unable to Save!", Toast.LENGTH_SHORT).show();
				}
		});}
    		}
		};
		th.start();
    	
	}
    
    private boolean galleryAddImg(String imageName,Bitmap img,File dir){
    	try {
    		
    		Thread thread=new Thread(){
				public void run() {
					handler.post(new Runnable(){
					public void run(){
						Toast.makeText(mainActivity, "Saving...", Toast.LENGTH_SHORT).show();
					}});
					
				}
    		};
    	thread.start();
    		
    		
    	    FileOutputStream out = new FileOutputStream(dir+"/"+imageName+".png");
    	    img.compress(Bitmap.CompressFormat.PNG, 90, out);
    	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    	    File f = new File(dir+"/"+imageName+".png");
       	    Uri contentUri = Uri.fromFile(f);
       	    mediaScanIntent.setData(contentUri);
       	    getActivity().sendBroadcast(mediaScanIntent);
       	    return true;
    	} catch (Exception e) {
    	       e.printStackTrace();
    	}
    	return false;
    }
    
	public void onSetWallpaper(){
        AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        alert.setTitle("Set Wallpaper");
        alert.setMessage("Change the wallpaper?");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            
            public void onClick(DialogInterface dialog, int whichButton) {
            	Thread th=new Thread(){
            		public void run(){
            			RssParseSync retriever=new RssParseSync(null,mainActivity); //****************************
            			WallpaperManager wallpaperManager=WallpaperManager.getInstance(mainActivity);
            			try{
            				
            				handler.post(new Runnable(){
            					public void run(){
            						Toast.makeText(mainActivity, "Setting wallpaper...", Toast.LENGTH_SHORT).show();
            					}});
            				
            				wallpaperManager.setBitmap(retriever.getFinalProcessedImage());
            				
            				handler.post(new Runnable(){
            					public void run(){
            						Toast.makeText(mainActivity, "Wallpaper set", Toast.LENGTH_SHORT).show();
            					}});
                			}
            			catch(Exception e){
            				e.printStackTrace();
            				handler.post(new Runnable(){
            					public void run(){
            						Toast.makeText(mainActivity, "Error setting wallpaper", Toast.LENGTH_SHORT).show();
            					}
            				});
            			}
            		}
            	};
            	th.start();
                         }
        });
        
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               
            }
        });
    
        alert.show();
    	
    }//method
    	}//class
