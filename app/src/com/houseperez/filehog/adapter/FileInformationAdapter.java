package com.houseperez.filehog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.houseperez.filehog.R;
import com.houseperez.filehog.activity.FileListFragment;
import com.houseperez.util.FileInformation;
import com.houseperez.util.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jhouse on 4/18/2014.
 */
public class FileInformationAdapter extends ArrayAdapter<FileInformation> {

    private List<FileInformation> hogFiles;

    private TextView txtNameLabel;
    //private TextView txtSizeLabel;
    private TextView txtLastModifiedLabel;
    private TextView txtFolderLabel;

    private TextView txtName;
    private TextView txtSize;
    private TextView txtLastModified;
    private TextView txtFolder;


    public FileInformationAdapter(Context context, int resource, List<FileInformation> hogFiles) {
        super(context, resource, hogFiles);
        this.hogFiles = hogFiles;
    }

    public void setFileInformations(List<FileInformation> hogFiles) {
        this.hogFiles = hogFiles;
        notifyDataSetChanged();
    }

    public void remove(int position) {
        hogFiles.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return hogFiles.size();
    }

    @Override
    public void clear() {
        this.hogFiles = new ArrayList<FileInformation>();
        notifyDataSetChanged();
    }

    @Override
    public FileInformation getItem(int position) {
        return hogFiles.get(position);
    }

    @Override
    public int getPosition(FileInformation item) {
        return hogFiles.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.file_information_adapter, parent, false);
        }

        int lightBlue = Color.parseColor("#1B89CC");
        int darkBlue = getContext().getResources().getColor(R.color.darkBlue);

        int aqua = Color.parseColor("#08FFD8");
        int orange = Color.parseColor("#FF6248");
        int red = Color.parseColor("#CC1B1E");
        int white = Color.WHITE;

        convertView.setBackgroundColor(white);

        txtNameLabel = (TextView) convertView.findViewById(R.id.txtNameLabel);
        //txtSizeLabel = (TextView) convertView.findViewById(R.id.txtSizeLabel);
        txtLastModifiedLabel = (TextView) convertView.findViewById(R.id.txtLastModifiedLabel);
        txtFolderLabel = (TextView) convertView.findViewById(R.id.txtFolderLabel);

        txtName = (TextView) convertView.findViewById(R.id.txtName);
        txtSize = (TextView) convertView.findViewById(R.id.txtSize);
        txtLastModified = (TextView) convertView.findViewById(R.id.txtLastModified);
        txtFolder = (TextView) convertView.findViewById(R.id.txtFolder);

        if (position % 2 == 0) {
            setBackgroundColor(darkBlue, white);
            setTextColor(white, orange);
        } else {
            setBackgroundColor(orange, white);
            setTextColor(white, darkBlue);
        }

        txtName.setText("");
        txtSize.setText("");
        txtLastModified.setText("");
        txtFolder.setText("");

        FileInformation fileInformation = hogFiles.get(position);
        if (fileInformation != null) {
            txtName.setText(fileInformation.getName());
            String size = Utility.getCorrectByteSize(fileInformation.getSize());
            txtSize.setText(size);
            Date date = new Date(fileInformation.getLastModified());
            txtLastModified.setText(date.toString());
            txtFolder.setText(fileInformation.getFolder());
        }

        return convertView;
    }

    private void setBackgroundColor(int labelColor, int valueColor) {
        txtNameLabel.setBackgroundColor(labelColor);
        //txtSizeLabel.setBackgroundColor(labelColor);
        txtLastModifiedLabel.setBackgroundColor(labelColor);
        txtFolderLabel.setBackgroundColor(labelColor);

        txtName.setBackgroundColor(valueColor);
        txtSize.setBackgroundColor(valueColor);
        txtLastModified.setBackgroundColor(valueColor);
        txtFolder.setBackgroundColor(valueColor);
    }

    private void setTextColor(int labelColor, int valueColor) {
        txtNameLabel.setTextColor(labelColor);
        //txtSizeLabel.setTextColor(labelColor);
        txtLastModifiedLabel.setTextColor(labelColor);
        txtFolderLabel.setTextColor(labelColor);

        txtName.setTextColor(valueColor);
        txtSize.setTextColor(valueColor);
        txtLastModified.setTextColor(valueColor);
        txtFolder.setTextColor(valueColor);
    }
}