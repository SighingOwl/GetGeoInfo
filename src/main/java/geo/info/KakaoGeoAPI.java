package geo.info;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import com.google.gson.Gson;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//  Kakao REST API 사용
public class KakaoGeoAPI {
    private static final String API_KEY = "5948b2b4db36d9fc48f9e6b1623f06e0";   // REST API Key
    private static final String API_BASE_URL = "https://dapi.kakao.com/v2/local/search/address.json";   // json 형식인 주소 정보 GET
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // 주소에 해당하는 위도, 경도 검색 메서드
    public static GeoInfo getAddressCoordinate(String address) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_BASE_URL).newBuilder();  // URL을 연결하는 객체 생성
        urlBuilder.addQueryParameter("query", address);

        Request req = new Request.Builder() // 서버에 요청하는 객체, OkHttpClinet 라이브러리
                .url(urlBuilder.build())
                .addHeader("Authorization", "KakaoAK " + API_KEY)    // 요청에 KaKao API 자격증명 정보 전달
                .build();

        try(Response resp = client.newCall(req).execute()) {    // OkHttpClient 라이브러리, newCall 메서드로 요청을 실행 후 응답을 받는 객체
            if(!resp.isSuccessful()) throw new IOException("Request Failed: " + resp);

            JsonObject jsonResp = gson.fromJson(resp.body().charStream(), JsonObject.class);
            JsonArray doc = jsonResp.getAsJsonArray("documents");    // Kakao API로 받은 응답 중 document의 내용을 추출
            GeoInfo geoInfo = new GeoInfo();
            for(JsonElement geo : doc) {
                JsonObject geoJson = geo.getAsJsonObject(); // 추출한 document 내용을 Json 객체로 변환
                geoInfo.setAddress(geoJson.get("address_name").getAsString());  // 전체 주소
                geoInfo.setLatitude(geoJson.get("y").getAsString()); // y = latitude
                geoInfo.setLongitude(geoJson.get("x").getAsString()); // x = longitude
            }
            return geoInfo;
        }
    }
}
