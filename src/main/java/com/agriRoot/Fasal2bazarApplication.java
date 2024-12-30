package com.agriRoot;

import com.agriRoot.entity.Role;
import com.agriRoot.entity.User;
import com.agriRoot.repository.RoleRepo;
import com.agriRoot.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Fasal2bazarApplication  implements CommandLineRunner {
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private UserRepo userRepo;

	public static void main(String[] args) {
		SpringApplication.run(Fasal2bazarApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Role admin = roleRepo.findByRoleName("ROLE_ADMIN").orElse(null);
		Role normal = roleRepo.findByRoleName("ROLE_NORMAL").orElse(null);

		if(admin==null){
			admin=new Role();
			admin.setRoleName("ROLE_ADMIN");
			roleRepo.save(admin);
		}

		if(normal==null){
			normal=new Role();
			normal.setRoleName("ROLE_NORMAL");
			roleRepo.save(normal);
		}

		User user = userRepo.findByEmail("vaib@gmail.com").orElse(null);
		if (user == null) {
			user = new User();
			user.setFullName("vaib");
			user.setEmail("vaib@gmail.com");
			user.setPassword("vaib");
			user.setAdhaarNumber("23412344344");
			user.setPanNumber("NCOP124KP12");
			user.setAddress("Vijay nagar indore");

			// Setting roles for the user
			List<Role> roles = new ArrayList<>();
			roles.add(admin);
			roles.add(normal);// Add admin role
			user.setRoles(List.of(admin));

			userRepo.save(user);
			System.out.println("User is created successfully");
		} else {
			System.out.println("User already exists");
		}
	}
	}

