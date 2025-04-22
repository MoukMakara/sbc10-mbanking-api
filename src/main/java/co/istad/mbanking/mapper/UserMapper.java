package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Role;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.auth.dto.RegisterRequest;
import co.istad.mbanking.features.auth.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(target = "roles", source = "roles")
//    UserResponse toUserResponse(User user);

    User fromRegisterRequest(RegisterRequest registerRequest);

    // Custom role mapping
//    default List<String> mapRolesToStrings(List<Role> roles) {
//        if (roles == null) return null;
//        return roles.stream()
//                .map(Role::getName) // adjust based on Role entity
//                .toList();
//    }
}


