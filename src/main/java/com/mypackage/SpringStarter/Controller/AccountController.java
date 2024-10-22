package com.mypackage.SpringStarter.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mypackage.SpringStarter.models.Account;
import com.mypackage.SpringStarter.services.AccountService;
import com.mypackage.SpringStarter.services.EmailService;
import com.mypackage.SpringStarter.util.AppUtiles;
import com.mypackage.SpringStarter.util.email.EmailDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Value("${password.token.reset.timeout.minutes}")
    private int password_token_reset_timeout_minutes;

    @Value("${site.domain}")
    private String site_domain;

    @GetMapping("/register")
    public String register(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "register";
    }

    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }
        accountService.save(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(Model model) {
        return "redirect:/";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated")
    public String profile(Model model, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOnebyemail(authUser);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
            return "profile";
        }
        return "redirect:/?error";
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated")
    public String post_profile(@Valid @ModelAttribute Account account, BindingResult result, Principal principal, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "profile";
        }
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOnebyemail(authUser);
        if (optionalAccount.isPresent()) {
            Account account_by_id = accountService.findById(account.getId()).get();
            account_by_id.setAge(account.getAge());
            account_by_id.setDate_of_birth(account.getDate_of_birth());
            account_by_id.setFirstname(account.getFirstname());
            account_by_id.setLastname(account.getLastname());
            account_by_id.setGender(account.getGender());
            account_by_id.setPassword(account.getPassword());
            accountService.save(account_by_id);
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();
            return "redirect:/";
        }
        return "redirect:/?error";
    }

    @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated")
    public String update_photo(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, Principal principal) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("error", "No file uploaded");
            return "redirect:/profile";
        } else {
            @SuppressWarnings("null")
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                int length = 10;
                boolean useLetters = true;
                boolean userNumbers = true;
                String generatedString = RandomStringUtils.random(length, useLetters, userNumbers);
                String final_photo_name = generatedString + fileName;
                String absolute_fileLocation = AppUtiles.get_upload_path(final_photo_name);
                Path path = Paths.get(absolute_fileLocation);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                String authUser = "email";
                if (principal != null) {
                    authUser = principal.getName();

                }
                Optional<Account> optionalAccount = accountService.findOnebyemail(authUser);
                if (optionalAccount.isPresent()) {
                    Account account = optionalAccount.get();
                    Account account_by_id = accountService.findById(account.getId()).get();
                    String relative_fileLocation = "/uploads/" + final_photo_name;
                    account_by_id.setPhoto(relative_fileLocation);
                    accountService.save(account_by_id);
                }
                return "redirect:/profile";
            } catch (IOException e) {
                System.out.println("error " + e);
            }
        }
        return "";
    }

    @GetMapping("/forgot_password")
    public String forgot_password(Model model) {
        return "forgot_password";
    }

    @PostMapping("/reset_password")
    public String reset_password(@RequestParam("email") String _email, RedirectAttributes attributes, Model model) {
        Optional<Account> optionalAccount = accountService.findOnebyemail(_email);
        if (optionalAccount.isPresent()) {
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            String resetToken = UUID.randomUUID().toString();
            account.setToken(resetToken);
            account.setPassword_reset_token_expiry(LocalDateTime.now().plusMinutes(password_token_reset_timeout_minutes));
            accountService.save(account);
            String reset_msg = "This is the reset Password link:" + site_domain + "change_password?token=" + resetToken;
            EmailDetails emailDetails = new EmailDetails(account.getEmail(), reset_msg, "Reset Password");
            if (!emailService.sendSimpleEmail(emailDetails)) {
                attributes.addFlashAttribute("error", "Error sending email ,contact Admin");
                return "redirect:/forgot_password";
            }
            attributes.addFlashAttribute("message", "Password reset email sent");
            return "redirect:/login";
        }
        attributes.addFlashAttribute("error", "No user found with the email");
        return "redirect:/forgot_password";
    }

    @GetMapping("/change_password")
    public String change_password(Model model, @RequestParam("token") String token, RedirectAttributes attributes) {
        if(token == null){
            attributes.addFlashAttribute("error","Invalid Token");
            return "redirect:/forgot-password";
        }
        Optional<Account> optionalAccount = accountService.findByToken(token);
        if (optionalAccount.isPresent()) {
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(optionalAccount.get().getPassword_reset_token_expiry())) {
                attributes.addFlashAttribute("error", "Token time Expired");
                return "redirect:/forgot_password";
            }
            model.addAttribute("account", account);
            return "change_password";
        }
        attributes.addFlashAttribute("error", "Token Invalid");
        return "redirect:/forgot_password";
    }

    @PostMapping("/change_password")
    public String changed_password(RedirectAttributes attributes,@ModelAttribute Account account) {
        Account account_by_id=accountService.findById(account.getId()).get();
        account_by_id.setPassword(account.getPassword());
        account_by_id.setToken(null);
        accountService.save(account_by_id);
        attributes.addFlashAttribute("message","Password Updated");
        return "redirect:/login"; 
    }
}
