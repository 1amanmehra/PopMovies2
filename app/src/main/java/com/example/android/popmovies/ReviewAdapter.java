package com.example.android.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aman on 18/7/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.NumberViewHolder> {

    private static final String TAG = VideoAdapter.class.getSimpleName();
    private final ListItemClickListener mClickHandler;

    private ArrayList<String> address;
    private Context mContext;

    public interface ListItemClickListener {
        void onClick(int itemclickIndex);
    }

    public ReviewAdapter(ListItemClickListener clickHandler, Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        String review = address.get(position);
        String pos = Integer.toString(position + 1);
        holder.bind(review, pos);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        if (null == address) return 0;
        return address.size();
    }

    /**
     * Cache of the children views for a list item.
     */
    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView reviewTextView;
        TextView reviewNoTextView;

        public NumberViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            reviewTextView = (TextView) itemView.findViewById(R.id.review);
            reviewNoTextView = (TextView) itemView.findViewById(R.id.review_no);
        }


        void bind(String imagePath, String position) {
            if (!imagePath.isEmpty()) {
                reviewTextView.setText(imagePath);
                reviewNoTextView.setText(position);
            }
        }

        public void onClick(final View view) {
            int pos = getAdapterPosition();
            mClickHandler.onClick(pos);
        }

    }

    public void setData(ArrayList<String> moviesData) {
        address = moviesData;
        notifyDataSetChanged();
    }
}
