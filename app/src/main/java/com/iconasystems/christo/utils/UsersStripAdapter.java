package com.iconasystems.christo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.iconasystems.christo.Constants;
import com.iconasystems.christo.cardview.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Christo on 12/5/2014.
 */
public class UsersStripAdapter extends RecyclerView.Adapter<UsersStripAdapter.SimpleViewHolder> {
    private final ArrayList<HashMap<String, String>> dataList;
    private final Context mContext;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private static final String TAG_BAR_IMAGE = "bar_image";
    private int resId;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_error)
            .showImageForEmptyUri(R.drawable.ic_error)
            .showImageOnFail(R.drawable.ic_error)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new CircleImageDisplayer())
            .considerExifParams(true)
            .build();


    public UsersStripAdapter(ArrayList<HashMap<String, String>> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
        this.resId = resId;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mPhotoStrip;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            mPhotoStrip = (ImageView) itemView.findViewById(R.id.photo_strip_image);
        }

        public void setData(int position) {
            HashMap<String, String> map;
            map = dataList.get(position);
            String image_url = map.get(Constants.NameConstants.TAG_PROFILE_PIC);

            ImageLoader.getInstance().displayImage(image_url, mPhotoStrip, options, animateFirstListener);

            /*Bitmap bitmap = ImageLoader.getInstance().loadImageSync(image_url);

            CircleDrawable circleDrawable = new CircleDrawable(bitmap);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                mPhotoStrip.setBackground(circleDrawable);
            else
                mPhotoStrip.setBackgroundDrawable(circleDrawable); */


            int mElevationCircle = dpToPx(10);
            int mSizeCircle = dpToPx(200);
            final int finalSizeCircle = mSizeCircle;
            ViewCompat.setElevation(mPhotoStrip, mElevationCircle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                mPhotoStrip.setOutlineProvider(
                        new ViewOutlineProvider(){
                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void getOutline(View view, Outline outline) {
                                outline.setOval(0, 0, finalSizeCircle, finalSizeCircle);
                            }
                        });
                mPhotoStrip.setClipToOutline(true);

           }
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_photo_strip_item, viewGroup, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int position) {
        simpleViewHolder.setData(position);
    }

    public int dpToPx(int dp) {
        Resources r = mContext.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
        return px;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }


}
