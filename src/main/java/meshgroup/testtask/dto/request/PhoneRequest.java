package meshgroup.testtask.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PhoneRequest {

    @NotBlank
    @Pattern(regexp = "\\d{11,13}", message = "Phone must contain 11-13 digits")
    private String phone;
}
