package com.wajumbie.nasadailyimageandnews;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.ListFragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class BreakingNewsFragment extends ListFragment {
	private static Activity mainActivity;
	private static ArrayList<Story> stories=new ArrayList<Story>();
	private static ArrayList<String> storyTitles=new ArrayList<String>();
	private static RssNewsParser parser;
	
	public BreakingNewsFragment(){
		
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
		setRetainInstance(true);
			
			
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(stories.isEmpty()){
			getStories();
		}
		
		String url = stories.get(position).getURL();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
	public void fetchStories(){
		
		parser=new RssNewsParser(getActivity(),this);
		if(stories.isEmpty()){
		parser.execute();
		}
	}
	public void getStories(){
		try {
			stories=parser.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
	}
public void updateList(){
	storyTitles.removeAll(storyTitles);
	for(Story story:stories){
		storyTitles.add(story.getTitle());
	}
	setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,storyTitles));
}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		if(getListAdapter() == null)
		{
			fetchStories();
		}
	}
	
	public void onRefresh() {
	//	getStories();
		
	//updateList();
		
	}

}
