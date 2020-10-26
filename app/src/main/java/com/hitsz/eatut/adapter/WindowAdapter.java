package com.hitsz.eatut.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.DishActivity;
import com.hitsz.eatut.R;

import java.text.DecimalFormat;
import java.util.List;
import java.lang.String;

//档口适配器

/**
 * @author lixiang，Lily
 */
public class WindowAdapter extends RecyclerView.Adapter<WindowAdapter.ViewHolder> {
    private List<window> mWindowList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView windowImage;
        TextView windowName;
        TextView windowScore;
        View windowview;
        public ViewHolder(View view){
            super(view);
            windowview=view;
            windowImage=view.findViewById(R.id.window_image);
            windowName=view.findViewById(R.id.window_name);
            windowScore=view.findViewById(R.id.window_score);
        }
    }
    public WindowAdapter(List<window> windowList){
        mWindowList=windowList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.window,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.windowview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position=holder.getAdapterPosition();
                window win=mWindowList.get(position);
                Intent intent = new Intent(v.getContext(), DishActivity.class);
                intent.putExtra("extra_data", win.getName());
                v.getContext().startActivity(intent);
            }
        });

        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        window win=mWindowList.get(position);
        //holder.windowImage.setImageResource(win.getImageID());
        holder.windowName.setText(win.getName());
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String score = decimalFormat.format(win.getScore());
        holder.windowScore.setText(score);

        /*
        Lily
        判断图片ID是否为0，不为0用drawable图片，否则用bitmap图片
        */
        if(win.getImageID() == 0){
            byte[] imagess = win.getWinShot();
            Bitmap bms = BitmapFactory.decodeByteArray(imagess, 0, imagess.length);
            holder.windowImage.setImageBitmap(bms);
        }else{
            holder.windowImage.setImageResource(win.getImageID());
        }
    }
    @Override
    public int getItemCount(){
        return mWindowList.size();
    }
}
