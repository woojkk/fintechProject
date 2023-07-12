package woojkk.fintechProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import woojkk.fintechProject.aop.AccountLockIdInterface;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
  private final LockService lockService;

  @Around("@annotation(woojkk.fintechProject.aop.AccountLock) && args(request)")
  public Object aroundMethod(
      ProceedingJoinPoint pjp,
      AccountLockIdInterface request
  ) throws Throwable {
    lockService.lock(request.getAccountId());
    try {
      return pjp.proceed();
    } finally {
      lockService.unlock(request.getAccountId());
    }
  }
}
