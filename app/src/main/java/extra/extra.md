package com.meitu.meipu.core.http;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RetrofitManager {
    /**
     * socket超时时间
     */
    public static final int SOCKET_TIMEOUT = 30 * 1000;

    private static Retrofit sRetrofit;
    private static ProductService sProductService;

    public static void init(RetrofitDelegate delegate) {
        sRetrofitDelegate = delegate;
        final Context context = BaseApplication.getApplication();
        final String baseUrl = HttpConfig.BASE_URL;

        OkHttpClient.Builder clientBuilder =
                new OkHttpClient.Builder();

        //dns拦截设置
        clientBuilder.addInterceptor(new MTDns4OkInterceptor());

        //设置缓存
        File cacheFile = new File(context.getCacheDir(), "retrofit");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 200); //200Mb
        clientBuilder.cache(cache);
//        clientBuilder.addInterceptor(new DomainInterceptor());
        clientBuilder.addInterceptor(new RetrofitInterceptor(context,
                delegate));

        if (AppConfig.isDeveloping && TestUtil.getOkhttpLogSwitch(AppConfig.isDebugable)) {
            HttpLoggingWriteInterceptor logInterceptor = new HttpLoggingWriteInterceptor();
            logInterceptor.setLevel(HttpLoggingWriteInterceptor.Level.BODY);
            clientBuilder.addInterceptor(logInterceptor);
            try {
                Class<?> cls = Class.forName("com.facebook.stetho.okhttp3.StethoInterceptor");
                Interceptor interceptor = (Interceptor) cls.newInstance();
                clientBuilder.addNetworkInterceptor(interceptor);
            } catch (Exception e) {
                Debug.w(e);
            }

        }

        //设置默认超时配置
        clientBuilder
                .connectTimeout(SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(SOCKET_TIMEOUT, TimeUnit.MILLISECONDS);

        //加入自定义超时
//        clientBuilder.addInterceptor(new TimeoutInterceptor());

//        clientBuilder.addInterceptor(new PostCacheInterceptor());

        OkHttpClient client = clientBuilder.build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit retrofit =
                new Retrofit.Builder().baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build();


        sProductService = retrofit.create(ProductService.class);
        sRetrofit = retrofit;
    }

    public static IErrorHandler getErrorHandler() {
        return sRetrofitDelegate.getRetrofitErrorHandler();
    }

    public static Retrofit getRetrofit(){
        return sRetrofit;
    }

    public static ProductService getProductService() {
        return sProductService;
    }
}

-----------------------------------------------------------------

public abstract class RetrofitCallback<T> implements Callback<RetrofitResult<T >> {
    final static String TAG = RetrofitCallback.class.getSimpleName();
    boolean  mIsCahceResponse = false;
    private Type dataClassName;

//    final IErrorHandler mErrorHandler = new MeiPuErrorHandler();

    private WeakReference<Object>  mRecyleObjRef;

    private RetrofitCallback(Object recyleObj) {
        if (recyleObj != null) {
            this.mRecyleObjRef = new WeakReference<Object>(recyleObj);
        } else {
            this.mRecyleObjRef = null;
        }
    }

    /**
     *请求回调和页面绑定,如果activity为空则请求不绑定页面
     * @param activity
     */
    public RetrofitCallback(Activity activity) {
        this((Object) activity);
    }

    /**
     *请求回调和页面绑定,如果fragment为空则请求不绑定页面
     * @param fragment
     */
    public RetrofitCallback(Fragment fragment) {
        this((Object) fragment);
    }

    public RetrofitCallback() {
        this((Object) null);
    }

    /**
     * Gson反序列化缓存时 需要获取到泛型的class类型
     */
    public RetrofitCallback<T> dataClassName(Type className) {
        dataClassName = className;
        return this;
    }
    protected boolean isCache() {
        return mIsCahceResponse;
    }

    @Override
    final public void onResponse(final Call call, Response response) {
        //绑定的activity不存在时，不需要更新UI
        if (mRecyleObjRef != null && null == mRecyleObjRef.get()) {
            return;
        }
        if (response.raw().cacheResponse() != null && response.raw().cacheResponse().isSuccessful()) {
            mIsCahceResponse = true;
        }
        final RetrofitResult<T> body = (RetrofitResult<T>) response.body();

        boolean networkAvailable = NetworkUtil.isNetworkAvailable(BaseApplication.getApplication());
        //网络不可用时顶部提示
        if (!networkAvailable) {
            ToastUtil.showNetworkHintToast();
        }
        if (null == body) {
            if(networkAvailable) {
                onWrapComplete(null, getUnknownException());
            }else{
                if (!getPostCache(call)) {
                    onWrapComplete(null, getNetworkException());
                }
            }
            return;
        }

        if (body.isSuccess()) {
            T data = body.getData();
            onWrapComplete(data, null);
        } else {
            onWrapComplete(null, new RetrofitException(body.getErrorCode(), body.getErrorMSG()));
        }
    }

    private RetrofitException getNetworkException(){
        return new RetrofitException(MeiPuErrorHouse.Local.NETWORK_UNAVAIABLE, MeiPuErrorHouse.getErrorMsg(MeiPuErrorHouse.Local.NETWORK_UNAVAIABLE));
    }

    private RetrofitException getUnknownException(){
        return new RetrofitException(MeiPuErrorHouse.Local.LOAD_FAILURE, MeiPuErrorHouse.getErrorMsg(MeiPuErrorHouse.Local.LOAD_FAILURE));
    }

    @Override
    final public void onFailure(Call call, Throwable t) {
        //绑定的activity不存在时，不需要更新UI
        if (mRecyleObjRef != null && null == mRecyleObjRef.get()) {
            return;
        }

        Debug.w("Request-End", "", t);
        if(!NetworkUtil.isNetworkAvailable(BaseApplication.getApplication())){
            if (!getPostCache(call)) {
                onWrapComplete(null, getNetworkException());
            }
        }else {
            onWrapComplete(null, getUnknownException());
        }
    }

    private boolean getPostCache(Call call) {
        try {
            Request request = call.request();
            if (request == null || !"POST".equals(request.method()) || TextUtils.isEmpty(request.header(RetrofitConstant.HEADER_POST_CACHE))) {
                //不使用缓存 或者网络可用 的情况下直接回调onFailure
                return false;
            }

            String url = request.url().toString();
            RequestBody requestBody = request.body();
            Charset charset = Charset.forName("UTF-8");
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            Buffer buffer = new Buffer();

            requestBody.writeTo(buffer);
            sb.append(buffer.readString(charset));
            buffer.close();
            String cache = CacheManager.getInstance().getCache(sb.toString());
            Log.d(CacheManager.TAG, "get cache->" + cache);

            if (!TextUtils.isEmpty(cache) && dataClassName != null) {
                RetrofitResult<T> body = new Gson().fromJson(cache, dataClassName);
                if (body == null) {
                    return false;
                }
                if (body.isSuccess()) {
                    onWrapComplete(body.getData(), null);
                }else {
                    onWrapComplete(null, new RetrofitException(body.getErrorCode(), body.getErrorMSG()));
                }
                return true;
            }
//            Debug.d(CacheManager.TAG, "onFailure->" + t.getMessage());
        } catch (Exception e) {
            Debug.e(CacheManager.TAG, e);
            return false;
        }
        return false;
    }

    private void onWrapComplete(T data, RetrofitException e) {
        try {
            if(e != null){
                //全局错误处理事件
                IErrorHandler handler = RetrofitManager.getErrorHandler();
                if (handler != null) {
                    handler.handleApiError(e);
                }
                if (AppConfig.isDebugable) {
                    Debug.w(e);
                    ToastUtil.show(e);
                }
            }
            onComplete(data, e);
        } catch (Throwable throwable) {
            Debug.w(TAG, "", throwable);
        }
    }

    public abstract void onComplete(T data, RetrofitException e);

}



-----------------------------------------------------------------------------------------------

public abstract class RetrofitDelegate {
    protected Context mContext;

    public RetrofitDelegate(Context context) {
        this.mContext = context;
    }
    /**
     * 统计平台生成的全局唯一ID
     * @return
     */
    public abstract String getGid();

    /**
     * 大帐号平台注册生成的 app id;
     * @return
     */
    public abstract String getAccountAppID();

    /**
     * Http Api 请求签名Slat值
     * @return
     */
    public abstract String getHttpApiSignSalt();

    public IErrorHandler getRetrofitErrorHandler() {
        return null;
    }

    public String getAnalyticsSdkVersiion() {
        return null;
    }

    public abstract String getMTAppClientKey();

}

    --------------------------------------------------------------

    public class RetrofitResult<T> implements Serializable {
    private boolean success;
    private String errorCode;
    private String errorMSG;
    private Long serverDateTime;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMSG() {
        return errorMSG;
    }

    public void setErrorMSG(String errorMSG) {
        this.errorMSG = errorMSG;
    }

    public void setServerDateTime(Long serverDateTime) {
        this.serverDateTime = serverDateTime;
    }

    public Long getServerDateTime() {
        return serverDateTime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private static String toJsonStr(Object src) {
        Gson gson = new GsonBuilder().create();
        return jsonFormatter(gson.toJson(src));
    }

    private static String jsonFormatter(String jsonString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonString);
        return gson.toJson(je);
    }

    @Override
    public String toString() {
        return toJsonStr(this);
    }
}