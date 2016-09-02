/*
package com.example.deepak.eprapplication;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.erpservices.ppc.adapter.ImportantDatesAdapter;
import com.erpservices.ppc.adapter.OnItemClickListener;
import com.erpservices.ppc.adapter.OnLoadMoreListener;
import com.erpservices.ppc.model.ImportantDatesModel;
import com.erpservices.ppc.utils.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;


*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class ImportantDates extends AppCompatActivity {

    public static final String TAG = "Importantdates";

    RecyclerView recyclerView;
    SoapPrimitive resultString;
    List<ImportantDatesModel> lstImpDates;
    int page = 0;
    boolean isLoaded;
    ImportantDatesAdapter mAdapter;
    ProgressDialog progressDialog;

    public ImportantDates() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_important_dates);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerImportantDates);
        page = 0;
        lstImpDates = new ArrayList<>();
        new ListImportantDates().execute();


    }

    private void setListData() {
        if (resultString != null) {

            try {
                JSONObject jsonObject = new JSONObject(resultString.toString().substring(7, resultString.toString().length()));
                JSONArray jsonArray = jsonObject.getJSONArray("Importantdate");
                String rows = jsonObject.getString("TotalRows");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    String Id = jsonData.getString("id");
                    String title = jsonData.getString("title");
                    String LastUpdated = jsonData.getString("last_updated");
                    String Date = jsonData.getString("maindate");
                    String ShowDate = jsonData.getString("ShowDate");

                    ImportantDatesModel noticeboard = new ImportantDatesModel();
                    noticeboard.setId(Id);
                    noticeboard.setTitle(ShowDate);
                    noticeboard.setLastUpdated(LastUpdated);
                    noticeboard.setDescription(title);

                    lstImpDates.add(noticeboard);
                }
                int totalRows = Integer.parseInt(rows);


                if (lstImpDates.size() > 0 && lstImpDates != null) {
                    if (page == 1) {
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ImportantDates.this));
                        mAdapter = new ImportantDatesAdapter(lstImpDates, recyclerView);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        if(lstImpDates.size() < totalRows) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    mAdapter.SetOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                        }
                    });
                    if(lstImpDates.size() < totalRows) {
                        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {

                                isLoaded = true;
                                new ListImportantDates().execute();
                                //   remove progress item

                                //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private int PageNo() {
        return page = page + 1;
    }

    private void getNoticeboardData() {
        String METHOD_NAME = "Calender_holiday_bymonth";
        String NAMESPACE = "http://tempuri.org/";
        String WebURL = "http://erp.erpservices.in/";
        String IMPORTANT_DATE = WebURL + "service.asmx";
        String SOAP_ACTION_IMPORTANT_DATES = "http://tempuri.org/Calender_holiday_bymonth";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("monthno", 9);
            Request.addProperty("year", 2016);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(IMPORTANT_DATE);
            transport.call(SOAP_ACTION_IMPORTANT_DATES, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
*/
/*at const page
public static final String SOAP_ACTION_IMPORTANT_DATES = "http://tempuri.org/Calender_holiday_bymonth";
public static final String WebURL = "http://erp.erpservices.in/";

public static final String IMPORTANT_DATE = WebURL + "service.asmx";

*//*

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }


    private class ListImportantDates extends AsyncTask<String, Void, Void> {

        public ListImportantDates() {
            if (page == 0) {
                progressDialog = new ProgressDialog(ImportantDates.this);
                progressDialog.setMessage("Please wait while processing...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            } else {
                lstImpDates.add(null);
                mAdapter.notifyItemInserted(lstImpDates.size() - 1);
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            getNoticeboardData();
            if (page == 1) {
                progressDialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (isLoaded) {
                if (lstImpDates.size() > 0) {
                    lstImpDates.remove(lstImpDates.size() - 1);
                    mAdapter.notifyItemRemoved(lstImpDates.size());
                }
            }
            setListData();
            if (isLoaded) {
                if(lstImpDates.size() > 0) {
                    mAdapter.notifyItemInserted(lstImpDates.size());
                    mAdapter.setLoaded();
                }
            }
        }
    }
}
*/
