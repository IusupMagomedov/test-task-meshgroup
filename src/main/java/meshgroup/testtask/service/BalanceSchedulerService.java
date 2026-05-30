package meshgroup.testtask.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meshgroup.testtask.entity.Account;
import meshgroup.testtask.repository.AccountRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceSchedulerService {

    private static final BigDecimal INTEREST_RATE = new BigDecimal("1.05");
    private static final BigDecimal MAX_MULTIPLIER = new BigDecimal("2.07");

    private final AccountRepository accountRepository;

    @Scheduled(fixedRateString = "${app.scheduler.interval-ms:30000}")

    @Transactional
    public void accrueInterest() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            BigDecimal maxBalance = account.getInitialBalance()
                    .multiply(MAX_MULTIPLIER)
                    .setScale(2, RoundingMode.HALF_DOWN);

            if (account.getBalance().compareTo(maxBalance) >= 0) {
                continue;
            }

            BigDecimal newBalance = account.getBalance()
                    .multiply(INTEREST_RATE)
                    .setScale(2, RoundingMode.HALF_DOWN);

            if (newBalance.compareTo(maxBalance) > 0) {
                newBalance = maxBalance;
            }

            account.setBalance(newBalance);
        }

        accountRepository.saveAll(accounts);
        log.debug("Balance accrual completed for {} accounts", accounts.size());
    }
}
