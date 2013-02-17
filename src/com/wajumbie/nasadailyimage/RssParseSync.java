package com.wajumbie.nasadailyimage;


import java.io.*;
import java.net.*;

import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import org.xmlpull.v1.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;



	public class RssParseSync extends AsyncTask<String,String,Bitmap>{  

		private Activity parent;
		private ProgressDialog dialog;
		private static Bitmap final_image; //must be static because a new instance is required to access getFinalImage();
		private String imageURL="";
		private static String imageName;
		private long totalTime;
		private long startTime;
		

		public RssParseSync(Activity parent){this.parent =parent;}//constructor to pass parent activity, need this to call findViewById
		public RssParseSync(){}

				@Override
				protected Bitmap doInBackground(String... info){
					
					URL iotd;
					int count=info.length;
					String title="",link="",description="",date="";
					
					System.out.println("Number of params is "+count);
					
					try{
						
						iotd = new URL("http://www.nasa.gov/rss/lg_image_of_the_day.rss");//set URl
						BufferedReader in;//new BufferedReader
						in = new BufferedReader(new InputStreamReader(iotd.openStream()));//get rss
						
						XmlPullParserFactory factory;
						factory = XmlPullParserFactory.newInstance();//new factory
						factory.setNamespaceAware(true);
						XmlPullParser xpp;
						xpp = factory.newPullParser();
						xpp.setInput(in);

						//rss is now parsed, free to use XmlPullParser functions to move around and evaulate the rss
					
						int eventType;
						eventType = xpp.getEventType();//returns an int which mean different things (START_DOCUMENT,START_TAG,etc)
					
			
					while(eventType!=XmlPullParser.END_DOCUMENT){//while the document has words
						
					 switch(eventType){
						
						case XmlPullParser.START_DOCUMENT://beginning of xml
							break;
						case XmlPullParser.START_TAG://case : at beginning of new tag
							String tagName=xpp.getName();
							System.out.println(tagName+" "+xpp.getDepth());
							
							if(tagName.equals("title")&& xpp.getDepth()==4){//depth is specific to this certain rss feed, there are multiple tags with the same names
								info[0]=xpp.nextText();
								title=info[0];

							}
							
							if(tagName.equals("description")&& xpp.getDepth()==4){//depth is specific to this certain rss feed, there are multiple tags with the same names
								info[1]=xpp.nextText();
								description=info[1];	
								StringBuilder tabbed=new StringBuilder(description);
								tabbed.insert(0, "\t");
								description=tabbed.toString();
								//info[1]=description;
							}
							
							if(tagName.equals("pubDate")){//no depth needed, only one tag is named pubDate
								info[2]=xpp.nextText();
								date=info[2];							
							}
							
							if(tagName.equals("enclosure")&&xpp.getDepth()==4){//this is where our image url is. this url is an attribute of the "enclosure" tag
								//System.out.println("Enclosure has "+xpp.getAttributeCount());
								for(int i=0; i<=xpp.getAttributeCount()-1; i++){
									System.out.println("in for");
									if(xpp.getAttributeName(i).equals("url")){
										link=xpp.getAttributeValue(i);
										info[3]=link;
										imageURL=info[3];
									}
								}
							}
							break;
						
						}
						eventType=xpp.next();
					}//switch
						
						publishProgress(title,description,date,link);
						
						in.close();//close BufferedReader
			        } catch (MalformedURLException e){
			        	e.printStackTrace();
			        }catch(XmlPullParserException e1){
			        	e1.printStackTrace();
			        }catch(IOException e2){
			        	e2.printStackTrace();
			        }	      
					Bitmap image=getResizedImage(imageURL);
					
					return image;
				}
				@Override
				protected void onProgressUpdate(String... progress){
					try {
						System.out.println("onProgressUpdate");
						setImageName(progress[0]);
						resetDisplay(progress[0],progress[1],progress[2],progress[3]);//feeding parsed rss values, title, description, date, and link
					} catch (MalformedURLException e) {//both exceptions are thrown by resetDisplay
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}

				private void setImageName(String title) {
					imageName=title;
					
				}


				protected void onPostExecute(Bitmap image){//error when doing this in resetDisplay.... onPostExecute is invoked by the ui thread so this may be why it works here and not in resetDisplay
					ImageView imageView=(ImageView) parent.findViewById(R.id.imageDisplay);
			    	imageView.setImageBitmap(image); //image is null here on n7
			    	//setFinal_image(image);
			    	dialog.dismiss();
			    	totalTime=System.nanoTime()-startTime;
			    	System.out.println(totalTime);
				}
				
				protected void onPreExecute(){
					if(final_image!=null){
						final_image.recycle();
					}
					
					nullAllViews();
					startTime=System.nanoTime();
					 dialog=ProgressDialog.show(parent, "Loading", "Loading the image of the day");
				}
				
			    private void resetDisplay(String title, String description,String date, String link) throws MalformedURLException, IOException{
			    	
			    	nullAllViews();
			    	
			    	TextView titleView=(TextView) parent.findViewById(R.id.imageTitle);
			    	titleView.setText(title);
			    	
			    	TextView dateView=(TextView) parent.findViewById(R.id.imageDate);
			    	dateView.setText(date);

			    	TextView descriptionView=(TextView) parent.findViewById(R.id.imageDescription);
			    	descriptionView.setText(description);
			    	
				//	ImageView imageView=(ImageView) parent.findViewById(R.id.imageDisplay);
			//    	imageView.setImageBitmap(final_image);

			    	
			    }
			    
			    public Bitmap getBitmapFromURL(String url) {
			    	try {
			    	Bitmap bitmap=null;	
			    	InputStream input = new java.net.URL(url).openStream();
			    	bitmap = BitmapFactory.decodeStream(input);//Decode an input stream into a bitmap. If the input stream is null, or cannot be used to decode a bitmap, the function returns null.
			    	input.close();
			    	return bitmap;
			    	} catch (IOException ioe) { return null; }
			    
			    }
			    
			    private Bitmap getResizedImage(final String imageUrl){

			    	try{
			    		
			    	BitmapFactory.Options options = new BitmapFactory.Options();
			    	options.inJustDecodeBounds=true;
			    	BitmapFactory.decodeStream(new URL(imageUrl).openStream(),null,options);
			    	DisplayMetrics displaymetrics=new DisplayMetrics();
			    	parent.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			    	int height=displaymetrics.heightPixels-25;
			    	int width=displaymetrics.widthPixels-25;

			    	options.inSampleSize=calculateInSampleSize(options, width ,height);
			    	options.inJustDecodeBounds=false;
			    	final_image=BitmapFactory.decodeStream(new URL(imageUrl).openStream(),null,options);
			    	
			    	}catch(Exception e){
			    		e.printStackTrace();
			    	}
		    	
			    	return final_image;
			    }
			    
			    
			    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
			    	//raw height and width of image
			    	final int height=options.outHeight;
			    	final int width=options.outWidth;
			    	//reqWidth-=500;
			    	//reqHeight-=500;
			    	int inSampleSize=1; //powers of 2 are most efficient
			    	
			    
			    	if(height>reqHeight||width>reqWidth){//get correct power of 2
			    		while(width/inSampleSize>=reqWidth && height/inSampleSize>=reqHeight)
			                inSampleSize*=2;
			    	}
			    	System.out.println(inSampleSize);
			    	return inSampleSize;
			 
			    }
			    
			    public Bitmap getFinalProcessedImage(){
			    	return final_image;
			    }


				//public static Bitmap getFinal_image() {
				//	return final_image;
			//	}


				public void setFinal_image(Bitmap img) {
					final_image = img;
				}
				
				public String getImageName(){
					return imageName;
				}
			  
				private void nullAllViews(){
					TextView titleView=(TextView) parent.findViewById(R.id.imageTitle);
			    	titleView.setText(null);
			    	
			    	TextView dateView=(TextView) parent.findViewById(R.id.imageDate);
			    	dateView.setText(null);

			    	TextView descriptionView=(TextView) parent.findViewById(R.id.imageDescription);
			    	descriptionView.setText(null);
			    	
			    	ImageView imageView=(ImageView) parent.findViewById(R.id.imageDisplay);
			    	imageView.setImageBitmap(null);
			    	
				}
	
	}//class
