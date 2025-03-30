package harmony.dbproject.error;

import lombok.Getter;


/**
   커스텀 된 상태코드. 기존 상태 코드와 동일한 역할을 하지만, 단위가 1000단위로 변경
 */
public enum ErrorCode  {
    TOKEN_NOT_FOUND(4001,"존재하지 않는 api_key 입니다."),
    TOKEN_IS_NOT_VALID(4002, "api_key의 유효기간이 만료되었습니다.");


    @Getter
    private final int code;
    @Getter
    private final String message;

    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }
}
