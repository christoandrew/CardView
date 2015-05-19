package com.iconasystems.christo.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.christo.cardview.R;

/**
 * Created by Christo on 1/12/2015.
 */
public class GridMenuAdapter  extends RecyclerView.Adapter<GridMenuAdapter.SimpleViewHolder>{
    private Context context;
    private String[] mMenuItems;
    private int[] mMenuImages = {R.drawable.ic_action_assessment, R.drawable.ic_action_trending_up, R.drawable.ic_social_notifications,
        R.drawable.ic_action_assignment, R.drawable.ic_image_portrait, R.drawable.ic_image_photo_camera,
        R.drawable.ic_action_submit_event, R.drawable.ic_action_grade, R.drawable.ic_action_info
    };

    public GridMenuAdapter(Context context) {
        this.context = context;
        this.mMenuItems = this.context.getResources().getStringArray(R.array.menu_item_names);
        //this.mMenuImages = this.context.getResources().getStringArray(R.array.menu_item_icons);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.grid_menu_item, parent, false);
        return  new SimpleViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.mItemName.setText(mMenuItems[position]);
        holder.mItemImage.setImageResource(mMenuImages[position]);
    }

    @Override
    public int getItemCount() {
        return mMenuImages.length;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder{
        final ImageView mItemImage;
        final TextView mItemName;
        public SimpleViewHolder(View itemView) {
            super(itemView);
            mItemImage = (ImageView) itemView.findViewById(R.id.item_image);
            mItemName = (TextView) itemView.findViewById(R.id.item_name);
        }
    }

}
