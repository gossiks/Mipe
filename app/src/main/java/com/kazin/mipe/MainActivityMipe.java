package com.kazin.mipe;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.kazin.interfaces.IMipeViewer;
import com.kazin.interfaces.IPhotoPresenter;
import com.kazin.interfaces.IPhotoViewer;
import com.kazin.interfaces.IStatsViewer;
import com.kazin.mvp.Singleton;


public class MainActivityMipe extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private Singleton singleton = Singleton.getInstance();

    public static Resources RESOURCES;
    public static Context CONTEXT;
    public static View ROOTVIEW;
    public static ComponentName activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setProgressBarIndeterminate(true);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        activity = getCallingActivity();
        CONTEXT = MainActivityMipe.this;
        singleton.setMainActivity(MainActivityMipe.this);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events. Facebook
        AppEventsLogger.activateApp(this);

        }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event. Facebook
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == singleton.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            IPhotoPresenter presenter = singleton.getPhotoPresenter();
            Log.d("onActivityResult.MainAct", "Data: ");
            presenter.onActivityResultGetPhoto(data);
        }
        if (requestCode == singleton.REQUEST_IMAGE_CHOOSE && resultCode == RESULT_OK) {
            IPhotoPresenter presenter = singleton.getPhotoPresenter();
            presenter.onAcivityResultChoosePhoto(data);
        }
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position){
            case 0:
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceHolderFragmentPhoto.newInstance(position + 1))
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.container, PlaceHolderFragmentStats.newInstance(position + 1))
                        .commit();
            /*default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;*/

        }

    }





    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle= getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section1);
                break;
            case 3:
                mTitle = getString(R.string.title_section2);
                break;
            case 4:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Singleton singleton = Singleton.getInstance();
        private IMipeViewer viewer;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            viewer = singleton.getMipeViewer();
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ViewPager pager = (ViewPager) rootView.findViewById(R.id.viewPageMipe);
            ProgressBar overall = (ProgressBar) rootView.findViewById(R.id.progressBarOverall);
            ProgressBar current = (ProgressBar) rootView.findViewById(R.id.progressBarCurrentImage);
            TextView progressBarText = (TextView) rootView.findViewById(R.id.progressBarText);
            FloatingActionButton uploadData = (FloatingActionButton) rootView.findViewById(R.id.action_upload);
            singleton.setProgressBarText(progressBarText);
            singleton.setProgressBarOverall(overall);
            singleton.setProgressBarCurrent(current);
            uploadData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewer = singleton.getMipeViewer();
                    viewer.onActionButtonUpload();
                }
            });

            viewer.setPager(pager);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivityMipe) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));

            this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            //singleton.getMainActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onResume() {
            super.onResume();
            viewer = singleton.getMipeViewer();
            viewer.onResume();
            viewer.onCreateView(viewer.getPager()); //TODO redo this overrecursion
        }

        @Override
        public void onPause() {
            super.onPause();
            viewer = singleton.getMipeViewer();
            viewer.onPause();
        }
    }

    public static class PlaceHolderFragmentPhoto extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Singleton singleton = Singleton.getInstance();
        private IPhotoViewer viewer;
        private IPhotoPresenter presenter;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceHolderFragmentPhoto newInstance(int sectionNumber) {
            PlaceHolderFragmentPhoto fragment = new PlaceHolderFragmentPhoto();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceHolderFragmentPhoto() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
            ImageView imageViewSend = (ImageView) rootView.findViewById(R.id.imageSent);
            EditText editText = (EditText) rootView.findViewById(R.id.editText);
            singleton.setImageViewSend(imageViewSend);
            singleton.setEditText(editText);
            Button takePhoto = (Button) rootView.findViewById(R.id.buttonTakePhoto);
            Button choosePhoto = (Button) rootView.findViewById(R.id.buttonChoosePhoto);
            Button sendPhoto = (Button) rootView.findViewById(R.id.buttonsend);

            ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarPhoto);
            //viewer = singleton.getPhotoViewer();
            presenter = singleton.getPhotoPresenter();
            presenter.setProgressBar(progressBar);
            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewer = singleton.getPhotoViewer();
                    viewer.onClickTakePhoto();
                }
            });
            choosePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewer = singleton.getPhotoViewer();
                    viewer.onClickChoosePhoto();
                }
            });
            sendPhoto.setOnClickListener(new View.OnClickListener() {
                private static final long THRESHOLD_MILLIS = 1000L;
                private long lastClickMillis;
                @Override
                public void onClick(View v) {
                    long now = SystemClock.elapsedRealtime();
                    if (now - lastClickMillis < THRESHOLD_MILLIS) {
                        return;
                    }
                    lastClickMillis = now;
                    viewer = singleton.getPhotoViewer();
                    viewer.onClickSendPhoto();
                }
            });


            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
                super.onAttach(activity);
            ((MainActivityMipe) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
            this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }


    }

    public static class PlaceHolderFragmentStats extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Singleton singleton = Singleton.getInstance();
        private IStatsViewer viewer;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceHolderFragmentStats newInstance(int sectionNumber) {
            PlaceHolderFragmentStats fragment = new PlaceHolderFragmentStats();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceHolderFragmentStats() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            viewer = singleton.getStatsViewer();
            View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
            GraphView statsGraph = (GraphView) rootView.findViewById(R.id.graph);
            singleton.setStatsGraph(statsGraph);
            viewer.onFragmentLoad();
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivityMipe) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));

            this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

}
