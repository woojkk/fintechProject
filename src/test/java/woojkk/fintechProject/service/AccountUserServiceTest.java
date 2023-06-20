package woojkk.fintechProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import woojkk.fintechProject.domain.AccountUser;
import woojkk.fintechProject.dto.LoginForm;

@SpringBootTest
class AccountUserServiceTest {
  @Autowired
  private AccountUserService accountUserService;

  @Test
  @DisplayName("로그인 성공")
  void login() {
    //given
    LoginForm form = LoginForm.builder()
        .email("woojkk@naver.com")
        .password("6877")
        .build();

    AccountUser accountUser = accountUserService.login(form);
    assertNotNull(accountUser.getEmail());
    assertNotNull(accountUser.getPassword());
    assertEquals("woojkk@naver.com", accountUser.getEmail());
    assertEquals("6877", accountUser.getPassword());
  }
}