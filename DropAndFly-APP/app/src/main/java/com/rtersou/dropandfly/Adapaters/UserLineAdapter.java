package com.rtersou.dropandfly.Adapaters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.helper.Helper;
import com.rtersou.dropandfly.models.Reservation;

import java.util.ArrayList;

public class UserLineAdapter extends ArrayAdapter<Reservation> {

    private UserLineAdapter.ViewHolder viewHolder;

    private static class ViewHolder {
        private LinearLayout itemView;
    }

    public UserLineAdapter(Context context, int textViewResourceId, ArrayList<Reservation> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.activity_history_user_line, parent, false);

            viewHolder = new UserLineAdapter.ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.hist_user_line);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserLineAdapter.ViewHolder) convertView.getTag();
        }

        Reservation item = getItem(position);
        if (item != null) {

            final TextView name = viewHolder.itemView.findViewById(R.id.hist_user_line_name);

            if(Helper.isMerchant){
                db.collection("users")
                        .document(item.getUser_id())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        name.setText(document.get("firstname").toString() + " " + document.get("lastname").toString());
                                        document.get("name").toString();
                                    } else {
                                    }
                                } else {
                                }
                            }
                        });
            }
            else {
                db.collection("shops")
                        .document(item.getShop_id())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        name.setText(document.get("name").toString());
                                        document.get("name").toString();
                                    } else {
                                    }
                                } else {
                                }
                            }
                        });
                name.setText(item.getShop_id());
            }


            TextView date_dep = viewHolder.itemView.findViewById(R.id.hist_user_line_date_dep);
            date_dep.setText(item.getDate_start());

            TextView h_dep = viewHolder.itemView.findViewById(R.id.hist_user_line_h_dep);
            h_dep.setText(item.getH_start());

            TextView date_ret = viewHolder.itemView.findViewById(R.id.hist_user_line_date_ret);
            date_ret.setText(item.getDate_end());

            TextView h_ret = viewHolder.itemView.findViewById(R.id.hist_user_line_h_ret);
            h_ret.setText(item.getH_end());

            TextView nb_lug = viewHolder.itemView.findViewById(R.id.hist_user_line_nb_lug);
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
