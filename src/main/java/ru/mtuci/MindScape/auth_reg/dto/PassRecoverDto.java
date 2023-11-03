/**
 * <p>Описание:</p>
 * Класс DTO для процедуры восстановления пароля.
 * Содержит поля для хранения адреса электронной почты, нового пароля, подтверждения нового пароля и кода подтверждения.
 */

package ru.mtuci.MindScape.auth_reg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassRecoverDto {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 6)
    private String password;

    @NotEmpty
    private String confirmPassword;

    private String code;
}