package com.ebcitron.diytracker.repositorys;

import com.ebcitron.diytracker.models.Project;
import com.ebcitron.diytracker.models.Project;
import com.ebcitron.diytracker.models.User;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepo extends CrudRepository<Project, Long> {

    Project findById(long id);

    Project findProjectsByUser(User user);




}
