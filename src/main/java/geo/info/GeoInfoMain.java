package geo.info;

import java.io.IOException;
import java.util.Scanner;

public class GeoInfoMain {
    public static void main(String[] args) {
        try {
            Scanner scan = new Scanner(System.in);
            System.out.print("주소를 입력하세요(도로명): ");
            String addr = scan.nextLine();
            GeoInfo geoInfo = KakaoGeoAPI.getAddressCoordinate(addr);
            if(geoInfo != null) {
                System.out.println("주소: " + geoInfo.getAddress());
                System.out.println("위도: " + geoInfo.getLatitude());
                System.out.println("경도: " + geoInfo.getLongitude());
            }   else {
                System.out.println("검색 결과가 존재하지않습니다.");
            }
        } catch (IOException e) {
            System.err.println("오류가 발생했습니다: " + e.getMessage());
        }
    }
}