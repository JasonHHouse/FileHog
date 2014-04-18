package com.houseperez.filehog.adapter;

import android.content.Context;
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

    public FileInformationAdapter(Context context, int resource, List<FileInformation> fileInformations) {
        super(context, resource, fileInformations);
        this.fileInformations = fileInformations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.file_information_adapter, parent, false);
        }

        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        TextView txtSize = (TextView) convertView.findViewById(R.id.txtSize);
        TextView txtLastModified = (TextView) convertView.findViewById(R.id.txtLastModified);
        TextView txtFolder = (TextView) convertView.findViewById(R.id.txtFolder);

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
}
