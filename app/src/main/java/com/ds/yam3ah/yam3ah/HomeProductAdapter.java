package com.ds.yam3ah.yam3ah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 4/17/2015.
 */
public class HomeProductAdapter extends BaseAdapter {

    private Activity activity;
    private  ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public static HashMap<String, String> selectedJSONOBJ;

//    public static HashMap<String, String> selectedObj;

    String strConvert;

    public HomeProductAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
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


        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            vi = inflater.inflate(R.layout.homeadapter, null);

            //viewHolder.companyLogo = (ImageView) vi.findViewById(R.id.companyLogo);
            //viewHolder.companyName = (TextView) vi.findViewById(R.id.companyName);
            //viewHolder.productName = (TextView) vi.findViewById(R.id.productName);
            viewHolder.productImage = (ImageView) vi.findViewById(R.id.productImage);
            //viewHolder.arrow = (ImageView) vi.findViewById(R.id.arrow);

            vi.setTag(viewHolder);

            vi.setPadding(0,0,0,5);
        } else {
            viewHolder = (ViewHolder) vi.getTag();
        }
        viewHolder.productImage.setTag(position);
        HashMap<String, String> products = new HashMap<String, String>();
        products = data.get(position);

        //System.out.println("Response check == adapter working " + products.get("company_name"));
      //  System.out.println("Response check == adapter working " + products.get("company_image_thumb"));

        //viewHolder.companyName.setText(products.get("company_name"));
        //viewHolder.productName.setText(products.get("product_name"));

        if (products.get("product_feature_image").length() > 0) {
            int stub_id = R.drawable.stub;
            Picasso.with(activity.getApplicationContext())
                    .load(products.get("product_feature_image"))
                    .placeholder(stub_id)
                    .centerCrop()
                    .resizeDimen(R.dimen.grid_item_height,R.dimen.grid_item_height)
                    .into(viewHolder.productImage);
        }
        Log.d("TaggedPosition"," TaggedPosition   == " + products.get("product_feature_image"));

       /* if (products.get("company_image_thumb").length() > 0) {
            int stub_id = R.drawable.stub;
            Picasso.with(activity.getApplicationContext())
                    .load(products.get("company_image_thumb"))
                    .placeholder(stub_id)
                    .fit()
                    .into(viewHolder.companyLogo);
        }*/

        viewHolder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int taggedPosition = (int) v.getTag();

                selectedJSONOBJ = data.get(taggedPosition);
                Log.d("TaggedPosition"," TaggedPosition   == " + data.get(taggedPosition));
                //data.get(taggedPosition).get("product_id");
                Intent intent = new Intent(parent.getContext(), ProductDetailActivity.class);
                intent.putExtra("listViewPosition", ""+taggedPosition);
                intent.putExtra("category_id", data.get(taggedPosition).get("category_id"));
                intent.putExtra("tag", "HomeProductAdapter");
                GlobalData.selectedCategory=data.get(taggedPosition).get("category_title");
                parent.getContext().startActivity(intent);
            }
        });

       // viewHolder.productImage.setTag(position);
/*

        viewHolder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taggedPosition = v.getTag().toString();

                System.out.println("taggedPosition == " + taggedPosition);

                //data.get(taggedPosition).get("product_id");
                Intent intent = new Intent(parent.getContext(), ProductDetailActivity.class);
                intent.putExtra("listViewPosition", taggedPosition);
                intent.putExtra("category_id", data.get(position).get("category_id"));
                intent.putExtra("tag", "HomeProductAdapter");
                GlobalData.selectedCategory=data.get(position).get("category_title");
                parent.getContext().startActivity(intent);
            }
        });
*/

        return vi;
    }


    public View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {



        }
    };


    static class ViewHolder {

        ImageView companyLogo;
        TextView companyName;
        TextView productName;
        ImageView productImage;
        ImageView arrow;

    }

    public void setData( ArrayList<HashMap<String, String>> d){
        this.data = d;
        notifyDataSetChanged();
    }

}
