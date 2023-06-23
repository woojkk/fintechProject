package woojkk.fintechProject.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static woojkk.fintechProject.type.AccountType.DEPOSIT;
import static woojkk.fintechProject.type.Bank.KB;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.domain.AccountUser;
import woojkk.fintechProject.dto.AccountDto;
import woojkk.fintechProject.repository.AccountRepository;
import woojkk.fintechProject.repository.AccountUserRepository;
import woojkk.fintechProject.type.AccountStatus;
import woojkk.fintechProject.type.AccountType;
import woojkk.fintechProject.type.Bank;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private AccountUserRepository accountUserRepository;

  @InjectMocks
  private AccountService accountService;

  @Test
  void successCreateAccountService() {
    //given
    AccountUser user = AccountUser.builder()
        .name("iii")
        .email("ko@naver.com")
        .password("1")
        .build();
    user.setId(4L);

    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.of(user));
    given(accountRepository.save(any()))
        .willReturn(Account.builder()
            .accountUser(user)
            .accountPassword("1")
            .bank(Bank.WOO_RI)
            .accountType(AccountType.INSTALLMENT_SAVING)
            .accountStatus(AccountStatus.IN_USE)
            .setLimit(50000000L)
            .accountNumber("1000000000")
            .balance(0L)
            .registeredAt(LocalDateTime.now())
            .unRegisteredAt(LocalDateTime.now())
            .build());

    ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

    //when
    AccountDto accountDto = accountService.createAccount(1L, 0L, "1", KB, DEPOSIT, 50000000L);

    //then
    verify(accountRepository, times(1)).save(captor.capture());
    assertEquals(4L, accountDto.getUserId());
  }
}