package com.ds.yam3ah.yam3ah;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.yam3ah.utils.Utils;
import com.squareup.picasso.Picasso;

import static android.view.View.*;

public class CartActivity extends ActionBarActivity {

    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    RelativeLayout checkOutBtn, PriceContainer, emptyCartPanel;
    TextView totalItemText;
    TextView totalPriceText;
    TextView totalItems,totalPrice,proceedToCheckOut;
    NumberPicker numberPicker;
    public static String TotalCost = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.hide();

        homeTab = (ImageView) findViewById(R.id.homeTab);
        categoryTab = (ImageView) findViewById(R.id.categoryTab);
        cartTab = (ImageView) findViewById(R.id.cartTab);
        checkOutTab = (ImageView) findViewById(R.id.checkOutTab);
        PriceContainer = (RelativeLayout) findViewById(R.id.priceContainer);
        homeTab.setBackgroundResource(R.drawable.tab_1_unselected);
        categoryTab.setBackgroundResource(R.drawable.tab_2_unseleted);
        cartTab.setBackgroundResource(R.drawable.tab_3_selected);
        checkOutTab.setBackgroundResource(R.drawable.tab_4_unselected);
        emptyCartPanel = (RelativeLayout) findViewById(R.id.emptycartpanel);
        TextView screenNameTop = (TextView) findViewById(R.id.screenNameTop);
        screenNameTop.setText("CART");
        screenNameTop.setTypeface(Utils.Optima(CartActivity.this));
        TextView cartItemnumber = (TextView) findViewById(R.id.caritemNo);

        checkOutBtn = (RelativeLayout) findViewById(R.id.checkOutBtn);
        totalItemText = (TextView) findViewById(R.id.totalItemText);
        totalPriceText = (TextView) findViewById(R.id.totalPriceText);
        totalItems=(TextView) findViewById(R.id.totalItemStr);
        totalPrice=(TextView) findViewById(R.id.totalPriceStr);
        proceedToCheckOut=(TextView) findViewById(R.id.proceedCheckOutId);

        totalItemText.setTypeface(Utils.Optima(CartActivity.this));
        totalPriceText.setTypeface(Utils.Optima(CartActivity.this));
        totalItems.setTypeface(Utils.Optima(CartActivity.this));
        totalPrice.setTypeface(Utils.Optima(CartActivity.this));
        proceedToCheckOut.setTypeface(Utils.Optima(CartActivity.this));

        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);


        setData();
    }

    private void setData() {
        String currency = null;
        double totalPrice = 0;

        ImageView productImage;
        TextView itemText;
        TextView quantityText;
        TextView priceText;
        ImageView editBtn;
        ImageView deleteBtn;

        LinearLayout cartContainer = (LinearLayout) findViewById(R.id.ll);
        if (HomeActivity.globalCart.size() == 0) {
            emptyCartPanel.setVisibility(VISIBLE);
            PriceContainer.setVisibility(GONE);
        }
        for (int i = 0; i < HomeActivity.globalCart.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vi = inflater.inflate(R.layout.cartadapter, null);

            productImage = (ImageView) vi.findViewById(R.id.productImage);
            itemText = (TextView) vi.findViewById(R.id.itemText);
            quantityText = (TextView) vi.findViewById(R.id.quantityText);
            priceText = (TextView) vi.findViewById(R.id.priceText);
            editBtn = (ImageView) vi.findViewById(R.id.editBtn);
            deleteBtn = (ImageView) vi.findViewById(R.id.deleteBtn);

            itemText.setTypeface(Utils.Optima(CartActivity.this));
            quantityText.setTypeface(Utils.Optima(CartActivity.this));
            priceText.setTypeface(Utils.Optima(CartActivity.this));

            editBtn.setTag(i);

            deleteBtn.setTag(i);

           /* Bitmap productImageBP = getBitmapFromURL(HomeActivity.globalCart.get(i).getProduct_feature_image());
            Drawable productImageDR = new BitmapDrawable(productImageBP);
            productImage.setImageDrawable(productImageDR);*/

            if (HomeActivity.globalCart.get(i).getProduct_feature_image().length() > 0) {
                int stub_id = R.drawable.stub;
                Picasso.with(CartActivity.this)
                        .load(HomeActivity.globalCart.get(i).getProduct_feature_image())
                        .placeholder(stub_id)
                        .fit()
                        .into(productImage);
            }

            itemText.setText(HomeActivity.globalCart.get(i).getProduct_name());
            quantityText.setText(String.valueOf(HomeActivity.globalCart.get(i).getQuantity()));
            priceText.setText(HomeActivity.globalCart.get(i).getProduct_price() + " " + HomeActivity.globalCart.get(i).getProduct_currency());
            // Toast.makeText("fdfdf""sdd" Toast.LENGTH_LONG);
            System.out.println("currency  :" + HomeActivity.globalCart.get(i).getProduct_currency());
            cartContainer.addView(vi);
            currency = HomeActivity.globalCart.get(i).getProduct_currency();
            double itemPrice = Double.parseDouble(HomeActivity.globalCart.get(i).getProduct_price()) * HomeActivity.globalCart.get(i).getQuantity();
            totalPrice = totalPrice + itemPrice;


            editBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    final int specificButton = (Integer) v.getTag();

                    PopUpDialogOrderActivity popUp = new PopUpDialogOrderActivity(CartActivity.this,HomeActivity.globalCart.get(specificButton).getQuantity(),HomeActivity.globalCart.get(specificButton).getProduct_Quantity());
                    //  popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //  popUp.getContext().setTheme(android.R.style.Theme_NoTitleBar);
                    popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    popUp.getContext().setTheme(android.R.style.Theme_NoTitleBar);
                    popUp.show();
                    PopUpDialogOrderActivity.valueSpecial = "";

                    if (HomeActivity.globalCart.get(specificButton).getProduct_speclRequest().length() > 0) {
                        PopUpDialogOrderActivity.txtspecial.setText(HomeActivity.globalCart.get(specificButton).getProduct_speclRequest());
                    }

                 popUp.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            String QuantityNumber = PopUpDialogOrderActivity.valueSelected;

                            HomeActivity.globalCart.get(specificButton).setQuantity(Integer.valueOf(QuantityNumber));
                            String text = HomeActivity.globalCart.get(specificButton).getProduct_speclRequest();

                            HomeActivity.globalCart.get(specificButton).setProduct_speclRequest(PopUpDialogOrderActivity.valueSpecial);

                            Intent intent = new Intent(CartActivity.this , CartActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

                }
            });

            deleteBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    final int specificButton = (Integer) v.getTag();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CartActivity.this);
                    alertDialogBuilder.setTitle(" Delete ");
                    alertDialogBuilder.setMessage("Are you sure you want to delete?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            HomeActivity.globalCart.remove(specificButton);
                            Intent intent = getIntent();
                            startActivity(intent);
                            finish();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {


                            dialog.cancel();

                        }

                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            });
        }
        checkOutBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, ActivityCheckOut.class);
                startActivity(intent);
                finish();
            }
        });

        int totalQuantity = 0;
        for (int i = 0; i < HomeActivity.globalCart.size(); i++) {
            totalQuantity = totalQuantity + HomeActivity.globalCart.get(i).getQuantity();
        }
        totalItemText.setText("" + totalQuantity);
        GlobalData.totalItemCount = totalQuantity;


        String totalPriceAfterFormated = String.format("%.2f",totalPrice);
       // System.out.println("prise currency :" + currency);
        totalPriceText.setText(totalPriceAfterFormated + " " + currency);
        TotalCost = (String.valueOf(totalPrice));
        GlobalData.TotalCostCart=(int)totalPrice;
    }


    private int showQuantityDialog(int specificButton) {

        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();

        // Setting Dialog Title
        alertDialog.setTitle("Update Quantity");

        // Setting Dialog Message
        alertDialog.setMessage("Test test test");

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();

        return specificButton;
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
                intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                intent = new Intent(CartActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cartTab:
                //intent = new Intent(CartActivity.this, CartActivity.class);
                //startActivity(intent);
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
                    intent = new Intent(CartActivity.this, ActivityCheckOut.class);
                    startActivity(intent);
                    finish();
                }

                break;

        }
    }
}
