package meshgroup.testtask.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransferRequest {

    @NotNull
    private Long toUserId;

    @NotNull
    @Positive
    private BigDecimal amount;
}
