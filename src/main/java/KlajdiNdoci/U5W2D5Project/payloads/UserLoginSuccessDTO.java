package KlajdiNdoci.U5W2D5Project.payloads;

import jakarta.validation.constraints.NotEmpty;

public record UserLoginSuccessDTO(
        @NotEmpty
        String accessToken
) {
}
