package com.ebcitron.diytracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long projectId;

    @Column(nullable = false)
    private String projectName;

    @ManyToOne
    @JoinColumn(name = "userid",
            nullable = false)
    @JsonIgnoreProperties("projects")
    private User user;

    private String description;




    private String photoUrl;

    private List<String> materials;

    private List<String> steps;

    public Project() {
    }

    public Project(String projectName, User user, String description, String photoUrl, List<String> materials, List<String> steps) {
        this.projectName = projectName;
        this.user = user;
        this.description = description;
        this.photoUrl = photoUrl;
        this.materials = materials;
        this.steps = steps;
    }

    public long getProjectId() {
        return projectId;
    }

//    public void setProjectId(long projectId) {
//        this.projectId = projectId;
//    }

    public User getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }


    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", user=" + user +
                ", description='" + description + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", materials=" + materials +
                ", steps=" + steps +
                '}';
    }
}
