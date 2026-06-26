package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

@Service
public class ProfileService
{
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }

    public Profile create(Profile profile)
    {
        return profileRepository.save(profile);
    }


    // returns the profile for a user, or null if they don't have one (a profile's id is the user id)
    public Profile getByUserId(int userId)
    {
        return profileRepository.findById(userId).orElse(null);
    }

    // updates a profile; we force the userId from the logged-in user so nobody can edit someone else's profile
    public Profile update(int userId, Profile profile)
    {
        profile.setUserId(userId);
        return profileRepository.save(profile);
    }
}
