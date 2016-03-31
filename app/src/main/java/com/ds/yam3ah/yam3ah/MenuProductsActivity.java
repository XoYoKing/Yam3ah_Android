package com.ds.yam3ah.yam3ah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.widget.AdapterView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ds.yam3ah.model.ProductDetail;
import com.ds.yam3ah.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;


public class MenuProductsActivity extends ActionBarActivity {

    ImageButton backBtn;
    ListView menuProductList;
    JSONArray menuProductResponseData;
    static JSONArray menuProductResponseDataGlobal;
    //public ArrayList<HashMap<String, String>> menuProductArrayList = new ArrayList<HashMap<String, String>>();
    public ArrayList<ProductDetail> menuProductArrayList = new ArrayList<ProductDetail>();
    MenuProductAdapter menuProductAdapter;
    FrameLayout cartlogo;
    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    public static TextView cartItemnumber, headerPriceText;
    String category_id,category_title;
    String listViewPosition,tag;
    String company_id , com_cat_title;
    String menuPosition;
    int getItemOfPosition;
    static JSONArray products_Array;
    Spinner costSpinner;
    ArrayList<String> costArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_products);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();
        ImageView toplogo = (ImageView) findViewById(R.id.toplogo);
        //toplogo.setVisibility(View.GONE);
        cartItemnumber = (TextView) findViewById(R.id.caritemNo);
        headerPriceText = (TextView) findViewById(R.id.headerPrice);

        costSpinner=(Spinner) findViewById(R.id.searchfieldMenu);
        costArray=new ArrayList<String>();

        String a1="--All prices--";
        String a2="Under KD 25";
        String a3="KD 25 - KD 30";
        String a4="KD 30 - KD 45";
        String a5="Over KD 45";


        costArray.add(0,a1);
        costArray.add(1,a2);
        costArray.add(2,a3);
        costArray.add(3,a4);
        costArray.add(4,a5);

        double totalPrice = 0;

        for (int i = 0; i < HomeActivity.globalCart.size(); i++) {
            double itemPrice = Double.parseDouble(HomeActivity.globalCart.get(i).getProduct_price()) * HomeActivity.globalCart.get(i).getQuantity();
            totalPrice = totalPrice + itemPrice;
        }
        GlobalData.TotalCostCart=(int)totalPrice;
        if(GlobalData.TotalCostCart>0){
            headerPriceText.setVisibility(View.VISIBLE);
            cartItemnumber.setVisibility(View.VISIBLE);
            cartItemnumber.setText("" + GlobalData.totalItemCount);

            cartItemnumber.setTypeface(Utils.Optima(MenuProductsActivity.this));

            headerPriceText.setText(""+totalPrice);
            headerPriceText.setTypeface(Utils.Optima(MenuProductsActivity.this));
        }
        else {

        }

        ArrayAdapter<String> costAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, costArray);
        //cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /*TextView spinnerItems=(TextView)findViewById(R.id.spinnerTarget);
        spinnerItems.setTypeface(Utils.Optima(MenuProductsActivity.this));*/
        costSpinner.setAdapter(costAdapter);
        cartlogo = (FrameLayout) findViewById(R.id.cartlogo);
        cartlogo.setVisibility(View.VISIBLE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category_id = extras.getString("category_id");
            company_id = extras.getString("company_id");
            listViewPosition = extras.getString("listViewPosition");
            menuPosition = extras.getString("menuPosition");
            com_cat_title = extras.getString("com_cat_title");
            category_title=extras.getString("category_title");
            tag=extras.getString("tag");


        }
    System.out.println("Company id "+company_id);
        getItemOfPosition = Integer.parseInt(menuPosition);

        menuProductList = (ListView) findViewById(R.id.menuProductList);

        backBtn = (ImageButton) findViewById(R.id.backBtn);
        homeTab = (ImageView) findViewById(R.id.homeTab);
        categoryTab = (ImageView) findViewById(R.id.categoryTab);
        cartTab = (ImageView) findViewById(R.id.cartTab);
        checkOutTab = (ImageView) findViewById(R.id.checkOutTab);
        //   Double.parseDouble(HomeActivity.globalCart.get().getProduct_price()) * HomeActivity.globalCart.get(i).getQuantity();
        if(tag.equalsIgnoreCase("ProductDetailActivity")){
            homeTab.setBackgroundResource(R.drawable.tab_1_selected);
            categoryTab.setBackgroundResource(R.drawable.tab_2_unseleted);
            cartTab.setBackgroundResource(R.drawable.tab_3_unseleted);
            checkOutTab.setBackgroundResource(R.drawable.tab_4_unselected);
        }else{
            homeTab.setBackgroundResource(R.drawable.tab_1_unselected);
            categoryTab.setBackgroundResource(R.drawable.tab_2_selected);
            cartTab.setBackgroundResource(R.drawable.tab_3_unseleted);
            checkOutTab.setBackgroundResource(R.drawable.tab_4_unselected);
        }


        TextView screenNameTop = (TextView) findViewById(R.id.screenNameTop);
        screenNameTop.setText(com_cat_title);
        screenNameTop.setTypeface(Utils.Optima(MenuProductsActivity.this));

        backBtn.setVisibility(View.VISIBLE);

        checkNetwork();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void checkNetwork() {
        // TODO Auto-generated method stub
        Network cd;
        boolean isInternetPresent = false;

        cd = new Network(this.getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            new getMenuProductData().execute();
            //  Toast.makeText(this, "internet connection is working", Toast.LENGTH_LONG).show();
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

    public void backMethod(View view) {
        Intent intent = new Intent(MenuProductsActivity.this, MenuActivity.class);
        intent.putExtra("category_id", category_id);
        intent.putExtra("company_id", company_id);
        intent.putExtra("category_title", category_title);
        intent.putExtra("listViewPosition", listViewPosition);
        intent.putExtra("tag", tag);
        startActivity(intent);
        finish();
    }


    public class getMenuProductData extends AsyncTask<String, Process, String> {

        public ProgressDialog pb;
        String response;

        @Override
        public void onPreExecute() {

            pb = ProgressDialog.show(MenuProductsActivity.this, null, "Loading....");
        }

        @Override
        protected String doInBackground(String... params) {

            // Creating HTTP client

            return null;
        }


        public void onPostExecute(String result) {
            super.onPostExecute(result);

            // Making HTTP Request
            //try {

            JSONObject indexedObj = null;
            try {

                //  Toast.makeText(MenuProductsActivity.this, "getItemOfPosition == "+getItemOfPosition, Toast.LENGTH_LONG).show();

                indexedObj = MenuActivity.menuResponseDataGlobal.getJSONObject(getItemOfPosition);

                 products_Array = indexedObj.getJSONArray("products");

                //  Toast.makeText(MenuProductsActivity.this, "products_Array length == "+products_Array.length(), Toast.LENGTH_LONG).show();

                JSONObject productIndexedObj = null;

                if (products_Array.length() > 0) {
                    for (int j = 0; j < products_Array.length(); j++) {
                        productIndexedObj = products_Array.getJSONObject(j);
                        String product_company_id = productIndexedObj.getString("company_id");
                        String product_id = productIndexedObj.getString("product_id");
                        String product_name = productIndexedObj.getString("product_name");
                        String product_desc = productIndexedObj.getString("product_desc");
                        String product_price = productIndexedObj.getString("product_price");
                        String product_currency = productIndexedObj.getString("product_currency");
                        String product_feature_image = productIndexedObj.getString("product_feature_image");
                        String product_feature_thumb = productIndexedObj.getString("product_feature_thumb");
                        String product_quantity = productIndexedObj.getString("product_quantity");
                        String com_cat_id = indexedObj.getString("com_cat_id");
                        String com_cat_title = indexedObj.getString("com_cat_title");

                        ProductDetail mProductDetail=new ProductDetail();
                        mProductDetail.setProduct_company_id(product_company_id);
                        mProductDetail.setProduct_id(product_id);
                        mProductDetail.setProduct_name(product_name);
                        mProductDetail.setProduct_desc(product_desc);
                        mProductDetail.setProduct_price(product_price);
                        mProductDetail.setProduct_currency(product_currency);
                        mProductDetail.setProduct_feature_image(product_feature_image);
                        mProductDetail.setProduct_feature_thumb(product_feature_thumb);
                        mProductDetail.setProduct_quantity(product_quantity);
                        mProductDetail.setCom_cat_id(com_cat_id);
                        mProductDetail.setCom_cat_title(com_cat_title);



                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put("product_company_id", product_company_id);
                        map.put("product_id", product_id);
                        map.put("product_name", product_name);
                        map.put("product_desc", product_desc);
                        map.put("product_price", product_price);
                        map.put("product_currency", product_currency);
                        map.put("product_feature_image", product_feature_image);
                        map.put("product_feature_thumb", product_feature_thumb);
                        map.put("com_cat_id", com_cat_id);
                        map.put("product_quantity", product_quantity);


                      //  menuProductArrayList.add(map);

                        menuProductArrayList.add(mProductDetail);
                        System.out.println("Response product_name  == " + product_company_id);
                    }

                    menuProductAdapter = new MenuProductAdapter(MenuProductsActivity.this, menuProductArrayList,tag);
                    menuProductList.setAdapter(menuProductAdapter);

                    costSpinnerDropDown();
                } else {
                    //  CustomAlertDialog showdialog = new CustomAlertDialog();
                    // showdialog.
                    //  showdialog.showAlertDialog(MenuProductsActivity.this, R.string.projectName, "There is no product found in this category");
                    // finish();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuProductsActivity.this);
                    builder.setTitle( R.string.projectName);

                    builder.setMessage("There is no product found in this category")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent intent = new Intent(MenuProductsActivity.this, MenuActivity.class);
                                    intent.putExtra("category_id", category_id);
                                    intent.putExtra("company_id", company_id);
                                    intent.putExtra("category_title", category_title);
                                    intent.putExtra("listViewPosition", listViewPosition);
                                    intent.putExtra("tag", tag);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            pb.dismiss();


        }

    }
    private void costSpinnerDropDown(){
        costSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            TextView selectedText;

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedText = (TextView) parentView.getChildAt(0);
                //selectedText.setHint("Select SubContractor");

                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                    selectedText.setTypeface(Utils.Optima(MenuProductsActivity.this));
                }

               String costSelectedTxt = costSpinner.getSelectedItem().toString();

               String a = (String) parentView.getItemAtPosition(position);
               // ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
                ArrayList<ProductDetail> mylist1 = new ArrayList<ProductDetail>();
                int myIntprice=0;
                for (int i = 0; i < menuProductArrayList.size(); i++) {

                        String product_company_id = menuProductArrayList.get(i).getProduct_company_id();
                        String product_id =  menuProductArrayList.get(i).getProduct_id();
                        String product_name =  menuProductArrayList.get(i).getProduct_name();
                        String product_desc =  menuProductArrayList.get(i).getProduct_desc();
                        String product_price =  menuProductArrayList.get(i).getProduct_price();
                        String product_currency =  menuProductArrayList.get(i).getProduct_currency();
                        String product_feature_image =  menuProductArrayList.get(i).getProduct_feature_image();
                        String product_feature_thumb =  menuProductArrayList.get(i).getProduct_feature_thumb();
                        String com_cat_id =  menuProductArrayList.get(i).getCom_cat_id();
                        String com_cat_title=menuProductArrayList.get(i).getCom_cat_title();

                    String decimalPattern = "([0-9]*)\\.([0-9]*)";

                    boolean match = Pattern.matches(decimalPattern, product_price);
                    if(match){
                        double value = Double.parseDouble(product_price);
                         myIntprice = (int) value;
                    }else{
                         myIntprice = Integer.parseInt(product_price);
                    }

                    if(position==0) {
                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put("product_company_id", product_company_id);
                        map.put("product_id", product_id);
                        map.put("product_name", product_name);
                        map.put("product_desc", product_desc);
                        map.put("product_price", product_price);
                        map.put("product_currency", product_currency);
                        map.put("product_feature_image", product_feature_image);
                        map.put("product_feature_thumb", product_feature_thumb);
                        map.put("com_cat_id", com_cat_id);

                      ProductDetail obj=new ProductDetail();
                        obj.setProduct_company_id(product_company_id);
                        obj.setProduct_id(product_id);
                        obj.setProduct_name(product_name);
                        obj.setProduct_desc(product_desc);
                        obj.setProduct_price(product_price);
                        obj.setProduct_currency(product_currency);
                        obj.setProduct_feature_image(product_feature_image);
                        obj.setProduct_feature_thumb(product_feature_thumb);
                        obj.setCom_cat_id(com_cat_id);
                        obj.setCom_cat_title(com_cat_title);
                       mylist1.add(obj);

                    }else if(position==1) {
                        try {
                            if (myIntprice < 25) {

                                HashMap<String, String> map25 = new HashMap<String, String>();

                                map25.put("product_company_id", product_company_id);
                                map25.put("product_id", product_id);
                                map25.put("product_name", product_name);
                                map25.put("product_desc", product_desc);
                                map25.put("product_price", product_price);
                                map25.put("product_currency", product_currency);
                                map25.put("product_feature_image", product_feature_image);
                                map25.put("product_feature_thumb", product_feature_thumb);
                                map25.put("com_cat_id", com_cat_id);

                                ProductDetail obj=new ProductDetail();
                                obj.setProduct_company_id(product_company_id);
                                obj.setProduct_id(product_id);
                                obj.setProduct_name(product_name);
                                obj.setProduct_desc(product_desc);
                                obj.setProduct_price(product_price);
                                obj.setProduct_currency(product_currency);
                                obj.setProduct_feature_image(product_feature_image);
                                obj.setProduct_feature_thumb(product_feature_thumb);
                                obj.setCom_cat_id(com_cat_id);
                                obj.setCom_cat_title(com_cat_title);
                                mylist1.add(obj);
                                //mylist1.add(map25);
                                //  break;
                            }
                        }catch (Exception e){
                            e.getMessage();
                        }

                    }else if(position==2) {
                        try {
                            if (myIntprice >= 25 && myIntprice < 30) {

                                HashMap<String, String> map30 = new HashMap<String, String>();

                                map30.put("product_company_id", product_company_id);
                                map30.put("product_id", product_id);
                                map30.put("product_name", product_name);
                                map30.put("product_desc", product_desc);
                                map30.put("product_price", product_price);
                                map30.put("product_currency", product_currency);
                                map30.put("product_feature_image", product_feature_image);
                                map30.put("product_feature_thumb", product_feature_thumb);
                                map30.put("com_cat_id", com_cat_id);

                                ProductDetail obj=new ProductDetail();
                                obj.setProduct_company_id(product_company_id);
                                obj.setProduct_id(product_id);
                                obj.setProduct_name(product_name);
                                obj.setProduct_desc(product_desc);
                                obj.setProduct_price(product_price);
                                obj.setProduct_currency(product_currency);
                                obj.setProduct_feature_image(product_feature_image);
                                obj.setProduct_feature_thumb(product_feature_thumb);
                                obj.setCom_cat_id(com_cat_id);
                                obj.setCom_cat_title(com_cat_title);
                                mylist1.add(obj);
                               // mylist1.add(map30);
                                //  break;
                            }
                        }catch(Exception e){
                            e.getMessage();
                        }
                    }else if(position==3) {
                        try {
                            if (myIntprice >= 30 && myIntprice < 45) {

                                HashMap<String, String> map45 = new HashMap<String, String>();

                                map45.put("product_company_id", product_company_id);
                                map45.put("product_id", product_id);
                                map45.put("product_name", product_name);
                                map45.put("product_desc", product_desc);
                                map45.put("product_price", product_price);
                                map45.put("product_currency", product_currency);
                                map45.put("product_feature_image", product_feature_image);
                                map45.put("product_feature_thumb", product_feature_thumb);
                                map45.put("com_cat_id", com_cat_id);

                                ProductDetail obj=new ProductDetail();
                                obj.setProduct_company_id(product_company_id);
                                obj.setProduct_id(product_id);
                                obj.setProduct_name(product_name);
                                obj.setProduct_desc(product_desc);
                                obj.setProduct_price(product_price);
                                obj.setProduct_currency(product_currency);
                                obj.setProduct_feature_image(product_feature_image);
                                obj.setProduct_feature_thumb(product_feature_thumb);
                                obj.setCom_cat_id(com_cat_id);
                                obj.setCom_cat_title(com_cat_title);

                                mylist1.add(obj);
                              //  mylist1.add(map45);
                                //  break;
                            }
                        }catch(Exception e){
                            e.getMessage();
                        }
                    }else if(position==4) {
                        try {
                            if (myIntprice >= 45) {

                                HashMap<String, String> map50 = new HashMap<String, String>();

                                map50.put("product_company_id", product_company_id);
                                map50.put("product_id", product_id);
                                map50.put("product_name", product_name);
                                map50.put("product_desc", product_desc);
                                map50.put("product_price", product_price);
                                map50.put("product_currency", product_currency);
                                map50.put("product_feature_image", product_feature_image);
                                map50.put("product_feature_thumb", product_feature_thumb);
                                map50.put("com_cat_id", com_cat_id);

                                ProductDetail obj=new ProductDetail();
                                obj.setProduct_company_id(product_company_id);
                                obj.setProduct_id(product_id);
                                obj.setProduct_name(product_name);
                                obj.setProduct_desc(product_desc);
                                obj.setProduct_price(product_price);
                                obj.setProduct_currency(product_currency);
                                obj.setProduct_feature_image(product_feature_image);
                                obj.setProduct_feature_thumb(product_feature_thumb);
                                obj.setCom_cat_id(com_cat_id);
                                obj.setCom_cat_title(com_cat_title);

                                mylist1.add(obj);
                               // mylist1.add(map50);
                                //  break;
                            }
                        }catch(Exception e){
                            e.getMessage();
                        }
                    }
                }

                if(mylist1.isEmpty())
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuProductsActivity.this);

                    alertDialog.setMessage("No Match Found");

                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User pressed Cancel button. Write Logic Here
                            dialog.cancel();
                            menuProductAdapter = new MenuProductAdapter(MenuProductsActivity.this, menuProductArrayList,tag);
                            menuProductList.setAdapter(menuProductAdapter);
                            /*defectAdapter=new BaseDefectAdapter(Defects.this, defectArrayList);
                            defectlistview.setAdapter(defectAdapter);*/
                        }
                    });

                    alertDialog.show();
                }else {
                    menuProductAdapter = new MenuProductAdapter(MenuProductsActivity.this, mylist1,tag);
                    menuProductList.setAdapter(menuProductAdapter);
                    //selectedSubContractorId=subContrArr.get(position).getSubContractorId();
                }

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
                intent = new Intent(MenuProductsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                intent = new Intent(MenuProductsActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cartTab:
                intent = new Intent(MenuProductsActivity.this, CartActivity.class);
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
                } else {
                    intent = new Intent(this.getApplicationContext(), ActivityCheckOut.class);
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }
}
