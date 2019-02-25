package com.rtersou.dropandfly.activities.user.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.models.Shop;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    private Context context;
    private Shop shop;

    public CustomInfoWindow(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_info_window, null);

        TextView name_shop = view.findViewById(R.id.name_shop);
        TextView description_shop = view.findViewById(R.id.description_shop);

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        shop = infoWindowData.getShop();
        name_shop.setText(infoWindowData.getTitle());
        description_shop.setText(infoWindowData.getDescription());


        return view;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(context, com.rtersou.dropandfly.activities.user.reservation.ReservationActivity.class);
        intent.putExtra("shop", shop);
        context.startActivity(intent);
    }

}
