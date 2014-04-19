package com.houseperez.filehog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.houseperez.filehog.R;
import com.houseperez.util.FileInformation;
import com.houseperez.util.Utility;

import java.util.Date;
import java.util.List;

/**
 * Created by jhouse on 4/18/2014.
 */
public class FileInformationAdapter extends ArrayAdapter<FileInformation> {

    private List<FileInformation> fileInformations;

    private TextView txtNameLabel;
    private TextView txtSizeLabel;
    private TextView txtLastModifiedLabel;
    private TextView txtFolderLabel;

    private TextView txtName;
    private TextView txtSize;
    private TextView txtLastModified;
    private TextView txtFolder;


    public FileInformationAdapter(Context context, int resource, List<FileInformation> fileInformations) {
        super(context, resource, fileInformations);
        this.fileInformations = fileInformations;
    }

    public void setFileInformations(List<FileInformation> fileInformations) {
        this.fileInformations = fileInformations;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fileInformations.size();
    }

    @Override
    public FileInformation getItem(int position) {
        return fileInformations.get(position);
    }

    @Override
    public int getPosition(FileInformation item) {
        return fileInformations.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.file_information_adapter, parent, false);
        }

        txtNameLabel = (TextView) convertView.findViewById(R.id.txtNameLabel);
        txtSizeLabel = (TextView) convertView.findViewById(R.id.txtSizeLabel);
        txtLastModifiedLabel = (TextView) convertView.findViewById(R.id.txtLastModifiedLabel);
        txtFolderLabel = (TextView) convertView.findViewById(R.id.txtFolderLabel);

        txtName = (TextView) convertView.findViewById(R.id.txtName);
        txtSize = (TextView) convertView.findViewById(R.id.txtSize);
        txtLastModified = (TextView) convertView.findViewById(R.id.txtLastModified);
        txtFolder = (TextView) convertView.findViewById(R.id.txtFolder);

        int lightBlue = Color.parseColor("#1B89CC");
        int darkBlue = Color.parseColor("#427899");
        int aqua = Color.parseColor("#08FFD8");
        int orange = Color.parseColor("#FF6248");
        int red = Color.parseColor("#CC1B1E");
        int white = Color.WHITE;

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

        FileInformation fileInformation = fileInformations.get(position);
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
        txtSizeLabel.setBackgroundColor(labelColor);
        txtLastModifiedLabel.setBackgroundColor(labelColor);
        txtFolderLabel.setBackgroundColor(labelColor);

        txtName.setBackgroundColor(valueColor);
        txtSize.setBackgroundColor(valueColor);
        txtLastModified.setBackgroundColor(valueColor);
        txtFolder.setBackgroundColor(valueColor);
    }

    private void setTextColor(int labelColor, int valueColor) {
        txtNameLabel.setTextColor(labelColor);
        txtSizeLabel.setTextColor(labelColor);
        txtLastModifiedLabel.setTextColor(labelColor);
        txtFolderLabel.setTextColor(labelColor);

        txtName.setTextColor(valueColor);
        txtSize.setTextColor(valueColor);
        txtLastModified.setTextColor(valueColor);
        txtFolder.setTextColor(valueColor);
    }
}
