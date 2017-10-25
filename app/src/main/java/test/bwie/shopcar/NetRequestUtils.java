package test.bwie.shopcar;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 蒋六六 on 2017/10/8.
 */

public class NetRequestUtils {
    public static void ok(String url, final Callback callback){
    OkHttpClient.Builder clientBuilder=new OkHttpClient.Builder();
    OkHttpClient client=clientBuilder.build();
    FormBody.Builder builder=new FormBody.Builder();RequestBody body=builder.build();
    Request request = new Request.Builder().post(body).url(url).build();
    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            if (callback!=null){
                callback.onFailure(call, e);
            }
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
             if (callback!=null){
                 if (response.isSuccessful())
                 callback.onResponse(call,response);
             }}});
}

public static void chuan(final Map<String,String> map, String url,final OkhttpIn okin){
    final OkHttpClient okHttpClient = new OkHttpClient();
    final FormBody.Builder builder = new FormBody.Builder();
    for (Map.Entry<String, String> entry : map.entrySet()) {
        builder.add(entry.getKey(),entry.getValue());
    }
    FormBody body = builder.build();
    final Request request = new Request.Builder().post(body).url(url).build();
    okHttpClient.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
             if(response.isSuccessful()){
              okin.onresponse(response.body().string());
             }
        }
    });
}
    public interface Okjiekou {
        void onFailyre(android.telecom.Call call, IOException e);
        void onResponse(android.telecom.Call call, Response response);
    }
    public interface OkhttpIn{
       void onresponse(String string);
    }
}
