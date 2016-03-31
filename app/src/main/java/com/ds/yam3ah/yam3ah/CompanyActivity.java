package com.ds.yam3ah.yam3ah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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


public class CompanyActivity extends ActionBarActivity {

    ImageButton backBtn;

    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    String cityId;
    String category_id;
    String categoryId;
    String category_title;
    ListView companyListView;
    JSONArray companyResponseData;
    static JSONArray companyResponseDataGlobal;
    public ArrayList<HashMap<String, String>> companyArrayList = new ArrayList<HashMap<String, String>>();
    CompanyAdapter companyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
             categoryId = extras.getString("category_id");
            /*cityId = extras.getString("cityId");
            GlobalData.cityId=cityId;*/
            String category_title=extras.getString("category_title");

        }


        System.out.println("category_name == "+category_title);

        homeTab = (ImageView)findViewById(R.id.homeTab);
        categoryTab = (ImageView)findViewById(R.id.categoryTab);
        cartTab = (ImageView)findViewById(R.id.cartTab);
        checkOutTab = (ImageView)findViewById(R.id.checkOutTab);

        homeTab.setBackgroundResource(R.drawable.tab_1_unselected);
        categoryTab.setBackgroundResource(R.drawable.tab_2_selected);
        cartTab.setBackgroundResource(R.drawable.tab_3_unseleted);
        checkOutTab.setBackgroundResource(R.drawable.tab_4_unselected);

        backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setVisibility(View.VISIBLE);

        TextView screenNameTop = (TextView)findViewById(R.id.screenNameTop);
        screenNameTop.setText(GlobalData.selectedCategory+" List");

        screenNameTop.setTypeface(Utils.Optima(CompanyActivity.this));

        companyListView = (ListView)findViewById(R.id.companyList);

        checkNetwork();

        companyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(parent.getContext(), CompanyDetialActivity.class);

                String pos = String.valueOf(position);

                intent.putExtra("listViewPosition", pos);
                intent.putExtra("category_id", category_id);
                intent.putExtra("category_title", GlobalData.selectedCategory);
                intent.putExtra("tag", "CompanyActivity");

                parent.getContext().startActivity(intent);
                finish();

            }

        });
    }

    private void checkNetwork() {
        // TODO Auto-generated method stub
        Network cd;
        boolean isInternetPresent = false;

        cd = new Network(this.getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            new getCompanyData().execute();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CompanyActivity.this, CategoryActivity.class);
        intent.putExtra("category_id", category_id);
        intent.putExtra("category_title", GlobalData.selectedCategory);
        startActivity(intent);
        finish();
    }

    public void backMethod(View view)
    {
        Intent intent = new Intent(CompanyActivity.this, CategoryActivity.class);
        intent.putExtra("category_id", category_id);
        intent.putExtra("category_title", GlobalData.selectedCategory);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company, menu);
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

    public class getCompanyData extends AsyncTask<String, Process, String> {

        public ProgressDialog pb;
        String response;
        HttpClient httpClient;
        HttpPost httpPost;
        HttpResponse httpresponse;

        @Override
        public void onPreExecute() {

            pb = ProgressDialog.show(CompanyActivity.this, null, "Loading....");
        }

        @Override
        protected String doInBackground(String... params) {

            // Creating HTTP client
            httpClient = HomeActivity.getHttpsClient(new DefaultHttpClient());
            // Creating HTTP Post
            httpPost = new HttpPost(getString(R.string.weburl));

            JSONObject requestParameters = new JSONObject();
            try
            {
                requestParameters.put("categoryid", categoryId);
              //  requestParameters.put("cityid", GlobalData.cityId);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }


            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("action", "getCategoryCompany"));
         //   nameValuePair.add(new BasicNameValuePair("action", "getCityProductList"));
            nameValuePair.add(new BasicNameValuePair("json", requestParameters.toString()));


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

                    responseStatus = Integer.parseInt(status.getString("status"));

                    if (responseStatus == 1) {
                        companyResponseData = JsonObj.getJSONArray("responseData");
                        companyResponseDataGlobal = JsonObj.getJSONArray("responseData");

                        System.out.println("companyResponseData == "+companyResponseData);

                        for (int i = 0; i < companyResponseData.length(); i++) {
                            JSONObject indexedObj = companyResponseData.getJSONObject(i);

                            String company_id = indexedObj.getString("company_id");
                            String company_name = indexedObj.getString("company_name");
                            String company_image = indexedObj.getString("company_thumb_image");
                            //String company_image = indexedObj.getString("company_image_thumb");
                            category_id = indexedObj.getString("category_id");
                           // GlobalData.selectedCategory=indexedObj.getString("category_title");
                            category_title=indexedObj.getString("category_title");
                            GlobalData.selectedCategory=category_title;

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("company_id", company_id);
                            map.put("company_name", company_name);
                            map.put("company_image", company_image);

                            companyArrayList.add(map);

                        }

                        companyAdapter = new CompanyAdapter(CompanyActivity.this, companyArrayList);
                        companyListView.setAdapter(companyAdapter);

                    }
                    else if(responseStatus == 0)
                    {
                        String responseMessage = status.getString("status_message");
                        CustomAlertDialog showdialog = new CustomAlertDialog();
                        showdialog.showAlertDialog(CompanyActivity.this, R.string.projectName, responseMessage);
                    }
                    else
                    {
                        CustomAlertDialog showdialog = new CustomAlertDialog();
                        showdialog.showAlertDialog(CompanyActivity.this, R.string.projectName, "Please check your internet connection and try again...");
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
                showdialog.showAlertDialog(CompanyActivity.this, R.string.projectName, "Please check your internet connection and try again.");
            }

            pb.dismiss();


        }

    }

    public void tabAction(View view)
    {
        int id = view.getId();
        Intent intent;

        switch(id) {
            case R.id.homeTab:
                intent = new Intent(CompanyActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                intent = new Intent(CompanyActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cartTab:
                intent = new Intent(CompanyActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.checkOutTab:
                if (HomeActivity.globalCart.size() == 0) {
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
                    intent = new Intent(this.getApplicationContext(), ActivityCheckOut.class);
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }
}
