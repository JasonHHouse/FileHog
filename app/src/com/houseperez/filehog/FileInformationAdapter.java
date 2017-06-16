package com.houseperez.filehog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.houseperez.filehog.util.FileInformation;
import com.houseperez.filehog.util.Utility;

import java.util.List;

/**
 * Created by jhouse on 6/16/17.
 */

public class FileInformationAdapter extends RecyclerView.Adapter<FileInformationAdapter.ViewHolder> {

    private List<FileInformation> hogFiles;

    public FileInformationAdapter(List<FileInformation> hogFiles) {
        this.hogFiles = hogFiles;
    }

    @Override
    public FileInformationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_information_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FileInformationAdapter.ViewHolder holder, int position) {
        FileInformation fileInformation = hogFiles.get(position);
        if (fileInformation != null) {
            holder.txtName.setText(fileInformation.getName());
            String size = Utility.getCorrectByteSize(fileInformation.getSize());
            holder.txtSize.setText(size);
        } else {
            holder.txtName.setText("");
            holder.txtSize.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return hogFiles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtSize;

        ViewHolder(View convertView) {
            super(convertView);

            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtSize = (TextView) convertView.findViewById(R.id.txtSize);
        }
    }
}
