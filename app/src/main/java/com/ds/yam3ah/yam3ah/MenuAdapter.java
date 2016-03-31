package com.ds.yam3ah.yam3ah;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ds.yam3ah.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 4/17/2015.
 */
public class MenuAdapter extends BaseAdapter {

    private Activity activity;
    private final ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    String strConvert;

    public MenuAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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

    public View getView(int position, View convertView, final ViewGroup parent) {
        View vi = convertView;

        System.out.println("Response check == adapter working ");
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            vi = inflater.inflate(R.layout.menuadapter, null);

            viewHolder.menuLogo = (ImageView) vi.findViewById(R.id.menuLogo);
            viewHolder.menuName = (TextView) vi.findViewById(R.id.menuName);
            viewHolder.selection_arrow = (ImageView) vi.findViewById(R.id.selection_arrow);

            vi.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) vi.getTag();
        }

        HashMap<String, String> companies = new HashMap<String, String>();
        companies = data.get(position);

        viewHolder.menuName.setText(companies.get("menu_name"));
        viewHolder.menuName.setTypeface(Utils.Optima(activity));

       /* Bitmap companyLogoImage = getBitmapFromURL(companies.get("menu_image"));
        RoundImage roundedImage = new RoundImage(companyLogoImage);
        viewHolder.menuLogo.setImageDrawable(roundedImage);*/

        if (companies.get("menu_image").length() > 0) {
            int stub_id = R.drawable.stub;
            Picasso.with(activity.getApplicationContext())
                    .load(companies.get("menu_image"))
                    .placeholder(stub_id)
                    .fit()
                    .into(viewHolder.menuLogo);
        }

        /*viewHolder.selection_arrow.setTag(position);

        viewHolder.selection_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taggedPosition =  v.getTag().toString();

                System.out.println("taggedPosition == "+taggedPosition);

                data.get(taggedPosition).get("product_id");
                Intent intent = new Intent(parent.getContext(), ProductDetailActivity.class);
                intent.putExtra("listViewPosition", taggedPosition);
                parent.getContext().startActivity(intent);
            }
        });*/

        return vi;
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

    static class ViewHolder {

        ImageView menuLogo;
        TextView menuName;
        ImageView selection_arrow;

    }

}
