/* -------------------
 * Author:Tyler Pfaff
 * 2/19/2013 
 * Target: 4.0+
 * -------------------*/
package com.wajumbie.nasadailyimage;

import java.io.File;
import java.io.FileOutputStream;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
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
import android.widget.Toast;


public class NasaDailyImage extends Fragment{
    /** Called when the activity is first created. */
	private String title="";
	private String link="";
	private String description="";
	private String date="";
	private ProgressDialog dog;
	private Bundle savedInstanceState;
	private Handler handler=new Handler();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    } 
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.main, container,false);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		getActivity().findViewById(R.id.fragment_iotd);
		registerForContextMenu(getActivity().findViewById(R.id.imageDisplay));//register the view which the context menu is associated with
	}
	
    public void onRefresh(){
    	System.out.println("in onRefresh");
    	new RssParseSync(getActivity()).execute(title,description,date,link);
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
					Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
				}});
			
		}else{
			handler.post(new Runnable(){
				public void run(){
					Toast.makeText(getActivity(), "Unable to Save!", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(getActivity(), "Saving...", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Set Wallpaper");
        alert.setMessage("Change the wallpaper?");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            
            public void onClick(DialogInterface dialog, int whichButton) {
            	Thread th=new Thread(){
            		public void run(){
            			RssParseSync retriever=new RssParseSync(getActivity());
            			WallpaperManager wallpaperManager=WallpaperManager.getInstance(getActivity());
            			try{
            				
            				handler.post(new Runnable(){
            					public void run(){
            						Toast.makeText(getActivity(), "Setting wallpaper...", Toast.LENGTH_SHORT).show();
            					}});
            				
            				wallpaperManager.setBitmap(retriever.getFinalProcessedImage());
            				
            				handler.post(new Runnable(){
            					public void run(){
            						Toast.makeText(getActivity(), "Wallpaper set", Toast.LENGTH_SHORT).show();
            					}});
                			}
            			catch(Exception e){
            				e.printStackTrace();
            				handler.post(new Runnable(){
            					public void run(){
            						Toast.makeText(getActivity(), "Error setting wallpaper", Toast.LENGTH_SHORT).show();
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
