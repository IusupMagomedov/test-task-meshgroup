package meshgroup.testtask.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private List<String> emails;
    private List<String> phones;
}
