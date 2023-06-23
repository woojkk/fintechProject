package woojkk.fintechProject.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.dto.AccountDto;
import woojkk.fintechProject.service.AccountService;

@RestController
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/account")
  public AccountDto.Response createAccount(
      @RequestBody @Valid AccountDto.Request request) {
    Account account = accountService.createAccount(
        request.getUserId(),
        request.getInitialBalance(),
        request.getAccountPassword(),
        request.getBank(),
        request.getAccountType(),
        request.getSetLimit());

    return AccountDto.Response.from(account);
  }
}
