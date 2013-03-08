package com.wajumbie.nasadailyimage;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

@SuppressLint("ValidFragment")
public class BreakingNewsFragment extends ListFragment {
	private static Activity mainActivity;
	private static ArrayList<Story> stories=new ArrayList<Story>();
	private static ArrayList<String> storyTitles=new ArrayList<String>();
	
	public BreakingNewsFragment(){
		
	}
	public BreakingNewsFragment(Activity mainActivity){
		this.mainActivity=mainActivity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
			
			
	}
	public void fetchStories(){
		RssNewsParser parser=new RssNewsParser(mainActivity,this);
		parser.execute();
		
		//stories=parser.getStories();
		

	}
public void updateList(){
	//String result;
	//result = storyTitles.toString();
	//setListAdapter(new ArrayAdapter<String>(mainActivity,android.R.layout.simple_list_item_1,storyTitles));
}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	
		
		
	}

}
