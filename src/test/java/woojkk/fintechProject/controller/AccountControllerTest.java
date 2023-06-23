package woojkk.fintechProject.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import woojkk.fintechProject.dto.AccountDto;
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
            .id(1L)
            .accountPassword("5678")
            .bank(Bank.KB)
            .accountNumber("1234567890")
            .balance(0L)
            .setLimit(30000000L)
            .accountType(AccountType.DEPOSIT)
            .accountStatus(AccountStatus.IN_USE)
            .registeredAt(LocalDateTime.now())
            .unRegisteredAt(LocalDateTime.now())
            .build());
    //when
    //then
    mockMvc.perform(post("/account")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new AccountDto.Request(3333L, "1234",  WOO_RI,
                    10L,50000000L, INSTALLMENT_SAVING)
            )))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accountId").value(1))
        .andExpect(jsonPath("$.bank").value(Bank.KB.name()))
        .andExpect(jsonPath("$.accountNumber").value("1234567890"))
        .andExpect(jsonPath("$.initialBalance").value(0))
        .andExpect(jsonPath("$.setLimit").value(30000000L))
        .andExpect(jsonPath("$.accountType").value(AccountType.DEPOSIT.name()))
        .andExpect(jsonPath("$.accountStatus").value(AccountStatus.IN_USE.name()))
        .andDo(print());
  }
}