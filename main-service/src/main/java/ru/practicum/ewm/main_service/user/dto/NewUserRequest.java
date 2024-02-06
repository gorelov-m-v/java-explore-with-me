package ru.practicum.ewm.main_service.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NewUserRequest {

    @NotNull
    @NotBlank
    @Email
    @Size(max = 254, min = 6)
    private String email;
    @NotBlank
    @NotNull
    @Size(max = 250, min = 2)
    private String name;
}
