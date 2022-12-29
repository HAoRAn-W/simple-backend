package one.whr.simple.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    @Email
    private String password;

    private String website;

    private String description;

//    private Set<String> roles;
}
