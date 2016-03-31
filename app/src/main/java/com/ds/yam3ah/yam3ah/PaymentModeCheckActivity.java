package com.ds.yam3ah.yam3ah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.yam3ah.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 5/8/2015.
 */
public class PaymentModeCheckActivity extends ActionBarActivity {
    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    RelativeLayout COD,KNEt;
    TextView cashTxt,knetTxt;
    private static ProgressDialog pb;
    String tag;


    public JSONObject objDataObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_paymentmode);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        homeTab = (ImageView) findViewById(R.id.homeTab);
        categoryTab = (ImageView) findViewById(R.id.categoryTab);
        cartTab = (ImageView) findViewById(R.id.cartTab);
        checkOutTab = (ImageView) findViewById(R.id.checkOutTab);

        homeTab.setBackgroundResource(R.drawable.tab_1_unselected);
        categoryTab.setBackgroundResource(R.drawable.tab_2_unseleted);
        cartTab.setBackgroundResource(R.drawable.tab_3_unseleted);
        checkOutTab.setBackgroundResource(R.drawable.tab_4_selected);

        TextView screenNameTop = (TextView) findViewById(R.id.screenNameTop);
        screenNameTop.setText("Payment Method");
        screenNameTop.setTypeface(Utils.Optima(PaymentModeCheckActivity.this));
        COD=(RelativeLayout)findViewById(R.id.btncod);
        KNEt=(RelativeLayout)findViewById(R.id.btnknet);
        cashTxt=(TextView)findViewById(R.id.cashText);
        knetTxt=(TextView)findViewById(R.id.knetText);

        cashTxt.setTypeface(Utils.Optima(PaymentModeCheckActivity.this));
        knetTxt.setTypeface(Utils.Optima(PaymentModeCheckActivity.this));

         KNEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag="1";
                pb = ProgressDialog.show(PaymentModeCheckActivity.this, null, "Loading....");
                new ProductAvailabilityService().execute(tag);

            }
        });
        COD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag="0";
                pb = ProgressDialog.show(PaymentModeCheckActivity.this, null, "Loading....");
                new ProductAvailabilityService().execute(tag);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
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

    public void tabAction(View view) {
        int id = view.getId();
        Intent intent;

        switch (id) {
            case R.id.homeTab:
                intent = new Intent(PaymentModeCheckActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                intent = new Intent(PaymentModeCheckActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cartTab:
                intent = new Intent(PaymentModeCheckActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.checkOutTab:
                //Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                //startActivity(intent);
                break;

        }
    }
    private class CallBackgroundService extends AsyncTask<String, String, String> {

        String response;
        HttpClient httpClient;
        HttpPost httpPost;
        HttpResponse httpresponse;

        @Override
        protected String doInBackground(String... strings) {
            JSONObject objData = new JSONObject();
            try {
                JSONObject objDataObject = new JSONObject();



                objDataObject.put("contact_number", GlobalData.contactNumber);
                objDataObject.put("address_line1", GlobalData.Area);
                objDataObject.put("email_id", GlobalData.Email);
                objDataObject.put("total_order_cost", GlobalData.TotalCostCart);
                objDataObject.put("paymenttype", "COD");

                objDataObject.put("block_number", GlobalData.blockNumber);
                objDataObject.put("street_number", GlobalData.streetNumber);
                objDataObject.put("avenue", GlobalData.avenue);
                objDataObject.put("house_building_number", GlobalData.buildingNumber);
                objDataObject.put("floor_number", GlobalData.floorNumber);
                objDataObject.put("apartment_number", GlobalData.apartmentNumber);
                objDataObject.put("note", GlobalData.notes);



                JSONArray arrMenus = new JSONArray();

                for (int i = 0; i < HomeActivity.globalCart.size(); i++) {
                    JSONObject objMenuData = new JSONObject();
                    objMenuData.put("product_qty", HomeActivity.globalCart.get(i).getQuantity());
                    objMenuData.put("product_id", HomeActivity.globalCart.get(i).getProduct_id());
                    objMenuData.put("product_price", HomeActivity.globalCart.get(i).getProduct_price() + " " + HomeActivity.globalCart.get(i).getProduct_currency());
                    objMenuData.put("company_id", HomeActivity.globalCart.get(i).getCompany_id());
                    objMenuData.put("special_req", HomeActivity.globalCart.get(i).getProduct_speclRequest());
                    arrMenus.put(objMenuData);
                }

                objDataObject.put("menus", arrMenus);

                objData.put("order", objDataObject);

                Log.e("checout json", "checkout json" + objData.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            httpClient = HomeActivity.getHttpsClient(new DefaultHttpClient());
            // Creating HTTP Post
            httpPost = new HttpPost(getString(R.string.weburl));

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);

            nameValuePair.add(new BasicNameValuePair("action", "saveProductOrder"));
            nameValuePair.add(new BasicNameValuePair("json", objData.toString()));

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
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("nameValuePair", response.toString());
            try {
                JSONObject objResponse = new JSONObject(s);
                JSONObject status = objResponse.getJSONObject("status");
                JSONObject responseData = objResponse.getJSONObject("responseData");
                int responseStatus = Integer.parseInt(status.getString("status"));
                int responseData1 = Integer.parseInt(responseData.getString("order_id"));

                PaymentActivity.orderid = responseData1;
                System.out.println("ashish1 " + PaymentActivity.orderid );
                if (responseStatus == 1) {
                    Intent intent = new Intent(PaymentModeCheckActivity.this, ActivityThankYou.class);
                    startActivity(intent);
                    finish();
                } else {
                    CustomAlertDialog showdialog = new CustomAlertDialog();
                    showdialog.showAlertDialog(PaymentModeCheckActivity.this, R.string.projectName, "Sending fail");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pb.dismiss();
        }
    }


    private class ProductAvailabilityService extends AsyncTask<String, String, String> {

        String response;
        HttpClient httpClient;
        HttpPost httpPost;
        HttpResponse httpresponse;
        String tag;

        @Override
        protected String doInBackground(String... tagg) {
            tag=tagg[0];
            JSONObject objData = new JSONObject();
            try {
                JSONObject objDataObject = new JSONObject();



                objDataObject.put("contact_number", GlobalData.contactNumber);
                objDataObject.put("address_line1", GlobalData.Area);
                objDataObject.put("email_id", GlobalData.Email);
                objDataObject.put("total_order_cost", GlobalData.TotalCostCart);
                objDataObject.put("paymenttype", "COD");

                objDataObject.put("block_number", GlobalData.blockNumber);
                objDataObject.put("street_number", GlobalData.streetNumber);
                objDataObject.put("avenue", GlobalData.avenue);
                objDataObject.put("house_building_number", GlobalData.buildingNumber);
                objDataObject.put("floor_number", GlobalData.floorNumber);
                objDataObject.put("apartment_number", GlobalData.apartmentNumber);
                objDataObject.put("note", GlobalData.notes);



               JSONArray arrMenus = new JSONArray();

                for (int i = 0; i < HomeActivity.globalCart.size(); i++) {
                    JSONObject objMenuData = new JSONObject();
                    objMenuData.put("product_qty", HomeActivity.globalCart.get(i).getQuantity());
                    objMenuData.put("product_id", HomeActivity.globalCart.get(i).getProduct_id());
                    objMenuData.put("product_price", HomeActivity.globalCart.get(i).getProduct_price() + " " + HomeActivity.globalCart.get(i).getProduct_currency());
                    objMenuData.put("company_id", HomeActivity.globalCart.get(i).getCompany_id());
                    objMenuData.put("special_req", HomeActivity.globalCart.get(i).getProduct_speclRequest());
                    arrMenus.put(objMenuData);
                }

                objDataObject.put("menus", arrMenus);

                objData.put("order", objDataObject);

                Log.e("checout json", "checkout json" + objData.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            httpClient = HomeActivity.getHttpsClient(new DefaultHttpClient());
            // Creating HTTP Post
            httpPost = new HttpPost(getString(R.string.weburl));

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);

            nameValuePair.add(new BasicNameValuePair("action", "checkOrderProductAvailablity"));
            nameValuePair.add(new BasicNameValuePair("json", objData.toString()));

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
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("nameValuePair", response.toString());
            try {
                JSONObject objResponse = new JSONObject(s);
                JSONObject status = objResponse.getJSONObject("status");
                //JSONObject status_message= objResponse.getJSONObject("status_message");

               // JSONObject responseData = objResponse.getJSONObject("responseData");
                int responseStatus = Integer.parseInt(status.getString("status"));
               // int responseData1 = Integer.parseInt(responseData.getString("order_id"));

               // PaymentActivity.orderid = responseData1;
               // System.out.println("ashish1 " + PaymentActivity.orderid );
                if (responseStatus == 1) {

                    if(tag.equalsIgnoreCase("0")) {
                        new CallBackgroundService().execute();
                    }else{
                        Intent intent=new Intent(PaymentModeCheckActivity.this,PaymentActivity.class);
                        startActivity(intent);
                    }


                } else {
                    CustomAlertDialog showdialog = new CustomAlertDialog();
                    showdialog.showAlertDialog(PaymentModeCheckActivity.this, R.string.projectName, status.getString("status_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pb.dismiss();
        }
    }
}
