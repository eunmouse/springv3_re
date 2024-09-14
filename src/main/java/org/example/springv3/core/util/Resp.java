package org.example.springv3.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Resp<T> { // 공통 응답 DTO
    private Integer status;
    private String msg; // 데이터를 응답할 때, 메세지를 같이 주어야 한다.
    private T body; // 실제로 응답할 데이터, 없으면 null 넣으면 됨. 타입이 뭐가 리턴될지 모르니 T 로 설계.

    // 생성자로 만들면 호출할 때 헷갈리기 때문에, static 으로 만들어놓는게 맞다.
    public static <B> Resp<?> ok(B body){ // insert 된 데이터 body 에 넣고
        return new Resp<>(200, "성공", body);
    }

    public static <B> Resp<?> ok(B body, String msg){
        return new Resp<>(200, msg, body);
    }

    public static Resp<?> fail(Integer status, String msg){
        return new Resp<>(status, msg, null);
    }
}
