

package fiek.ds.android.fieknote.async;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

import de.greenrobot.event.EventBus;
import fiek.ds.android.fieknote.MainActivity;
import fiek.ds.android.fieknote.SettingsActivity;
import fiek.ds.android.fieknote.async.bus.NavigationUpdatedEvent;
import fiek.ds.android.fieknote.db.DbHelper;
import fiek.ds.android.fieknote.models.Category;
import fiek.ds.android.fieknote.models.ONStyle;
import fiek.ds.android.fieknote.models.adapters.NavDrawerCategoryAdapter;
import fiek.ds.android.fieknote.models.views.NonScrollableListView;


public class CategoryMenuTask extends AsyncTask<Void, Void, List<Category>> {

    private final WeakReference<Fragment> mFragmentWeakReference;
    private final MainActivity mainActivity;
    private NonScrollableListView mDrawerCategoriesList;
    private View settingsView;
    private View settingsViewCat;
    private NonScrollableListView mDrawerList;


    public CategoryMenuTask(Fragment mFragment) {
        mFragmentWeakReference = new WeakReference<>(mFragment);
        this.mainActivity = (MainActivity) mFragment.getActivity();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDrawerList = (NonScrollableListView) mainActivity.findViewById(fiek.ds.android.fieknote.R.id.drawer_nav_list);
        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        settingsView = mainActivity.findViewById(fiek.ds.android.fieknote.R.id.settings_view);

        // Settings view when categories are available
        mDrawerCategoriesList = (NonScrollableListView) mainActivity.findViewById(fiek.ds.android.fieknote.R.id.drawer_tag_list);
        if (mDrawerCategoriesList.getAdapter() == null && mDrawerCategoriesList.getFooterViewsCount() == 0) {
            settingsViewCat = inflater.inflate(fiek.ds.android.fieknote.R.layout.drawer_category_list_footer, null);
            mDrawerCategoriesList.addFooterView(settingsViewCat);
        } else {
            settingsViewCat = mDrawerCategoriesList.getChildAt(mDrawerCategoriesList.getChildCount() - 1);
        }

    }


    @Override
    protected List<Category> doInBackground(Void... params) {
        if (isAlive()) {
            return buildCategoryMenu();
        } else {
            cancel(true);
            return null;
        }
    }


    @Override
    protected void onPostExecute(final List<Category> categories) {
        if (isAlive()) {
            mDrawerCategoriesList.setAdapter(new NavDrawerCategoryAdapter(mainActivity, categories,
                    mainActivity.getNavigationTmp()));
            if (categories.size() == 0) {
                setWidgetVisibility(settingsViewCat, false);
                setWidgetVisibility(settingsView, true);
            } else {
                setWidgetVisibility(settingsViewCat, true);
                setWidgetVisibility(settingsView, false);
            }
            mDrawerCategoriesList.justifyListViewHeightBasedOnChildren();
        }
    }


    private void setWidgetVisibility(View view, boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }


    private boolean isAlive() {
        return mFragmentWeakReference.get() != null
                && mFragmentWeakReference.get().isAdded()
                && mFragmentWeakReference.get().getActivity() != null
                && !mFragmentWeakReference.get().getActivity().isFinishing();
    }


    private List<Category> buildCategoryMenu() {
        // Retrieves data to fill tags list
        List<Category> categories = DbHelper.getInstance().getCategories();

        View settings = categories.isEmpty() ? settingsView : settingsViewCat;
        if (settings == null) return categories;
//        Fonts.overrideTextSize(mainActivity,
//                mainActivity.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS),
//                settings);
        settings.setOnClickListener(v -> {
			Intent settingsIntent = new Intent(mainActivity, SettingsActivity.class);
			mainActivity.startActivity(settingsIntent);
		});

        // Sets click events
        mDrawerCategoriesList.setOnItemClickListener((arg0, arg1, position, arg3) -> {

			Object item = mDrawerCategoriesList.getAdapter().getItem(position);
			if (mainActivity.updateNavigation(String.valueOf(((Category) item).getId()))) {
                mDrawerCategoriesList.setItemChecked(position, true);
                // Forces redraw
                if (mDrawerList != null) {
                    mDrawerList.setItemChecked(0, false);
                    EventBus.getDefault().post(new NavigationUpdatedEvent(mDrawerCategoriesList.getItemAtPosition
                            (position)));
                }
			}
		});

        // Sets long click events
        mDrawerCategoriesList.setOnItemLongClickListener((arg0, view, position, arg3) -> {
			if (mDrawerCategoriesList.getAdapter() != null) {
				Object item = mDrawerCategoriesList.getAdapter().getItem(position);
				// Ensuring that clicked item is not the ListView header
				if (item != null) {
					mainActivity.editTag((Category) item);
				}
			} else {
				mainActivity.showMessage(fiek.ds.android.fieknote.R.string.category_deleted, ONStyle.ALERT);
			}
			return true;
		});

        return categories;
    }

}