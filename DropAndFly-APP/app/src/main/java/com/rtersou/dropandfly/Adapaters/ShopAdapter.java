package com.rtersou.dropandfly.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.models.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Shop> shopList = null;
    private ArrayList<Shop> arraylist;

    public ShopAdapter(Context context, List<Shop> shopList) {
        mContext = context;
        this.shopList = shopList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(shopList);
    }

    public class ViewHolder {
        TextView name;
        TextView address1;
        TextView address2;
    }

    @Override
    public int getCount() {
        return shopList.size();
    }

    @Override
    public Shop getItem(int position) {
        return shopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ShopAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ShopAdapter.ViewHolder();
            view = inflater.inflate(R.layout.shop_line, null);

            holder.name     = (view.findViewById(R.id.shop_line_name));
            holder.address1 = (view.findViewById(R.id.shop_line_address1));
            holder.address2 = (view.findViewById(R.id.shop_line_address2));
            view.setTag(holder);
        } else {
            holder = (ShopAdapter.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(shopList.get(position).getName());
        holder.address1.setText(shopList.get(position).getAddress_number() + " " + shopList.get(position).getAddress_street());
        holder.address2.setText(shopList.get(position).getAddress_cp() + ", " + shopList.get(position).getAddress_city());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        shopList.clear();
        if (charText.length() == 0) {
            shopList.addAll(arraylist);
        } else {
            for (Shop shop : arraylist) {
                if (shop.getAddress_city().toLowerCase(Locale.getDefault()).contains(charText) ||
                        shop.getAddress_city().toLowerCase(Locale.getDefault()).contains(charText) ||
                        shop.getAddress_street().toLowerCase(Locale.getDefault()).contains(charText) ||
                        shop.getAddress_country().toLowerCase(Locale.getDefault()).contains(charText) ||
                        shop.getAddress_cp().toLowerCase(Locale.getDefault()).contains(charText) ||
                        shop.getName().toLowerCase(Locale.getDefault()).contains(charText)
                        ) {
                    shopList.add(shop);
                }
            }
        }
        notifyDataSetChanged();
    }
}
