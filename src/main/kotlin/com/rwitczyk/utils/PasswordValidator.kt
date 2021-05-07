package com.rwitczyk.utils

import java.util.HashSet

class PasswordValidator {
    fun isPasswordStrongEnough(password: String): Boolean {
        if (password.length < 5) {
            return false
        }

        if (!password.contains(Regex("[A-Z]+"))) {
            return false
        }

        val specialCharacters: Set<Char> = HashSet(
            listOf(
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'
            )
        )
        var hasAnySpecialChar = false;
        for (i in password.toCharArray()) {
            if (specialCharacters.contains(i)) {
                hasAnySpecialChar = true;
            }
        }

        return hasAnySpecialChar;
    }
}
