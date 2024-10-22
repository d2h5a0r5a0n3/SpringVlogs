package com.mypackage.SpringStarter.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mypackage.SpringStarter.models.Account;
import com.mypackage.SpringStarter.models.Authority;
import com.mypackage.SpringStarter.models.Post;
import com.mypackage.SpringStarter.services.AccountService;
import com.mypackage.SpringStarter.services.AuthorityService;
import com.mypackage.SpringStarter.services.PostService;
import com.mypackage.SpringStarter.util.constants.Prvilages;
import com.mypackage.SpringStarter.util.constants.Roles;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {

        for (Prvilages auth : Prvilages.values()) {
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getprvilages());
            authorityService.save(authority);
        }

        List<Post> posts = postService.getAll();
        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();
        Account account04 = new Account();

        account01.setEmail("user01@email.com");
        account01.setPassword("user01");
        account01.setFirstname("firstname01");
        account01.setLastname("lastname01");
        account01.setAge(20);
        account01.setDate_of_birth(LocalDate.of(2003, 05, 25));
        account01.setGender("Male");

        account02.setEmail("dharaneshwarkalavena@gmail.com");
        account02.setPassword("1264");
        account02.setFirstname("Dharaneshwar");
        account02.setLastname("Kalavena");
        account02.setRole(Roles.USER.getRole());
        account02.setDate_of_birth(LocalDate.of(2003, 05, 25));
        account02.setAge(21);
        account02.setGender("Male");

        account03.setEmail("admin01@email.com");
        account03.setPassword("admin01");
        account03.setFirstname("firstname03");
        account03.setLastname("lastname03");
        account03.setRole(Roles.ADMIN.getRole());
        account03.setAge(30);
        account03.setDate_of_birth(LocalDate.of(1999, 11, 24));
        account03.setGender("Male");

        account04.setEmail("editor01@email.com"); 
        account04.setPassword("editor01");
        account04.setFirstname("firstname04");
        account04.setLastname("lastname04");
        account04.setRole(Roles.EDITOR.getRole());
        account04.setDate_of_birth(LocalDate.of(2002, 10, 10));
        account04.setAge(35);
        account04.setGender("Male");
        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Prvilages.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);
        authorityService.findById(Prvilages.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);
        account04.setAuthorities(authorities);

        // accountService.save(account01);
        // accountService.save(account02);
        // accountService.save(account03);
        // accountService.save(account04);

        if (posts.isEmpty()) {
            Post post01 = new Post();
            post01.setTitle("title01");
            post01.setBody("body01");
            post01.setAccount(account01);

            // postService.save(post01);

            Post post02 = new Post();
            post02.setTitle("title02");
            post02.setBody("body02");
            post02.setAccount(account02);

            // postService.save(post02);
        }
    }

}
