package woojkk.fintechProject.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import woojkk.fintechProject.config.JwtAuthenticationProvider;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.domain.UserVo;
import woojkk.fintechProject.dto.AccountInfo;
import woojkk.fintechProject.dto.CreateAccountDto;
import woojkk.fintechProject.dto.DeleteAccountDto;
import woojkk.fintechProject.dto.DeleteAccountDto.Response;
import woojkk.fintechProject.service.AccountService;
import woojkk.fintechProject.type.Bank;

@RestController
@RequiredArgsConstructor
public class AccountController {

  private final JwtAuthenticationProvider provider;

  private final AccountService accountService;

  @PostMapping("/account")
  public CreateAccountDto.Response createAccount(@RequestHeader(name = "X-AUTH-TOKEN") String token,
      @RequestBody @Valid CreateAccountDto.Request request) {
    UserVo vo = provider.getUserVo(token);
    Account account = accountService.createAccount(
        vo.getId(),
        request.getInitialBalance(),
        request.getAccountPassword(),
        request.getBank(),
        request.getAccountType(),
        request.getSetLimit());

    return CreateAccountDto.Response.from(account);
  }

  @DeleteMapping("/account")
  public DeleteAccountDto.Response deleteAccount(@RequestHeader(name = "X-AUTH-TOKEN") String token,
      @RequestBody @Valid DeleteAccountDto.Request request
  ) {
    UserVo vo = provider.getUserVo(token);

    return Response.from(
        accountService.deleteAccount(
            vo.getId(),
            request.getAccountNumber(),
            request.getAccountPassword(),
            request.getBank(),
            request.getAccountType()
        ));
  }

    @GetMapping("/account")
    public List<AccountInfo> getAccountByUserId(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                @RequestParam("user_id") Long userId) {

      return accountService.getAccountsByUserId(userId)
          .stream().map(account -> AccountInfo.builder()
              .bank(account.getBank())
              .accountType(account.getAccountType())
              .accountNumber(account.getAccountNumber())
              .accountStatus(account.getAccountStatus())
              .balance(account.getBalance())
              .build())
          .collect(Collectors.toList());
    }

  @GetMapping("/account/{user_id}/bank")
  public List<AccountInfo> getAccountByUserIdAndUser(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                     @PathVariable("user_id") Long userId,
                                                     @RequestParam Bank bank) {

    return accountService.getAccountsByUserIdAndBank(userId, bank)
        .stream().map(account -> AccountInfo.builder()
            .bank(account.getBank())
            .accountType(account.getAccountType())
            .accountNumber(account.getAccountNumber())
            .accountStatus(account.getAccountStatus())
            .balance(account.getBalance())
            .build())
        .collect(Collectors.toList());
  }
}
