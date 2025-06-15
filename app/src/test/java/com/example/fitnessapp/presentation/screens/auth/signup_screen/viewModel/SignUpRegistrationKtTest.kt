package com.example.fitnessapp.presentation.screens.auth.signup_screen.viewModel

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * ...What makes Test Failed
// * empty user name
// * empty email
// * empty password
// * invalid email
// * invalid password
// * invalid user name
* */
class SignUpRegistrationKtTest {

    @Test
    fun `empty user name returns Username cannot be empty`() {
        val result = registrationUtil(
            userName = "",
            email = "abdallahalqiran765@gmail.com",
            password = "hello world"
        )
        assertThat(result).isEqualTo(mutableMapOf(
            "userName" to "Username cannot be empty",
        ))
    }


    @Test
    fun `empty email returns Email can't be empty`() {
        val result = registrationUtil(
            userName = "Abdallah Alqiran",
            email = "",
            password = "hello world"
        )
        assertThat(result).isEqualTo(mutableMapOf(
            "email" to "Email can't be empty",
        ))
    }


    @Test
    fun `empty password returns password can't be empty`() {
        val result = registrationUtil(
            userName = "Abdallah Alqiran",
            email = "abdallahalqiran765@gmail.com",
            password = ""
        )
        assertThat(result).isEqualTo(mutableMapOf(
            "password" to "Password can't be empty"
        ))
    }

    @Test
    fun `invalid email returns Invalid email format`() {
        val result = registrationUtil(
            userName = "Abdallah Alqiran",
            email = "abdallah alqiran",
            password = "hello world"
        )
        assertThat(result).isEqualTo(mutableMapOf(
            "email" to "Invalid email format",
        ))
    }



    @Test
    fun `Invalid Password returns Password must be at least 6 characters`() {
        val result = registrationUtil(
            userName = "Abdallah Alqiran",
            email = "abdallahalqiran765@gmail.com",
            password = "765"
        )
        assertThat(result).isEqualTo(mutableMapOf(
            "password" to "Password must be at least 6 characters"
        ))
    }


    @Test
    fun `Invalid User name format returns Invalid UserName Format`() {
        val result = registrationUtil(
            userName = "0a",
            email = "abdallahalqiran765@gmail.com",
            password = "hello world"
        )
        assertThat(result).isEqualTo(mutableMapOf(
            "userName" to "Invalid UserName Format",
        ))
    }


    @Test
    fun `All Empty`() {
        val result = registrationUtil(
            userName = "",
            email = "",
            password = ""
        )
        assertThat(result).isEqualTo(mutableMapOf(
            "userName" to "Username cannot be empty",
            "email" to "Email can't be empty",
            "password" to "Password can't be empty"
        ))
    }


}