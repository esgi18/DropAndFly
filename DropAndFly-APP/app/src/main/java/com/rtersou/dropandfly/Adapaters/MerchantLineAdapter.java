package com.rtersou.dropandfly.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.models.Reservation;

import java.util.ArrayList;

public class MerchantLineAdapter extends ArrayAdapter<Reservation> {

    private MerchantLineAdapter.ViewHolder viewHolder;

    private static class ViewHolder {
        private LinearLayout itemView;
    }

    public MerchantLineAdapter(Context context, int textViewResourceId, ArrayList<Reservation> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.activity_history_merchant_line, parent, false);

            viewHolder = new MerchantLineAdapter.ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.hist_merc_line);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MerchantLineAdapter.ViewHolder) convertView.getTag();
        }

        Reservation item = getItem(position);
        if (item != null) {

            TextView name = viewHolder.itemView.findViewById(R.id.hist_merc_line_name);
            name.setText(item.getUser_id());

            TextView date_dep = viewHolder.itemView.findViewById(R.id.hist_merc_line_date_dep);
            date_dep.setText(item.getDate_start());

            TextView h_dep = viewHolder.itemView.findViewById(R.id.hist_merc_line_h_dep);
            h_dep.setText(item.getH_start());

            TextView date_ret = viewHolder.itemView.findViewById(R.id.hist_merc_line_date_ret);
            date_ret.setText(item.getDate_end());

            TextView h_ret = viewHolder.itemView.findViewById(R.id.hist_merc_line_h_ret);
            h_ret.setText(item.getH_end());

            TextView nb_lug = viewHolder.itemView.findViewById(R.id.hist_merc_line_nb_lug);
            int nb = item.getNb_luggage();
            if(nb > 1){
                nb_lug.setText(Integer.toString(item.getNb_luggage())+ " bagages");
            }
            else {
                nb_lug.setText(Integer.toString(item.getNb_luggage())+ " bagages");
            }

        }
        return convertView;
    }
}
