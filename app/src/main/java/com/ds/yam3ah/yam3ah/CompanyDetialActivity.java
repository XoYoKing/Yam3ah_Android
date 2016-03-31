package com.ds.yam3ah.yam3ah;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ds.yam3ah.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class CompanyDetialActivity extends ActionBarActivity {

    RelativeLayout menuBtn;

    ImageButton backBtn;
    ImageView companyImage;
    TextView companyName;

    TextView descriptionText;
    TextView addressText;
    TextView phoneText;
    TextView buttonTxt;

    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;

    String listViewPosition;
    String category_id;
    RelativeLayout Menubutton;
    String company_id,category_title;
    String company_name;
    String company_desc;
    String company_address;
    String company_phone;
    String company_image,tag;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detial);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listViewPosition = extras.getString("listViewPosition");
            category_id = extras.getString("category_id");
            category_title= extras.getString("category_title");
            tag=extras.getString("tag");
            GlobalData.selectedCategory=category_title;
            System.out.println("listViewPosition == " + listViewPosition);
        }

        menuBtn = (RelativeLayout) findViewById(R.id.menuBtn);
        homeTab = (ImageView) findViewById(R.id.homeTab);
        categoryTab = (ImageView) findViewById(R.id.categoryTab);
        cartTab = (ImageView) findViewById(R.id.cartTab);
        checkOutTab = (ImageView) findViewById(R.id.checkOutTab);

        buttonTxt=(TextView)findViewById(R.id.productPrice);
        if (GlobalData.selectedCategory.equals("Kitchen")) {
            buttonTxt.setText("Menu");
        }else if(GlobalData.selectedCategory.equals("LifeStyle")){
            buttonTxt.setText("Items");
        }


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


        companyName = (TextView) findViewById(R.id.companyName);
        companyImage = (ImageView) findViewById(R.id.companyImage);
        addressText = (TextView) findViewById(R.id.addressText);
        phoneText = (TextView) findViewById(R.id.phoneText);
        descriptionText = (TextView) findViewById(R.id.descriptionText);

        Menubutton = (RelativeLayout) findViewById(R.id.menuBtn);
        backBtn = (ImageButton) findViewById(R.id.backBtn);
        TextView screenNameTop = (TextView) findViewById(R.id.screenNameTop);
        System.out.println("title " + GlobalData.selectedCategory);
        Resources res = getResources(); //resource handle

        if (GlobalData.selectedCategory.equals("Kitchen")) {
            screenNameTop.setText("Kitchen Detail");

            // menuBtn.set(R.drawable.menu_btn);
            Drawable drawable = res.getDrawable(R.drawable.menu_btn);
            menuBtn.setBackground(drawable);
        } else {
            screenNameTop.setText("Lifestyle Detail");
            Drawable drawable = res.getDrawable(R.drawable.product_btn);
            menuBtn.setBackground(drawable);
        }
        screenNameTop.setTypeface(Utils.Optima(CompanyDetialActivity.this));

        backBtn.setVisibility(View.VISIBLE);

        checkNetwork();

        int getItemOfPosition = Integer.parseInt(listViewPosition);


        JSONObject indexedObj = null;
        try {
           // indexedObj = CompanyActivity.companyResponseDataGlobal.getJSONObject(getItemOfPosition);
           // if (GlobalData.selectedCategory.equals("Kitchen")) {
            if(tag.equalsIgnoreCase("ProductDetailActivity")) {
               // indexedObj = HomeActivity.productResponseDataGlobal.getJSONObject(getItemOfPosition);
                //}else if(GlobalData.selectedCategory.equals("Lifestyle")){
                indexedObj=new JSONObject();
                 company_id=HomeProductAdapter.selectedJSONOBJ.get("company_id");
                 company_name=HomeProductAdapter.selectedJSONOBJ.get("company_name");
                 company_desc=HomeProductAdapter.selectedJSONOBJ.get("company_desc");
                 company_address=HomeProductAdapter.selectedJSONOBJ.get("company_address");
                 company_phone=HomeProductAdapter.selectedJSONOBJ.get("company_phone");
                 company_image=HomeProductAdapter.selectedJSONOBJ.get("company_image");
                 category_id=HomeProductAdapter.selectedJSONOBJ.get("category_id");
                 category_title=HomeProductAdapter.selectedJSONOBJ.get("category_title");


            }else{
                indexedObj = CompanyActivity.companyResponseDataGlobal.getJSONObject(getItemOfPosition);
                company_id = indexedObj.getString("company_id");
                company_name = indexedObj.getString("company_name");
                company_desc = indexedObj.getString("company_desc");
                company_address = indexedObj.getString("company_address");
                company_phone = indexedObj.getString("company_phone");
                company_image = indexedObj.getString("company_image");
                category_id = indexedObj.getString("category_id");
                category_title = indexedObj.getString("category_title");
            }
            /*else{
                indexedObj = HomeActivity.productResponseDataGlobal.getJSONObject(getItemOfPosition);
            }*/


            companyName.setText(company_name);
            companyName.setText(company_name);
            addressText.setText(company_address);
            phoneText.setText(company_phone);
            descriptionText.setText(company_desc);

            companyName.setTypeface(Utils.Optima(CompanyDetialActivity.this));
            addressText.setTypeface(Utils.Optima(CompanyDetialActivity.this));
            phoneText.setTypeface(Utils.Optima(CompanyDetialActivity.this));
            descriptionText.setTypeface(Utils.Optima(CompanyDetialActivity.this));

            /*Bitmap companyFeatureImage = getBitmapFromURL(company_image);
            Drawable companyDrawable = new BitmapDrawable(companyFeatureImage);
            companyImage.setBackgroundDrawable(companyDrawable);*/

            if (company_image.length() > 0) {
                int stub_id = R.drawable.stub;
                Picasso.with(CompanyDetialActivity.this.getApplicationContext())
                        .load(company_image)
                        .placeholder(stub_id)
                        .fit()
                        .into(companyImage);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void checkNetwork() {
        // TODO Auto-generated method stub
        Network cd;
        boolean isInternetPresent = false;

        cd = new Network(this.getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
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


    public void menuMethod(View view) {
        Intent intent = new Intent(CompanyDetialActivity.this, MenuActivity.class);
        intent.putExtra("company_id", company_id);
        intent.putExtra("category_id", category_id);
        intent.putExtra("category_title", category_title);
        intent.putExtra("listViewPosition", listViewPosition);
        intent.putExtra("tag", tag);

        startActivity(intent);
        finish();
    }

    public void backMethod(View view) {
        onBackPressed();

        if(tag.equalsIgnoreCase("ProductDetailActivity")){
            Intent intent = new Intent(CompanyDetialActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(CompanyDetialActivity.this, CompanyActivity.class);
            intent.putExtra("category_id", category_id);
            intent.putExtra("category_title", category_title);
            // intent.putExtra("cityId", GlobalData.cityId);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(tag.equalsIgnoreCase("ProductDetailActivity")){
            Intent intent = new Intent(CompanyDetialActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(CompanyDetialActivity.this, CompanyActivity.class);
            intent.putExtra("category_id", category_id);
            intent.putExtra("category_title", category_title);
            // intent.putExtra("cityId", GlobalData.cityId);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company_detial, menu);
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
                intent = new Intent(CompanyDetialActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                intent = new Intent(CompanyDetialActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cartTab:
                intent = new Intent(CompanyDetialActivity.this, CartActivity.class);
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

    /*public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
