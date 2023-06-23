package woojkk.fintechProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static woojkk.fintechProject.type.AccountType.DEPOSIT;
import static woojkk.fintechProject.type.AccountType.INSTALLMENT_SAVING;
import static woojkk.fintechProject.type.Bank.KB;
import static woojkk.fintechProject.type.Bank.WOO_RI;

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
            .balance(0L)
            .accountNumber("1000000000")
            .bank(Bank.WOO_RI)
            .setLimit(50000000L)
            .accountType(AccountType.INSTALLMENT_SAVING)
            .accountStatus(AccountStatus.IN_USE)
            .registeredAt(LocalDateTime.now())
            .unRegisteredAt(LocalDateTime.now())
            .build());

    ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

    //when
    Account account = accountService.createAccount(1L, 10L, "1234", KB, DEPOSIT, 30000000L);

    //then
    verify(accountRepository, times(1)).save(captor.capture());
    assertEquals(4L, account.getAccountUser().getId());
    assertEquals(0, account.getBalance());
    assertEquals("1", account.getAccountPassword());
    assertEquals("1000000000", account.getAccountNumber());
    assertEquals(WOO_RI, account.getBank());
    assertEquals(50000000, account.getSetLimit());
    assertEquals(INSTALLMENT_SAVING, account.getAccountType());
    assertEquals(AccountStatus.IN_USE, account.getAccountStatus());
  }
}