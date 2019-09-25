package com.ebcitron.diytracker.services;

import com.ebcitron.diytracker.exceptions.ResourceNotFoundException;
import com.ebcitron.diytracker.models.Project;
import com.ebcitron.diytracker.models.User;
import com.ebcitron.diytracker.repositorys.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepo projectRepos;


    @Override
    public List<Project> returnAllProjects() {
        List<Project> projects = new ArrayList<>();
        projectRepos.findAll().iterator().forEachRemaining(projects::add);
        return projects;
    }

    @Override
    public List<Project> findProjectsByUser(User user) {
        return (List<Project>) projectRepos.findProjectsByUser(user);
    }

    @Override
    public Project findById(long id) {
       return projectRepos.findById(id).orElseThrow(()-> new ResourceNotFoundException("Project with ID \" + id + \" Can not be found!"));
    }

    @Override
    public Project save(Project project, boolean admin) {
        if (projectRepos.findProjectsByUser(project.getProjectName()) != null)
        {
            throw new ResourceNotFoundException(project.getProjectName() + " Is already Taken!");
        }
        Project newProject = new Project();

        newProject.setProjectName(project.getProjectName());
        newProject.setDescription(project.getDescription());
    }

    @Override
    public Project update(boolean admin, Project project, long id) {
        return null;
    }

    @Override
    public void delete(long id, boolean admin){
        if (projectRepos.findById(id).isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            throw new ResourceNotFoundException("Project with ID " + id + " Can not be found!");
        }
        // .orElseThrow(() -> new ResourceNotFoundException("Project with ID " + id + " Can not be found!"));
        projectRepos.deleteById(id);
    }
    }
}
