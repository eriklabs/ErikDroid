package com.erik.erikdroid.ui.adapters;

import uk.co.senab.photoview.PhotoView;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.erik.erikdroid.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ErikDetailPagerAdapter extends PagerAdapter {

    private String[] images;
    private Context context;

    public ErikDetailPagerAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        ViewGroup v = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.erik_detail_pager_item, null);
        PhotoView imageView = (PhotoView) v.findViewById(R.id.pager_item_image);
        ProgressBar loading = (ProgressBar) v.findViewById(R.id.pager_item_progress);

        imageView.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
        Picasso.with(context).load(images[position]).placeholder(null).into(imageView, new ImageLoadingCallback(imageView, loading));

        ((ViewPager) collection).addView(v, 0);

        return v;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    private final class ImageLoadingCallback implements Callback {
        private PhotoView imageView;
        private ProgressBar loading;

        public ImageLoadingCallback(PhotoView imageView, ProgressBar loading) {
            this.imageView = imageView;
            this.loading = loading;
        }

        @Override
        public void onSuccess() {
            loading.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError() {
            loading.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }
    }
}
