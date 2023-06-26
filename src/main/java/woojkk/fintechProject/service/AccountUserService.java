package woojkk.fintechProject.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import woojkk.fintechProject.domain.AccountUser;
import woojkk.fintechProject.dto.LoginForm;
import woojkk.fintechProject.repository.AccountUserRepository;

@Service
@RequiredArgsConstructor
public class AccountUserService {

  private final AccountUserRepository accountUserRepository;

  public AccountUser login(LoginForm form) {
    Optional<AccountUser> userEmail = accountUserRepository.findByEmail(form.getEmail());
    if (userEmail.isPresent()) {
      return userEmail.get();
    } else {
      AccountUser accountUser = AccountUser.from(form);
      return accountUserRepository.save(accountUser);
    }
  }

  public Optional<AccountUser> findByIdAndEmail(Long id, String email) {
    return accountUserRepository.findById(id)
        .stream().filter(accountUser -> accountUser.getEmail()
            .equals(email))
        .findFirst();
  }

  public Optional<AccountUser> findValidAccountUser(String email, String password) {
    return accountUserRepository.findByEmail(email)
        .stream()
        .filter(accountUser -> accountUser.getPassword().equals(password))
        .findFirst();
  }

}
