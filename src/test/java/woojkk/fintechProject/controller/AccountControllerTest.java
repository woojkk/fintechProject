package woojkk.fintechProject.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static woojkk.fintechProject.type.AccountStatus.IN_USE;
import static woojkk.fintechProject.type.AccountType.INSTALLMENT_SAVING;
import static woojkk.fintechProject.type.Bank.WOO_RI;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import woojkk.fintechProject.config.JwtAuthenticationProvider;
import woojkk.fintechProject.config.JwtConfig;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.domain.UserVo;
import woojkk.fintechProject.dto.CreateAccountDto;
import woojkk.fintechProject.dto.DeleteAccountDto;
import woojkk.fintechProject.service.AccountService;
import woojkk.fintechProject.type.AccountType;
import woojkk.fintechProject.type.Bank;

@WebMvcTest(controllers = AccountController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
            classes = {JwtAuthenticationProvider.class, JwtConfig.class})
    })
@AutoConfigureDataJpa
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

  @MockBean
  private JwtAuthenticationProvider provider;
  @MockBean
  private AccountService accountService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void successCreateAccount() throws Exception {

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(2L, "test@naver.com"));

    given(accountService.createAccount(anyLong(), anyLong(), any(), any(), any(), anyLong()))
        .willReturn(Account.builder()
            .bank(Bank.KB)
            .accountType(AccountType.DEPOSIT)
            .balance(0L)
            .accountNumber("1234567890")
            .accountStatus(IN_USE)
            .setLimit(30000000L)
            .build());

    mockMvc.perform(post("/account")
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-AUTH-TOKEN",
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJPOCs1ZVE1UFdTZ1FQb0JpVHdOenBRPT0iLCJqdGkiOiI3K3dXQnczZmFwd2xZcDV3YXZnTTd3PT0iLCJpYXQiOjE2ODgxOTAzOTQsImV4cCI6MTY4ODE5MjE5NH0.xF4xT99DGNzXbjA2I5utpNDuGY2yuI0D3Cv9QUFyLYU")
            .content(objectMapper.writeValueAsString(
                new CreateAccountDto.Request("1234", WOO_RI,
                    10L, 50000000L, INSTALLMENT_SAVING)
            )))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bank").value(Bank.KB.name()))
        .andExpect(jsonPath("$.accountType").value(AccountType.DEPOSIT.name()))
        .andExpect(jsonPath("$.initialBalance").value(0))
        .andExpect(jsonPath("$.accountNumber").value("1234567890"))
        .andExpect(jsonPath("$.setLimit").value(30000000L))
        .andDo(print());
  }

  @Test
  void successDeleteAccount() throws Exception {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(2L, "test@naver.com"));

    given(accountService.deleteAccount(any(), anyString(), anyString(), any(), any()))
        .willReturn(Account.builder()
            .bank(Bank.KB)
            .accountType(AccountType.DEPOSIT)
            .balance(0L)
            .accountNumber("1234567890")
            .accountStatus(IN_USE)
            .build());
    //when
    //then
    mockMvc.perform(delete("/account")
            .header("X-AUTH-TOKEN",
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJPOCs1ZVE1UFdTZ1FQb0JpVHdOenBRPT0iLCJqdGkiOiI3K3dXQnczZmFwd2xZcDV3YXZnTTd3PT0iLCJpYXQiOjE2ODgxOTAzOTQsImV4cCI6MTY4ODE5MjE5NH0.xF4xT99DGNzXbjA2I5utpNDuGY2yuI0D3Cv9QUFyLYU")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new DeleteAccountDto.Request("4416", "1111111111",
                    WOO_RI, INSTALLMENT_SAVING)
            )))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bank").value(Bank.KB.name()))
        .andExpect(jsonPath("$.accountNumber").value("1234567890"))
        .andExpect(jsonPath("$.accountType").value(AccountType.DEPOSIT.name()))
        .andExpect(jsonPath("$.accountStatus").value(IN_USE.name()))
        .andDo(print());
  }

}