package form;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterForm {

    //@NotBlank Determine if exists blank in a String
    //@NotNull Determine if null
    //@NotEmpty 用于集合， such as List
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}
