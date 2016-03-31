package com.ds.yam3ah.yam3ah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ds.yam3ah.model.Area;
import com.ds.yam3ah.model.City;
import com.ds.yam3ah.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shivangi on 6/4/2015.
 */
public class AreaAdapter extends BaseAdapter {

    private Activity activity;
    private final ArrayList<Area> data;
    private static LayoutInflater inflater = null;
    String category_id;

    String strConvert;

    public AreaAdapter(Activity a, ArrayList<Area> d,String category_id) {
        activity = a;
        data = d;
        this.category_id=category_id;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {

        return data.size();
    }

    public Area getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        View vi = convertView;

        System.out.println("Response check == adapter working ");
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            vi = inflater.inflate(R.layout.areaadapter, null);

            viewHolder.cityContainer = (LinearLayout) vi.findViewById(R.id.cityId);
            viewHolder.stateName = (TextView) vi.findViewById(R.id.stateTextId);
            vi.setTag(viewHolder);

            viewHolder.stateName.setText(data.get(position).getState());

            viewHolder.stateName.setTypeface(Utils.Optima(activity));


            for (int i = 0; i < data.get(position).getCityarr().size(); i++) {

                View childRow = inflater.inflate(R.layout.area_child_row, null);
                TextView childcitynameTxt = (TextView) childRow.findViewById(R.id.cityTextId);
                LinearLayout cityRowLL=(LinearLayout)childRow.findViewById(R.id.cityRowId);
                childcitynameTxt.setText("" + data.get(position).getCityarr().get(i).getName());
                childcitynameTxt.setTypeface(Utils.Optima(activity));

                cityRowLL.setTag(data.get(position).getCityarr().get(i));

                viewHolder.cityContainer.addView(childRow);

                cityRowLL.setOnClickListener(cityClick);

            }

        }
        else
        {
            viewHolder = (ViewHolder) vi.getTag();
        }

        return vi;
    }

    private View.OnClickListener cityClick= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            City cityObj = (City)view.getTag();
            if(cityObj.isClicked())
                cityObj.setClicked(false);
            else
                cityObj.setClicked(true);

            String cityId="";

            for(int i=0;i<data.size();i++){
                for(int j=0;j<data.get(i).getCityarr().size();j++){

                    if(data.get(i).getCityarr().get(j).isClicked()) {

                        cityId = data.get(i).getCityarr().get(j).getId();


                    }

                }
            }

            Intent intent=new Intent(activity,CompanyActivity.class);
            intent.putExtra("cityId",cityId);
            intent.putExtra("category_id",category_id);
            activity.startActivity(intent);

        }
    };

    static class ViewHolder {
        TextView stateName;
        LinearLayout cityContainer;

    }

}

