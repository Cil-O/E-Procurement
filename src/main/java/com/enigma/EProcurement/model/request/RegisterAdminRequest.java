package com.enigma.EProcurement.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RegisterAdminRequest {
    @NotBlank(message = "email is required")
    @Email(message = "invalid email")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
    @NotBlank(message = "store name is required")
    private String name;
}
