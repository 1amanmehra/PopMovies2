package com.example.android.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import static android.widget.ImageView.ScaleType.FIT_XY;

//Class for RecyclerView.

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.NumberViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private final ListItemClickListener mClickHandler;

    private ArrayList<Movies> mPosterItems;
    private Context mContext;

    public interface ListItemClickListener {
        void onClick(int itemclickIndex);
    }

    public MovieAdapter(ArrayList posterList, ListItemClickListener clickHandler, Context context) {
        mPosterItems = posterList;
        mClickHandler = clickHandler;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
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
        Log.d(TAG, "#" + position);
        Movies movieDetail;
        movieDetail = mPosterItems.get(position);
        holder.bind(movieDetail.getMoviePoster());
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return mPosterItems.size();
    }

    /**
     * Cache of the children views for a list item.
     */
    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterImageView;

        public NumberViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            posterImageView = (ImageView) itemView.findViewById(R.id.poster_view);
            posterImageView.setScaleType(FIT_XY);
        }


        void bind(String imagePath) {
            URL newUrl = NetworkUtils.buildImageUrl(imagePath);
            Picasso.with(getContext()).load(newUrl.toString()).into(posterImageView);
        }

        public void onClick(final View view) {
            int pos = getAdapterPosition();
            mClickHandler.onClick(pos);
        }


    }
}
