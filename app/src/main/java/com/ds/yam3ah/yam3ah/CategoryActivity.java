package com.ds.yam3ah.yam3ah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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


public class CategoryActivity extends ActionBarActivity {

    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;

    ListView categoryListView;
    JSONArray categoryResponseData;
    static JSONArray categoryResponseDataGlobal;
    public ArrayList<HashMap<String, String>> categoryArrayList = new ArrayList<HashMap<String, String>>();
    CategoryAdapter categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView screenNameTop = (TextView) findViewById(R.id.screenNameTop);
        screenNameTop.setText("CATEGORY");

        screenNameTop.setTypeface(Utils.Optima(CategoryActivity.this));

        homeTab = (ImageView) findViewById(R.id.homeTab);
        categoryTab = (ImageView) findViewById(R.id.categoryTab);
        cartTab = (ImageView) findViewById(R.id.cartTab);
        checkOutTab = (ImageView) findViewById(R.id.checkOutTab);

        homeTab.setBackgroundResource(R.drawable.tab_1_unselected);
        categoryTab.setBackgroundResource(R.drawable.tab_2_selected);
        cartTab.setBackgroundResource(R.drawable.tab_3_unseleted);
        checkOutTab.setBackgroundResource(R.drawable.tab_4_unselected);

        categoryListView = (ListView) findViewById(R.id.categoryList);

        checkNetwork();

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*Intent intent = new Intent(parent.getContext(), AreaActivity.class);
                intent.putExtra("category_id", categoryArrayList.get(position).get("category_id").toString());
                intent.putExtra("category_title",categoryArrayList.get(position).get("category_title").toString());
                parent.getContext().startActivity(intent);
                finish();*/

                Intent intent = new Intent(parent.getContext(), CompanyActivity.class);
                intent.putExtra("category_id", categoryArrayList.get(position).get("category_id").toString());
                intent.putExtra("category_title",categoryArrayList.get(position).get("category_title").toString());
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
            new getCategoryData().execute();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
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

    public class getCategoryData extends AsyncTask<String, Process, String> {

        public ProgressDialog pb;
        String response;
        HttpClient httpClient;
        HttpPost httpPost;
        HttpResponse httpresponse;

        @Override
        public void onPreExecute() {

            pb = ProgressDialog.show(CategoryActivity.this, null, "Loading....");
        }

        @Override
        protected String doInBackground(String... params) {

            // Creating HTTP client
            httpClient = HomeActivity.getHttpsClient(new DefaultHttpClient());
            // Creating HTTP Post
            httpPost = new HttpPost(getString(R.string.weburl));

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("action", "category"));


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
                        categoryResponseData = JsonObj.getJSONArray("responseData");
                        //categoryResponseDataGlobal = JsonObj.getJSONArray("responseData");

                        for (int i = 0; i < categoryResponseData.length(); i++) {
                            JSONObject indexedObj = categoryResponseData.getJSONObject(i);

                            String category_id = indexedObj.getString("category_id");
                            String category_title = indexedObj.getString("category_title");
                            String category_image_url = indexedObj.getString("category_url");

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("category_id", category_id);
                            map.put("category_title", category_title);
                            map.put("category_image_url", category_image_url);

                            categoryArrayList.add(map);

                        }

                        categoryAdapter = new CategoryAdapter(CategoryActivity.this, categoryArrayList);
                        categoryListView.setAdapter(categoryAdapter);

                    } else {
                        CustomAlertDialog showdialog = new CustomAlertDialog();
                        showdialog.showAlertDialog(CategoryActivity.this, R.string.projectName, "Please check your internet connection and try again.");
                    }

                    //responseStatus = Integer.parseInt(JsonObj.getString("status"));
                    System.out.println("Response check == " + responseStatus);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } else {
                CustomAlertDialog showdialog = new CustomAlertDialog();
                showdialog.showAlertDialog(CategoryActivity.this, R.string.projectName, "Please check your internet connection and try again.");
            }

            pb.dismiss();


        }

    }

    public void tabAction(View view) {
        int id = view.getId();
        Intent intent;

        switch (id) {
            case R.id.homeTab:
                intent = new Intent(CategoryActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                //intent = new Intent(ProductDetailActivity.this, CategoryActivity.class);
                //startActivity(intent);
                break;
            case R.id.cartTab:
                intent = new Intent(CategoryActivity.this, CartActivity.class);
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
                    intent = new Intent(CategoryActivity.this, ActivityCheckOut.class);
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }
}
