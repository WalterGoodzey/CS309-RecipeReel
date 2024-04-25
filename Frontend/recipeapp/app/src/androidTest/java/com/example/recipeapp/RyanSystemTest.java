package com.example.recipeapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.recipeapp.activities.EntryActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;




/**
 * This testing file uses ActivityScenarioRule instead of ActivityTestRule
 * to demonstrate system testings cases
 */
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time
public class RyanSystemTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule
    public ActivityScenarioRule<EntryActivity> activityScenarioRule = new ActivityScenarioRule<>(EntryActivity.class);

    /**
     * Start the server and run this test
     *
     * This test attempts to sign up as a new user with a unique username, email address, and
     * password based on the current time in milliseconds
     *
     * Test ends by deleting the test user
     */
    @Test
    public void signup(){

        //click signup button
        onView(withId(R.id.entry_signup_btn)).perform(click());
        // Put thread to sleep to allow system to switch to signup activity
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        String signupVals = generateNewSignup();
        String[] signupValArray = signupVals.split(System.lineSeparator());

        String username = signupValArray[0];
        String email = signupValArray[1];
        String password = signupValArray[2];

        //in ProfileActivity now
        //check that profile info is correct
        onView(withId(R.id.profile_username_txt)).check(matches(withText(username)));
        onView(withId(R.id.profile_description_txt)).check(matches(withText(email)));

        //delete new generated profile so that testing doesn't leave a bunch of
        //empty test users to the server
        deleteProfile(password);
    }

    /**
     * Start the server and run this test
     *
     * Test to check that the login sequence works
     *
     * Signups a new user, logs them out, logs back in, then deletes the account
     */
    @Test
    public void login(){
        /* SIGNUP AND LOGOUT */
        //must first signup and then logout
        //click signup button
        onView(withId(R.id.entry_signup_btn)).perform(click());
        // Put thread to sleep to allow system to switch to signup activity
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}
        String signupVals = generateNewSignup();
        //now in ProfileActivity
        String[] signupValArray = signupVals.split(System.lineSeparator());
        String username = signupValArray[0];
        String email = signupValArray[1];
        String password = signupValArray[2];
        //Logout and return to EntryActivity
        //clicks option button
        onView(withId(R.id.profile_options_overflow)).perform(click());
        //clicks logout button
        onView(withText(R.string.profile_options_logout)).perform(click());
        // Put thread to sleep to allow system to switch to entry activity
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        /* LOGIN */
        //click login button
        onView(withId(R.id.entry_login_btn)).perform(click());
        // Put thread to sleep to allow system to switch to login activity
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        //in LoginActivity now
        //fill in signup edit text with generated user info above
        onView(withId(R.id.login_username_edt)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText(password), closeSoftKeyboard());

        //clicks login button
        onView(withId(R.id.login_login_btn)).perform(click());
        // Put thread to sleep to allow system to switch to profile activity and submit volley request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        //in ProfileActivity now
        //check that profile info is correct
        onView(withId(R.id.profile_username_txt)).check(matches(withText(username)));
        onView(withId(R.id.profile_description_txt)).check(matches(withText(email)));

        //delete new generated profile so that testing doesn't leave a bunch of
        //empty test users to the server
        deleteProfile(password);
    }

    /**
     * Start the server and run this test
     *
     * Test to check that invalid passwords are not accepted when trying to
     * create an account and that the UI generates the proper error messages
     */
    @Test
    public void signupInvalidPasswords(){
        String username = "testusername";
        String email = "testemail@gmail.com";
        String password1 = "password1!";
        String password2 = "password2!";
        String passwordNoSpecial = "testpassword";
        String passwordShort = "1234!";

        //click signup button
        onView(withId(R.id.entry_signup_btn)).perform(click());
        // Put thread to sleep to allow system to switch to signup activity
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        //in SignupActivity now

        //no special characters test
        //fill in signup edit text with generated user info above
        onView(withId(R.id.signup_username_edt)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.signup_email_edt)).perform(typeText(email), closeSoftKeyboard());
        //matching passwords but with not special characters or numbers
        onView(withId(R.id.signup_password_edt)).perform(typeText(passwordNoSpecial), closeSoftKeyboard());
        onView(withId(R.id.signup_confirm_edt)).perform(typeText(passwordNoSpecial), closeSoftKeyboard());

        //clicks signup button
        onView(withId(R.id.signup_signup_btn)).perform(click());
        // check that error message is displayed
        onView(withId(R.id.signup_error_txt)).check(matches(withText(R.string.invalid_password_msg)));

        //short password test
        onView(withId(R.id.signup_password_edt)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.signup_confirm_edt)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edt)).perform(typeText(passwordShort), closeSoftKeyboard());
        onView(withId(R.id.signup_confirm_edt)).perform(typeText(passwordShort), closeSoftKeyboard());
        //clicks signup button
        onView(withId(R.id.signup_signup_btn)).perform(click());
        // check that error message is displayed
        onView(withId(R.id.signup_error_txt)).check(matches(withText(R.string.invalid_password_msg)));

        //different valid passwords test
        onView(withId(R.id.signup_password_edt)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.signup_confirm_edt)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edt)).perform(typeText(password1), closeSoftKeyboard());
        onView(withId(R.id.signup_confirm_edt)).perform(typeText(password2), closeSoftKeyboard());
        //clicks signup button
        onView(withId(R.id.signup_signup_btn)).perform(click());
        // check that error message is displayed
        onView(withId(R.id.signup_error_txt)).check(matches(withText(R.string.mismatch_password_msg)));

    }

//    @Test
//    public void editProfile(){
//        /* SIGNUP */
//        //click signup button
//        onView(withId(R.id.entry_signup_btn)).perform(click());
//        // Put thread to sleep to allow system to switch to signup activity
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {}
//
//        String signupVals = generateNewSignup();
//        String[] signupValArray = signupVals.split(System.lineSeparator());
//
//        String username = signupValArray[0];
//        String email = signupValArray[1];
//        String password = signupValArray[2];
//
//        //in ProfileActivity now
//        //check that profile info is correct
//        onView(withId(R.id.profile_username_txt)).check(matches(withText(username)));
//        onView(withId(R.id.profile_description_txt)).check(matches(withText(email)));
//
//        /* EDIT PROFILE */
//        //clicks option button
//        onView(withId(R.id.profile_options_overflow)).perform(click());
//        //clicks edit profile button
//        onView(withText(R.string.profile_options_edit)).perform(click());
//        // Put thread to sleep to allow system to switch to password check activity
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {}
//        //in password check activity - type in password and press confirm
//        onView(withId(R.id.password_check_etx)).perform(typeText(password), closeSoftKeyboard());
//        onView(withId(R.id.password_check_confirm_btn)).perform(click());
//        // Put thread to sleep to allow system to switch to edit profile activity
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {}
//        //in EditProfileActivity now
//        //edit username and email
//        String newUsername = username + "!!";
//        String newEmail = "new" + email;
//        onView(withId(R.id.editprofile_username_edt)).perform(clearText(), closeSoftKeyboard());
//        onView(withId(R.id.editprofile_email_edt)).perform(clearText(), closeSoftKeyboard());
//        onView(withId(R.id.editprofile_username_edt)).perform(typeText(newUsername), closeSoftKeyboard());
//        onView(withId(R.id.editprofile_email_edt)).perform(typeText(newEmail), closeSoftKeyboard());
//        //click save button
//        onView(withId(R.id.editprofile_save_btn)).perform(click());
//
//        //in ProfileActivity now
//        //check that profile info is correct updated values
//        onView(withId(R.id.profile_username_txt)).check(matches(withText(newUsername)));
//        onView(withId(R.id.profile_description_txt)).check(matches(withText(newEmail)));
//
//        //delete test account
//        deleteProfile(password);
//    }

    /**
     * Start the server and run this test
     * Test for the continue as guest functionality in EntryActivity
     */
    @Test
    public void continueAsGuest(){
        //click continue as guest button
        onView(withId(R.id.entry_guest_btn)).perform(click());
        // Put thread to sleep to allow system to switch to profile activity
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        //in ProfileActivity now, not signed in
        // check that guest message is displayed
        onView(withId(R.id.profile_guest_txt)).check(matches(withText(R.string.profile_guest_text)));
        //click on entry button
        onView(withId(R.id.profile_entry_btn)).perform(click());
        // Put thread to sleep to allow system to switch back to entry activity
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}
        //check that it goes back to entry activity with right messages
        onView(withId(R.id.entry_msg_txt)).check(matches(withText(R.string.entry_main_msg)));
        onView(withId(R.id.entry_sub_msg_txt)).check(matches(withText(R.string.entry_sub_msg)));
    }

    /**
     * Helper function to generate and signup a new user, starting in SignupActivity
     * and ending in ProfileActivity
     *
     * @return a string containing the new user's info in the format: "username + '\n' + email + '\n' + password"
     */
    private String generateNewSignup(){
        long timeMillis = System.currentTimeMillis();
        String username = "testusername" + timeMillis;
        String email = "testemail" + timeMillis + "@gmail.com";
        String password = "testpassword" + timeMillis;


        //fill in signup edit text with generated user info above
        onView(withId(R.id.signup_username_edt)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.signup_email_edt)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edt)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.signup_confirm_edt)).perform(typeText(password), closeSoftKeyboard());

        //clicks signup button
        onView(withId(R.id.signup_signup_btn)).perform(click());
        // Put thread to sleep to allow system to switch to profile activity and submit volley request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        return (username + '\n' + email + '\n' + password);
    }

    /**
     * Deletes the current user's account given their password, starting in ProfileActivity
     * and ending in EntryActivity
     *
     * @param password - signed in user's password
     */
    private void deleteProfile(String password){
        //helper function assumes it is called in in profile activity, logged in already
        //and given the correct password for the account it is logged into
        //clicks option button
        onView(withId(R.id.profile_options_overflow)).perform(click());
        //clicks edit profile button
        onView(withText(R.string.profile_options_edit)).perform(click());
        // Put thread to sleep to allow system to switch to password check activity
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}
        //in password check activity - type in password and press confirm
        onView(withId(R.id.password_check_etx)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.password_check_confirm_btn)).perform(click());
        // Put thread to sleep to allow system to switch to edit profile activity
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}
        //in EditProfileActivity now
        //clicks delete profile button and confirm on the popup
        onView(withId(R.id.editprofile_delete_btn)).perform(click());
        onView(withId(R.id.editprofile_popup_confirm_btn)).perform(click());
        // Put thread to sleep to allow system to make volley request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}
    }
}
