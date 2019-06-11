package com.example.administrator.jianshang.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.activity.NewDaHuoClothesActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2019/5/22.
 */

public class KuanShiImageListRecyclerViewAdapter extends RecyclerView.Adapter<KuanShiImageListRecyclerViewAdapter.MyViewHodler> {
    private OnItemClickListener onItemClickListener;
    private OnImageClickListener onImageClickListener;
    private OnTextClickListener onTextClickListener;

    private int orientation;
    private final Context context;
    private ArrayList<String> datas;

    /**
     * @param context
     * @param datas
     * @param orientation 0:HORIZONTAL 1:VERTICAL
     */
    public KuanShiImageListRecyclerViewAdapter(Context context, ArrayList<String> datas, int orientation) {
        this.context = context;
        this.datas = datas;
        this.orientation = orientation;
    }

    /**
     * 创建View和ViewHodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_img_test_recyclerview, null);
        LinearLayout linearLayout = itemView.findViewById(R.id.id_linear_layout);
        linearLayout.setOrientation(orientation);


        return new MyViewHodler(itemView);
    }


    /**
     * 数据和View绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHodler holder, int position) {
        //根据位置得到对应的数据
        String data = datas.get(position);
        holder.iv_icon.getLayoutParams().height = 600;


        //赋值数据
        holder.tv_title.setText(data);

        String folderName = context.getString(R.string.my_photo_folder_name);
        File fileUri = new File(Environment.getExternalStorageDirectory().getPath()
                + "/" + folderName + "/" + datas.get(position));

        //int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, context.getResources().getDisplayMetrics());
        //int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400f, context.getResources().getDisplayMetrics());
        Glide.with(context)
                .load(Uri.fromFile(fileUri))
                .placeholder(R.drawable.default_no_img)     //占位图
                .error(R.drawable.default_no_img)           //出错的占位图
                //.override(width, height)                    //图片显示的分辨率，像素值，可转化为dp再设
//              .animate(R.anim.glide_anim)                 //动画
                .centerCrop()                               //图片显示样式
                .fitCenter()                                //图片显示样式
                .into(holder.iv_icon);

    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return datas.size();
    }


    /**
     * 添加数据
     *
     * @param position
     * @param data
     */
    public void addData(int position, String data) {
        datas.add(position, data);

        //刷新适配器 插入操作
        notifyItemInserted(position);
    }

    /**
     * 移除数据
     *
     * @param position
     */
    public void removeData(int position) {
        datas.remove(position);

        //刷新适配器 移除操作
        notifyItemRemoved(position);
    }


    /**
     * 根据新的列表刷新数据
     * @param datas
     */
    public void updateData(ArrayList<String> datas){
        this.datas = datas;

        //刷新适配器
        notifyDataSetChanged();
    }


    class MyViewHodler extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_title;

        public MyViewHodler(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_title = itemView.findViewById(R.id.tv_title);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, datas.get(getLayoutPosition()));
                    }
                }
            });


//            iv_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onImageClickListener != null){
//                        onImageClickListener.onImageClick(view,datas.get(getLayoutPosition()));
//                    }
//                }
//            });
//
//
//
//            tv_title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onTextClickListener != null){
//                        onTextClickListener.onTextClick(view,datas.get(getLayoutPosition()));
//                    }
//                }
//            });

        }
    }


//-------------------------点击RecyclerView某条----------------------------------------------

    /**
     * 点击RecyclerView某条的监听接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, String data);
    }

    /**
     * 外部需要实现监听事件时调用该方法实例化监听
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


//--------------------------点击RecyclerView中的图片---------------------------------------------

    /**
     * 点击RecyclerView中图片的监听接口
     */
    public interface OnImageClickListener {
        public void onImageClick(View view, String data);
    }

    /**
     * 外部需要实现监听事件时调用该方法实例化监听
     *
     * @param onImageClickListener
     */
    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }


    //--------------------------点击RecyclerView中的文字---------------------------------------------

    /**
     * 点击RecyclerView中文字的监听接口
     */
    public interface OnTextClickListener {
        public void onTextClick(View view, String data);
    }

    /**
     * 外部需要实现监听事件时调用该方法实例化监听
     *
     * @param onTextClickListener
     */
    public void setOnTextClickListener(OnTextClickListener onTextClickListener) {
        this.onTextClickListener = onTextClickListener;
    }


}
