package com.example.application.data.service;

import com.example.application.data.entity.Users;
import com.example.application.data.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> findAllUsers(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return usersRepository.findAll();
        } else {
            return usersRepository.search(stringFilter);
        }
    }

    public void deleteUsers(Users users) {
        usersRepository.delete(users);
    }

    public void saveUsers(Users users) {
        if (users == null) {
            System.err.println("Users is null. Are you sure you have connected your form to the application?");
            return;
        }
        usersRepository.save(users);
    }

    public List<Users> findAllRequesters() {
        return usersRepository.findAll();
    }

    public List<Users> findAllExecutors() {
        return usersRepository.findAll();
    }

}
