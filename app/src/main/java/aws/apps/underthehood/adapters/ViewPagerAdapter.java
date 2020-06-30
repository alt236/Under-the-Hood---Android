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

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapter extends PagerAdapter {
    private final String TAG = this.getClass().getName();

    final private ArrayList<View> mViewList;
    final private ArrayList<String> mTitleList;

    public ViewPagerAdapter() {
        mViewList = new ArrayList<>();
        mTitleList = new ArrayList<>();
    }

    public void addView(View view, String title) {
        if (view == null) {
            //Log.w(Constants.TAG, "^ ViewPagerAdapter: View is null");
            return;
        }
        mViewList.add(view);
        mTitleList.add(title.toUpperCase());
    }

    public void Clear() {
        mViewList.clear();
        mTitleList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
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

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ((ViewPager) container).addView(mViewList.get(position), 0);
        return mViewList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, @NonNull Object object) {
        return view.equals(object);
    }

    public boolean shouldHideIndicator() {
        if (getCount() < 2) {
            Log.d(TAG, "^ VIEWPAGERADAPTER: There are " + getCount() + " pages. Should hide!");
            return true;
        }
        return false;
    }
}
