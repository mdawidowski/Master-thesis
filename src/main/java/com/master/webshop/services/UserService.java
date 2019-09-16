package com.master.webshop.services;

import com.master.webshop.model.User;

public interface UserService {

    public User findUserByEmail(String email);

    public void saveUser(User user);
}