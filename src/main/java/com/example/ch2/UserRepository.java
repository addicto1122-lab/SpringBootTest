package com.example.ch2;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends  CrudRepository<User, Long> {
}
