package com.example.backend.services.user;

import com.example.backend.models.user.UserDto;

import java.util.List;

public interface UserService {
    /**
     * Registers a new user in the system.
     *
     * @param userDto the user data to register
     */
    void register(UserDto userDto);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the user's email
     * @return the user data if found
     */
    UserDto getUserByEmail(String email);

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the user's ID
     * @return the user data if found
     */
    UserDto getUserById(Long id);

    /**
     * Updates the information of an existing user.
     *
     * @param userDto the user data to update
     */
    void updateUser(UserDto userDto);

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the ID of the user to delete
     */
    void deleteUserById(Long id);

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of user data
     */
    List<UserDto> getAllUsers();
}
