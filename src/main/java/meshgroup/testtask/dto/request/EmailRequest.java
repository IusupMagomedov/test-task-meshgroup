package meshgroup.testtask.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailRequest {

    @NotBlank
    @Email
    private String email;
}
