package com.ebcitron.diytracker.services;
// Need help debugging
import com.ebcitron.diytracker.exceptions.ResourceFoundException;
import com.ebcitron.diytracker.exceptions.ResourceNotFoundException;
import com.ebcitron.diytracker.models.Role;
import com.ebcitron.diytracker.models.User;
import com.ebcitron.diytracker.models.UserRoles;
import com.ebcitron.diytracker.models.Useremail;
import com.ebcitron.diytracker.repositorys.RoleRepository;
import com.ebcitron.diytracker.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService
{

    @Autowired
    private UserRepository userRepos;

    @Autowired
    private RoleRepository roleRepos;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepos.findByUsername(username);
        if (user == null)
        {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthority());
    }

    public User findUserById(long id) throws ResourceNotFoundException
    {
        return userRepos.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found!"));
    }

    @Override
    public List<User> findAll()
    {
        List<User> list = new ArrayList<>();
        userRepos.findAll()
                 .iterator()
                 .forEachRemaining(list::add);
        return list;
    }

    @Transactional
    @Override
    public void delete(long id)
    {
        userRepos.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found!"));
        userRepos.deleteById(id);
    }

    @Override
    public User findByName(String name)
    {
        User uu = userRepos.findByUsername(name);
        if (uu == null)
        {
            throw new ResourceNotFoundException("User name " + name + " not found!");
        }
        return uu;
    }

    @Transactional
    @Override
    public User save(User user)
    {
        if (userRepos.findByUsername(user.getUsername()) != null)
        {
            throw new ResourceFoundException(user.getUsername() + " is already taken!");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPasswordNoEncrypt(user.getPassword());

        ArrayList<UserRoles> newRoles = new ArrayList<>();
        for (UserRoles ur : user.getUserroles())
        {
            long id = ur.getRole()
                        .getRoleid();
            Role role = roleRepos.findById(id)
                                 .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found!"));
            newRoles.add(new UserRoles(newUser, ur.getRole()));
        }
        newUser.setUserroles(newRoles);

        for (Useremail ue : user.getUseremails())
        {
            newUser.getUseremails()
                   .add(new Useremail(newUser, ue.getUseremail()));
        }

        return userRepos.save(newUser);
    }


    @Transactional
    @Override
    public User update(User user, long id, boolean isAdmin)
    {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        User currentUser = userRepos.findByUsername(authentication.getName());

        if (id == currentUser.getUserid() || isAdmin)
        {
            if (user.getUsername() != null)
            {
                currentUser.setUsername(user.getUsername());
            }

            if (user.getPassword() != null)
            {
                currentUser.setPasswordNoEncrypt(user.getPassword());
            }

            if (user.getUserroles()
                    .size() > 0)
            {
                throw new ResourceFoundException("User Roles are not updated through User");
            }

            if (user.getUseremails()
                    .size() > 0)
            {
                for (Useremail ue : user.getUseremails())
                {
                    currentUser.getUseremails()
                               .add(new Useremail(currentUser, ue.getUseremail()));
                }
            }

            return userRepos.save(currentUser);
        } else
        {
            throw new ResourceNotFoundException(id + " Not current user");
        }
    }

    @Transactional
    @Override
    public void deleteUserRole(long userid, long roleid)
    {
        userRepos.findById(userid)
                 .orElseThrow(() -> new ResourceNotFoundException("User id " + userid + " not found!"));
        roleRepos.findById(roleid)
                 .orElseThrow(() -> new ResourceNotFoundException("Role id " + roleid + " not found!"));

        if (roleRepos.checkUserRolesCombo(userid, roleid)
                     .getCount() > 0)
        {
            roleRepos.deleteUserRoles(userid, roleid);
        } else
        {
            throw new ResourceNotFoundException("Role and User Combination Does Not Exists");
        }
    }

    @Transactional
    @Override
    public void addUserRole(long userid, long roleid)
    {
        userRepos.findById(userid)
                 .orElseThrow(() -> new ResourceNotFoundException("User id " + userid + " not found!"));
        roleRepos.findById(roleid)
                 .orElseThrow(() -> new ResourceNotFoundException("Role id " + roleid + " not found!"));

        if (roleRepos.checkUserRolesCombo(userid, roleid)
                     .getCount() <= 0)
        {
            roleRepos.insertUserRoles(userid, roleid);
        } else
        {
            throw new ResourceFoundException("Role and User Combination Already Exists");
        }
    }
}
