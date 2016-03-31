package com.ds.yam3ah.yam3ah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Chirag on 4/28/2015.
 */
public class ActivityCheckOut extends ActionBarActivity implements View.OnClickListener {

    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    JSONArray cityResponseData,cityResponseDataGlobal;
    public ArrayList<HashMap<String, String>> cityArrayList = new ArrayList<HashMap<String, String>>();
    public ArrayList<String> cityArrList = new ArrayList<String>();
    String areaSelectedTxt;
    TextView pleaseEnter;

    Spinner addressSpinner;
    private Button btn_checkout;
    private EditText edtEmail, edtContactNumber,blockNumber,
            streetNumber,avenue,buildingNumber,floorNumber,apartmentNumber,notes;
    //  private EditText edtFirstname, edtLastName , edtAddressLine2, edtCity,
    //          edtState, edtPostalCode;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        homeTab = (ImageView) findViewById(R.id.homeTab);
        categoryTab = (ImageView) findViewById(R.id.categoryTab);
        cartTab = (ImageView) findViewById(R.id.cartTab);
        checkOutTab = (ImageView) findViewById(R.id.checkOutTab);
        addressSpinner=(Spinner)findViewById(R.id.addressSpinner);
        pleaseEnter=(TextView)findViewById(R.id.pleaseEnterId);
        homeTab.setBackgroundResource(R.drawable.tab_1_unselected);
        categoryTab.setBackgroundResource(R.drawable.tab_2_unseleted);
        cartTab.setBackgroundResource(R.drawable.tab_3_unseleted);
        checkOutTab.setBackgroundResource(R.drawable.tab_4_selected);

        TextView screenNameTop = (TextView) findViewById(R.id.screenNameTop);
        screenNameTop.setText("CHECKOUT");
        screenNameTop.setTypeface(Utils.Optima(ActivityCheckOut.this));
        pleaseEnter.setTypeface(Utils.Optima(ActivityCheckOut.this));
        btn_checkout = (Button) findViewById(R.id.btn_checkout);
        btn_checkout.setTypeface(Utils.Optima(ActivityCheckOut.this));
        btn_checkout.setOnClickListener(this);

//        edtFirstname = (EditText) findViewById(R.id.edtFirstname);
//        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtEmail.setTypeface(Utils.Optima(ActivityCheckOut.this));
        edtContactNumber = (EditText) findViewById(R.id.edtContactNumber);
        edtContactNumber.setTypeface(Utils.Optima(ActivityCheckOut.this));

        blockNumber=(EditText) findViewById(R.id.blockNumberId);
        streetNumber=(EditText) findViewById(R.id.streetNumberId);
        avenue=(EditText) findViewById(R.id.AvenueId);
        buildingNumber=(EditText) findViewById(R.id.buildingnumberId);
        floorNumber=(EditText) findViewById(R.id.floorNumberId);
        apartmentNumber=(EditText) findViewById(R.id.apartmentNumberId);
        notes=(EditText) findViewById(R.id.notesId);

        blockNumber.setTypeface(Utils.Optima(ActivityCheckOut.this));
        streetNumber.setTypeface(Utils.Optima(ActivityCheckOut.this));
        avenue.setTypeface(Utils.Optima(ActivityCheckOut.this));
        buildingNumber.setTypeface(Utils.Optima(ActivityCheckOut.this));
        floorNumber.setTypeface(Utils.Optima(ActivityCheckOut.this));
        apartmentNumber.setTypeface(Utils.Optima(ActivityCheckOut.this));
        notes.setTypeface(Utils.Optima(ActivityCheckOut.this));

      //  edtAddressLine1 = (EditText) findViewById(R.id.edtAddressLine1);
//        edtAddressLine2 = (EditText) findViewById(R.id.edtAddressLine2);
//        edtCity = (EditText) findViewById(R.id.edtCity);
//        edtState = (EditText) findViewById(R.id.edtState);
//        edtPostalCode = (EditText) findViewById(R.id.edtPostalCode);
        new getCityData().execute();

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


    public class getCityData extends AsyncTask<String, Process, String> {

        public ProgressDialog pb;
        String response;
        HttpClient httpClient;
        HttpPost httpPost;
        HttpResponse httpresponse;

        @Override
        public void onPreExecute() {

            pb = ProgressDialog.show(ActivityCheckOut.this, null, "Loading....");
        }

        @Override
        protected String doInBackground(String... params) {

            // Creating HTTP client
            httpClient = HomeActivity.getHttpsClient(new DefaultHttpClient());
            // Creating HTTP Post
            httpPost = new HttpPost(getString(R.string.weburl));

            JSONObject requestParameters = new JSONObject();
           /* try
            {
              //  requestParameters.put("categoryid", category_id);
                //  requestParameters.put("cityid", GlobalData.cityId);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
*/

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
           // nameValuePair.add(new BasicNameValuePair("action", "getCategoryCompany"));
               nameValuePair.add(new BasicNameValuePair("action", "getCityProductList"));
              // nameValuePair.add(new BasicNameValuePair("json", requestParameters.toString()));


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
                        cityResponseData = JsonObj.getJSONArray("responseData");
                        cityResponseDataGlobal = JsonObj.getJSONArray("responseData");

                        System.out.println("companyResponseData == "+cityResponseData);

                        for (int i = 0; i < cityResponseData.length(); i++) {
                            JSONObject indexedObj = cityResponseData.getJSONObject(i);

                            String area_name = indexedObj.getString("area_name");



                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("area_name", area_name);


                            cityArrayList.add(map);
                            cityArrList.add(area_name);

                        }
                        cityArrList.add(0,"Areas");
                       /* companyAdapter = new CompanyAdapter(CompanyActivity.this, companyArrayList);
                        companyListView.setAdapter(companyAdapter);*/
                        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, cityArrList);
                        //cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        addressSpinner.setAdapter(cityAdapter);
                        addressSpinnerDropDown();


                    }
                    else if(responseStatus == 0)
                    {
                        String responseMessage = status.getString("status_message");
                        CustomAlertDialog showdialog = new CustomAlertDialog();
                        showdialog.showAlertDialog(ActivityCheckOut.this, R.string.projectName, responseMessage);
                    }
                    else
                    {
                        CustomAlertDialog showdialog = new CustomAlertDialog();
                        showdialog.showAlertDialog(ActivityCheckOut.this, R.string.projectName, "Please check your internet connection and try again...");
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
                showdialog.showAlertDialog(ActivityCheckOut.this, R.string.projectName, "Please check your internet connection and try again.");
            }

            pb.dismiss();


        }

    }
    private void addressSpinnerDropDown(){
        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            TextView selectedText;

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedText = (TextView) parentView.getChildAt(0);
                //selectedText.setHint("Select SubContractor");

                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                }

                areaSelectedTxt = addressSpinner.getSelectedItem().toString();

                String a = (String) parentView.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

                //selectedText.setHint("Select SubContractor");

            }
        });
    }
    public void tabAction(View view) {
        int id = view.getId();
        Intent intent;

        switch (id) {
            case R.id.homeTab:
                intent = new Intent(ActivityCheckOut.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                intent = new Intent(ActivityCheckOut.this, CategoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cartTab:
                intent = new Intent(ActivityCheckOut.this, CartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.checkOutTab:
                //Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                //startActivity(intent);
                break;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_checkout:
                checkValidations();
                break;
        }
    }

    private void checkValidations() {


        if (!edtContactNumber.getText().toString().isEmpty()) {
            // if (edtAddressLine1.getText().toString().replace(" ", "").length() > 0) {
            if ((areaSelectedTxt.length() > 0) && (!areaSelectedTxt.equalsIgnoreCase("Areas"))) {
                if (edtEmail.getText().toString().isEmpty() || (!edtEmail.getText().toString().isEmpty() && edtEmail.getText().toString().matches(emailPattern))) {
                    // if (edtEmail.getText().toString().matches(emailPattern)) {

                    if(!blockNumber.getText().toString().isEmpty()){

                        if(!streetNumber.getText().toString().isEmpty()){
                            //   if(avenue.getText().toString().replace(" ", "").length() > 0){

                            if(!buildingNumber.getText().toString().isEmpty()){

                                if(!floorNumber.getText().toString().isEmpty()){

                                    if(!apartmentNumber.getText().toString().isEmpty()){

                                        //   if(notes.getText().toString().replace(" ", "").length() > 0){

                                        GlobalData.Email=edtEmail.getText().toString();
                                        GlobalData.contactNumber= edtContactNumber.getText().toString();
                                        //GlobalData.Address= edtAddressLine1.getText().toString();
                                        GlobalData.Area= areaSelectedTxt;
                                        GlobalData.blockNumber=blockNumber.getText().toString();
                                        GlobalData.streetNumber=streetNumber.getText().toString();
                                        GlobalData.avenue=avenue.getText().toString();
                                        GlobalData.buildingNumber=buildingNumber.getText().toString();
                                        GlobalData.floorNumber=floorNumber.getText().toString();
                                        GlobalData.apartmentNumber=apartmentNumber.getText().toString();
                                        GlobalData.notes=notes.getText().toString();
                                        Intent intent = new Intent(ActivityCheckOut.this, ActivityTermandCondition.class);
                                        startActivity(intent);

                                               /* }else{
                                                    notes.setError("Please enter Notes");
                                                    notes.requestFocus();
                                                }*/

                                    }else{
                                        apartmentNumber.setError("Please enter Apartment Number");
                                        apartmentNumber.requestFocus();
                                    }

                                }else{
                                    floorNumber.setError("Please enter Floor Number");
                                    floorNumber.requestFocus();
                                }

                            }else{
                                buildingNumber.setError("Please enter Building Number");
                                buildingNumber.requestFocus();
                            }

                            //  }
                              /*else{
                                    avenue.setError("Please enter Avenue");
                                    avenue.requestFocus();
                                }*/
                        }else{
                            streetNumber.setError("Please enter Street Number");
                            streetNumber.requestFocus();
                        }

                    }else{
                        blockNumber.setError("Please enter Block Number");
                        blockNumber.requestFocus();
                    }


                    // }
                    /*else {
                        edtEmail.setError("Please enter valid email id");
                        edtEmail.requestFocus();
                    }*/
                } else {

                    edtEmail.setError("Please enter valid email id");
                    edtEmail.requestFocus();

                  /* GlobalData.Email= edtEmail.getText().toString();
                    GlobalData.contactNumber= edtContactNumber.getText().toString();
                    //GlobalData.Address= edtAddressLine1.getText().toString();
                    GlobalData.Area= areaSelectedTxt;
                    GlobalData.blockNumber=blockNumber.getText().toString();
                    GlobalData.streetNumber=streetNumber.getText().toString();
                    GlobalData.avenue=avenue.getText().toString();
                    GlobalData.buildingNumber=buildingNumber.getText().toString();
                    GlobalData.floorNumber=floorNumber.getText().toString();
                    GlobalData.apartmentNumber=apartmentNumber.getText().toString();
                    GlobalData.notes=notes.getText().toString();
                    Intent intent = new Intent(ActivityCheckOut.this, ActivityTermandCondition.class);
                    startActivity(intent);*/
                }
            } else {
                /*edtAddressLine1.setError("Please enter address");
                edtAddressLine1.requestFocus();*/

                Toast.makeText(ActivityCheckOut.this,"Please Select Areas",Toast.LENGTH_SHORT).show();
            }
        } else {
            edtContactNumber.setError("Please enter contact number");
            edtContactNumber.requestFocus();
        }
       /* if (edtEmail.getText().toString().replace(" ", "").length() > 0) {
            if (edtEmail.getText().toString().matches(emailPattern)) {
                if (edtContactNumber.getText().toString().replace(" ", "").length() > 0) {
                    if (edtAddressLine1.getText().toString().replace(" ", "").length() > 0) {

                        GlobalData.Email = edtEmail.getText().toString();
                        GlobalData.contactNumber = edtContactNumber.getText().toString();
                        GlobalData.Address = edtAddressLine1.getText().toString();
                        Intent intent = new Intent(ActivityCheckOut.this, ActivityTermandCondition.class);
                        startActivity(intent);
                    } else {

                        edtAddressLine1.setError("Please enter address");
                        edtAddressLine1.requestFocus();
                    }
                } else {

                    edtContactNumber.setError("Please enter contact no");
                    edtContactNumber.requestFocus();
                }
            } else {
                edtEmail.setError("Please enter valid email id");
                edtEmail.requestFocus();
            }
        } else {
            edtEmail.setError("Please enter email");
            edtEmail.requestFocus();
        }*/
    }
}