package com.sheba.retrofitapicalling.utility;

import android.content.Context;

import com.sheba.retrofitapicalling.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceGeneratorForCaching {
    public static OkHttpClient client;
    public static Request request;
    public static Response response;
    public static Retrofit retrofit;
    private static final String CACHE_CONTROL = "Cache-Control";
    public static int user_id = 0;

    public static Context context1;
    public static  <S> S createService(Class<S> servieClass, final Context context){

        context1 = context;

        /*if (context != null) {
            appPreferenceHelper = new AppPreferenceHelper(context);

            if (appPreferenceHelper.isCurrentUserLoggedIn()) {
                user_id = appPreferenceHelper.getCurrentUserId();
            } else {
                user_id = 0;
            }
        }*/

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(new ResponseCacheInterceptor())
                .addInterceptor(new OfflineResponseCacheInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("User-Agent", System.getProperty("http.agent"))
                                .addHeader("portal-name", "customer-app")
                                .addHeader("user-id", String.valueOf(user_id))
                                .addHeader("version-code", String.valueOf(BuildConfig.VERSION_CODE))
                                .build();
                        return chain.proceed(request);
                    }
                })
                .cache(new Cache(new File(context.getCacheDir(),
                        "apiResponses"), 5 * 1024 * 1024));

        if(BuildConfig.DEBUG){
            try {
                builder.addInterceptor(interceptor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        OkHttpClient okHttpClient1 = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(okHttpClient1)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(servieClass);
    }

    private static class ResponseCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            if(Boolean.valueOf(request.header("ApplyResponseCache"))) {
               // Timber.i("Response cache applied");
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .removeHeader("ApplyResponseCache")
                        .header("Cache-Control", "public, max-age=" + 60)
                        .build();
            } else {
                //Timber.i("Response cache not applied");
                return chain.proceed(chain.request());
            }
        }
    }

    private static class OfflineResponseCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            if(Boolean.valueOf(request.header("ApplyOfflineCache"))) {
               // Timber.i("Offline cache applied");
                if(! NetworkUtil.isNetworkConnected(context1)) {
                    request = request.newBuilder()
                            .removeHeader("ApplyOfflineCache")
                            .header("Cache-Control",
                                    "public, only-if-cached, max-stale=" + 2419200)
                            .build();
                }
            } else{

            }
               //Timber.i("Offline cache not applied");

            return chain.proceed(request);
        }
    }
}
