package com.pProject.ganada;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ExampleParse extends AsyncTask{

    private String API_KEY = BuildConfig.API_KEY;
    @Override
    protected Object doInBackground(Object[] objects) {
       // url connection
        try {
            StringBuilder urlBuilder = new StringBuilder("https://opendict.korean.go.kr/api/search");   // open API 요청 URL
            urlBuilder.append("?key=" + API_KEY); //Service Key//
            urlBuilder.append("&" + "req_type=" + "json"); // 요청 타입(json)
            urlBuilder.append("&" + "part=" + "exam");  // 검색 대상(예문)
            urlBuilder.append("&" + URLEncoder.encode("q", "UTF-8") + "=" + URLEncoder.encode((String) objects[0], "UTF-8"));  // 검색어
            urlBuilder.append("&" + "sort=" + "popular"); // 정렬 기준(많이 찾은 순)
            urlBuilder.append("&" + "start=" + 1);  // 검색의 시작 번호(1번부터)
            urlBuilder.append("&" + "num=" + 20);   // 결과 출력 건수(10개)

            URL url = new URL(urlBuilder.toString());   // 위에서 선언한 urlBuilder(우리말샘 open API 요청 URL)

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // url을 통해 HttpURLConnection 클래스를 생성
            conn.setRequestMethod("GET");   // GET 방식으로 전송해서 파라미터 받아오기
            conn.setRequestProperty("Content-type", "application/json");    // 헤더의 Content Type을 정의

            BufferedReader rd;  // 데이터를 버퍼에 저장해서 하나의 데이터로 만들기(입출력 라이브러리 - BufferedReader 클래스 사용)
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder(); // 결과값(변경 가능한 문자열을 만들어줌)을 저장할 변수 sb

            String line;    // rd.readLine을 저장할 String 변수
            while ((line = rd.readLine()) != null) {
                sb.append(line + "\n");     // BufferedReader는 한 줄을 통째로 입력받았기 때문에 readLine으로 개행문자를 포함해 한 줄을 전부 읽어옴.
            }
            rd.close();
            conn.disconnect();

            String result = sb.toString();  // 위에서 읽어온 Buffer 데이터를 readLine() 메소드를 사용해서 한줄씩 읽어서 sb 변수에 저장함 -> 그 변수를 result로 선언

            // Parsing part
            JSONObject jsonObject = new JSONObject(result);
            JSONObject wordInfoResult = (JSONObject) jsonObject.get("channel"); // key가 wordInfoResult인 value를 추출하기 위해서 get()을 사용

            JSONArray wordInfo = (JSONArray) wordInfoResult.get("item");    // key와  value안에 또다시 JSON이 존재 -> Array 형태
            JSONObject example = (JSONObject) wordInfo.get(0);  // 그렇게 얻은 데이터에서 마지막으로 key가 wordInfo인 value를 JSONObject에 다시 넣어주기

            String exam = (String) example.get("example");

            return exam;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
