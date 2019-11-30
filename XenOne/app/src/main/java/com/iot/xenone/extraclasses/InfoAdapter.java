package com.iot.xenone.extraclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iot.xenone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfoAdapter extends ArrayAdapter<HashMap> {

    private Context context;
    private List<HashMap> itemList;

    public InfoAdapter(Context context, ArrayList<HashMap> list){
        super(context, 0, list);
        this.context = context;
        this.itemList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
        }

        HashMap currentItem = new HashMap(itemList.get(position));
        byte[] decodedString = Base64.decode(currentItem.get("image").toString(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        ImageView imageView = listItem.findViewById(R.id.image);
        imageView.setImageBitmap(decodedByte);

        TextView cords = listItem.findViewById(R.id.cords);
        String cordsString = "Cords: " + currentItem.get("cords").toString();
        cords.setText(cordsString);

        TextView filling = listItem.findViewById(R.id.filling);
        String filString = "Filling: " + currentItem.get("filling").toString();
        filling.setText(filString);

        TextView export = listItem.findViewById(R.id.export);
        String exportString = "Export: " + currentItem.get("export").toString();
        export.setText(exportString);

        return listItem;
    }
}
