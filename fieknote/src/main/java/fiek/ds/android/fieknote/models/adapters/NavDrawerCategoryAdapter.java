/*
 * Copyright (C) 2015 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fiek.ds.android.fieknote.models.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.neopixl.pixlui.components.textview.TextView;
import fiek.ds.android.fieknote.MainActivity;
import fiek.ds.android.fieknote.models.Category;
import fiek.ds.android.fieknote.utils.Constants;
import fiek.ds.android.fieknote.utils.Fonts;

import java.util.List;


public class NavDrawerCategoryAdapter extends BaseAdapter {

    private Activity mActivity;
    private int layout;
    private List<Category> categories;
    private LayoutInflater inflater;
    private final String navigationTmp;


    public NavDrawerCategoryAdapter(Activity mActivity, List<Category> categories) {
        this(mActivity, categories, null);
    }


    public NavDrawerCategoryAdapter(Activity mActivity, List<Category> categories, String navigationTmp) {
        this.mActivity = mActivity;
        this.layout = fiek.ds.android.fieknote.R.layout.drawer_list_item;
        this.categories = categories;
        this.navigationTmp = navigationTmp;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return categories.size();
    }


    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        // Finds elements
        Category category = categories.get(position);

        NoteDrawerCategoryAdapterViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);

            // Overrides font sizes with the one selected from user
            Fonts.overrideTextSize(mActivity, mActivity.getSharedPreferences(Constants.PREFS_NAME,
                    Context.MODE_MULTI_PROCESS), convertView);

            holder = new NoteDrawerCategoryAdapterViewHolder();

            holder.imgIcon = (ImageView) convertView.findViewById(fiek.ds.android.fieknote.R.id.icon);
            holder.txtTitle = (TextView) convertView.findViewById(fiek.ds.android.fieknote.R.id.title);
            holder.count = (android.widget.TextView) convertView.findViewById(fiek.ds.android.fieknote.R.id.count);
            convertView.setTag(holder);
        } else {
            holder = (NoteDrawerCategoryAdapterViewHolder) convertView.getTag();
        }

        // Set the results into TextViews	
        holder.txtTitle.setText(category.getName());

        if (isSelected(position)) {
            holder.txtTitle.setTypeface(null, Typeface.BOLD);
            holder.txtTitle.setTextColor(Integer.parseInt(category.getColor()));
        } else {
            holder.txtTitle.setTypeface(null, Typeface.NORMAL);
            holder.txtTitle.setTextColor(mActivity.getResources().getColor(fiek.ds.android.fieknote.R.color.drawer_text));
        }

        // Set the results into ImageView checking if an icon is present before
        if (category.getColor() != null && category.getColor().length() > 0) {
            Drawable img = mActivity.getResources().getDrawable(fiek.ds.android.fieknote.R.drawable.ic_folder_special_black_24dp);
            ColorFilter cf = new LightingColorFilter(Color.parseColor("#000000"), Integer.parseInt(category.getColor()));
            // Before API 16 the object is mutable yet
            if (Build.VERSION.SDK_INT >= 16) {
                img.mutate().setColorFilter(cf);
            } else {
                img.setColorFilter(cf);
            }
            holder.imgIcon.setImageDrawable(img);
            int padding = 4;
            holder.imgIcon.setPadding(padding, padding, padding, padding);
        }

        // Sets category count if set in preferences
        if (mActivity.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS).getBoolean
                ("settings_show_category_count", true)) {
            holder.count.setText(String.valueOf(category.getCount()));
            holder.count.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    private boolean isSelected(int position) {

        // Getting actual navigation selection
        String[] navigationListCodes = mActivity.getResources().getStringArray(
                fiek.ds.android.fieknote.R.array.navigation_list_codes);

        // Managing temporary navigation indicator when coming from a widget
        String navigationTmpLocal = MainActivity.class.isAssignableFrom(mActivity.getClass()) ? ((MainActivity)
                mActivity).getNavigationTmp() : null;
        navigationTmpLocal = this.navigationTmp != null ? this.navigationTmp : navigationTmpLocal;

        String navigation = navigationTmp != null ? navigationTmpLocal
                : mActivity.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS)
                .getString(Constants.PREF_NAVIGATION,
                        navigationListCodes[0]);

        return navigation.equals(String.valueOf(categories.get(position).getId()));
    }

}


/**
 * Holder object
 *
 * @author fede
 */
class NoteDrawerCategoryAdapterViewHolder {

    ImageView imgIcon;
    TextView txtTitle;
    android.widget.TextView count;
}
