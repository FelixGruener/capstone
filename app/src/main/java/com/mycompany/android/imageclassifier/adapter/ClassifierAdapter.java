package com.mycompany.android.imageclassifier.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mycompany.android.imageclassifier.R;
import com.mycompany.android.imageclassifier.model.Classification;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.BaseColumns._ID;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_IMAGE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LABEL_DESC;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LANDMARK_DESC;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LATITUDE;
import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.COLUMN_LONGITUDE;

/**
 * Created by delaroy on 9/5/18.
 */

public class ClassifierAdapter extends RecyclerView.Adapter<ClassifierAdapter.ClassifierViewHolder> {

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;

    private Cursor cursor;
    private Context mContext;
    private final LayoutInflater inflater;


    public ClassifierAdapter(Context context, ItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mItemClickListener = listener;
    }

    @Override
    public ClassifierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.classifier_item, parent, false);

        return new ClassifierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassifierViewHolder holder, int position) {

        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                int indexid = cursor.getColumnIndex(_ID);
                int landmark_desc = cursor.getColumnIndex(COLUMN_LANDMARK_DESC);
                int latitude = cursor.getColumnIndex(COLUMN_LATITUDE);
                int longitude = cursor.getColumnIndex(COLUMN_LONGITUDE);
                int label_desc = cursor.getColumnIndex(COLUMN_LABEL_DESC);
                int image = cursor.getColumnIndex(COLUMN_IMAGE);

                int id = cursor.getInt(indexid);
                String landmarkDesc = cursor.getString(landmark_desc);
                Double mlatitude = cursor.getDouble(latitude);
                Double mlongitude = cursor.getDouble(longitude);
                String labelDesc = cursor.getString(label_desc);
                String mImage = cursor.getString(image);

                holder.itemView.setTag(id);
                holder.label.setText(labelDesc);
                holder.location.setText(landmarkDesc);
                Glide.with(mContext)
                        .load(mImage)
                        .into(holder.imageView);


            }
        }
    }

    public Cursor setData(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (cursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = cursor;
        this.cursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return -1;
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }

    public Classification getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Invalid item position requested");
        }
        return new Classification(cursor);
    }

    private void postItemClick(ClassifierViewHolder holder) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
        }
    }


    // Inner class for creating ViewHolders
    public class ClassifierViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView label;
        TextView location;
        CircleImageView imageView;
        public RelativeLayout viewBackground, viewForeground;

        public ClassifierViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            location = itemView.findViewById(R.id.location);
            imageView = itemView.findViewById(R.id.image);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            postItemClick(this);
        }
    }
}
