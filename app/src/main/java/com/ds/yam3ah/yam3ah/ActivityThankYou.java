package com.ds.yam3ah.yam3ah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ds.yam3ah.utils.Utils;

/**
 * Created by Chirag on 4/29/2015.
 */
public class ActivityThankYou  extends ActionBarActivity {

    ImageView homeTab;
    ImageView categoryTab;
    ImageView cartTab;
    ImageView checkOutTab;
    TextView yam3ah,yourOrder;

    private TextView txtThankYou , txtThankYouOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);


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
        screenNameTop.setText("THANK YOU");
        screenNameTop.setTypeface(Utils.Optima(ActivityThankYou.this));


        txtThankYou = (TextView) findViewById(R.id.txtThankYou);
        txtThankYouOrderId = (TextView) findViewById(R.id.txtThankYouOrderId);
        yam3ah = (TextView) findViewById(R.id.yam3ahId);
        yourOrder = (TextView) findViewById(R.id.yourOrder);
        //txtThankYou.setText(Html.fromHtml("<font color=#FFFFFF>THANK </font> <font color=#52C0B4> YOU</font>"));

        txtThankYou.setTypeface(Utils.Optima(ActivityThankYou.this));
        txtThankYouOrderId.setTypeface(Utils.Optima(ActivityThankYou.this));
        yam3ah.setTypeface(Utils.Optima(ActivityThankYou.this));
        yourOrder.setTypeface(Utils.Optima(ActivityThankYou.this));
        txtThankYouOrderId.setText(Html.fromHtml("<font color=#FFFFFF>YOUR ORDER ID IS:  "+PaymentActivity.orderid+"</font>"));
        GlobalData.TotalCostCart=0;
        GlobalData.totalItemCount=0;
        HomeActivity.globalCart.clear();
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
                intent = new Intent(ActivityThankYou.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.categoryTab:
                intent = new Intent(ActivityThankYou.this, CategoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cartTab:
                intent = new Intent(ActivityThankYou.this, CartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.checkOutTab:
                //Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                //startActivity(intent);
                break;

        }
    }
}

