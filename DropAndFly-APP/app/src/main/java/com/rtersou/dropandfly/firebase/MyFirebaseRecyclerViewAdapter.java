package com.rtersou.dropandfly.firebase;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.firebase.FirebaseFragment.OnListFragmentInteractionListener;
import com.rtersou.dropandfly.firebase.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFirebaseRecyclerViewAdapter extends RecyclerView.Adapter<MyFirebaseRecyclerViewAdapter.ViewHolder> implements EventListener<QuerySnapshot> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Query query;
    private ListenerRegistration registration;

    public MyFirebaseRecyclerViewAdapter(Query query, OnListFragmentInteractionListener listener) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_firebase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).lastname);
        holder.mContentView.setText(mValues.get(position).firstname);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
            switch (dc.getType()) {
                case ADDED:
                    mValues.add(dc.getNewIndex(), dc.getDocument().toObject(DummyItem.class));
                    notifyItemInserted(dc.getNewIndex());
                    break;
                case MODIFIED:
                    if(dc.getNewIndex() == dc.getOldIndex()) {
                        mValues.set(dc.getOldIndex(), dc.getDocument().toObject(DummyItem.class));
                        notifyItemChanged(dc.getOldIndex());
                    } else {
                        mValues.remove(dc.getOldIndex());
                        mValues.add(dc.getNewIndex(), dc.getDocument().toObject(DummyItem.class));
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
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
