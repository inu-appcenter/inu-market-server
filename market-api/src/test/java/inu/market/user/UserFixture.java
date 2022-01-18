package inu.market.user;

import inu.market.user.domain.Role;
import inu.market.user.domain.User;
import inu.market.user.dto.UserCreateRequest;
import inu.market.user.dto.UserLoginRequest;
import inu.market.user.dto.UserResponse;
import inu.market.user.dto.UserUpdateProfileRequest;

import static inu.market.CommonFixture.TEST_IMAGE_URL;

public class UserFixture {

    public static final int TEST_INU_ID = 201601757;
    public static final String TEST_PASSWORD = "password";
    public static final String TEST_NICKNAME = "황주환";
    public static final Double TEST_SCORE = 0.0;
    public static final String TEST_PUSH_TOKEN = "pushToken";

    public static final User TEST_USER
            = new User(1L, TEST_INU_ID, TEST_NICKNAME, TEST_IMAGE_URL, Role.ROLE_USER, TEST_SCORE, TEST_PUSH_TOKEN);

    public static final User TEST_USER1
            = new User(2L, TEST_INU_ID, TEST_NICKNAME, TEST_IMAGE_URL, Role.ROLE_USER, TEST_SCORE, TEST_PUSH_TOKEN);

    public static final UserCreateRequest TEST_USER_CREATE_REQUEST
            = new UserCreateRequest(TEST_INU_ID, TEST_PASSWORD);

    public static final UserUpdateProfileRequest TEST_USER_UPDATE_PROFILE_REQUEST
            = new UserUpdateProfileRequest(TEST_NICKNAME, TEST_IMAGE_URL);

    public static final UserLoginRequest TEST_USER_LOGIN_REQUEST
            = new UserLoginRequest(TEST_INU_ID, TEST_PASSWORD, TEST_PUSH_TOKEN);

    public static final UserResponse TEST_USER_RESPONSE = UserResponse.from(TEST_USER);

}
