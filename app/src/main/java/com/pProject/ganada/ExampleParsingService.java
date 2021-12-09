package com.pProject.ganada;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExampleParsingService {

    private ExamParsingView examParsingView;

    void setExamParsingView(ExamParsingView examParsingView) {
        this.examParsingView = examParsingView;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://opendict.korean.go.kr/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ExampleParsingRetrofitInterface service = retrofit.create(ExampleParsingRetrofitInterface.class);

    void getExample(HashMap hm) {
        String word = hm.get("recognizedText").toString();

        service.getExample(word).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                JsonObject wordInfoResult = jsonObject.getAsJsonObject("channel");  // key가 wordInfoResult인 value를 추출하기 위해서 get()을 사용
                JsonArray wordInfo = wordInfoResult.getAsJsonArray("item"); // key와  value안에 또다시 JSON이 존재 -> Array 형태
                String exam = wordInfo.get(0).getAsJsonObject().get("example").toString();  // 그렇게 얻은 데이터에서 마지막으로 key가 wordInfo인 value를 JSONObject에 다시 넣어주기
                exam = exam.replaceAll("\\\"","");  //큰따옴표 제거
                hm.put("exam", exam);

                examParsingView.onParsingSuccess(hm);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("ExampleParsingService", "getExample error: " + t.toString());
            }
        });
    }
}
