package form;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserForm {

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
