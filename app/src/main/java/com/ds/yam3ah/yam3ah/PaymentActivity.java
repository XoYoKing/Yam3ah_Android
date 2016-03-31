package com.ds.yam3ah.yam3ah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 5/8/2015.
 */
public class PaymentActivity extends ActionBarActivity {
    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    WebView WebPay;
    private Bundle bundle;
    ProgressDialog pb;
    private String firstName, lastName, email, contactNumber, addressLine1, addressLine2, city, state, postalCode,payment_id,payment_transaction_id,refno,is_payment_success;
    public static Integer orderid;
  //  public  List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
   public JSONObject objDataObject = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentweb);

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
        screenNameTop.setText("Payment");
        screenNameTop.setTypeface(Utils.Optima(PaymentActivity.this));

        WebPay = (WebView) this.findViewById(R.id.webViewPayment);
        WebPay.getSettings().setJavaScriptEnabled(true);
        WebPay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
      //  WebPay.loadUrl("http://ds211.projectstatus.co.uk/alyam3ah/knet_pay/buy.php?amount="+ GlobalData.TotalCostCart);
        WebPay.loadUrl("https://www.yam3ah.com/knet_pay/buy.php?amount=" + GlobalData.TotalCostCart);
        WebPay.setWebViewClient(new myWebViewClient());

        pb = ProgressDialog.show(PaymentActivity.this, null, "Loading....");

    }

    private class myWebViewClient extends WebViewClient {

      //  List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            pb.show();
            view.loadUrl(url);

            if(url.contains("result.php")){
                List<NameValuePair> params = null;
                try {
                    params = URLEncodedUtils.parse(new URI(url), "UTF-8");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

               for (NameValuePair param : params) {

                    System.out.println(param.getName() + " : " + param.getValue());

                    if(param.getName().equals("PaymentID")) {
                        payment_id=param.getValue();
                    }
                    if(param.getName().equals("TranID")){
                        payment_transaction_id=param.getValue();
                    }
                     if (param.getName().equals("Ref")){
                        refno=param.getValue();
                     }
                   if(param.getName().equals("Result")) {
                      
                       String s =String.valueOf(param.getValue());
                       if (s.equals("CAPTURED")){
                           System.out.println("ss "+s);
                           is_payment_success="1";



                           Handler handler = new Handler();
                           handler.postDelayed(new Runnable(){
                               @Override
                               public void run(){
                                   new CallBackgroundService(pb).execute();
                               }
                           }, 10000);
                       }
                       else{
                           Toast.makeText(PaymentActivity.this,"Invalid card entries ",Toast.LENGTH_LONG).show();
                       }
                    }
               }
                System.out.println("ashish1  " + view.getUrl());

                String changeUrl = "";
                String[] fields = url.split("\\?");

                for (String field : fields) {
                    changeUrl = field;
                }


                // = url.replace("https://www.knetpaytest.com.kw/php/result.php?","https://www.yam3ah.com/knet_pay/response_handle.php?");


             //   view.loadUrl("  http://ds211.projectstatus.co.uk/alyam3ah/knet_pay/response_handle.php?"+changeUrl);
                view.loadUrl("https://www.yam3ah.com/knet_pay/response_handle.php?"+changeUrl);
                        //  view.loadUrl("https://www.yam3ah.com/knet_pay/response_handle.php?" + view.getUrl());
                        //  System.out.println("url loded  " + view.getUrl());
                System.out.println("rewrw" + "https://www.yam3ah.com/knet_pay/response_handle.php?"+changeUrl);
            }
            else if(url.contains("error.php")){


                String changeUrl = "";
                String[] fields = url.split("\\?");

                for (String field : fields) {
                    changeUrl = field;
                }

                //String changeUrl = url.replace("https://www.knetpaytest.com.kw/php/error.php?","https://www.yam3ah.com/knet_pay/response_handle.php?");

               /* List<NameValuePair> params = null;
                try {
                    params = URLEncodedUtils.parse(new URI(url), "UTF-8");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                for (NameValuePair param : params) {

                    System.out.println(param.getName() + " : " + param.getValue());

                    if (param.getName().equals("PaymentID")) {
                        payment_id = param.getValue();
                    }
                    if (param.getName().equals("TranID")) {
                        payment_transaction_id = param.getValue();
                    }
                    if (param.getName().equals("Ref")) {
                        refno = param.getValue();
                    }
                }*/
             //   view.loadUrl("  http://ds211.projectstatus.co.uk/alyam3ah/knet_pay/response_handle.php?"+changeUrl);
               view.loadUrl("https://www.yam3ah.com/knet_pay/response_handle.php?"+changeUrl);//"http://ds211.projectstatus.co.uk/alyam3ah/knet_pay/response_handle.php?" + view.getUrl());
               System.out.println("rewrw" + "https://www.yam3ah.com/knet_pay/response_handle.php?"+changeUrl);
            }
             else{

                /*String changeUrl = "";
                String[] fields = url.split("\\?");

                for (String field : fields) {
                    changeUrl = field;
                }
                view.loadUrl("https://www.yam3ah.com/knet_pay/response_handle.php?"+changeUrl);//"http://ds211.projectstatus.co.uk/alyam3ah/knet_pay/response_handle.php?" + view.getUrl());
                System.out.println("rewrw" + "https://www.yam3ah.com/knet_pay/response_handle.php?"+changeUrl);*/

               view.loadUrl(url);
            }
            String webUrl = view.getUrl();
            System.out.println("web url   " + webUrl);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            Thread splashThread = new Thread() {
                @Override
                public void run() {
                    int count = 2000;
                    for (int i = 0; i < count; i++) {
                        try {
                            Thread.sleep(3);
                        } catch (InterruptedException ignore) {
                        }
                    }
                    pb.dismiss();
                }
            };
            splashThread.start();
        }
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
                intent = new Intent(PaymentActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                intent = new Intent(PaymentActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cartTab:
                intent = new Intent(PaymentActivity.this, CartActivity.class);
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
        ProgressDialog pb;

        public CallBackgroundService(ProgressDialog pb) {
            this.pb = pb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.show();
        }

        @Override
        protected String doInBackground(String... strings) {
           JSONObject objData = new JSONObject();
            try {
               JSONObject objDataObject = new JSONObject();

                objDataObject.put("contact_number", GlobalData.contactNumber);
//                objDataObject.put("city", city);
//                objDataObject.put("first_name", firstName);
                objDataObject.put("address_line1", GlobalData.Area);
                //               objDataObject.put("last_name", lastName);
                objDataObject.put("email_id", GlobalData.Email);
                objDataObject.put("total_order_cost", GlobalData.TotalCostCart);
                objDataObject.put("paymenttype", "KNET");
                objDataObject.put("payment_id", payment_id);
                objDataObject.put("reference_id", refno);
                objDataObject.put("payment_transaction_id", payment_transaction_id);
                objDataObject.put("is_payment_success", is_payment_success);

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

            Log.e("request parameters", nameValuePair.toString());

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

                orderid = responseData1;
                // int orderid = Integer.parseInt(responseData.getString("order_id"));
                System.out.println("ashish " + orderid);
                if (responseStatus == 1) {
                    Intent intent = new Intent(PaymentActivity.this, ActivityThankYou.class);
                    startActivity(intent);
                    finish();
                } else {
                    CustomAlertDialog showdialog = new CustomAlertDialog();
                    showdialog.showAlertDialog(PaymentActivity.this, R.string.projectName, "Sending fail");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
          pb.dismiss();
        }
    }
}
