package test.bwie.shopcar.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.Map;

import test.bwie.shopcar.OnItemClickListener;
import test.bwie.shopcar.R;
import test.bwie.shopcar.bean.Check;
import test.bwie.shopcar.bean.ShopCar;


/**
 * Created by 蒋六六 on 2017/10/18.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.CarAdapter> {
    private List<ShopCar.DataBean> data;
    private Context content;
    private String uid;
    private Map<String, String> map;
    public ShopAdapter(Context content, List<ShopCar.DataBean> data, String uid) {
        this.content=content;
        this.data=data;
        this.uid=uid;
    }
    public delect dd;
    public void setdelect(delect dd){
        this.dd=dd;
    }
    public Qcheck qq;
    public void setQcheck(Qcheck qq){
        this.qq=qq;
    }
    @Override
    public CarAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(content, R.layout.shapcar1_item, null);
        CarAdapter car=new CarAdapter(inflate);
        return car;
    }

    @Override
    public void onBindViewHolder(final CarAdapter holder, int position) {
        String sellerName = data.get(position).sellerName;
        final List<ShopCar.DataBean.ListBean> lliisstt = data.get(position).list;
      //
        boolean ff=true;
        for (ShopCar.DataBean.ListBean listBean : lliisstt) {
            if (listBean.selected==0){
                ff=false;
            }
        }
        holder.ckk.setChecked(ff);
        //
        holder.tv_shophome.setText(sellerName);
        holder.shop_li_recycle.setLayoutManager(new LinearLayoutManager(content));
        final ShopCarLi caliadapter = new ShopCarLi(content, lliisstt);
        holder.shop_li_recycle.setAdapter(caliadapter);
        //====================================================
        caliadapter.setCheck(new Check() {
            @Override
            public void checkli(final int itemnum) {
                boolean f=true;
                for (ShopCar.DataBean.ListBean listBean : lliisstt) {
                    if(listBean.selected==0){
                        f=false;
                    }
                }
               //商家选框
                holder.ckk.setChecked(f);
                //holder.get商家条目，itemnum商品条目
                qq.one(holder.getLayoutPosition(),itemnum);

        }
        });
        holder.ckk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ShopCar.DataBean.ListBean listBean : lliisstt) {
                    if(holder.ckk.isChecked()){
                        listBean.selected=1;
                    }else {
                        listBean.selected=0;
                    }
                }
                qq.two(holder.getLayoutPosition());
                caliadapter.notifyDataSetChanged();
            }
        });
        caliadapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Adleg(position,dd,holder.getLayoutPosition());
            }
            private void Adleg(final int position, final delect dd, final int layoutPosition) {
                AlertDialog.Builder builder=new AlertDialog.Builder(content);
                builder.setTitle("删除，请三思");
                builder.setMessage("这可是你自己要删除的，想好了啊");
                builder.setPositiveButton("去意已决", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,final int i) {
                        String pid = lliisstt.get(position).pid;
                        dd.dele(pid,position,layoutPosition);
                    }
                }).create();

                builder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(content,"少侠，你很机智",Toast.LENGTH_SHORT).show();
                    }
                }).create();
                builder.show();
            }
        });
       //======================================================
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    class CarAdapter extends RecyclerView.ViewHolder{
        private TextView tv_shophome;
        private RecyclerView shop_li_recycle;
        private CheckBox ckk;
        public CarAdapter(View itemView) {
            super(itemView);
             tv_shophome = itemView.findViewById(R.id.tv_shophome);
             shop_li_recycle = itemView.findViewById(R.id.shop_li_recy);
             ckk = itemView.findViewById(R.id.ck_shophome);
        }
    }
public interface Qcheck{
    void one(int i, int o);
    void two(int p);
}
public interface delect{
    void dele(String pid, int postion, int p);
}
}
