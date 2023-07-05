package woojkk.fintechProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static woojkk.fintechProject.type.AccountType.DEPOSIT;
import static woojkk.fintechProject.type.AccountType.INSTALLMENT_SAVING;
import static woojkk.fintechProject.type.Bank.HA_NA;
import static woojkk.fintechProject.type.Bank.KB;
import static woojkk.fintechProject.type.Bank.NH;
import static woojkk.fintechProject.type.Bank.SHIN_HAN;
import static woojkk.fintechProject.type.Bank.WOO_RI;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.domain.AccountUser;
import woojkk.fintechProject.exception.AccountException;
import woojkk.fintechProject.exception.ErrorCode;
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

  @Test
  void createAccountUserNotFound() {
    //given

    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.empty());
    //when
    AccountException exception = assertThrows(AccountException.class,
        () -> accountService.createAccount(
            0L, 0L, "0000",
            KB, INSTALLMENT_SAVING, 0L));
    //then
    assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
  }

  @Test
  void successDeleteAccount() {
    //given
    AccountUser user = AccountUser.builder()
        .name("iii")
        .email("ko@naver.com")
        .password("1234")
        .build();

    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.of(user));
    given(accountRepository.findByAccountNumber(anyString()))
        .willReturn(Optional.of(Account.builder()
            .accountUser(user)
            .accountPassword("1")
            .bank(HA_NA)
            .accountNumber("1234567890")
            .balance(0L)
            .accountType(DEPOSIT)
            .build()));
    //when

    Account deleteAccount = accountService.deleteAccount(1L, "1000000000", "1", HA_NA, DEPOSIT);

    //then
    assertEquals("1234567890", deleteAccount.getAccountNumber());
    assertEquals(HA_NA, deleteAccount.getBank());
    assertEquals(DEPOSIT, deleteAccount.getAccountType());
    assertEquals(AccountStatus.UNREGISTERED, deleteAccount.getAccountStatus());
  }


  @Test
  void test_MAX_COUNT_PER_USER() {
    //given
    AccountUser user = AccountUser.builder()
        .name("iii")
        .email("ko@naver.com")
        .password("1")
        .build();

    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.of(user));
    given(accountRepository.countByAccountUserAndAccountTypeAndBank(any(), any(), any()))
        .willReturn(5);
    //when
    AccountException exception = assertThrows(AccountException.class,
        () -> accountService.createAccount(1L, 0L, "1111",
            KB, INSTALLMENT_SAVING, 10L));
    //then
    assertEquals(ErrorCode.MAX_COUNT_PER_USER, exception.getErrorCode());
  }

  @Test
  void test_NOT_FOUND_ACCOUNT() {
    //given
    AccountUser user = AccountUser.builder()
        .name("iii")
        .email("ko@naver.com")
        .password("1234")
        .build();

    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.of(user));

    given(accountRepository.findByAccountNumber(anyString()))
        .willReturn(Optional.empty());
    //when
    AccountException exception = assertThrows(AccountException.class,
        () -> accountService.deleteAccount(1L, "1111111111", "1234",
            SHIN_HAN, DEPOSIT));
    //then
    assertEquals(ErrorCode.NOT_FOUND_ACCOUNT, exception.getErrorCode());
  }

  @Test
  void test_BALANCE_NOT_EMPTY() {
    //given
    AccountUser user = AccountUser.builder()
        .name("iii")
        .email("ko@naver.com")
        .password("1234")
        .build();

    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.of(user));

    given(accountRepository.findByAccountNumber(anyString()))
        .willReturn(Optional.of(Account.builder()
            .accountUser(user)
            .accountPassword("1234")
            .balance(10L)
            .build()));
    //when
    AccountException exception = assertThrows(AccountException.class,
        () -> accountService.deleteAccount(1L, "1111111111", "1234",
            SHIN_HAN, DEPOSIT));
    //then
    assertEquals(ErrorCode.BALANCE_NOT_EMPTY, exception.getErrorCode());
  }

  @Test
  void test_ALREADY_UREGISTERED_ACCOUNT() {
    //given
    AccountUser user = AccountUser.builder()
        .name("iii")
        .email("ko@naver.com")
        .password("1234")
        .build();

    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.of(user));

    given(accountRepository.findByAccountNumber(anyString()))
        .willReturn(Optional.of(Account.builder()
            .accountUser(user)
            .accountPassword("1234")
            .accountStatus(AccountStatus.UNREGISTERED)
            .build()));
    //when
    AccountException exception = assertThrows(AccountException.class,
        () -> accountService.deleteAccount(1L, "1111111111", "1234",
            SHIN_HAN, DEPOSIT));
    //then
    assertEquals(ErrorCode.ALREADY_UNREGISTERED_ACCOUNT, exception.getErrorCode());
  }

  @Test
  void test_ACCOUNT_USER_UNMATCHED() {
    //given
    AccountUser user = AccountUser.builder()
        .name("iii")
        .email("ko@naver.com")
        .password("1234")
        .build();
    user.setId(12L);

    AccountUser user2 = AccountUser.builder()
        .name("aaa")
        .email("bb@naver.com")
        .password("1111")
        .build();
    user2.setId(13L);

    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.of(user));

    given(accountRepository.findByAccountNumber(anyString()))
        .willReturn(Optional.of(Account.builder()
            .accountUser(user2)
            .accountPassword("1234")
            .build()));
    //when
    AccountException exception = assertThrows(AccountException.class,
        () -> accountService.deleteAccount(1L, "1111111111", "1234",
            SHIN_HAN, DEPOSIT));
    //then
    assertEquals(ErrorCode.ACCOUNT_USER_UNMATCHED, exception.getErrorCode());
  }

  @Test
  void test_NOT_MATCHED_PASSWORD() {
    //given
    AccountUser user = AccountUser.builder()
        .name("iii")
        .email("ko@naver.com")
        .password("1234")
        .build();

    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.of(user));

    given(accountRepository.findByAccountNumber(anyString()))
        .willReturn(Optional.of(Account.builder()
            .accountUser(user)
            .accountPassword("1111")
            .build()));
    //when
    AccountException exception = assertThrows(AccountException.class,
        () -> accountService.deleteAccount(1L, "1111111111", "1234",
            SHIN_HAN, DEPOSIT));
    //then
    assertEquals(ErrorCode.NOT_MATCHED_PASSWORD, exception.getErrorCode());
  }

  @Test
  void successGetAccountsByUserId() {
      //given
    AccountUser user = AccountUser.builder()
        .name("kim")
        .email("w@naver.com")
        .password("1")
        .build();

    List<Account> accounts = Arrays.asList(
        Account.builder()
            .accountUser(user)
            .accountNumber("1111111111")
            .bank(KB)
            .build(),
        Account.builder()
            .accountUser(user)
            .accountNumber("2222222222")
            .bank(NH)
            .build(),
        Account.builder()
            .accountUser(user)
            .accountNumber("3333333333")
            .bank(SHIN_HAN)
            .build()
    );
    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.of(user));
    given(accountRepository.findByAccountUser(any()))
        .willReturn(accounts);
      //when
    List<Account> accountList = accountService.getAccountsByUserId(1L);
    //then
    assertEquals(3, accountList.size());
    assertEquals(KB, accountList.get(0).getBank());
    assertEquals(NH, accountList.get(1).getBank());
    assertEquals(SHIN_HAN, accountList.get(2).getBank());
    assertEquals("1111111111", accountList.get(0).getAccountNumber());
    assertEquals("2222222222", accountList.get(1).getAccountNumber());
    assertEquals("3333333333", accountList.get(2).getAccountNumber());

  }

  @Test
  void failedGetAccountsByUserId() {
      //given
    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.empty());
      //when
    AccountException exception = assertThrows(AccountException.class,
        () -> accountService.getAccountsByUserId(1L));
    //then
    assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
  }

  @Test
  void successGetAccountsByUserIdAndBank() {
    //given
    AccountUser user = AccountUser.builder()
        .name("kim")
        .email("w@naver.com")
        .password("1")
        .build();

    List<Account> accounts = Arrays.asList(
        Account.builder()
            .accountUser(user)
            .accountNumber("1111111111")
            .bank(KB)
            .build(),
        Account.builder()
            .accountUser(user)
            .accountNumber("2222222222")
            .bank(NH)
            .build(),
        Account.builder()
            .accountUser(user)
            .accountNumber("3333333333")
            .bank(KB)
            .build()
    );
    given(accountUserRepository.findById(anyLong()))
        .willReturn(Optional.of(user));
    given(accountRepository.findByAccountUserAndBank(any(),any()))
        .willReturn(accounts);
    //when
    List<Account> accountList = accountService.getAccountsByUserIdAndBank(1L, KB);
    //then
    assertEquals(2, accountList.size());
    assertEquals(KB, accountList.get(0).getBank());
    assertEquals("1111111111", accountList.get(0).getAccountNumber());
    assertEquals("3333333333", accountList.get(1).getAccountNumber());

  }
}