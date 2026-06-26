package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

// REST controller for /profile. @PreAuthorize("isAuthenticated()") means you must be logged in to use it.
@RestController
@RequestMapping("profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    private final ProfileService profileService;
    private final UserService userService;   // used to turn the logged-in username into a user id

    public ProfileController(ProfileService profileService, UserService userService)
    {
        this.profileService = profileService;
        this.userService = userService;
    }

    // GET /profile - returns the logged-in user's own profile (404 if they somehow don't have one)
    @GetMapping
    public Profile getProfile(Principal principal)
    {
        // Principal is the logged-in user (from the JWT); look them up to get their id
        User user = userService.getByUserName(principal.getName());
        Profile profile = profileService.getByUserId(user.getId());

        if (profile == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return profile;
    }

    // PUT /profile - updates the logged-in user's own profile
    @PutMapping
    public Profile updateProfile(@RequestBody Profile profile, Principal principal)
    {
        User user = userService.getByUserName(principal.getName());
        return profileService.update(user.getId(), profile);
    }
}
