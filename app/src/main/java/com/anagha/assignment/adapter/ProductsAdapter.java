package com.anagha.assignment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.anagha.assignment.R;
import com.anagha.assignment.models.Row;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    List<Row> data;
    private DisplayImageOptions options;

    public ProductsAdapter(List<Row> productReviewsListForAdapter) {
        data = productReviewsListForAdapter;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher_foreground)
                .showImageForEmptyUri(R.drawable.ic_launcher_foreground)
                .showImageOnFail(R.drawable.ic_launcher_foreground)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductsViewHolder myViewHolder;
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_row, parent, false);
        myViewHolder = new ProductsViewHolder(itemLayoutView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder myHolder, int position) {
        ProductsViewHolder myViewHolderview = myHolder;
        if (getItem(position).getTitle() != null) {
            myViewHolderview.listRowNameTV.setText(getItem(position).getTitle());
        } else {
            myViewHolderview.listRowNameTV.setText("No Title");
        }
        if (getItem(position).getDescription() != null) {
            myViewHolderview.listRowDescTV.setText(Html.fromHtml(getItem(position).getDescription()));
        } else {
            myViewHolderview.listRowDescTV.setText("No Description");
        }
        if (getItem(position).getImageHref() != null) {
            try {
                ImageLoader.getInstance().displayImage(getItem(position).getImageHref(), myViewHolderview.listRowIM, options);
            } catch (NullPointerException npe) {
                ImageLoader.getInstance().displayImage(String.valueOf(R.drawable.ic_launcher_foreground), myViewHolderview.listRowIM, options);            }
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private Row getItem(int position) {
        return data.get(position);
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder {
        TextView listRowNameTV;
        TextView listRowDescTV;
        ImageView listRowIM;

        public ProductsViewHolder(View itemView) {
            super(itemView);
            listRowNameTV = (TextView) itemView.findViewById(R.id.list_row_nameTV);
            listRowDescTV = (TextView) itemView.findViewById(R.id.list_row_descTV);
            listRowIM = (ImageView) itemView.findViewById(R.id.list_row_IM);
        }
    }
}
