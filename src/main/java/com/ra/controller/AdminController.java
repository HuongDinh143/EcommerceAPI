package com.ra.controller;

import com.ra.model.dto.UserPermissionDto;
import com.ra.model.dto.UserResponseDto;
import com.ra.model.dto.UserSignUpRequestDto;
import com.ra.model.entity.Role;
import com.ra.service.auth.AuthService;
import com.ra.service.role.RoleService;
import com.ra.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "5") int limit,
            @RequestParam(name = "sortBy", defaultValue = "username") String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy
    ) {
        Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<UserResponseDto> responseDtos = userService.pagination(pageable);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);

    }
    @PatchMapping("users/{id}")
    public ResponseEntity<?> updatePermissionUser(@RequestBody @Valid UserPermissionDto userPermissionDTO, @PathVariable String id) throws Exception {
        UserResponseDto userResponseDto = authService.updatePermission(userPermissionDTO, Long.valueOf(id));
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
    @PatchMapping("/users/{id}/remove-role")
    public ResponseEntity<?> removePermissionUser(
            @RequestBody @Valid UserPermissionDto userPermissionDTO,
            @PathVariable Long id) throws Exception {

        UserResponseDto userResponseDto = authService.removePermission(userPermissionDTO, id);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
    @PutMapping("/users/{userId}")
    public void updateStatusUser(@PathVariable Long userId) throws Exception {
        userService.updateStatusUser(userId);
    }
    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles(){
        List<Role> roles = roleService.getRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }


}
