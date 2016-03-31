package com.ds.yam3ah.yam3ah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chirag on 4/29/2015.
 */
public class ActivityTermandCondition extends ActionBarActivity {

    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    CheckBox agreecheck;
    TextView iAgree;
    Button btncontinue, btncancel;
    private static ProgressDialog progressDialog;

    TextView termConditionText;


    WebView TermandCond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termcondition);


        TermandCond = (WebView) this.findViewById(R.id.webViewCondition);
        TermandCond.getSettings().setJavaScriptEnabled(true);

        TermandCond.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        TermandCond.setBackgroundColor(82192180);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        homeTab = (ImageView) findViewById(R.id.homeTab);
        categoryTab = (ImageView) findViewById(R.id.categoryTab);
        cartTab = (ImageView) findViewById(R.id.cartTab);
        checkOutTab = (ImageView) findViewById(R.id.checkOutTab);
        iAgree=(TextView)findViewById(R.id.iAgreeId);
        iAgree.setTypeface(Utils.Optima(ActivityTermandCondition.this));

        progressDialogView();
        new CallBackgroundTandC().execute();

        homeTab.setBackgroundResource(R.drawable.tab_1_unselected);
        categoryTab.setBackgroundResource(R.drawable.tab_2_unseleted);
        cartTab.setBackgroundResource(R.drawable.tab_3_unseleted);
        checkOutTab.setBackgroundResource(R.drawable.tab_4_selected);
        agreecheck = (CheckBox) findViewById(R.id.checkboxagree);
        btncontinue = (Button) findViewById(R.id.btn_continue);
        TextView screenNameTop = (TextView) findViewById(R.id.screenNameTop);
        screenNameTop.setText("Term & conditions");
        screenNameTop.setTypeface(Utils.Optima(ActivityTermandCondition.this));
        FrameLayout cartlogo = (FrameLayout) findViewById(R.id.cartlogo);
        cartlogo.setVisibility(View.GONE);
        ImageView applogo = (ImageView) findViewById(R.id.toplogo);
        applogo.setVisibility(View.VISIBLE);
        btncontinue.getBackground().setAlpha(128);
        btncontinue.setEnabled(false);
        btncancel = (Button) findViewById(R.id.backBtnCancel);
        btncancel.setTypeface(Utils.Optima(ActivityTermandCondition.this));
        btncontinue.setTypeface(Utils.Optima(ActivityTermandCondition.this));
        btncancel.setVisibility(View.VISIBLE);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        agreecheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                   /* Toast.makeText(ActivityTermandCondition.this,
                            "Bro, try Android :)", Toast.LENGTH_LONG).show();*/
                    btncontinue.getBackground().setAlpha(255);
                    btncontinue.setEnabled(true);
                } else {
                    btncontinue.getBackground().setAlpha(128);
                    btncontinue.setEnabled(false);

                }

            }
        });
        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* progressDialog.show();
                new CallBackgroundService().execute();*/
                // Toast.makeText(ActivityTermandCondition.this, "clicked  ghg ghgf h ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ActivityTermandCondition.this, PaymentModeCheckActivity.class);
                /*intent.putExtra("email", edtEmail.getText().toString());
                intent.putExtra("contactNumber", edtContactNumber.getText().toString());
                intent.putExtra("addressLine1", edtAddressLine1.getText().toString());*/

                startActivity(intent);
                finish();
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
                intent = new Intent(ActivityTermandCondition.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                intent = new Intent(ActivityTermandCondition.this, CategoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cartTab:
                intent = new Intent(ActivityTermandCondition.this, CartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.checkOutTab:
                //Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                //startActivity(intent);
                break;

        }
    }



    private void progressDialogView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait ...");
        progressDialog.setCancelable(false);
    }



    // Webservice  for term and conditon

                public class CallBackgroundTandC extends AsyncTask<String, String, String> {

                String response;
                HttpClient httpClient;
                HttpPost httpPost;
                HttpResponse httpresponse;
                    public ProgressDialog pb;
                    @Override
                    public void onPreExecute() {

                        pb = ProgressDialog.show(ActivityTermandCondition.this, null, "Loading....");
                    }
                @Override
                protected String doInBackground(String... strings) {
                    JSONObject objData = new JSONObject();


                    httpClient = HomeActivity.getHttpsClient(new DefaultHttpClient());
                    // Creating HTTP Post
                    httpPost = new HttpPost(getString(R.string.weburl));

                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                    nameValuePair.add(new BasicNameValuePair("action", "gettermcondition"));
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
                JSONObject responseData = objResponse.getJSONObject("responseData");
                String description = responseData.getString("description");
                 TermandCond.loadDataWithBaseURL("", "<font color='#ffffff'>"+String.valueOf(Html.fromHtml(description.toString().replaceAll("&nbsp;", " ").replaceAll( "&amp;quot;","'\'")))+"</font>", "text/html", "UTF-8", "");


            } catch (JSONException e) {
                e.printStackTrace();
            }
           // progressDialog.dismiss();
            pb.dismiss();
        }
    }

}

