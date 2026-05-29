package meshgroup.testtask.service;

import lombok.RequiredArgsConstructor;
import meshgroup.testtask.dto.request.AuthRequest;
import meshgroup.testtask.dto.response.AuthResponse;
import meshgroup.testtask.entity.User;
import meshgroup.testtask.exception.BusinessException;
import meshgroup.testtask.repository.UserRepository;
import meshgroup.testtask.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmails_Email(request.getLogin())
                .or(() -> userRepository.findByPhones_Phone(request.getLogin()))
                .orElseThrow(() -> new BusinessException("Invalid login or password", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid login or password", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtTokenProvider.generateToken(user.getId());
        return new AuthResponse(token);
    }
}
