package ch09.item59;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * transferTo 메서드를 이용해 URL의 내용 가져오기 (자바 9부터 가능)
 * 리눅스의 curl 명령어와 유사
 *
 * 참고: 리눅스의 curl 명령어는 HTTP 요청을 보내고 응답을 받아올 수 있음
 * curl https://example.com
 * 로 실행시, 웹페이지의 HTML을 가져올 수 있습니다:
 */
public class Curl {
    public static void main(String[] args) throws IOException {
        try (InputStream in = new URL("https://google.com").openStream()) {
            in.transferTo(System.out);
        }
    }
}
