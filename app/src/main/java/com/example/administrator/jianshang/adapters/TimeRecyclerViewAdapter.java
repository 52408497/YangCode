package com.example.administrator.jianshang.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.administrator.jianshang.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/5/22.
 */

public class TimeRecyclerViewAdapter extends RecyclerView.Adapter<TimeRecyclerViewAdapter.MyViewHodler> {
    private OnItemClickListener onItemClickListener;

    private final Context context;
    private ArrayList<String> datas;

    public TimeRecyclerViewAdapter(Context context, ArrayList<String> datas) {
        this.context = context;
        this.datas = datas;
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
        View itemView = View.inflate(context, R.layout.item_time_recyclerview, null);
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

        //赋值数据
        holder.tv_time.setText(data);
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
     * @param position
     * @param data
     */
    public void addData(int position, String data) {
        datas.add(position,data);

        //刷新适配器 插入操作
        notifyItemInserted(position);
    }

    /**
     * 移除数据
     * @param position
     */
    public void removeData(int position) {
        datas.remove(position);

        //刷新适配器 移除操作
        notifyItemRemoved(position);
    }

    class MyViewHodler extends RecyclerView.ViewHolder {

        private TextView tv_time;

        public MyViewHodler(View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(view,datas.get(getLayoutPosition()));
                    }
                }
            });


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
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



}
