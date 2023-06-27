package woojkk.fintechProject.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import woojkk.fintechProject.config.JwtAuthenticationProvider;
import woojkk.fintechProject.domain.Account;
import woojkk.fintechProject.domain.UserVo;
import woojkk.fintechProject.dto.CreateAccountDto;
import woojkk.fintechProject.dto.DeleteAccountDto;
import woojkk.fintechProject.dto.DeleteAccountDto.Response;
import woojkk.fintechProject.service.AccountService;

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
  public Response deleteAccount(@RequestHeader(name = "X-AUTH-TOKEN") String token,
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
}
