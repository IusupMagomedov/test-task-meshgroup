package meshgroup.testtask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meshgroup.testtask.dto.request.TransferRequest;
import meshgroup.testtask.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request,
                                         Authentication authentication) {
        Long fromUserId = (Long) authentication.getPrincipal();
        transferService.transfer(fromUserId, request.getToUserId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}
