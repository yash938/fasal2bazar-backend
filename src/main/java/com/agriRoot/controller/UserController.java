package com.agriRoot.controller;

import com.agriRoot.domain.AllApiHandlers;
import com.agriRoot.domain.ImageResponse;
import com.agriRoot.domain.PaegableResponse;
import com.agriRoot.dto.UserDto;
import com.agriRoot.service.ImageFile;
import com.agriRoot.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    @Autowired
    private UserService userService;

    @Autowired
    private ImageFile imageFile;
    @GetMapping("/welcome")
    public String hello(){
        return "hello Springboot";
    }

    @PostMapping
    public ResponseEntity<UserDto> createUsers(@Valid @RequestBody UserDto userDto){
        UserDto createUser = userService.createUser(userDto);
        return new ResponseEntity<>(createUser, HttpStatus.CREATED);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserDto> update(@PathVariable int id,@RequestBody UserDto userDto){
        UserDto updateUser = userService.updateUser(id, userDto);
        return new ResponseEntity<>(updateUser,HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<PaegableResponse<UserDto>> allUser(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "20",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "fullName",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PaegableResponse<UserDto> userDtoPaegableResponse = userService.allUser(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoPaegableResponse,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable int id){
        UserDto userById = userService.getUserById(id);
        return new ResponseEntity<>(userById,HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> userByEmail(@PathVariable String email){
        UserDto userByEmail = userService.findUserByEmail(email);
        return new ResponseEntity<>(userByEmail,HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUser(@RequestParam String keyword){
        List<UserDto> userDtos = userService.searchByName(keyword);
        return new ResponseEntity<>(userDtos,HttpStatus.FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AllApiHandlers> deleteUser(@PathVariable int id){
        userService.deleteUser(id);
        AllApiHandlers delete = AllApiHandlers.builder().message("User deleted successfully").success(true).status(HttpStatus.OK).date(LocalDate.now()).build();
        return new ResponseEntity<>(delete,HttpStatus.OK);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<UserDto> uploadFile(@PathVariable int id, @RequestParam("image") MultipartFile image){
        UserDto singleUser = userService.getUserById(id);
        String imageName = imageFile.uploadFile(image, imageUploadPath);
        singleUser.setImage(imageName);
        UserDto userDto = userService.updateUser(id, singleUser);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @GetMapping("/image/{id}")
    public void ServeImage(@PathVariable int id, HttpServletResponse response) throws IOException {
        UserDto singleUser = userService.getUserById(id);
        log.info("user image name {}",singleUser.getImage());
        InputStream resource = imageFile.getResource(imageUploadPath, singleUser.getImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response. getOutputStream());
    }

}
