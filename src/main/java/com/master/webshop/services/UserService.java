package com.master.webshop.services;

import com.master.webshop.model.User;

public interface UserService {

    User findUserByUsername(String username);

    void saveUser(User user);

}