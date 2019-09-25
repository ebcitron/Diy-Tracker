package com.ebcitron.diytracker.services;

import com.lambdaschool.diytracker.models.Useremail;

import java.util.List;

public interface UseremailService
{
        List<Useremail> returnAll();

        Useremail findUserEmailById(long id);

        List<Useremail> findByUserName(String username);

        void delete(long id, boolean isAdmin);

        Useremail save(Useremail useremail, boolean isAdmin);
        }
