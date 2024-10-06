package com.karakoc.enewsletter.account;

import com.karakoc.enewsletter.account.requests.LoginRequest;
import com.karakoc.enewsletter.account.requests.LoginResponse;
import com.karakoc.enewsletter.account.requests.OAuth2Response;
import com.karakoc.enewsletter.account.requests.RegisterRequest;
import com.karakoc.enewsletter.exceptions.general.UnauthorizatedException;
import com.karakoc.enewsletter.security.UserPrincipal;
import com.karakoc.enewsletter.user.UserDTO;
import com.karakoc.enewsletter.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    // Normal giriş
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.attemptLogin(request.getEmail(), request.getPassword());
    }

    // Normal kayıt
    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterRequest request) {
        return authService.attemptRegister(request.getEmail(),request.getName(), request.getPassword());
    }

    // Kullanıcının kendi bilgilerini getirme
    @GetMapping("/getme")
    public UserDTO getMe(@AuthenticationPrincipal UserPrincipal principal) {
        return userService.getUserByEmail(principal.getEmail());
    }


    @PostMapping("/oauth2/{accessToken}")
    public UserDTO registerUserWithOAuth2(@AuthenticationPrincipal UserPrincipal principal,@PathVariable String accessToken,@RequestBody OAuth2Response user){
        System.out.println("User email:"+ principal.getUserId());
        System.out.println("Received access token: " + accessToken);
        System.out.println("Received data: " + user);

        return userService.assignGmailTokenToUser(principal.getUserId(),accessToken,user);

    }


    @PostMapping("/delete/oauth2connection/{userId}")
    public void unlinkGmailAccount(@AuthenticationPrincipal UserPrincipal principal,@PathVariable String userId){
        if (!principal.getUserId().equals(userId)) {
            throw new UnauthorizatedException("User Id's are not matching with logged in and path variable id");
        }
        userService.unlinkGmailAccount(userId);
    }



}
