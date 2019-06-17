package com.example.acer.kerusakanbangunan;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class KondisiKolomListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Model2> kolomList;

    public KondisiKolomListAdapter(Context context, int layout, ArrayList<Model2> kolomList) {
        this.context = context;
        this.layout = layout;
        this.kolomList = kolomList;
    }
    @Override
    public int getCount() {
        return kolomList.size();
    }

    @Override
    public Object getItem(int position) {
        return kolomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView strukturTxt;
        TextView textKondisi;
        ImageButton addPoto;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        KondisiKolomListAdapter.ViewHolder holder = new KondisiKolomListAdapter.ViewHolder();

        if (row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.textKondisi = row.findViewById(R.id.keterangan_level);
            holder.strukturTxt = row.findViewById(R.id.list_txt);
            holder.addPoto = row.findViewById(R.id.add_foto);


//            holder.imageView = row.findViewById(R.id.poto_bangunan);
            row.setTag(holder);
        }
        else {
            holder = (KondisiKolomListAdapter.ViewHolder)row.getTag();
        }

        Model2 model = kolomList.get(position);
        int a =position+1;
        holder.strukturTxt.setText(a+"");
        System.out.println("ini dia aaaa "+model.getLevel());
        if(model.getLevel()==0){
            holder.textKondisi.setText("Tidak rusak");
        }
        if(model.getLevel()==1){
            holder.textKondisi.setText("Rusak Ringan");
        }
        if(model.getLevel()==2){
            holder.textKondisi.setText("Rusak Sedang");
        }
        if(model.getLevel()==3){
            holder.textKondisi.setText("Rusak Berat");
        }

//        byte[] recordImage = model.getPoto();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage,0,recordImage.length);
//        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
