package woojkk.fintechProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import woojkk.fintechProject.domain.AccountUser;
import woojkk.fintechProject.dto.LoginForm;
import woojkk.fintechProject.repository.AccountUserRepository;

@ExtendWith(MockitoExtension.class)
class AccountUserServiceTest {

  @Mock
  private AccountUserRepository accountUserRepository;

  @InjectMocks
  private AccountUserService accountUserService;

  @Test
  void loginTest() {
      //given
    given(accountUserRepository.findByEmail(any()))
        .willReturn(Optional.of(AccountUser.builder()
                .name("rrr")
                .email("test@naver.com")
                .password("123")
            .build()));
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    //when
    AccountUser accountUser = accountUserService.login(LoginForm.builder()
            .email("abc@naver.com")
            .password("456")
        .build());
      //then
    verify(accountUserRepository, times(1)).findByEmail(captor.capture());
    verify(accountUserRepository, times(0)).save(any());
    assertEquals("abc@naver.com", captor.getValue());
    assertNotEquals("abcd@naver.com", captor.getValue());
    assertEquals("123", accountUser.getPassword());
    assertEquals("rrr", accountUser.getName());
  }

//
//  @Test
//  @DisplayName("로그인 성공")
//  void login() {
//    //given
//    LoginForm form = LoginForm.builder()
//        .email("abc@naver.com")
//        .password("6877")
//        .build();
//
//    AccountUser accountUser = accountUserService.login(form);
//    assertNotNull(accountUser.getEmail());
//    assertNotNull(accountUser.getPassword());
//    assertEquals("abc@naver.com", accountUser.getEmail());
//    assertEquals("4416", accountUser.getPassword());
//  }
}