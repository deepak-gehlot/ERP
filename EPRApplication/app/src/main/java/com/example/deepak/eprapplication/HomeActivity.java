package com.example.deepak.eprapplication;

import com.google.gson.Gson;

import com.example.deepak.eprapplication.Model.EventBean;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private static int tag = 0;
    CaldroidFragment caldroidFragment;
    private HashMap<Date, Drawable> eventDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getNoticeboardData(int m, int y) {
        String METHOD_NAME = "Calender_holiday_bymonth";
        String NAMESPACE = "http://tempuri.org/";
        String WebURL = "http://erp.erpservices.in/";
        String IMPORTANT_DATE = WebURL + "service.asmx";
        String SOAP_ACTION_IMPORTANT_DATES = "http://tempuri.org/Calender_holiday_bymonth";
        String responce = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("monthno", m);
            Request.addProperty("year", y);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(IMPORTANT_DATE);
            transport.call(SOAP_ACTION_IMPORTANT_DATES, soapEnvelope);
            SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
            responce = resultString.toString().substring(7, resultString.toString().length());
            Log.e("", "Error: " + resultString.toString());
        } catch (Exception ex) {
            Log.e("", "Error: " + ex.getMessage());
        }
        return responce;
    }

    private class Task extends AsyncTask<Void, Void, String> {

        private int mounth, year;
        private ProgressDialog mProgressDialog;


        public Task(int m, int y) {
            this.mounth = m;
            this.year = y;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(HomeActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading event...");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            return getNoticeboardData(mounth, year);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
            if (!s.trim().isEmpty()) {
                EventBean event = new Gson().fromJson(s, EventBean.class);
                eventDates = new HashMap<>();
                ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(HomeActivity.this, R.color.cyan_500));
                for (EventBean.Calender item : event.Calender) {

                    DateFormat df = new SimpleDateFormat("dd MMMMM yyyy");
                    Date startDate;
                    try {
                        startDate = df.parse(item.ShowDate);
                       /* String newDateString = df.format(startDate);
                        System.out.println(newDateString);*/
                        eventDates.put(startDate, colorDrawable);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (eventDates != null && eventDates.size() != 0) {
                        caldroidFragment.setBackgroundDrawableForDates(eventDates);
                        caldroidFragment.refreshView();
                    } else {
                        Toast.makeText(HomeActivity.this, "No event for this month.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void showEventDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setTitle("Event");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Calendar calendar = Calendar.getInstance();
            int m = calendar.get(Calendar.MONTH);
            int y = calendar.get(Calendar.YEAR);

            CaldroidListener caldroidListener = new CaldroidListener() {
                @Override
                public void onSelectDate(Date date, View view) {
                    if (eventDates != null && eventDates.size() != 0) {
                        Drawable drawable = eventDates.get(date);
                        if (drawable != null) {
                            DateFormat df = new SimpleDateFormat("dd MMMMM yyyy");
                            String dateStr = df.format(date);
                            showEventDialog("" + dateStr + " \n No description available.");
                        }
                    }
                }

                @Override
                public void onChangeMonth(int month, int year) {
                    super.onChangeMonth(month, year);
                    if (tag != 0) {
                        new Task(month, year).execute();
                    } else {
                        tag = 1;
                    }
                }
            };

            caldroidFragment = new CaldroidFragment();
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            caldroidFragment.setArguments(args);
            caldroidFragment.setCaldroidListener(caldroidListener);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.content_home, caldroidFragment);
            t.commit();

            new Task(m + 1, y).execute();

        } else if (id == R.id.nav_gallery) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home, PlaceholderFragment.newInstance(1))
                .commit();
        } else if (id == R.id.nav_slideshow) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home, BlankFragment.newInstance())
                .commit();
        } else if (id == R.id.nav_manage) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home, BlankFragment.newInstance())
                .commit();
        } else if (id == R.id.nav_share) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home, BlankFragment.newInstance())
                .commit();
        } else if (id == R.id.nav_send) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home, BlankFragment.newInstance())
                .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static class BlankFragment extends Fragment {
        public BlankFragment() {
        }

        public static BlankFragment newInstance() {
            return new BlankFragment();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.blanck_fragment, container, false);
            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private GridView mGridView;
        private ViewPager mViewPager;
        private TextView mTitleTxt;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity2, container, false);
            mGridView = (GridView) rootView.findViewById(R.id.grid_view);
            mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
            mTitleTxt = (TextView) rootView.findViewById(R.id.txt_album_name);


            mGridView.setAdapter(new GallaryAdapter(getActivity()));
            mViewPager.setAdapter(new GallaryViewPagerAdapter(getActivity()));


            return rootView;
        }


        private class GallaryAdapter extends BaseAdapter {

            private LayoutInflater layoutInflater;
            private GallaryAdapter.ViewHolder viewHolder;

            public GallaryAdapter(Context context) {
                layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return 20;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(final int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = layoutInflater.inflate(R.layout.gallary_grid_row, viewGroup, false);
                    viewHolder = new GallaryAdapter.ViewHolder();
                    viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (GallaryAdapter.ViewHolder) view.getTag();
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setVisibility(View.VISIBLE);
                        mViewPager.setCurrentItem(i, false);
                    }
                });
                return view;
            }

            private class ViewHolder {
                private ImageView imageView;
            }
        }

        private class GallaryViewPagerAdapter extends PagerAdapter {

            public GallaryViewPagerAdapter(Context context) {

            }

            @Override
            public int getCount() {
                return 20;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(R.layout.gallary_view_pager_row, container, false);
/*
                TextView textView = (TextView) view.findViewById(R.id.txt_description);
                ImageView imageView = (ImageView) view.findViewById(R.id.image);*/


                container.addView(view);
                return view;

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        }
    }
}
