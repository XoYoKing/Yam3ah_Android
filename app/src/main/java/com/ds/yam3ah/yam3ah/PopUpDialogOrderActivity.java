package com.ds.yam3ah.yam3ah;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.ds.yam3ah.utils.Utils;

public class PopUpDialogOrderActivity extends Dialog implements View.OnClickListener{

    private Context context;
    private Activity activity;
    private TextView btnNumberPick,txtvalue;
    public static String valueSelected;
    private int valueSelectedItem = 1;
    private int number;
    public static EditText txtspecial;
    private ImageView minusbtn,plusbtn;
    public static String valueSpecial="";
    String product_quantity;

    public PopUpDialogOrderActivity(Activity activity, int quantity,String product_quantity ) {
        super(activity);
        this.activity = activity;
        number = quantity;
        this.product_quantity=product_quantity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_number);


        txtspecial=(EditText)findViewById(R.id.txtspecialReq);
        minusbtn = (ImageView) findViewById(R.id.minusbtn);
        plusbtn = (ImageView) findViewById(R.id.plusbtn);
        txtvalue=(TextView)findViewById(R.id.txtvalue);
        TextView quantityTxt=(TextView)findViewById(R.id.quantityId);
        quantityTxt.setTypeface(Utils.Optima(activity));
        txtspecial.setTypeface(Utils.Optima(activity));
        txtvalue.setTypeface(Utils.Optima(activity));
        valueSelectedItem = number;
        valueSelected = String.valueOf(valueSelectedItem);
        txtvalue.setText(valueSelected);

        minusbtn.setOnClickListener(this);
        plusbtn.setOnClickListener(this);
        /*final String[] values = new String[1000];
        for (int i = 1; i < values.length; i++) {
            values[i] = Integer.toString(i);
        }
        np.setMinValue(1);
        np.setMaxValue(values.length - 1);
        np.setWrapSelectorWheel(false);

        np.setValue(number);
        valueSelected = String.valueOf(number);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                valueSelected = values[newVal];
            }
        });*/

        btnNumberPick = (TextView) findViewById(R.id.btnNumberPick);
        btnNumberPick.setTypeface(Utils.Optima(activity));
        btnNumberPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueSpecial=txtspecial.getText().toString();

                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.plusbtn:

                if(valueSelectedItem<Integer.parseInt(product_quantity)) {
                    valueSelectedItem = valueSelectedItem + 1;
                    valueSelected = String.valueOf(valueSelectedItem);
                    txtvalue.setText(valueSelected);
                }

                break;
            case R.id.minusbtn:
                if(valueSelectedItem >= 2){
                    valueSelectedItem = valueSelectedItem - 1;
                    valueSelected = String.valueOf(valueSelectedItem);
                    txtvalue.setText(valueSelected);
                }
                break;
        }
    }
}
