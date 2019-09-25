package com.ebcitron.diytracker.repositorys;

import com.lambdaschool.diytracker.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>
{
    User findByUsername(String username);
}
