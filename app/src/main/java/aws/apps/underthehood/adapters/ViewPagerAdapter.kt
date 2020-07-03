/*******************************************************************************
 * Copyright 2010 Alexandros Schillings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aws.apps.underthehood.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import java.util.*

class ViewPagerAdapter : PagerAdapter() {
    private val TAG = this.javaClass.name
    private val views: ArrayList<View> = ArrayList()
    private val titles: ArrayList<String> = ArrayList()

    fun addView(view: View?, title: String) {
        if (view == null) {
            return
        }
        views.add(view)
        titles.add(title.toUpperCase())
    }

    fun clear() {
        views.clear()
        titles.clear()
        notifyDataSetChanged()
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun getCount(): Int {
        return views.size
    }

    fun getItem(position: Int): Any {
        return views[position]
    }

    fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getPageTitle(position: Int): String? {
        return titles[position]
    }

    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return views[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(views[position], 0)
        return views[position]
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    fun shouldHideIndicator(): Boolean {
        if (count < 2) {
            Log.d(TAG, "There are $count pages. Should hide!")
            return true
        }
        return false
    }

}