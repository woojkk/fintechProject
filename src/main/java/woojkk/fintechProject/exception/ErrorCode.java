package woojkk.fintechProject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 회원이 없습니다."),
  MAX_COUNT_PER_USER(HttpStatus.BAD_REQUEST, "해당은행의 해당종류 계좌는 최대 5개까지 생성 가능합니다."),
  LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "아이디 또는 패스워드를 확인해주세요.");





  private final HttpStatus httpStatus;
  private final String detail;

}
