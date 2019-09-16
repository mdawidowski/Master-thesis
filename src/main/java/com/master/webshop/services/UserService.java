package com.master.webshop.services;

import com.master.webshop.model.User;

public interface UserService {

    public User findUserByUsername(String username);

    public void saveUser(User user);
}