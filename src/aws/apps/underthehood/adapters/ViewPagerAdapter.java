/*******************************************************************************
 * Copyright 2010 Alexandros Schillings
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package aws.apps.underthehood.adapters;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {
	private final String TAG =  this.getClass().getName();
	
	final private ArrayList<View> mViewList;
	final private ArrayList<String> mTitleList;

	public ViewPagerAdapter() {
		mViewList = new ArrayList<View>();
		mTitleList = new ArrayList<String>();
	}

	public void addView(View view, String title) {
		if(view == null){
			//Log.w(Constants.TAG, "^ ViewPagerAdapter: View is null");
			return;
		}
		mViewList.add(view);
		mTitleList.add(title.toUpperCase());
	}

	public void Clear(){
		mViewList.clear();
		mTitleList.clear();
		notifyDataSetChanged();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object view) {
		((ViewPager) container).removeView((View) view);
	}

	public int getCount() {
		return mViewList.size();
	}

	public Object getItem(int position) {
		return mViewList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public String getPageTitle(int position) {
		return mTitleList.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return mViewList.get(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(mViewList.get(position), 0);
		return mViewList.get(position);
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals( object );
	}
	
	public boolean shouldHideIndicator(){
		if(mViewList == null || getCount() < 2){
			
			Log.d(TAG, "^ VIEWPAGERADAPTER: There are " + (mViewList==null ? "NULL" : getCount()) + " pages. Should hide!");
			return true;
		}
		return false;
	}
}
