package com.pProject.ganada;

import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CaptionService {

    private static Retrofit RETROFIT;

    private CaptionView captionView;

    public static Retrofit getRetrofit() {
        if (RETROFIT == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .build();

            RETROFIT = new Retrofit.Builder()
                    .baseUrl("http://ipconfig:5000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        }

        return RETROFIT;
    }

    public static CaptionRetrofitInterface getCaptionRetrofitInterface() {
        return getRetrofit().create(CaptionRetrofitInterface.class);
    }

    public void setCaptionView(CaptionView captionView) {
        this.captionView = captionView;
    }

    public void test() {
        getCaptionRetrofitInterface().test().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("CaptionService", "test: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("CaptionService", "test ERROR: " + t.toString());
            }
        });
    }

    public void getPictureCaption(File photoFile) {
        MultipartBody.Part file = getImageMultipartBody(photoFile);

        getCaptionRetrofitInterface().pictureCaption(file).enqueue(new Callback<Caption>() {
            @Override
            public void onResponse(Call<Caption> call, Response<Caption> response) {
                Log.d("CaptionService", "getPictureCaption: " + response.body().toString());
                captionView.onCaptionSuccess(Uri.fromFile(photoFile), response.body());
            }

            @Override
            public void onFailure(Call<Caption> call, Throwable t) {
                Log.e("CaptionService", "getPictureCaption ERROR: " + t.toString());
            }
        });
    }

    public void getGalleryCaption(Uri uri) {
        File file = new File(uri.toString());
        MultipartBody.Part photoFile = getImageMultipartBody(file);

        getCaptionRetrofitInterface().galleryCaption(photoFile).enqueue(new Callback<Caption>() {
            @Override
            public void onResponse(Call<Caption> call, Response<Caption> response) {
                if (response.code() == 200) {
                    Log.d("CaptionService", "getGalleryCaption: " + response.body().toString());
                    captionView.onCaptionSuccess(uri, response.body());
                } else {
                    Log.e("CaptionService", "getGalleryCaption fail code: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<Caption> call, Throwable t) {
                Log.e("CaptionService", "getGalleryCaption ERROR: " + t.toString());
            }
        });
    }

    private MultipartBody.Part getImageMultipartBody(File photoFile) {
        if (!photoFile.exists())
            photoFile.mkdir();

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), photoFile);

        return MultipartBody.Part.createFormData("file", photoFile.getName(), requestFile);
    }

}
