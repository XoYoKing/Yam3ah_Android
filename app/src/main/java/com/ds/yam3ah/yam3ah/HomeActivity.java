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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.yam3ah.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HomeActivity extends ActionBarActivity implements SwipeRefreshLayoutBottom.OnRefreshListener {

    //EditText searchField;
    GridView productListView;
    JSONArray productResponseData;
    static JSONArray productResponseDataGlobal;
    public ArrayList<HashMap<String, String>> productArrayList;
    public ArrayList<HashMap<String, String>> productArrayList1 = new ArrayList<HashMap<String, String>>();
    HomeProductAdapter productAdapter;
    int count;
    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    boolean searchFlag = false;
    boolean flag_loading=true;
    int currentScrollState = 0;
    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 0;
    int totalItemCount = 0;
    private SwipeRefreshLayoutBottom mSwipeRefreshLayout;

    private SearchView searchfield;
    public static ArrayList<GlobalData> globalCart = new ArrayList<GlobalData>();
   public boolean initLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        productListView = (GridView) findViewById(R.id.productlist);
        //  searchField = (EditText)findViewById(R.id.searchfield);

        homeTab = (ImageView) findViewById(R.id.homeTab);
        categoryTab = (ImageView) findViewById(R.id.categoryTab);
        cartTab = (ImageView) findViewById(R.id.cartTab);
        checkOutTab = (ImageView) findViewById(R.id.checkOutTab);

        homeTab.setBackgroundResource(R.drawable.tab_1_selected);
        categoryTab.setBackgroundResource(R.drawable.tab_2_unseleted);
        cartTab.setBackgroundResource(R.drawable.tab_3_unseleted);
        checkOutTab.setBackgroundResource(R.drawable.tab_4_unselected);

        TextView screenNameTop = (TextView) findViewById(R.id.screenNameTop);
        screenNameTop.setText("HOME");

        mSwipeRefreshLayout= (SwipeRefreshLayoutBottom) findViewById(R.id.swipeRefreshLayout);


        screenNameTop.setTypeface(Utils.Optima(HomeActivity.this));

        productArrayList = new ArrayList<HashMap<String, String>>();
        productAdapter = new HomeProductAdapter(HomeActivity.this, productArrayList);
        productListView.setAdapter(productAdapter);

       /* if(productArrayList!=null && productArrayList.size()>0){
            productArrayList.clear();
        }*/

        checkNetwork();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        searchfield = (SearchView) findViewById(R.id.searchfield);
        mSwipeRefreshLayout.setOnRefreshListener(this);

       /* mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {

                count=count+15;
                new getProductData("").execute();
            }
        });*/
        searchfield.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchFlag = true;
                count=0;
                productArrayList.clear();
                productAdapter.notifyDataSetChanged();
                new getProductData(query).execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() < 1) {
                    searchFlag = false;
                    new getProductData("").execute();
                }
                return false;
            }
        });
        /*searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                int textLength = searchField.getText().length();
                if(textLength > 0)
                {
                    if (3 == EditorInfo.IME_ACTION_SEARCH)
                    {
                        searchFlag = true;
                        new getProductData().execute();
                    }
                    else
                    {
                        //
                    }
                }
                else
                {
                    //searchFlag = false;
                }


                return false;
            }
        });*/



        /*try{

            searchField.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //adapter.getFilter().filter(s.toString());
                    int textLength = searchField.getText().length();
                    System.out.println("textLength ==== "+textLength);

                    try
                    {
                        if(textLength > 0)
                        {
                            *//*if (EditorInfo.IME_ACTION_SEARCH == 0)
                            {
                                searchFlag = true;
                                new getProductData().execute();
                            }
                            else
                            {
                                //
                            }*//*
                        }
                        else
                        {
                            searchFlag = false;
                            new getProductData().execute();
                        }


                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){
            e.getMessage();
        }*/
    }




    private void checkNetwork() {
        // TODO Auto-generated method stub
        Network cd;
        boolean isInternetPresent = false;

        cd = new Network(this.getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            new getProductData("").execute();
            //    Toast.makeText(this, "internet connection is working", Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    @Override
    public void onRefresh() {
      if(searchFlag==false) {
          count = count + 15;
      }else{
          count=0;
      }
        new getProductData("").execute();
    }


    public class getProductData extends AsyncTask<String, Process, String> {

        public ProgressDialog pb;
        String response;
        HttpClient httpClient;
        HttpPost httpPost;
        HttpResponse httpresponse;
        private String search;

        public getProductData(String s) {
            search = s;

        }

        @Override
        public void onPreExecute() {
            if(!initLoader) {
                pb = ProgressDialog.show(HomeActivity.this, null, "Loading....");
                initLoader=true;
            }
        }

        @Override
        protected String doInBackground(String... params) {

            // Creating HTTP client
            httpClient =getHttpsClient(new DefaultHttpClient());
            // Creating HTTP Post
            httpPost = new HttpPost(getString(R.string.weburl));

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);

            if (searchFlag == false) {
                nameValuePair.add(new BasicNameValuePair("action", "getLatestProductWithProductDetail"));
                JSONObject objData = new JSONObject();
                try {
                    objData.put("offset", count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                nameValuePair.add(new BasicNameValuePair("json", objData.toString()));
            } else {
                JSONObject requestParameters = new JSONObject();

                try {
                    requestParameters.put("search", search);
                    requestParameters.put("offset", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                nameValuePair.add(new BasicNameValuePair("action", "getSearchProductWithDetail"));
                nameValuePair.add(new BasicNameValuePair("json", requestParameters.toString()));
            }

            Log.e("nameValuePair", nameValuePair.toString());

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            } catch (UnsupportedEncodingException e) {
                // writing error to Log
                e.printStackTrace();
            }

            try {
                httpresponse = httpClient.execute(httpPost);
                response = EntityUtils.toString(httpresponse.getEntity());

                return response;
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }

        }


        public void onPostExecute(String result) {
            super.onPostExecute(result);

            if(mSwipeRefreshLayout!=null){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            // Making HTTP Request
            //try {


            if (result != null) {
               /* String response = null;
                try {
                    response = EntityUtils.toString(httpresponse.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                JSONObject JsonObj = null;
                int responseStatus = 0;
                try {
                    JsonObj = new JSONObject(response);
                    JSONObject status = JsonObj.getJSONObject("status");

                    responseStatus = Integer.parseInt(status.getString("status"));

                    if (responseStatus == 1) {

                        productResponseData = JsonObj.optJSONArray("responseData");

                        if(productResponseDataGlobal != null && productResponseDataGlobal.length() > 1){

                           JSONArray refreshArrObj = JsonObj.getJSONArray("responseData");
                            productResponseDataGlobal.put(refreshArrObj);
                        }else {

                            productResponseDataGlobal = JsonObj.getJSONArray("responseData");
                        }
                        Log.d("HomeViewAdapter","Response  :" + productResponseDataGlobal);
                        System.out.println("Response  :" + productResponseDataGlobal);


                        for (int i = 0; i < productResponseData.length(); i++) {
                            JSONObject indexedObj = productResponseData.getJSONObject(i);

                            String product_id = indexedObj.getString("product_id");
                            String product_name = indexedObj.getString("product_name");
                            String product_currency = indexedObj.getString("product_currency");
                            String product_feature_image = indexedObj.getString("feature_image_thumb");
                            String product_desc = indexedObj.getString("product_desc");
                            String product_price = indexedObj.getString("product_price");
                            String company_name = indexedObj.getString("company_name");
                            String company_id = indexedObj.getString("company_id");
                            String company_image_thumb = indexedObj.getString("company_image_thumb");
                            String category_id = indexedObj.getString("category_id");
                            String category_title = indexedObj.getString("category_title");
                            String product_quantity = indexedObj.getString("product_quantity");

                            String company_desc = indexedObj.getString("company_desc");
                            String company_address = indexedObj.getString("company_address");
                            String company_phone = indexedObj.getString("company_phone");
                            String company_image = indexedObj.getString("company_image");
                            String product_images = indexedObj.getString("product_images");

                            JSONArray pImages=indexedObj.getJSONArray("product_images");





                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("product_id", product_id);
                            map.put("product_name", product_name);
                            map.put("product_feature_image", product_feature_image);
                            map.put("company_name", company_name);
                            map.put("company_id", company_id);
                            map.put("company_image_thumb", company_image_thumb);
                            map.put("category_id", category_id);
                            map.put("category_title", category_title);
                            map.put("product_quantity", product_quantity);
                            map.put("product_desc", product_desc);
                            map.put("product_price", product_price);
                            map.put("product_currency", product_currency);
                            map.put("category_title", category_title);
                            map.put("company_desc", company_desc);
                            map.put("company_address", company_address);
                            map.put("company_phone", company_phone);
                            map.put("company_image", company_image);
                            map.put("product_images", pImages.toString());
                            productArrayList.add(map);
                            System.out.println("Response product_name  == " + company_id);


                        }

                        mSwipeRefreshLayout.setRefreshing(false);
                     // if(productArrayList.size() )
                        productAdapter.setData(productArrayList);


                     /*   productListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                            public void onScrollStateChanged(AbsListView view, int scrollState) {
                                 //currentScrollState = scrollState;
                                 flag_loading=false;
                                 //isScrollCompleted();

                            }

                            private void isScrollCompleted() {
                                if (currentVisibleItemCount > 0 && currentScrollState == SCROLL_STATE_IDLE && totalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
                                    *//*** In this way I detect if there's been a scroll which has completed ***//*
                                    *//*** do the work for load more date! ***//*
                                    if (!flag_loading) {
                                        count = 0;

                                        if (flag_loading == false) {
                                            flag_loading = true;

                                            count = count + 15;
                                            new getProductData("").execute();

                                        }
                                    }
                                }
                            }

                            public void onScroll(AbsListView view, int firstVisibleItem,
                                                 int visibleItemCount, int totalItemCount) {

                                currentFirstVisibleItem = firstVisibleItem;
                                currentVisibleItemCount = visibleItemCount;
                                totalItemCount = totalItemCount;

                                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0 )
                                {
                                    count=0;

                                    if(flag_loading == false)
                                    {
                                        flag_loading = true;
                                       *//* additems();*//*
                                        count = count + 15;
                                      new  getProductData("").execute();
                                    //  productAdapter.notifyDataSetChanged();

                                    }
                                }
                            }


                        });*/

                    } else {


                       // productAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        CustomAlertDialog showdialog = new CustomAlertDialog();
                        showdialog.showAlertDialog(HomeActivity.this, R.string.projectName, "No record found.");
                    }

                    //responseStatus = Integer.parseInt(JsonObj.getString("status"));
                    System.out.println("Response check == " + responseStatus);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                CustomAlertDialog showdialog = new CustomAlertDialog();
                showdialog.showAlertDialog(HomeActivity.this, R.string.projectName, "Please check your internet connection and try again.");
            }
            if(pb!=null)
            pb.dismiss();

            System.out.println("Response product_name sze  == " + productResponseDataGlobal.length());
        }

    }



    public static HttpClient getHttpsClient(HttpClient client) {
        try {
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager clientConnectionManager = client.getConnectionManager();
            SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
            return new DefaultHttpClient(clientConnectionManager, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }






   /* public class getSearchProductData extends AsyncTask<String, Process, String> {

        public ProgressDialog pb;
        String response;
        HttpClient httpClient;
        HttpPost httpPost;
        HttpResponse httpresponse;

        @Override
        public void onPreExecute() {

            pb = ProgressDialog.show(HomeActivity.this, null, "Loading....");
        }

        @Override
        protected String doInBackground(String... params) {

            // Creating HTTP client
            httpClient = new DefaultHttpClient();
            // Creating HTTP Post
            httpPost = new HttpPost(getString(R.string.weburl));

            JSONObject requestParameters = new JSONObject();
            try {
                requestParameters.put("search", searchField.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("action", "getSearchProductWithDetail"));
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
                        JSONArray productResponseData = JsonObj.getJSONArray("responseData");
                        JSONArray productResponseDataGlobal = JsonObj.getJSONArray("responseData");

                        for (int i = 0; i < productResponseData.length(); i++) {
                            JSONObject indexedObj = productResponseData.getJSONObject(i);

                            String product_id = indexedObj.getString("product_id");
                            String product_name = indexedObj.getString("product_name");
                            String product_feature_image = indexedObj.getString("feature_image");
                            String company_name = indexedObj.getString("company_name");
                            String company_image_thumb = indexedObj.getString("company_image_thumb");

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("product_id", product_id);
                            map.put("product_name", product_name);
                            map.put("product_feature_image", product_feature_image);
                            map.put("company_name", company_name);
                            map.put("company_image_thumb", company_image_thumb);

                            productArrayList.add(map);
                            //productArrayListGlobal.add(map);
                            System.out.println("Response product_name search == " + product_name);
                        }

                        productAdapter = new HomeProductAdapter(HomeActivity.this, productArrayList);
                        productListView.setAdapter(productAdapter);

                    } else {
                        CustomAlertDialog showdialog = new CustomAlertDialog();
                        showdialog.showAlertDialog(HomeActivity.this, R.string.projectName, "Please check your internet connection and try again.");
                    }

                    //responseStatus = Integer.parseInt(JsonObj.getString("status"));
                    System.out.println("Response check == " + responseStatus);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } else {
                CustomAlertDialog showdialog = new CustomAlertDialog();
                showdialog.showAlertDialog(HomeActivity.this, R.string.projectName, "Please check your internet connection and try again.");
            }

            pb.dismiss();


        }

    }*/

    public void tabAction(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.homeTab:
                //Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                //startActivity(intent);
                break;
            case R.id.categoryTab:
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.cartTab:
                intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
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
                } else {
                    intent = new Intent(HomeActivity.this, ActivityCheckOut.class);
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }
}
