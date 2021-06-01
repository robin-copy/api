package com.robincopy.robincopyapi.models;

import com.robincopy.robincopyapi.dto.UserDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_data")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AbstractEntity {

    private String firstName;

    private String lastName;

    public static User from(UserDto userDto){
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build();
    }
}
