package woojkk.fintechProject.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static woojkk.fintechProject.type.AccountType.INSTALLMENT_SAVING;
import static woojkk.fintechProject.type.Bank.WOO_RI;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.dto.CreateAccountDto;
import woojkk.fintechProject.dto.DeleteAccountDto;
import woojkk.fintechProject.service.AccountService;
import woojkk.fintechProject.type.AccountStatus;
import woojkk.fintechProject.type.AccountType;
import woojkk.fintechProject.type.Bank;

@WebMvcTest(AccountController.class)
class AccountControllerTest {


  @MockBean
  private AccountService accountService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;



  @Test
  void successCreateAccount() throws Exception {
    //given
    given(accountService.createAccount(anyLong(), anyLong(), any(), any(), any(), anyLong()))
        .willReturn(Account.builder()
            .bank(Bank.KB)
            .accountType(AccountType.DEPOSIT)
            .balance(0L)
            .accountNumber("1234567890")
            .accountStatus(AccountStatus.IN_USE)
            .setLimit(30000000L)
            .registeredAt(LocalDateTime.now())
            .build());
    //when
    //then
    mockMvc.perform(post("/account")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new CreateAccountDto.Request("1234", WOO_RI,
                    10L, 50000000L, INSTALLMENT_SAVING)
            )))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bank").value(Bank.KB.name()))
        .andExpect(jsonPath("$.accountType").value(AccountType.DEPOSIT.name()))
        .andExpect(jsonPath("$.initialBalance").value(0))
        .andExpect(jsonPath("$.accountNumber").value("1234567890"))
        .andExpect(jsonPath("$.accountStatus").value(AccountStatus.IN_USE.name()))
        .andExpect(jsonPath("$.setLimit").value(30000000L))
        .andExpect(jsonPath("$.registeredAt").value(LocalDateTime.now()))
        .andDo(print());
  }

  @Test
  void successDeleteAccount() throws Exception {
    //given
    given(accountService.deleteAccount(any(), anyString(), anyString(), any(), any()))
        .willReturn(Account.builder()
            .bank(Bank.KB)
            .accountNumber("1234567890")
            .accountType(AccountType.DEPOSIT)
            .accountStatus(AccountStatus.IN_USE)
            .unRegisteredAt(LocalDateTime.now())
            .build());
    //when
    //then
    mockMvc.perform(post("/account")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new DeleteAccountDto.Request("1234", "1111111111",
                    WOO_RI, INSTALLMENT_SAVING)
            )))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bank").value(Bank.KB.name()))
        .andExpect(jsonPath("$.accountType").value(AccountType.DEPOSIT.name()))
        .andExpect(jsonPath("$.initialBalance").value(0))
        .andExpect(jsonPath("$.accountNumber").value("1234567890"))
        .andExpect(jsonPath("$.setLimit").value(30000000L))
        .andExpect(jsonPath("$.accountStatus").value(AccountStatus.IN_USE.name()))
        .andDo(print());
  }

}