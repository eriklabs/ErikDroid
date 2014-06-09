package com.erik.erikdroid.ui.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.erik.erikdroid.R;
import com.erik.erikdroid.data.ErikDataModel;
import com.erik.erikdroid.utils.AppUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ErikAdapter extends BaseAdapter {

    public Context context;
    private LayoutInflater mInflater;

    ArrayList<ErikDataModel> listData;

    public ErikAdapter(Context context, ArrayList<ErikDataModel> data) {
        this.context = context;
        mInflater = LayoutInflater.from(this.context);
        this.listData = data;
    }

    public void setData(ArrayList<ErikDataModel> newData) {
        this.listData = newData;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public ErikDataModel getItem(int arg0) {
        return listData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.erik_listrow, null);

            holder = new ViewHolder();

            holder.image = (ImageView) convertView.findViewById(R.id.erik_list_row_image);
            holder.title = (TextView) convertView.findViewById(R.id.erik_list_row_title);
            holder.loading = (ProgressBar) convertView.findViewById(R.id.erik_list_row_progress);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ErikDataModel item = getItem(position);

        holder.title.setText(AppUtils.convertToHumanReadable(item.Title));

        if (item.MainImage != null) {
            holder.image.setVisibility(View.INVISIBLE);
            holder.loading.setVisibility(View.VISIBLE);
            Picasso.with(context).load(item.MainImage).placeholder(null).into(holder.image, new ImageLoadingCallback(holder.image, holder.loading));
        }

        return convertView;
    }

    private final class ImageLoadingCallback implements Callback {

        private ProgressBar loading;
        private ImageView imageView;

        public ImageLoadingCallback(ImageView iv, ProgressBar loading) {
            this.imageView = iv;
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

    static class ViewHolder {
        TextView title;
        ImageView image;
        ProgressBar loading;
    }

}
