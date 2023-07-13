package com.mudit.admin.user;

import org.springframework.data.repository.CrudRepository;

import com.mudit.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{

}
