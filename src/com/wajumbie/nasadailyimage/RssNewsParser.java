package com.wajumbie.nasadailyimage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class RssNewsParser{
	URL newsURL;
	ArrayList<Story> stories=new ArrayList<Story>();
	RssNewsParser(){
		
	}
	public ArrayList<Story> getStories(){
		return stories;
	}
	public void parse(){
		int eventType;
		int storyCount= -1;
		try{
			
		newsURL = new URL("http://www.nasa.gov/rss/lg_image_of_the_day.rss");//set URl						 
		BufferedReader in = new BufferedReader(new InputStreamReader(newsURL.openStream()));//get rss
		XmlPullParserFactory factory;
		factory = XmlPullParserFactory.newInstance();//new factory
		factory.setNamespaceAware(true);
		XmlPullParser xpp;
		xpp = factory.newPullParser();
		xpp.setInput(in);
		eventType = xpp.getEventType();//returns an int which mean different things (START_DOCUMENT,START_TAG,etc)
		
		
		
	while(eventType!=XmlPullParser.END_DOCUMENT){//while the document has words
		
	 switch(eventType){
		
		case XmlPullParser.START_DOCUMENT://beginning of xml
			break;
		case XmlPullParser.START_TAG://case : at beginning of new tag
			String tagName=xpp.getName();
	
			if(tagName.equals("item")){
				Story story=new Story();
				stories.add(story);
				storyCount++;
				xpp.getDepth();
			}
			if(tagName.equals("title")){
				xpp.getDepth();
				stories.get(storyCount).setTitle(xpp.nextText());
			}
			if(tagName.equals("link")){
				stories.get(storyCount).setURL(xpp.nextText());
				xpp.getDepth();
			}
			if(tagName.equals("description")){
				stories.get(storyCount).setDescription(xpp.nextText());
				xpp.getDepth();
			}
			
			break;
		
		}
		eventType=xpp.next();
	}//switch	
		in.close();//close BufferedReader
    } catch (MalformedURLException e){
    	e.printStackTrace();
    }catch(XmlPullParserException e1){
    	e1.printStackTrace();
    }catch(IOException e2){
    	e2.printStackTrace();
    }	      

}
	

	

	
}
