package com.master.webshop.services;

import com.master.webshop.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User findUserByUsername(String username);

    void saveUser(User user);

}