package example.localcloud.localcloud.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.ArrayList;

import example.localcloud.localcloud.R;
import example.localcloud.localcloud.models.FolderModel;


public class FilesAdapter extends ArrayAdapter<FolderModel> {

    private Context context;
    private ArrayList<FolderModel> folderModels = new ArrayList<>();
    private int position;


    public FilesAdapter(Context context, ArrayList<FolderModel> folderModels, int position) {
        super(context, R.layout.adapter_photosfolder, folderModels);
        this.folderModels = folderModels;
        this.context = context;
        this.position = position;


    }

    @Override
    public int getCount() {
        return folderModels.get(position).getFiles().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (folderModels.get(position).getFiles().size() > 0) {
            return folderModels.get(position).getFiles().size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_photosfolder, parent, false);
            viewHolder.tv_foldern = (TextView) convertView.findViewById(R.id.tv_folder);
            viewHolder.tv_foldersize = (TextView) convertView.findViewById(R.id.tv_folder2);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_foldern.setVisibility(View.GONE);
        viewHolder.tv_foldersize.setVisibility(View.GONE);


        Glide.with(context).load("file://" + folderModels.get(this.position).getFiles().get(position).getPath())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(viewHolder.iv_image);


        return convertView;

    }

    private static class ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image;


    }
}