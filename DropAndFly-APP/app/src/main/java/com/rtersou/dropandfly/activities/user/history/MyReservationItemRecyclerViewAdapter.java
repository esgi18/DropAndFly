package com.rtersou.dropandfly.activities.user.history;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.models.Reservation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MyReservationItemRecyclerViewAdapter extends RecyclerView.Adapter<MyReservationItemRecyclerViewAdapter.ViewHolder> implements EventListener<QuerySnapshot> {

    private final List<com.rtersou.dropandfly.models.Reservation> mValues;
    private final ReservationItemFragment.OnListFragmentInteractionListener mListener;
    private Query query;
    private ListenerRegistration registration;

    public MyReservationItemRecyclerViewAdapter(Query query, ReservationItemFragment.OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
        this.query = query;
    }


    public void startListening() {
        if( query != null && registration == null ) {
            registration = query.addSnapshotListener(this);
        }
    }

    public void stopListening() {
        if( registration != null ) {
            registration.remove();
            registration = null;
        }
        mValues.clear();
        notifyDataSetChanged();
    }

    @Override
    public MyReservationItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reservationitem, parent, false);
        return new MyReservationItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        FirebaseFirestore.getInstance()
                .collection("shops")
                .document(mValues.get(position).getShop_id())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        holder.mName.setText(documentSnapshot.get("name").toString());
                    }
                });
        holder.mDateStart.setText(mValues.get(position).getDate_start());
        holder.mDateEnd.setText(mValues.get(position).getDate_end());
        holder.mTimeStart.setText(mValues.get(position).getH_start());
        holder.mTimeEnd.setText(mValues.get(position).getH_end());
        holder.mNbLuggage.setText(mValues.get(position).getNb_luggage() + "baggages");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    Reservation reservation = new Reservation(mValues.get(position).getId(), mValues.get(position).getDate_start(), mValues.get(position).getDate_end(), mValues.get(position).getH_start(), mValues.get(position).getH_end(), mValues.get(position).getNb_luggage(), mValues.get(position).getStatut(), mValues.get(position).getPrice(), mValues.get(position).getUser_id(), mValues.get(position).getShop_id());
                    mListener.onListFragmentInteraction(holder.mItem);
                    Intent DetailReservationActivity = new Intent(v.getContext(), com.rtersou.dropandfly.activities.common.detail_reservation.DetailReservationActivity.class);
                    DetailReservationActivity.putExtra("reservation", reservation);
                    v.getContext().startActivity(DetailReservationActivity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if( e != null ) {
            // TODO : handle exception
            return;
        }
        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            Reservation reservation = new Reservation(dc.getDocument().getId(),
                    dc.getDocument().get("date_start").toString(),
                    dc.getDocument().get("date_end").toString(),
                    dc.getDocument().get("h_start").toString(),
                    dc.getDocument().get("h_end").toString(),
                    Integer.parseInt(dc.getDocument().get("nb_luggage").toString()),
                    Integer.parseInt(dc.getDocument().get("statut").toString()),
                    Integer.parseInt(dc.getDocument().get("price").toString()),
                    dc.getDocument().get("user_id").toString(),
                    dc.getDocument().get("shop_id").toString());
            switch (dc.getType()) {
                case ADDED:
                    mValues.add(dc.getNewIndex(), reservation);
                    notifyItemInserted(dc.getNewIndex());
                    break;
                case MODIFIED:
                    if(dc.getNewIndex() == dc.getOldIndex()) {
                        mValues.set(dc.getOldIndex(), reservation);
                        notifyItemChanged(dc.getOldIndex());
                    } else {
                        mValues.remove(dc.getOldIndex());
                        mValues.add(dc.getNewIndex(), reservation);
                        notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                    }
                    break;
                case REMOVED:
                    mValues.remove(dc.getOldIndex());
                    notifyItemRemoved(dc.getOldIndex());
                    break;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mDateStart;
        public final TextView mDateEnd;
        public final TextView mTimeStart;
        public final TextView mTimeEnd;
        public final TextView mNbLuggage;

        public com.rtersou.dropandfly.models.Reservation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            this.mName = (TextView) view.findViewById(R.id.name_shop_res);
            this.mDateStart = (TextView) view.findViewById(R.id.date_start_res);
            this.mDateEnd = (TextView) view.findViewById(R.id.date_end_res);
            this.mTimeStart = (TextView) view.findViewById(R.id.time_start_res);
            this.mTimeEnd = (TextView) view.findViewById(R.id.time_end_res);
            this.mNbLuggage = (TextView) view.findViewById(R.id.nb_luggage_res);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
