package woojkk.fintechProject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 회원이 없습니다."),
  NOT_FOUND_ACCOUNT(HttpStatus.BAD_REQUEST, "일치하는 계좌가 없습니다."),
  NOT_FOUND_TRANSACTION(HttpStatus.BAD_REQUEST, "거래 내역이 없습니다."),
  ACCOUNT_CLOSE_FAIL_REMAIN_BALANCE(HttpStatus.BAD_REQUEST, "계좌에 잔액이 남아있어 해지할 수 없습니다."),
  INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),
  ACCOUNT_TRANSACTION_LOCK(HttpStatus.BAD_REQUEST, "해당 계좌는 사용중입니다. \n 잠시 후 다시 시도해 주세요"),
  ACCOUNT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "잔액이 계좌의 한도 금액을 초과합니다."),
  ALREADY_UNREGISTERED_ACCOUNT(HttpStatus.BAD_REQUEST, "이미 해지되 계좌입니다."),
  ACCOUNT_USER_UNMATCHED(HttpStatus.BAD_REQUEST, "사용자와 계좌의 소유주가 다릅니다."),
  NOT_MATCHED_PASSWORD(HttpStatus.BAD_REQUEST, "계좌 비밀번호가 일치하지 않습니다."),
  UNAUTHORIZED_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 토큰입니다."),
  NOT_MATCHED_TRANSACTION_TYPE(HttpStatus.BAD_REQUEST, "거래 종류를 확인해 주시기 바랍니다."),
  MAX_COUNT_PER_USER(HttpStatus.BAD_REQUEST, "해당은행의 해당종류 계좌는 최대 5개까지 생성 가능합니다.");





  private final HttpStatus httpStatus;
  private final String detail;

}