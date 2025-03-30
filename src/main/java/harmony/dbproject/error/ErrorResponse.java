package harmony.dbproject.error;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int code;
    private final String message;

    private ErrorResponse(int code, String message){
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }


    @Override
    public String toString(){
        String code = "code : " + this.code + "\n"
                        +"message : " + this.message;

        return code;
    }
}
