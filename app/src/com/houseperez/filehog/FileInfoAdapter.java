package com.houseperez.filehog;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.houseperez.util.FileInformation;
import com.houseperez.util.Utility;

import java.util.Date;
import java.util.List;

import static com.houseperez.filehog.R.color.darkBlue;

/**
 * Created by jhouse on 6/16/17.
 */

public class FileInfoAdapter extends RecyclerView.Adapter<FileInfoAdapter.ViewHolder> {

    private static final int lightBlue = Color.parseColor("#1B89CC");
    //private static final int darkBlue = getContext().getResources().getColor(R.color.darkBlue);
    private static final int aqua = Color.parseColor("#08FFD8");
    private static final int orange = Color.parseColor("#FF6248");
    private static final int red = Color.parseColor("#CC1B1E");
    private static final int white = Color.WHITE;

    private List<FileInformation> hogFiles;

    public FileInfoAdapter(List<FileInformation> hogFiles) {
        this.hogFiles = hogFiles;
    }

    @Override
    public FileInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_information_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FileInfoAdapter.ViewHolder holder, int position) {

        holder.view.setBackgroundColor(white);

        if (position % 2 == 0) {
            setBackgroundColor(holder, darkBlue, white);
            setTextColor(holder, white, orange);
        } else {
            setBackgroundColor(holder, orange, white);
            setTextColor(holder, white, darkBlue);
        }

        holder.txtName.setText("");
        holder.txtSize.setText("");
        holder.txtLastModified.setText("");
        holder.txtFolder.setText("");

        FileInformation fileInformation = hogFiles.get(position);
        if (fileInformation != null) {
            holder.txtName.setText(fileInformation.getName());
            String size = Utility.getCorrectByteSize(fileInformation.getSize());
            holder.txtSize.setText(size);
            Date date = new Date(fileInformation.getLastModified());
            holder.txtLastModified.setText(date.toString());
            holder.txtFolder.setText(fileInformation.getFolder());
        }
    }

    @Override
    public int getItemCount() {
        return hogFiles.size();
    }

    private void setBackgroundColor(ViewHolder viewHolder, int labelColor, int valueColor) {
        viewHolder.txtNameLabel.setBackgroundColor(labelColor);
        viewHolder.txtLastModifiedLabel.setBackgroundColor(labelColor);
        viewHolder.txtFolderLabel.setBackgroundColor(labelColor);
        viewHolder.txtName.setBackgroundColor(valueColor);
        viewHolder.txtSize.setBackgroundColor(valueColor);
        viewHolder.txtLastModified.setBackgroundColor(valueColor);
        viewHolder.txtFolder.setBackgroundColor(valueColor);
    }

    private void setTextColor(ViewHolder viewHolder, int labelColor, int valueColor) {
        viewHolder.txtNameLabel.setTextColor(labelColor);
        viewHolder.txtLastModifiedLabel.setTextColor(labelColor);
        viewHolder.txtFolderLabel.setTextColor(labelColor);
        viewHolder.txtName.setTextColor(valueColor);
        viewHolder.txtSize.setTextColor(valueColor);
        viewHolder.txtLastModified.setTextColor(valueColor);
        viewHolder.txtFolder.setTextColor(valueColor);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView txtNameLabel;
        private TextView txtLastModifiedLabel;
        private TextView txtFolderLabel;
        private TextView txtName;
        private TextView txtSize;
        private TextView txtLastModified;
        private TextView txtFolder;

        public ViewHolder(View convertView) {
            super(convertView);

            view = convertView;
            txtNameLabel = (TextView) convertView.findViewById(R.id.txtNameLabel);
            txtLastModifiedLabel = (TextView) convertView.findViewById(R.id.txtLastModifiedLabel);
            txtFolderLabel = (TextView) convertView.findViewById(R.id.txtFolderLabel);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtSize = (TextView) convertView.findViewById(R.id.txtSize);
            txtLastModified = (TextView) convertView.findViewById(R.id.txtLastModified);
            txtFolder = (TextView) convertView.findViewById(R.id.txtFolder);
        }
    }
}
