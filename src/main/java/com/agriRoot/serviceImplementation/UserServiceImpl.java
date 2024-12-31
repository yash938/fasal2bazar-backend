package com.agriRoot.serviceImplementation;

import com.agriRoot.Utils.Helper;
import com.agriRoot.domain.PaegableResponse;
import com.agriRoot.dto.UserDto;
import com.agriRoot.entity.Role;
import com.agriRoot.entity.User;
import com.agriRoot.repository.RoleRepo;
import com.agriRoot.repository.UserRepo;
import com.agriRoot.Exception.UserNotFound;
import com.agriRoot.service.ImageFile;
import com.agriRoot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Autowired
    private ImageFile imageFile;
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Role role = roleRepo.findByRoleName("ROLE_NORMAl").orElseThrow(() -> new UserNotFound("role is not found"));
        user.setRoles(List.of(role));
        User saveUser = userRepo.save(user);
        UserDto createUser = modelMapper.map(saveUser, UserDto.class);
        return createUser;
    }

    @Override
    public UserDto getUserById(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFound("User is not found"));
        UserDto findUser = modelMapper.map(user, UserDto.class);
        return findUser;
    }

    @Override
    public UserDto updateUser(int id,UserDto userDto) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFound("User is not found"));
        user.setFullName(userDto.getFullName());
        user.setPanNumber(userDto.getPanNumber());
        user.setEmail(userDto.getEmail());
        user.setAdhaarNumber(userDto.getAdhaarNumber());
        user.setImage(userDto.getImage());
        User updateUser = userRepo.save(user);
        return modelMapper.map(updateUser,UserDto.class);
    }

    @Override
    public PaegableResponse<UserDto> allUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortBy.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending()) ;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> all = userRepo.findAll(pageRequest);
        PaegableResponse<UserDto> paegable = Helper.getPaegable(all, UserDto.class);
        return paegable;
    }

    @Override
    public void deleteUser(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFound("User is not found"));
        String path = imagePath + user.getImage();

        try{
            Path path1 = Paths.get(path);
            Files.delete(path1);
        }catch (NoSuchFileException ex){
            log.info("User image not found in folder {}");
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        userRepo.delete(user);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFound("User with given email is not found"));
        UserDto userEmail = modelMapper.map(user, UserDto.class);
        return userEmail;
    }

    @Override
    public List<UserDto> searchByName(String keyword) {
        List<User> byNameContaining = userRepo.findByFullNameContaining(keyword);
        List<UserDto> collect = byNameContaining.stream().map(byName -> modelMapper.map(byNameContaining, UserDto.class)).collect(Collectors.toList());
        return collect;
    }


}
