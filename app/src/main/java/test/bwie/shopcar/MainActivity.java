package test.bwie.shopcar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import test.bwie.shopcar.Adapter.ShopAdapter;
import test.bwie.shopcar.bean.ShopCar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int sumNum;
    private Double sumPrice = 0.0;
    private DecimalFormat decimalFormat;
    private RecyclerView car_recy;
    private CheckBox mCheckboxQuanxuan;
    private TextView tv_sum;
    private TextView mJiesuanButton;
    private String url="http://120.27.23.105/product/getCarts?uid=114";
    private List<ShopCar.DataBean> data;
    private ShopAdapter shopadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        okhttp();
        decimalFormat = new DecimalFormat("#0.00");
    }
    private void okhttp() {
        NetRequestUtils.ok(url, new Callback() {
            private ShopCar shopCar;
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String  string = response.body().string();
                Gson gson=new Gson();
                ShopCar shopCar = gson.fromJson(string, ShopCar.class);
                data = shopCar.data;
                if (data!=null){
                    for (ShopCar.DataBean dataBean : data) {
                        for (ShopCar.DataBean.ListBean listBean : dataBean.list) {
                            if (listBean.selected==1){
                                sumNum++;
                                sumPrice = sumPrice + Double.parseDouble(listBean.price);
                            }}}
                    //==========分割线
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_sum.setText("合计:¥"+decimalFormat.format(sumPrice));
                            mJiesuanButton.setText("去结算("+sumNum+")");
                            shopadapter = new ShopAdapter(MainActivity.this, data, "185");
                            car_recy.setAdapter(shopadapter);
                            delect();
                            shopadapter.setQcheck(new ShopAdapter.Qcheck() {
                                @Override
                                public void one(int i, int o) {
                                    // TODO: 2017/10/21 商品选中状态改变计算价格
                                    String p = data.get(i).list.get(o).price;
                                    if (data.get(i).list.get(o).selected == 1){
                                        sumPrice = sumPrice + Double.parseDouble(p);
                                        sumNum++;
                                    } else {
                                        sumPrice = sumPrice - Double.parseDouble(p);
                                        sumNum--;
                                    }

                                    tv_sum.setText("合计:¥"+decimalFormat.format(sumPrice));
                                    mJiesuanButton.setText("去结算("+sumNum+")");
                                    //-------------------华丽的分割线---------------
                                    boolean f = true;
                                    for (ShopCar.DataBean dataBean : data) {
                                        for (ShopCar.DataBean.ListBean listBean : dataBean.list) {
                                            if (listBean.selected == 0) {
                                                f = false;
                                           }
                                        }
                                    }
                                    mCheckboxQuanxuan.setChecked(f);
                                }

                                @Override
                                public void two(int p) {
                                    boolean f = true;
                                    for (ShopCar.DataBean dataBean : data) {
                                        for (ShopCar.DataBean.ListBean listBean : dataBean.list) {
                                            if (listBean.selected == 0) {
                                                f = false;
                                            }
                                        }
                                    }
                                    mCheckboxQuanxuan.setChecked(f);
                                }
                            });

                        }
                    });
                }
            }
        });
    }

    private void delect() {
        shopadapter.setdelect(new ShopAdapter.delect() {
            @Override
            public void dele(String pid, final int postion, final int lapotion) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("pid", pid);
                map.put("uid", "185");
                NetRequestUtils.chuan(map, "http://120.27.23.105/product/deleteCart", new NetRequestUtils.OkhttpIn() {
                    @Override
                    public void onresponse(String string) {
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                data.get(lapotion).list.remove(postion);
                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).list.size() == 0) {
                                        data.remove(i);
                                    }
                                }
                                shopadapter.notifyDataSetChanged();
                            }});}});}});
    }


    private void initView() {
        car_recy = (RecyclerView) findViewById(R.id.shopcar_recycle);
        car_recy.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mCheckboxQuanxuan = (CheckBox) findViewById(R.id.checkbox_quanxuan);
        mCheckboxQuanxuan.setOnClickListener(this);
        tv_sum = (TextView) findViewById(R.id.tv_sum);
        mJiesuanButton = (TextView) findViewById(R.id.jiesuan_button);
        mJiesuanButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopcar_recycle:
                break;
            case R.id.checkbox_quanxuan:
                QXcheck();
                break;
            case R.id.jiesuan_button:
                int num=1;
                if(num>0){
                    Toast.makeText(MainActivity.this,decimalFormat.format(sumPrice),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void QXcheck() {
        for (ShopCar.DataBean dataBean : data) {
            for (ShopCar.DataBean.ListBean listBean : dataBean.list) {
                if(mCheckboxQuanxuan.isChecked()){
                    if (listBean.selected == 0) {
                        //// TODO: 2017/10/21 点击全部选中时，先加上之前未选中的价钱 然后改变状态
                        sumNum++;
                        sumPrice = sumPrice + Double.parseDouble(listBean.price);
                        listBean.selected=1;
                    }
                }else {
                    if (listBean.selected == 1) {
                        //// TODO: 2017/10/21 点击全部取消选中时，先减去之前选中的价钱 然后改变状态
                        sumNum--;
                        sumPrice = sumPrice - Double.parseDouble(listBean.price);
                        listBean.selected = 0;
                    }
                }
            }
        }


        tv_sum.setText("合计:¥"+decimalFormat.format(sumPrice));
        mJiesuanButton.setText("去结算("+sumNum+")");
        shopadapter.notifyDataSetChanged();
    }
}
