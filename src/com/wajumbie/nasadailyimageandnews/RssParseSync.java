/* -------------------
 * Author:Tyler Pfaff
 * 2/19/2013 
 * Target: 4.0+
 * -------------------*/
package com.wajumbie.nasadailyimageandnews;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



	public class RssParseSync extends AsyncTask<String,String,Bitmap>{  

		private Context parent;
		private ProgressDialog dialog;
		private static Bitmap final_image; //must be static because a new instance is required to access getFinalImage();
		private String imageURL="";
		private static String imageName;
		private long totalTime;
		private long startTime;
		private static View v;
		private Activity mainActivity;
		

		public RssParseSync(View v,Activity mainActivity){
			
			this.v=v;
			this.mainActivity=mainActivity;

			String result=mainActivity.toString();
		}//constructor to pass parent activity, need this to call findViewById
		
		public RssParseSync(){}

				@Override
				protected Bitmap doInBackground(String... info){
					URL iotd;
					int count=info.length;
					boolean finished=false;
					String title="",link="",description="",date="";
					
					try{
						
						iotd = new URL("http://www.nasa.gov/rss/lg_image_of_the_day.rss");//set URl						 
						BufferedReader in = new BufferedReader(new InputStreamReader(iotd.openStream()));//get rss
						XmlPullParserFactory factory;
						factory = XmlPullParserFactory.newInstance();//new factory
						factory.setNamespaceAware(true);
						XmlPullParser xpp;
						xpp = factory.newPullParser();
						xpp.setInput(in);
						
						int eventType = xpp.getEventType();//returns an int which mean different things (START_DOCUMENT,START_TAG,etc)
					
			
					while(!finished){//while the document has words
						
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
								//removeHTML(info[1],"colorful");
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
						case XmlPullParser.END_TAG:
							tagName=xpp.getName();
							if(tagName.equals("item") && xpp.getEventType()==(xpp.END_TAG)){
								finished=true;
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
				private void removeHTML(String text,String tag) {
					
					
							text.replace(tag, "");
						
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


			    protected void onPostExecute(Bitmap image){
					//error when doing this in resetDisplay.... onPostExecute is invoked by the ui thread so this may be why it works here and not in resetDisplay
					ImageView imageView=(ImageView) v.findViewById(R.id.imageDisplay);
			    	imageView.setImageBitmap(image); 
			    	dialog.dismiss(); 
			    	//this.cancel(true);
				}
				
			    protected void onPreExecute(){
					if(final_image!=null){
						final_image.recycle();
					}
					
					nullAllViews();
					dialog=ProgressDialog.show(mainActivity, "Loading", "Loading the image of the day");
				}
				
			    private void resetDisplay(String title, String description,String date, String link) throws MalformedURLException, IOException{
			    	
			    	nullAllViews();
			    	
			    	TextView titleView=(TextView) v.findViewById(R.id.imageTitle);
			    	titleView.setText(title);
			    	
			    	TextView dateView=(TextView) v.findViewById(R.id.imageDate);
			    	dateView.setText(date);

			    	TextView descriptionView=(TextView) v.findViewById(R.id.imageDescription);
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
			    	int height;
			    	int width;
			    	try{
			    		
			    	BitmapFactory.Options options = new BitmapFactory.Options();
			    	options.inJustDecodeBounds=true;
			    	BitmapFactory.decodeStream(new URL(imageUrl).openStream(),null,options);
			    	DisplayMetrics displaymetrics=new DisplayMetrics();
			    	mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			    	height=displaymetrics.heightPixels;
			    	width=displaymetrics.widthPixels;

			    	options.inSampleSize=calculateInSampleSize(options, width ,height);
			    	options.inJustDecodeBounds=false;
			    	final_image=BitmapFactory.decodeStream(new URL(imageUrl).openStream(),null,options);
			    	
			    	}catch(Exception e){
			    		e.printStackTrace();
			    	}
			    	String what=final_image.toString();
			    	return final_image;
			    }
			    
			    
			    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
			    	//raw height and width of image
			    	final int height=options.outHeight;
			    	final int width=options.outWidth;
			    	int inSampleSize=1; //powers of 2 are most efficient
			    	
			    
			    	if(height>reqHeight||width>reqWidth){//get correct power of 2
			    		while(width/inSampleSize>=reqWidth && height/inSampleSize>=reqHeight)
			                inSampleSize*=2;
			    	}
			    	return inSampleSize;
			 
			    }
			    
			    public Bitmap getFinalProcessedImage(){
			    	return final_image;
			    }

			    public void setFinal_image(Bitmap img) {
					final_image = img;
				}
				
			    public String getImageName(){
					return imageName;
				}
			  
			    private void nullAllViews(){
					TextView titleView;
					titleView=(TextView) v.findViewById(R.id.imageTitle);
			    	titleView.setText(null);
			    	
			    	TextView dateView=(TextView) v.findViewById(R.id.imageDate);
			    	dateView.setText(null);

			    	TextView descriptionView=(TextView) v.findViewById(R.id.imageDescription);
			    	descriptionView.setText(null);
			    	
			    	ImageView imageView=(ImageView) v.findViewById(R.id.imageDisplay);
			    	imageView.setImageBitmap(null);
			    	
				}
	
	}//class
