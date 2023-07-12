package woojkk.fintechProject.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import woojkk.fintechProject.exception.AccountException;
import woojkk.fintechProject.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockService {
  private final RedissonClient redissonClient;

  public void lock(Long accountId) {
    RLock lock = redissonClient.getLock(getLockKey(accountId));
    log.debug("Trying lock for accountId : {}", accountId);

    try {
      boolean isLock = lock.tryLock(1, 3, TimeUnit.SECONDS);
      if (!isLock) {
        log.error("Lock acquisition failed");
        throw new AccountException(ErrorCode.ACCOUNT_TRANSACTION_LOCK);
      }
    } catch (AccountException e) {
      throw e;
    }  catch (Exception e) {
      log.error("Redis lock failed", e);
    }
  }

  public void unlock(Long accountId) {
    log.debug("Unlock for accountId : {}", accountId);
    redissonClient.getLock(getLockKey(accountId)).unlock();
  }

  private static String getLockKey(Long accountId) {
    return "ACCOUNTLOCKKKEY:" + accountId;
  }
}
