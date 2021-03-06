

package fiek.ds.android.fieknote;

import android.animation.ValueAnimator;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import de.greenrobot.event.EventBus;
import fiek.ds.android.fieknote.async.CategoryMenuTask;
import fiek.ds.android.fieknote.async.MainMenuTask;
import fiek.ds.android.fieknote.async.bus.CategoriesUpdatedEvent;
import fiek.ds.android.fieknote.async.bus.DynamicNavigationReadyEvent;
import fiek.ds.android.fieknote.async.bus.NavigationUpdatedEvent;
import fiek.ds.android.fieknote.async.bus.NavigationUpdatedNavDrawerClosedEvent;
import fiek.ds.android.fieknote.async.bus.NotesLoadedEvent;
import fiek.ds.android.fieknote.async.bus.NotesUpdatedEvent;
import fiek.ds.android.fieknote.async.bus.SwitchFragmentEvent;
import fiek.ds.android.fieknote.models.Category;
import fiek.ds.android.fieknote.models.NavigationItem;
import fiek.ds.android.fieknote.utils.Constants;
import fiek.ds.android.fieknote.utils.Display;


public class NavigationDrawerFragment extends Fragment {

    static final int BURGER = 0;
    static final int ARROW = 1;

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    private MainActivity mActivity;
    private boolean alreadyInitialized;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(fiek.ds.android.fieknote.R.layout.fragment_navigation_drawer, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        init();
    }


    private MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }


    public void onEventMainThread(DynamicNavigationReadyEvent event) {
        if (alreadyInitialized) {
            alreadyInitialized = false;
        } else {
            refreshMenus();
        }
    }


    public void onEvent(CategoriesUpdatedEvent event) {
        refreshMenus();
    }


    public void onEventAsync(NotesUpdatedEvent event) {
        alreadyInitialized = false;
    }


    public void onEvent(NotesLoadedEvent event) {
        if (mDrawerLayout != null) {
			if (!isDoublePanelActive()) {
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			}
		}
        if (getMainActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
            init();
        }
        refreshMenus();
        alreadyInitialized = true;
    }


    public void onEvent(SwitchFragmentEvent event) {
        switch (event.direction) {
            case CHILDREN:
                animateBurger(ARROW);
                break;
            default:
                animateBurger(BURGER);
        }
    }


    public void onEvent(NavigationUpdatedEvent navigationUpdatedEvent) {
        if (navigationUpdatedEvent.navigationItem.getClass().isAssignableFrom(NavigationItem.class)) {
            mActivity.getSupportActionBar().setTitle(((NavigationItem) navigationUpdatedEvent.navigationItem).getText());
        } else {
            mActivity.getSupportActionBar().setTitle(((Category) navigationUpdatedEvent.navigationItem).getName());
        }
        if (mDrawerLayout != null) {
			if (!isDoublePanelActive()) {
				mDrawerLayout.closeDrawer(GravityCompat.START);
			}
            new Handler().postDelayed(() -> EventBus.getDefault().post(new NavigationUpdatedNavDrawerClosedEvent
                    (navigationUpdatedEvent.navigationItem)), 400);
        }
    }


    public void init() {
        Log.d(Constants.TAG, "Started navigation drawer initialization");

        mDrawerLayout = (DrawerLayout) mActivity.findViewById(fiek.ds.android.fieknote.R.id.drawer_layout);
        mDrawerLayout.setFocusableInTouchMode(false);

        // Setting specific bottom margin for Kitkat with translucent nav bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View leftDrawer = getView().findViewById(fiek.ds.android.fieknote.R.id.left_drawer);
            int leftDrawerBottomPadding = Display.getNavigationBarHeightKitkat(getActivity());
            leftDrawer.setPadding(leftDrawer.getPaddingLeft(), leftDrawer.getPaddingTop(),
                    leftDrawer.getPaddingRight(), leftDrawerBottomPadding);
        }

        // ActionBarDrawerToggle± ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(mActivity,
                mDrawerLayout,
                getMainActivity().getToolbar(),
                fiek.ds.android.fieknote.R.string.drawer_open,
                fiek.ds.android.fieknote.R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                mActivity.supportInvalidateOptionsMenu();
            }


            public void onDrawerOpened(View drawerView) {
                mActivity.commitPending();
                mActivity.finishActionMode();
            }
        };

        if (isDoublePanelActive()) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }

        // Styling options
        mDrawerLayout.setDrawerShadow(fiek.ds.android.fieknote.R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        Log.d(Constants.TAG, "Finished navigation drawer initialization");
    }


    private void refreshMenus() {
        buildMainMenu();
        Log.d(Constants.TAG, "Finished main menu initialization");
        buildCategoriesMenu();
        Log.d(Constants.TAG, "Finished categories menu initialization");
        mDrawerToggle.syncState();
    }


    private void buildCategoriesMenu() {
        CategoryMenuTask task = new CategoryMenuTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void buildMainMenu() {
        MainMenuTask task = new MainMenuTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    void animateBurger(int targetShape) {
        if (mDrawerToggle != null) {
            if (targetShape != BURGER && targetShape != ARROW)
                return;
            ValueAnimator anim = ValueAnimator.ofFloat((targetShape + 1) % 2, targetShape);
            anim.addUpdateListener(valueAnimator -> {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                mDrawerToggle.onDrawerSlide(mDrawerLayout, slideOffset);
            });
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(500);
            anim.start();
        }
    }


	public static boolean isDoublePanelActive() {
//		Resources resources = FiekNote.getAppContext().getResources();
//		return resources.getDimension(R.dimen.navigation_drawer_width) == resources.getDimension(R.dimen
//				.navigation_drawer_reserved_space);
		return false;
	}
}
