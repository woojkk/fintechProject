package woojkk.fintechProject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import woojkk.fintechProject.config.JwtAuthenticationProvider;
import woojkk.fintechProject.domain.AccountUser;
import woojkk.fintechProject.dto.LoginForm;
import woojkk.fintechProject.exception.AccountException;
import woojkk.fintechProject.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class LoginService {
  private final JwtAuthenticationProvider provider;
  private final AccountUserService accountUserService;



  public String userLoginToken(LoginForm form) {
    AccountUser loginCheck = accountUserService.findValidAccountUser(form.getEmail(), form.getPassword())
        .orElseThrow(() -> new AccountException(ErrorCode.LOGIN_CHECK_FAIL));

    return provider.createToken(
        loginCheck.getEmail(),
        loginCheck.getId());
  }
}
