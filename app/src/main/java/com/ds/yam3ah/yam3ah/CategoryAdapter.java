package com.ds.yam3ah.yam3ah;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
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
public class CategoryAdapter extends BaseAdapter {

    private Activity activity;
    private final ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
  //  public static String categoryTag="";
    String strConvert;

    public CategoryAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.categoryadapter, null);

            viewHolder.categoryImage = (ImageView) vi.findViewById(R.id.categoryImage);

            viewHolder.categoryName = (TextView) vi.findViewById(R.id.categoryName);
            vi.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) vi.getTag();
        }

        HashMap<String, String> categories = new HashMap<String, String>();
        categories = data.get(position);
       // String textFromHtml = Jsonup.parse(MY_HTML_STRING_HERE).text();
       // TextView desc = (TextView) dialog.findViewById(R.id.description);
        viewHolder.categoryName.setText(Html.fromHtml(Html.fromHtml(categories.get("category_title")).toString()));
        viewHolder.categoryName.setTypeface(Utils.Optima(activity));
       // viewHolder.categoryName.setText(categories.get("category_title"));
        System.out.println("ashish   :"+categories.get("category_title"));
       /* Bitmap categoryImage = getBitmapFromURL(categories.get("category_image_url"));
        Drawable categoryDrawable = new BitmapDrawable(categoryImage);
        viewHolder.categoryImage.setBackgroundDrawable(categoryDrawable);*/

        if (categories.get("category_image_url").length() > 0) {
            int stub_id = R.drawable.stub;
            Picasso.with(activity.getApplicationContext())
                    .load(categories.get("category_image_url"))
                    .placeholder(stub_id)
                    .fit()
                    .into(viewHolder.categoryImage);
        }

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

        ImageView categoryImage;
        TextView categoryName;
        //ImageView arrow;

    }

}
