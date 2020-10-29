package com.hitsz.eatut.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.TypedArrayUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.WindowActivity;
import com.hitsz.eatut.database.CanteenInfo;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.lang.String;

//餐厅适配器

/**
 * @author lixiang
 */
public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.ViewHolder> {
    private List<canteen> mCanteenList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView canteenImage;
        TextView canteenName;
        TextView canteenScore;
        View canteenview;
        public ViewHolder(View view){
            super(view);
            canteenview=view;
            canteenImage=view.findViewById(R.id.canteen_image);
            canteenName=view.findViewById(R.id.canteen_name);
            canteenScore=view.findViewById(R.id.canteen_score);
        }
    }
    public CanteenAdapter(List<canteen> canteenList){
        mCanteenList=canteenList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.canteen,parent,false);
        final Calendar calendar=Calendar.getInstance();
        final ViewHolder holder=new ViewHolder(view);
        holder.canteenview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position=holder.getAdapterPosition();
                canteen can=mCanteenList.get(position);
                Intent intent = new Intent(v.getContext(), WindowActivity.class);
                intent.putExtra("extra_data", can.getName());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        canteen can = mCanteenList.get(position);

        holder.canteenName.setText(can.getName());
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String score = decimalFormat.format(can.getScore()) + "分";
        holder.canteenScore.setText(score);

        /*
        Lily
        判断图片ID是否为0，不为0用drawable图片，否则用bitmap图片
        */
        if(can.getImageID() == 0){
            byte[] imagess = can.getCanteenshot();
            Bitmap bms = BitmapFactory.decodeByteArray(imagess, 0, imagess.length);
            holder.canteenImage.setImageBitmap(bms);
        }else{
            holder.canteenImage.setImageResource(can.getImageID());
        }


//        if(position<3){
//            holder.canteenImage.setImageResource(can.getImageID());
//        }else{
//            byte[] imagess = can.getCanteenshot();
//            Bitmap bms = BitmapFactory.decodeByteArray(imagess, 0, imagess.length);
//            holder.canteenImage.setImageBitmap(bms);
//
//        }

    }

    @Override
    public int getItemCount(){
        return mCanteenList.size();
    }
}
