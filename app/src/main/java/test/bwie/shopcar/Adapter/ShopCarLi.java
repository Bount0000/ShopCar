package test.bwie.shopcar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

import test.bwie.shopcar.OnItemClickListener;
import test.bwie.shopcar.R;
import test.bwie.shopcar.bean.Check;
import test.bwie.shopcar.bean.ShopCar;


/**
 * Created by 蒋六六 on 2017/10/18.
 */

public class ShopCarLi extends RecyclerView.Adapter<ShopCarLi.Carli> {
    public Context context;
    public List<ShopCar.DataBean.ListBean> list;
    public Check check;
    public void setCheck(Check check){
        this.check=check;
    }
    public OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){this.onItemClickListener=onItemClickListener;}
    public ShopCarLi(Context content, List<ShopCar.DataBean.ListBean> lliisstt) {
        this.context=content;
        this.list=lliisstt;
    }
    @Override
    public Carli onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.shopcar2_item, null);
        Carli car=new Carli(inflate);
        return car;
    }
    @Override
    public void onBindViewHolder(final Carli holder, final int position) {
        String images = list.get(position).images;
        String[] split = images.split("\\|");

        if(list.get(position).selected==0){
            holder.ck.setChecked(false);
        }else {
            holder.ck.setChecked(true);
        }

        holder.ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    list.get(position).selected=1;
                }else {
                    list.get(position).selected=0;
                }
                if(check!=null){
                    check.checkli(holder.getLayoutPosition());
                }
            }
        });
        Glide.with(context).load(split[0]).into(holder.img);
        holder.tv.setText(list.get(position).subhead);
        holder.tv1.setText(list.get(position).title);
        holder.tv2.setText("¥"+list.get(position).price);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(holder.itemView,position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class Carli extends RecyclerView.ViewHolder{
        public ImageView img;
        public TextView tv;
        public TextView tv1;
        public TextView tv2;

        public CheckBox ck;
        public Carli(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView12);
            tv=itemView.findViewById(R.id.tv_jianjie);
            tv1=itemView.findViewById(R.id.tv_jianjie2);
            tv2=itemView.findViewById(R.id.tv_shop_price);
            ck=itemView.findViewById(R.id.ck_gt);
        }
    }
}
