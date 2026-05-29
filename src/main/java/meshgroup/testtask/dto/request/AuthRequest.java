package meshgroup.testtask.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @NotBlank
    @Schema(example = "79201234567")
    private String login;

    @NotBlank
    @Schema(example = "password123")
    private String password;
}
