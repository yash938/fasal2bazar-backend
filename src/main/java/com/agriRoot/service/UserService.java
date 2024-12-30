package com.agriRoot.service;

import com.agriRoot.domain.PaegableResponse;
import com.agriRoot.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(int id);
    UserDto updateUser(int id,UserDto userDto);
    PaegableResponse<UserDto> allUser(int pageNumber, int pageSize, String sortBy, String sortDir);
    void deleteUser(int id);
    UserDto findUserByEmail(String email);
    List<UserDto> searchByName(String keyword);

}
