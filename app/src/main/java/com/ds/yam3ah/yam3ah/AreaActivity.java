package com.ds.yam3ah.yam3ah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.yam3ah.model.Area;
import com.ds.yam3ah.model.City;
import com.ds.yam3ah.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AreaActivity extends ActionBarActivity {

    JSONArray areaResponseData ,cityResponseData;
    JSONArray areaResponseDataGlobal ,cityResponseDataGlobal;
    AreaAdapter areaAdapter;
    ListView areaListView;
    public ArrayList<HashMap<String, String>> areaArrayList = new ArrayList<HashMap<String, String>>();
    public ArrayList<HashMap<String, String>> cityArrayList = new ArrayList<HashMap<String, String>>();
    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    ImageButton backBtn;
    String category_title,category_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView screenNameTop = (TextView) findViewById(R.id.screenNameTop);
        screenNameTop.setText("Select Area");
        screenNameTop.setTypeface(Utils.Optima(AreaActivity.this));
        homeTab = (ImageView) findViewById(R.id.homeTab);
        categoryTab = (ImageView) findViewById(R.id.categoryTab);
        cartTab = (ImageView) findViewById(R.id.cartTab);
        checkOutTab = (ImageView) findViewById(R.id.checkOutTab);

        homeTab.setBackgroundResource(R.drawable.tab_1_unselected);
        categoryTab.setBackgroundResource(R.drawable.tab_2_selected);
        cartTab.setBackgroundResource(R.drawable.tab_3_unseleted);
        checkOutTab.setBackgroundResource(R.drawable.tab_4_unselected);

        backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            category_id = extras.getString("category_id");
            category_title=extras.getString("category_title");
            GlobalData.selectedCategory=category_title;
        }

        areaListView=(ListView)findViewById(R.id.areaList);
        checkNetwork();


    }


    private void checkNetwork() {
        // TODO Auto-generated method stub
        Network cd;
        boolean isInternetPresent = false;

        cd = new Network(this.getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            new getAreaData().execute();
            // Toast.makeText(this, "internet connection is working", Toast.LENGTH_LONG).show();
        } else {

            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Sorry, Please check your internet connection")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    public class getAreaData extends AsyncTask<String, Process, String> {

        public ProgressDialog pb;
        String response;
        HttpClient httpClient;
        HttpPost httpPost;
        HttpResponse httpresponse;

        @Override
        public void onPreExecute() {

            pb = ProgressDialog.show(AreaActivity.this, null, "Loading....");
        }

        @Override
        protected String doInBackground(String... params) {

            // Creating HTTP client
            httpClient = HomeActivity.getHttpsClient(new DefaultHttpClient());
            // Creating HTTP Post
            httpPost = new HttpPost(getString(R.string.weburl));

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("action", "getStateCityList"));

            Log.e("nameValuePair", nameValuePair.toString());

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            } catch (UnsupportedEncodingException e) {
                // writing error to Log
                e.printStackTrace();
            }

            try {
                httpresponse = httpClient.execute(httpPost);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }


        public void onPostExecute(String result) {
            super.onPostExecute(result);

            // Making HTTP Request
            //try {


            if (httpresponse != null) {
                String response = null;
                try {
                    response = EntityUtils.toString(httpresponse.getEntity());


                    System.out.println("response == "+response);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject JsonObj = null;
                int responseStatus = 0;
                try {
                    JsonObj = new JSONObject(response);
                    JSONObject status = JsonObj.getJSONObject("status");
                    JSONObject responseData = JsonObj.getJSONObject("responseData");

                    responseStatus = Integer.parseInt(status.getString("status"));

                    if (responseStatus == 1) {
                        areaResponseData = responseData.getJSONArray("area");
                        areaResponseDataGlobal = responseData.getJSONArray("area");

                        System.out.println("areaResponseData == "+areaResponseData);
                        ArrayList<Area> areaArr=new ArrayList<Area>();
                        for (int i = 0; i < areaResponseData.length(); i++) {
                            Area aObj=new Area();

                            JSONObject indexedObj = areaResponseData.getJSONObject(i);

                            String state = indexedObj.getString("state");
                            String city = indexedObj.getString("city");

                            aObj.setState(state);

                            cityResponseData= indexedObj.getJSONArray("city");
                            cityResponseDataGlobal=indexedObj.getJSONArray("city");
                            ArrayList<City> cArr=new ArrayList<City>();

                            for(int j=0; j <cityResponseData.length();j++){

                                City cObj=new City();
                                JSONObject cityObj = cityResponseData.getJSONObject(j);

                                String id = cityObj.getString("id");
                                String name = cityObj.getString("name");

                                cObj.setId(id);
                                cObj.setName(name);


                               cArr.add(cObj);


                            }

                            aObj.setCityarr(cArr);


                            areaArr.add(aObj);

                        }

                       areaAdapter = new AreaAdapter(AreaActivity.this, areaArr,category_id);
                       areaListView.setAdapter(areaAdapter);


                    }
                    else if(responseStatus == 0)
                    {
                        String responseMessage = status.getString("status_message");
                        CustomAlertDialog showdialog = new CustomAlertDialog();
                        showdialog.showAlertDialog(AreaActivity.this, R.string.projectName, responseMessage);
                    }
                    else
                    {
                        CustomAlertDialog showdialog = new CustomAlertDialog();
                        showdialog.showAlertDialog(AreaActivity.this, R.string.projectName, "Please check your internet connection and try again...");
                    }

                    //responseStatus = Integer.parseInt(JsonObj.getString("status"));
                    System.out.println("Response check == " + responseStatus);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
            else
            {
                CustomAlertDialog showdialog = new CustomAlertDialog();
                showdialog.showAlertDialog(AreaActivity.this, R.string.projectName, "Please check your internet connection and try again.");
            }

            pb.dismiss();


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AreaActivity.this,CategoryActivity.class);
        startActivity(intent);
        finish();
    }

    public void backMethod(View view)
    {
        //onBackPressed();

        Intent intent=new Intent(AreaActivity.this,CategoryActivity.class);
        startActivity(intent);
        finish();
    }

    public void tabAction(View view) {
        int id = view.getId();
        Intent intent;

        switch (id) {
            case R.id.homeTab:
                intent = new Intent(AreaActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                //intent = new Intent(ProductDetailActivity.this, CategoryActivity.class);
                //startActivity(intent);
                break;
            case R.id.cartTab:
                intent = new Intent(AreaActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.checkOutTab:
                if (HomeActivity.globalCart.size() == 0) {
                    // Toast.makeText(this.getApplicationContext(),"you have not any product in to cart",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Notice!");

                    builder.setMessage("Your cart is empty.Add item to cart")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    intent = new Intent(AreaActivity.this, ActivityCheckOut.class);
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }

}
