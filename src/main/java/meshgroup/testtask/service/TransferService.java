package meshgroup.testtask.service;

import lombok.RequiredArgsConstructor;
import meshgroup.testtask.entity.Account;
import meshgroup.testtask.exception.BusinessException;
import meshgroup.testtask.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (fromUserId.equals(toUserId)) {
            throw new BusinessException("Cannot transfer to yourself", HttpStatus.BAD_REQUEST);
        }

        // Acquire locks in consistent order to prevent deadlocks
        Long firstId = Math.min(fromUserId, toUserId);
        Long secondId = Math.max(fromUserId, toUserId);

        Account first = accountRepository.findByUserIdWithLock(firstId)
                .orElseThrow(() -> new BusinessException("Account not found", HttpStatus.NOT_FOUND));
        Account second = accountRepository.findByUserIdWithLock(secondId)
                .orElseThrow(() -> new BusinessException("Account not found", HttpStatus.NOT_FOUND));

        Account sender = fromUserId.equals(firstId) ? first : second;
        Account receiver = fromUserId.equals(firstId) ? second : first;

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Insufficient funds", HttpStatus.BAD_REQUEST);
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }
}
