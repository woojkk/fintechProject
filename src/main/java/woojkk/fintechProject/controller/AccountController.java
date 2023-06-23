package woojkk.fintechProject.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import woojkk.fintechProject.dto.AccountDto;
import woojkk.fintechProject.dto.CreateAccount;
import woojkk.fintechProject.service.AccountService;

@RestController
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/account/create")
  public CreateAccount.Response createAccount(
      @RequestBody @Valid CreateAccount.Request request) {
    AccountDto accountDto = accountService.createAccount(
        request.getUserId(),
        request.getInitialBalance(),
        request.getAccountPassword(),
        request.getBank(),
        request.getAccountType(),
        request.getSetLimit());

    return CreateAccount.Response.from(accountDto);
  }
}
