package com.ds.yam3ah.yam3ah;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class PopUpDialogSpecialRequest extends Dialog {

    private Context context;
   private EditText txtspecial;
    private Button btnOk,btnCancel;
    public static String valueSpecial="";


    public PopUpDialogSpecialRequest(Context context) {
        super(context);
        this.context = context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_specialrequest);

        txtspecial=(EditText)findViewById(R.id.txtspecialReq);

        btnOk=(Button)findViewById(R.id.btnokpop);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtspecial.getText().toString().replace(" ", "").length() > 0){
                    valueSpecial=txtspecial.getText().toString();
                    dismiss();
                }
                else{
                    Toast.makeText(context,"Please Enter Special Request",Toast.LENGTH_LONG).show();
                }

            }
        });
        btnCancel=(Button)findViewById(R.id.btncancelpop);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
