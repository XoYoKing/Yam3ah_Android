package com.ds.yam3ah.yam3ah;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.yam3ah.model.ProductDetail;
import com.ds.yam3ah.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProductDetailActivity extends ActionBarActivity {

    String listViewPosition;
    String category_id;
    ImageButton backBtn;
    ImageView productImage;
    TextView productName;
    TextView productPrice;
    TextView addToCart;
    TextView buyNow;
    TextView descriptionText;
    LinearLayout galleryView;
    ImageButton goToCompanyBtn;
    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    int quantityChanged;
    String product_id;
    String product_name;
    String product_currency;
    String product_price;
    String product_desc;
    String product_feature_image;
    String com_cat_id;
    String company_id;
    String category_title;
    String product_quantity;
    RelativeLayout productNameRL;
    LinearLayout addToCartSliceLL;
    //String currency;
    int totalQuantity = 0;
    double totalPrice = 0;
    String tag="";
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            listViewPosition = extras.getString("listViewPosition");
            category_id = extras.getString("category_id");
            tag= extras.getString("tag");
        }

        homeTab = (ImageView) findViewById(R.id.homeTab);
        categoryTab = (ImageView) findViewById(R.id.categoryTab);
        cartTab = (ImageView) findViewById(R.id.cartTab);
        checkOutTab = (ImageView) findViewById(R.id.checkOutTab);
        goToCompanyBtn=(ImageButton)findViewById(R.id.goToCompanyBtnId);

        productNameRL=(RelativeLayout)findViewById(R.id.productNameLL);
        addToCartSliceLL=(LinearLayout)findViewById(R.id.addToCartSlice);

        if(tag.equalsIgnoreCase("ProductDetailActivity") || tag.equalsIgnoreCase("HomeProductAdapter")){
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



        backBtn = (ImageButton) findViewById(R.id.backBtn);
        TextView screenNameTop = (TextView) findViewById(R.id.screenNameTop);
        screenNameTop.setText("Product Detail");
        screenNameTop.setTypeface(Utils.Optima(ProductDetailActivity.this));
        productImage = (ImageView) findViewById(R.id.productImage);

        productName = (TextView) findViewById(R.id.productName);
        productPrice = (TextView) findViewById(R.id.productPrice);
        addToCart = (TextView) findViewById(R.id.addToCart);
        viewPager=(ViewPager)findViewById(R.id.imageViewPager);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addToCartMethod(view);
            }
        });
        buyNow = (TextView) findViewById(R.id.buyNow);
        productName.setTypeface(Utils.Optima(ProductDetailActivity.this));
        productPrice.setTypeface(Utils.Optima(ProductDetailActivity.this));
        addToCart.setTypeface(Utils.Optima(ProductDetailActivity.this));
        buyNow.setTypeface(Utils.Optima(ProductDetailActivity.this));

        if(tag.equalsIgnoreCase("HomeProductAdapter")){
            goToCompanyBtn.setVisibility(View.VISIBLE);
            productNameRL.setVisibility(View.VISIBLE);
            addToCartSliceLL.setVisibility(View.VISIBLE);
        }
        //else if(tag.equalsIgnoreCase("MenuProductAdapter")){
        else{
            goToCompanyBtn.setVisibility(View.INVISIBLE);
            productNameRL.setVisibility(View.GONE);
            addToCartSliceLL.setVisibility(View.GONE);
        }
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.globalCart.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
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
                    Intent intent = new Intent(ProductDetailActivity.this, ActivityCheckOut.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        goToCompanyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent= new Intent(ProductDetailActivity.this,CompanyDetialActivity.class);
                intent.putExtra("listViewPosition", listViewPosition);
                intent.putExtra("category_id", category_id);
                intent.putExtra("category_title", category_title);
                intent.putExtra("tag", "ProductDetailActivity");
                startActivity(intent);
                finish();
            }
        });

        descriptionText = (TextView) findViewById(R.id.descriptionText);
        descriptionText.setTypeface(Utils.Optima(ProductDetailActivity.this));
        galleryView = (LinearLayout) findViewById(R.id.galleryView);
        backBtn.setVisibility(View.VISIBLE);


        int getItemOfPosition = Integer.parseInt(listViewPosition);

        JSONObject indexedObj = null;
        try {

            if(tag.equalsIgnoreCase("HomeProductAdapter")){

                Log.d("ProductDetail","ProductImage POSITION :"+getItemOfPosition);

              //  if(getItemOfPosition > HomeActivity.productResponseDataGlobal.length())
                //indexedObj = new JSONObject(HomeProductAdapter.selectedJSONOBJ);//HomeActivity.productResponseDataGlobal.getJSONObject(getItemOfPosition);

                product_id = HomeProductAdapter.selectedJSONOBJ.get("product_id");
                product_name = HomeProductAdapter.selectedJSONOBJ.get("product_name");
                product_currency = HomeProductAdapter.selectedJSONOBJ.get("product_currency");
                product_price = HomeProductAdapter.selectedJSONOBJ.get("product_price");
                product_desc = HomeProductAdapter.selectedJSONOBJ.get("product_desc");
                product_quantity = HomeProductAdapter.selectedJSONOBJ.get("product_quantity");
try {
    product_feature_image = HomeProductAdapter.selectedJSONOBJ.get("product_feature_image");
    com_cat_id = HomeProductAdapter.selectedJSONOBJ.get("com_cat_id");
    company_id = HomeProductAdapter.selectedJSONOBJ.get("company_id");
    category_title = HomeProductAdapter.selectedJSONOBJ.get("category_title");
}catch(Exception e){
    e.getMessage();
}

                indexedObj = new JSONObject();
                indexedObj.put("product_id",product_id);
                indexedObj.put("product_name",product_name);
                indexedObj.put("product_currency", product_currency);
                indexedObj.put("product_price", product_price);
                indexedObj.put("product_desc", product_desc);
                indexedObj.put("product_quantity", product_quantity);
                indexedObj.put("feature_image_thumb",product_feature_image);
                indexedObj.put("com_cat_id",com_cat_id);

                indexedObj.put("company_id",company_id);
                indexedObj.put("category_title",category_title);
                indexedObj.put("product_images",HomeProductAdapter.selectedJSONOBJ.get("product_images"));
               /* indexedObj.put("com_cat_id",com_cat_id);*/

                Log.d("PoductJSON","JSON 1 :"+indexedObj.optString("product_images"));

               // JSONArray product_images_array = indexedObj.getJSONArray("product_images");
               JSONArray jsonArrayNew=new JSONArray(indexedObj.optString("product_images"));

                   ArrayList<String> imageList= new ArrayList<String>();

                for (int i = 0; i < jsonArrayNew.length(); i++) {
                    JSONObject imageIndexedObj = jsonArrayNew.getJSONObject(i);
                    String product_image_url = imageIndexedObj.getString("product_image_url");
                    imageList.add(product_image_url);
                  /*  Log.e("product",product_image_url);
               *//* Bitmap productBitmapImage = getBitmapFromURL(product_image_url);
                Drawable productDrawable = new BitmapDrawable(productBitmapImage);*//*

                    ImageView imageView = new ImageView(this);

                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,0,10,0);
                    imageView.setLayoutParams(params);
                    //imageView.setAdjustViewBounds(true);

                    //imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    //  imageView.setImageDrawable(productDrawable);

                    if (product_image_url.length() > 0) {
                        int stub_id = R.drawable.stub;
                        Picasso.with(ProductDetailActivity.this.getApplicationContext())
                                .load(product_image_url)
                                .placeholder(stub_id)
                                .into(imageView);
                    }


                    //imageView.setBackgroundDrawable(productDrawable);
                    galleryView.addView(imageView);*/
                }

                if(imageList!=null && !imageList.isEmpty()){
                    ImagePagerAdapter  imagePagerAdapter= new ImagePagerAdapter(imageList);
                    viewPager.setAdapter(imagePagerAdapter);
                }


            }
           // else if(tag.equalsIgnoreCase("CompanyActivity")){
            else {
                try {

                    ArrayList<String> imageList= new ArrayList<String>();
                    indexedObj = MenuProductsActivity.products_Array.getJSONObject(getItemOfPosition);
                    product_id = indexedObj.getString("product_id");
                    product_name = indexedObj.getString("product_name");
                    product_currency = indexedObj.getString("product_currency");
                    product_price = indexedObj.getString("product_price");
                    product_desc = indexedObj.getString("product_desc");
                    product_feature_image = indexedObj.getString("product_feature_image");
                    // com_cat_id = indexedObj.getString("com_cat_id");
                    product_quantity = indexedObj.getString("product_quantity");
                    company_id = indexedObj.getString("company_id");

                    JSONArray product_images_array = indexedObj.getJSONArray("product_image");

                    Log.d("PoductJSON", "JSON :" + indexedObj);

                    for (int i = 0; i < product_images_array.length(); i++) {
                        JSONObject imageIndexedObj = product_images_array.getJSONObject(i);
                        String product_image_url = imageIndexedObj.getString("product_image_url");
                        imageList.add(product_image_url);
                        Log.e("product", product_image_url);
               /* Bitmap productBitmapImage = getBitmapFromURL(product_image_url);
                Drawable productDrawable = new BitmapDrawable(productBitmapImage);*/
/*
                        ImageView imageView = new ImageView(this);

                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;

                        imageView.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));

                       // imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        //imageView.setAdjustViewBounds(true);

                       // imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                        //  imageView.setImageDrawable(productDrawable);

                        if (product_image_url.length() > 0) {
                            int stub_id = R.drawable.stub;
                            Picasso.with(ProductDetailActivity.this.getApplicationContext())
                                    .load(product_image_url)
                                    .placeholder(stub_id)

                                    .into(imageView);
                        }


                        //imageView.setBackgroundDrawable(productDrawable);
                        galleryView.addView(imageView);*/
                    }

                    if(imageList!=null && !imageList.isEmpty()){
                        ImagePagerAdapter  imagePagerAdapter= new ImagePagerAdapter(imageList);
                        viewPager.setAdapter(imagePagerAdapter);
                    }

                } catch (Exception e) {
                    e.getMessage();
                }
            }
          //  }


            //Log.e("product",indexedObj.getString("product_images"));

            //currency = indexedObj.getString("product_currency");

            productName.setText(product_name);
            productPrice.setText(product_price + " " + product_currency);
            descriptionText.setText(product_desc);
            screenNameTop.setText(product_name);



        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void backMethod(View view) {

        onBackPressed();

       /* Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();*/
    }

    public void addToCartMethod(View view) {

        totalQuantity = 0;
        totalPrice = 0;

              /*  HashMap<String, String> menuProduct = new HashMap<String, String>();
                menuProduct = data.get(taggedPosition);*/

        // System.out.println("taggedPosition == " + taggedPosition);

        int PID = Integer.parseInt(product_id);
        if (Integer.parseInt(product_quantity) > 0) {
            if (HomeActivity.globalCart.size() > 0) {
                boolean addToCartFlag = false;

                for (int i = 0; i < HomeActivity.globalCart.size(); i++) {

                    int cartPID = Integer.parseInt(HomeActivity.globalCart.get(i).getProduct_id());

                    if (cartPID == PID) {
                        addToCartFlag = true;
                        quantityChanged = HomeActivity.globalCart.get(i).getQuantity();
                        // if((Integer.parseInt(menuProduct.get("product_quantity")))>0) {
                        Log.d("MenuAdapter", "Quantity :" + cartPID + ", Clicked Quantity :" + PID);
                        Log.d("MenuAdapter", "TOTAL Quantity :" + product_quantity);
                        if (quantityChanged < Integer.parseInt(product_quantity)) {

                            quantityChanged++;
                            Toast.makeText(ProductDetailActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                            Log.d("MenuAdapter", "TOTAL Quantity TRUE :" + quantityChanged);
                            HomeActivity.globalCart.get(i).setQuantity(quantityChanged);
                            HomeActivity.globalCart.get(i).setProduct_Quantity(product_quantity);
                        } else {

                            Log.d("MenuAdapter", "TOTAL Quantity ELSE :");
                            Toast.makeText(ProductDetailActivity.this, "Product limit exceeded", Toast.LENGTH_SHORT).show();
                            /*CustomAlertDialog showdialog = new CustomAlertDialog();
                            showdialog.showAlertDialog(activity, R.string.projectName, "Stock Finished");*/
                        }
                        // }



                       /* String text = HomeActivity.globalCart.get(i).getProduct_speclRequest();
                        if(text.length() > 0) {
                            HomeActivity.globalCart.get(i).setProduct_speclRequest(text + " , " + PopUpDialogSpecialRequest.valueSpecial);
                        }else{
                            HomeActivity.globalCart.get(i).setProduct_speclRequest(PopUpDialogSpecialRequest.valueSpecial);
                        }*/

                    } else {
                    }
                }


                if (addToCartFlag == false) {
                    GlobalData data = new GlobalData();

                    data.setProduct_id(product_id);
                    data.setProduct_name(product_name);
                    data.setProduct_currency(product_currency);
                    data.setProduct_price(product_price);
                    data.setProduct_desc(product_desc);
                    data.setProduct_feature_image(product_feature_image);
                    data.setCom_cat_id(com_cat_id);
                    data.setCompany_id(company_id);
                    data.setQuantity(1);
                    data.setCurrency(product_id);
                    data.setProduct_speclRequest("");//PopUpDialogSpecialRequest.valueSpecial);
                    data.setProduct_Quantity(product_quantity);

                    HomeActivity.globalCart.add(data);
                    Toast.makeText(ProductDetailActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                }


            } else {
                //Toast.makeText(activity,"Product Not Available",Toast.LENGTH_SHORT).show();
                GlobalData data = new GlobalData();

                data.setProduct_id(product_id);
                data.setProduct_name(product_name);
                data.setProduct_currency(product_currency);
                data.setProduct_price(product_price);
                data.setProduct_desc(product_desc);
                data.setProduct_feature_image(product_feature_image);
                data.setCom_cat_id(com_cat_id);
                data.setCompany_id(company_id);
                data.setQuantity(1);
                data.setCurrency(product_id);
                data.setProduct_speclRequest("");//PopUpDialogSpecialRequest.valueSpecial);
                data.setProduct_Quantity(product_quantity);

                HomeActivity.globalCart.add(data);
            }

       //     MenuProductsActivity.headerPriceText.setVisibility(View.VISIBLE);
       //     MenuProductsActivity.cartItemnumber.setVisibility(View.VISIBLE);

            for (int i = 0; i < HomeActivity.globalCart.size(); i++) {

                totalQuantity = totalQuantity + HomeActivity.globalCart.get(i).getQuantity();
                double itemPrice = Double.parseDouble(HomeActivity.globalCart.get(i).getProduct_price()) * HomeActivity.globalCart.get(i).getQuantity();
                totalPrice = totalPrice + itemPrice;
            }
            //totalQuantity = totalQuantity + HomeActivity.globalCart.size();

           // MenuProductsActivity.cartItemnumber.setText("" + totalQuantity);
            GlobalData.totalItemCount = totalQuantity;

                /*for (int i = 0; i < HomeActivity.globalCart.size(); i++) {
                    double itemPrice = Double.parseDouble(HomeActivity.globalCart.get(i).getProduct_price()) * HomeActivity.globalCart.get(i).getQuantity();
                    totalPrice = totalPrice + itemPrice;

                }*/
            GlobalData.TotalCostCart = (int) totalPrice;
         //   MenuProductsActivity.headerPriceText.setText("" + totalPrice);
        }else{
            Toast.makeText(ProductDetailActivity.this, "Product limit exceeded", Toast.LENGTH_SHORT).show();
        }}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
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

   /* public Bitmap getBitmapFromURL(String imageUrl) {
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

    public void tabAction(View view) {
        int id = view.getId();
        Intent intent;

        switch (id) {
            case R.id.homeTab:
                intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                intent = new Intent(ProductDetailActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cartTab:
                intent = new Intent(ProductDetailActivity.this, CartActivity.class);
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



    private class ImagePagerAdapter extends PagerAdapter {

        private ArrayList<String> images;
        private LayoutInflater inflater;

      public ImagePagerAdapter(ArrayList<String> images) {
            this.images = images;
            inflater = getLayoutInflater();

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            if(images.size()==0)
                return 1;

            return images.size();

        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {

            View imageLayout = inflater.inflate(R.layout.detail_image_item,
                    view, false);
            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.imageView);
           String imageUrl="";

            if(images.size()>0){
                imageUrl=images.get(position);


            }


            Picasso.with(ProductDetailActivity.this)
                    .load(imageUrl)
                    .placeholder(R.drawable.stub)
                    .into(imageView);

            ((ViewPager) view).addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

}
