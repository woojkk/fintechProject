package woojkk.fintechProject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import woojkk.fintechProject.dto.LoginForm;
import woojkk.fintechProject.service.LoginService;

@RestController
@RequiredArgsConstructor
public class AccountUserController {

  private final LoginService loginService;
  //final을 적지 않아 값이 들어가지 않았고 nullPointException 발생

  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@RequestBody LoginForm form) {
    return ResponseEntity.ok(loginService.userLoginToken(form));
  }

}
