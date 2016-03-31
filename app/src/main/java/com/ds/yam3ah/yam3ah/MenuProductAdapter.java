package com.ds.yam3ah.yam3ah;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.yam3ah.model.ProductDetail;
import com.ds.yam3ah.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 4/17/2015.
 */
public class MenuProductAdapter extends BaseAdapter {

    private Activity activity;
   // private final ArrayList<HashMap<String, String>> data;
    private final ArrayList<ProductDetail> data;
    private static LayoutInflater inflater = null;
    int quantityChanged;
    int totalQuantity = 0;
    double totalPrice = 0;
    int stock;
    int clickCount;
    String tag;


    String strConvert;

    public MenuProductAdapter(Activity a, ArrayList<ProductDetail> d,String tag) {
        activity = a;
        data = d;
        this.tag=tag;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {

        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        View vi = convertView;

        System.out.println("Response check == Menu Product Adapter ");
         ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            vi = inflater.inflate(R.layout.menuproductadapter, null);

            viewHolder.menuProductImage = (ImageView) vi.findViewById(R.id.menuProductImage);

            viewHolder.menuProductName = (TextView) vi.findViewById(R.id.menuProductName);
            viewHolder.priceText = (TextView) vi.findViewById(R.id.priceText);
            viewHolder.menuProductDesc=(TextView)vi.findViewById(R.id.productDescId);
            viewHolder.menuProductQuantityTxt=(TextView)vi.findViewById(R.id.menuProductQuantity);

            viewHolder.addToCart = (ImageView) vi.findViewById(R.id.addToCart);
            viewHolder.menuRel=(RelativeLayout)vi.findViewById(R.id.menuId);

            viewHolder.menuProductQuantity=(TextView) vi.findViewById(R.id.menuProductQuantValue);

            vi.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) vi.getTag();
        }

        ArrayList<ProductDetail> menuProducts = new ArrayList<ProductDetail>();
        menuProducts = data;



        viewHolder.menuProductName.setText(menuProducts.get(position).getProduct_name());
        viewHolder.menuProductDesc.setText(menuProducts.get(position).getProduct_desc());
       // viewHolder.priceText.setText(menuProducts.get("product_price") + ".00" + menuProducts.get("product_currency"));
        viewHolder.priceText.setText(menuProducts.get(position).getProduct_price() +menuProducts.get(position).getProduct_currency());
        viewHolder.menuProductQuantity.setText(menuProducts.get(position).getProduct_quantity());
        viewHolder.menuProductName.setTypeface(Utils.Optima(activity));
        viewHolder.menuProductDesc.setTypeface(Utils.Optima(activity));
        viewHolder.priceText.setTypeface(Utils.Optima(activity));
        viewHolder.menuProductQuantity.setTypeface(Utils.Optima(activity));
        viewHolder.menuProductQuantityTxt.setTypeface(Utils.Optima(activity));

       ProductDetail obj=new ProductDetail();

        obj=data.get(position);
        if (menuProducts.get(position).getProduct_feature_image().length() > 0) {
            int stub_id = R.drawable.stub;
            Picasso.with(activity.getApplicationContext())
                    .load(menuProducts.get(position).getProduct_feature_image())
                    .placeholder(stub_id)
                    .fit()
                    .into(viewHolder.menuProductImage);
        }


        viewHolder.addToCart.setTag(data.get(position));
        viewHolder.menuRel.setTag(position);



        viewHolder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // final int taggedPosition = Integer.parseInt(v.getTag().toString());

               /* PopUpDialogSpecialRequest.valueSpecial = "";
                PopUpDialogSpecialRequest popUp = new PopUpDialogSpecialRequest(activity);
                popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUp.getContext().setTheme(android.R.style.Theme_NoTitleBar);
                popUp.show();*/
                //popUp.setOnKeyListener();
                /*popUp.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        update(taggedPosition);
                    }
                });*/
                ProductDetail obj=(ProductDetail)v.getTag();


                obj.setIcClicked(true);

               /* if(obj.isIcClicked()){

                    if(obj.getClickCount()<=Integer.parseInt(obj.getProduct_quantity())){
                        int a=obj.getClickCount();
                        a++;
                        obj.setClickCount(a);
                    }else{
                        obj.setIcClicked(false);
                    }

                }
*/


               // updateAvailability(obj);
                update(obj);



            }

            private void updateAvailability(ProductDetail obj) {
              /*  HashMap<String, String> menuProduct = new HashMap<String, String>();
                menuProduct = data.get(taggedPosition);*/

               /* viewHolder.menuProductQuantity.setText(menuProduct.get("product_quantity"));*/
              if(obj.getClickCount()>Integer.parseInt(obj.getProduct_quantity())){
               // if( HomeActivity.globalCart.size()<Integer.parseInt(obj.getProduct_quantity())){
                  // Toast.makeText(activity,"Stock Finished",Toast.LENGTH_SHORT).show();
                  CustomAlertDialog showdialog = new CustomAlertDialog();
                  showdialog.showAlertDialog(activity, R.string.projectName, "Stock Finished");

               }else{

                }
            }

            private void update(ProductDetail obj) {

                totalQuantity = 0;
                totalPrice = 0;

              /*  HashMap<String, String> menuProduct = new HashMap<String, String>();
                menuProduct = data.get(taggedPosition);*/

                // System.out.println("taggedPosition == " + taggedPosition);

                int PID = Integer.parseInt(obj.getProduct_id());
                if (Integer.parseInt(obj.getProduct_quantity()) > 0) {
                    if (HomeActivity.globalCart.size() > 0) {
                        boolean addToCartFlag = false;

                        for (int i = 0; i < HomeActivity.globalCart.size(); i++) {

                            int cartPID = Integer.parseInt(HomeActivity.globalCart.get(i).getProduct_id());

                            if (cartPID == PID) {
                                addToCartFlag = true;
                                quantityChanged = HomeActivity.globalCart.get(i).getQuantity();
                                // if((Integer.parseInt(menuProduct.get("product_quantity")))>0) {
                                Log.d("MenuAdapter", "Quantity :" + cartPID + ", Clicked Quantity :" + PID);
                                Log.d("MenuAdapter", "TOTAL Quantity :" + obj.getProduct_quantity());
                                if (quantityChanged < Integer.parseInt(obj.getProduct_quantity())) {

                                    quantityChanged++;
                                    Log.d("MenuAdapter", "TOTAL Quantity TRUE :" + quantityChanged);
                                    HomeActivity.globalCart.get(i).setQuantity(quantityChanged);
                                    HomeActivity.globalCart.get(i).setProduct_Quantity(obj.getProduct_quantity());
                                } else {

                                    Log.d("MenuAdapter", "TOTAL Quantity ELSE :");
                                    Toast.makeText(activity, "Product limit exceeded", Toast.LENGTH_SHORT).show();
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

                            data.setProduct_id(obj.getProduct_id());
                            data.setProduct_name(obj.getProduct_name());
                            data.setProduct_currency(obj.getProduct_currency());
                            data.setProduct_price(obj.getProduct_price());
                            data.setProduct_desc(obj.getProduct_desc());
                            data.setProduct_feature_image(obj.getProduct_feature_image());
                            data.setCom_cat_id(obj.getCom_cat_id());
                            data.setCompany_id(obj.getProduct_company_id());
                            data.setQuantity(1);
                            data.setCurrency(obj.getProduct_id());
                            data.setProduct_speclRequest("");//PopUpDialogSpecialRequest.valueSpecial);
                            data.setProduct_Quantity(obj.getProduct_quantity());

                            HomeActivity.globalCart.add(data);
                            Toast.makeText(activity, "Item added to cart", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        //Toast.makeText(activity,"Product Not Available",Toast.LENGTH_SHORT).show();
                        GlobalData data = new GlobalData();

                        data.setProduct_id(obj.getProduct_id());
                        data.setProduct_name(obj.getProduct_name());
                        data.setProduct_currency(obj.getProduct_currency());
                        data.setProduct_price(obj.getProduct_price());
                        data.setProduct_desc(obj.getProduct_desc());
                        data.setProduct_feature_image(obj.getProduct_feature_image());
                        data.setCom_cat_id(obj.getCom_cat_id());
                        data.setCompany_id(obj.getProduct_company_id());
                        data.setQuantity(1);
                        data.setCurrency(obj.getProduct_id());
                        data.setProduct_speclRequest("");//PopUpDialogSpecialRequest.valueSpecial);
                        data.setProduct_Quantity(obj.getProduct_quantity());

                        HomeActivity.globalCart.add(data);
                    }

                    MenuProductsActivity.headerPriceText.setVisibility(View.VISIBLE);
                    MenuProductsActivity.cartItemnumber.setVisibility(View.VISIBLE);

                    for (int i = 0; i < HomeActivity.globalCart.size(); i++) {

                        totalQuantity = totalQuantity + HomeActivity.globalCart.get(i).getQuantity();
                        double itemPrice = Double.parseDouble(HomeActivity.globalCart.get(i).getProduct_price()) * HomeActivity.globalCart.get(i).getQuantity();
                        totalPrice = totalPrice + itemPrice;
                    }
                    //totalQuantity = totalQuantity + HomeActivity.globalCart.size();

                    MenuProductsActivity.cartItemnumber.setText("" + totalQuantity);
                    GlobalData.totalItemCount = totalQuantity;

                /*for (int i = 0; i < HomeActivity.globalCart.size(); i++) {
                    double itemPrice = Double.parseDouble(HomeActivity.globalCart.get(i).getProduct_price()) * HomeActivity.globalCart.get(i).getQuantity();
                    totalPrice = totalPrice + itemPrice;

                }*/
                    GlobalData.TotalCostCart = (int) totalPrice;
                    MenuProductsActivity.headerPriceText.setText("" + totalPrice);
                }else{
                    Toast.makeText(activity, "Product limit exceeded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.menuRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int taggedPosition = Integer.parseInt(view.getTag().toString());

                ProductDetail obj=data.get(position);
                Intent intent=new Intent(activity,ProductDetailActivity.class);
                intent.putExtra("listViewPosition", ""+taggedPosition);
                intent.putExtra("category_id", obj.getCom_cat_id());
                intent.putExtra("tag", tag);
                GlobalData.selectedCategory=obj.getCom_cat_title();
                activity.startActivity(intent);
            }
        });

        return vi;
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

    static class ViewHolder {

        ImageView menuProductImage;
        TextView menuProductName;
        TextView priceText;
        ImageView addToCart;
        RelativeLayout menuRel;
        TextView menuProductDesc;
        TextView menuProductQuantity;
        TextView menuProductQuantityTxt;

    }

}
