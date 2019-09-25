package com.ebcitron.diytracker.services;

import com.ebcitron.diytracker.models.Project;
import com.ebcitron.diytracker.models.User;

import java.util.List;

public interface ProjectService {
    List<Project> returnAllProjects();

    List<Project> findProjectsByUser(User user);

    Project findById(long id);

    Project save(Project project, boolean admin);

    Project update(boolean admin, Project project, long id);

    void delete(long id, boolean admin);


}
