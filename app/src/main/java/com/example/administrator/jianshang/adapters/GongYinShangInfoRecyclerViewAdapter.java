package com.example.administrator.jianshang.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.bean.DBGongyinshangInfoBean;
import com.example.administrator.jianshang.bean.FuLiaoInfoBean;
import com.example.administrator.jianshang.bean.GongYinShangBean;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2019/5/22.
 */

public class GongYinShangInfoRecyclerViewAdapter extends RecyclerView.Adapter<GongYinShangInfoRecyclerViewAdapter.MyViewHodler> {
    private OnItemClickListener onItemClickListener;
    private OnImageClickListener onImageClickListener;
    private OnTextClickListener onTextClickListener;

    private int orientation;
    private final Context context;
    private ArrayList<DBGongyinshangInfoBean> datas;

    /**
     * @param context
     * @param datas
     * @param orientation 0:HORIZONTAL 1:VERTICAL
     */
    public GongYinShangInfoRecyclerViewAdapter(Context context, ArrayList<DBGongyinshangInfoBean> datas, int orientation) {
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
        View itemView = View.inflate(context, R.layout.item_gongyinshang_info_recyclerview, null);
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
        DBGongyinshangInfoBean data = datas.get(position);

        //赋值数据
        holder.tvCangkuAddress.setText(data.getCangKuAddress());
        holder.tvCangkuTelephone.setText(data.getCangKuTelephone() + "");
        holder.tvDangkouAddress.setText(data.getDangKouAddress());
        holder.tvDangkouTelephone.setText(data.getDangKouTelephone() + "");
        holder.tvGongyinshangId.setText(data.getId() + "");
        holder.tvGongyinshangType.setText(data.getGongYinShangType());
        holder.tvGongyinshangName.setText(data.getGongYinShangName());

        String imageFileName = data.getMingPianImgZM();
        String folderName = this.context.getString(R.string.my_photo_folder_name);
        File fileUri = new File(Environment.getExternalStorageDirectory().getPath() +
                "/" + folderName + "/" + imageFileName);
        Uri fileUriForContent = Uri.fromFile(fileUri);

        Glide.with(context)
                .load(fileUriForContent)
                .placeholder(R.drawable.default_no_img)     //占位图
                .error(R.drawable.default_no_img)           //出错的占位图
//                .override(width, height)                    //图片显示的分辨率，像素值，可转化为dp再设
//              .animate(R.anim.glide_anim)                 //动画
//                .centerCrop()                               //图片显示样式
//                .fitCenter()                                //图片显示样式
                .dontAnimate()
                .into(holder.ivMingpianImg);


//        holder.tv_fl_name.setText(data.getFuliao_name());
//        holder.tv_fl_gongyinshang.setText(data.getGongyingshang());
//        holder.tv_fl_jiage.setText(data.getJiage()+"");
//
//        String imageFileName = data.getFuliao_img_name();
//        String folderName = this.context.getString(R.string.my_photo_folder_name);
//        File fileUri = new File(Environment.getExternalStorageDirectory().getPath() +
//                "/" + folderName + "/" + imageFileName);
//        Uri fileUriForContent = Uri.fromFile(fileUri);
//
//        Glide.with(context)
//                .load(fileUriForContent)
//                .placeholder(R.drawable.default_no_img)     //占位图
//                .error(R.drawable.default_no_img)           //出错的占位图
////                .override(width, height)                    //图片显示的分辨率，像素值，可转化为dp再设
////              .animate(R.anim.glide_anim)                 //动画
////                .centerCrop()                               //图片显示样式
////                .fitCenter()                                //图片显示样式
//                .into(holder.iv_fl_tupian);
//
//        holder.tv_dh_id.setText(data.getDahuoId()+"");
//        holder.tv_fl_id.setText(data.getId()+"");
//        holder.tv_fl_bz.setText(data.getBeizhu());
//        holder.tv_fl_gongyinshang_id.setText(data.getGongyinshangId()+"");
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
     * 根据新的列表刷新数据
     *
     * @param datas
     */
    public void updateData(ArrayList<DBGongyinshangInfoBean> datas) {
        this.datas = datas;
        //刷新适配器
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param position
     * @param data
     */
    public void addData(int position, DBGongyinshangInfoBean data) {
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

    class MyViewHodler extends RecyclerView.ViewHolder {


        private TextView tvGongyinshangId;
        private TextView tvGongyinshangType;
        private ImageView ivMingpianImg;
        private TextView tvGongyinshangName;
        private TextView tvDangkouAddress;
        private TextView tvCangkuAddress;
        private TextView tvDangkouTelephone;
        private TextView tvCangkuTelephone;


        public MyViewHodler(View itemView) {
            super(itemView);

            tvGongyinshangId = (TextView) itemView.findViewById(R.id.tv_gongyinshang_id);
            tvGongyinshangType = (TextView) itemView.findViewById(R.id.tv_gongyinshang_type);
            ivMingpianImg = (ImageView) itemView.findViewById(R.id.iv_mingpian_img);
            tvGongyinshangName = (TextView) itemView.findViewById(R.id.tv_gongyinshang_name);
            tvDangkouAddress = (TextView) itemView.findViewById(R.id.tv_dangkou_address);
            tvCangkuAddress = (TextView) itemView.findViewById(R.id.tv_cangku_address);
            tvDangkouTelephone = (TextView) itemView.findViewById(R.id.tv_dangkou_telephone);
            tvCangkuTelephone = (TextView) itemView.findViewById(R.id.tv_cangku_telephone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, datas.get(getLayoutPosition()), getLayoutPosition());
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
        public void onItemClick(View view, DBGongyinshangInfoBean data, int position);
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
        public void onImageClick(View view, DBGongyinshangInfoBean data);
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
        public void onTextClick(View view, DBGongyinshangInfoBean data);
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
