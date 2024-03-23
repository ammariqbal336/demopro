package com.domain.validator


import java.util.regex.Pattern

object Validator {
    const val NAME_PATTERN = "^[A-Za-z]|[A-Za-z][A-Za-z,\\s]*[A-Za-z]\$"
    const val NAME_PATTERN_dot = "^[A-Za-z]|[A-Za-z][A-Za-z.\\s]*[A-Za-z.]\$"
    const val NAME_PATTERN_comma = "^[A-Za-z]|[A-Za-z][A-Za-z,\\s]*[A-Za-z]\$"
    const val AlphaNumeric_PATTERN_comma_splash = "^[A-Za-z]|[A-Za-z][A-Za-z0-9,-/\\s]*[A-Za-z0-9]\$"
    const val PHONE_CONST = "^[+|(|\\d]?[\\d|(|\\s|]+[)|\\s]+\\d+[-|\\s]+\\d+\$"

    fun checkEmpty(string: String?): MutableList<String>? =
        if (string.isNullOrEmpty()) mutableListOf("This field is required")
        else
            null

    fun checkEmpty(string: String?,fieldName:String): MutableList<String>? =
        if (string.isNullOrEmpty()) mutableListOf("Please enter the $fieldName")
        else
            null


    fun checkEmptyField(string: String?,fieldName:String): MutableList<String>? =
        if (string.isNullOrEmpty()) mutableListOf("$fieldName field cannot be empty")
        else
            null

    fun fullNameValidation(string: String?,fieldName: String? =""): List<String> {

        var list = fieldName?.let { checkEmpty(string, it) } ?: mutableListOf()

        if (list.size == 0) {
            if (string!!.length < 3)
                list.add("$fieldName must be greater than 3 characters.".trim())
        }
        return list
    }


    fun NameValidation(string: String?,fieldName:String? =""): List<String> {

        var list = fieldName?.let { checkEmpty(string, it) } ?: mutableListOf()
        if (list.size == 0) {
            val NAME_PATTERN = "^[A-Za-z]|[A-Za-z][A-Za-z\\s]*[A-Za-z]\$"
            if (string!!.length < 3)
                list.add("Name must be greater than 3 characters.")
            if (!Pattern.compile(NAME_PATTERN).matcher(string).matches()) {
                list.add("Enter valid $fieldName")
            }


        }
        return list
    }

    fun NonMandatory_NameValidation(string: String?): List<String> {

        var list = ArrayList<String>()
        if (!string.isNullOrEmpty()) {
            val NAME_PATTERN = "^[A-Za-z]|[A-Za-z][A-Za-z,\\s]*[A-Za-z]\$"
            if (string!!.length < 3)
                list.add("Field must be greater than 3 characters.")
            if (!Pattern.compile(NAME_PATTERN).matcher(string).matches()) {
                list.add("Enter valid input")
            }
        }
        return list
    }

    fun NonMandatory_NameValidation(string: String?, pattern: String): List<String> {

        var list = ArrayList<String>()
        if (!string.isNullOrEmpty()) {
            if (string!!.length < 3)
                list.add("Field must be greater than 3 characters.")
            if (!Pattern.compile(pattern).matcher(string).matches()) {
                list.add("Enter valid input")
            }
        }
        return list
    }

    fun phoneNumberValidation(string: String?,fieldName: String?): List<String> {

        var list = fieldName?.let { checkEmpty(string, it) } ?: mutableListOf()


        if (list.size == 0) {
            if (string!!.length <= 7 || string!!.length >= 21)
                list.add("Please enter a valid phone number")

        }

        return list
    }

    fun UsPhoneNumValidation(string: String?,pattern: String) : List<String>{
        var list = ArrayList<String>()
        if (!string.isNullOrEmpty()) {
            if (string!!.length <= 7 || string.length >= 25)
                list.add("Field must be greater than 3 characters.")
            if (!Pattern.compile(pattern).matcher(string).matches()) {
                list.add("Enter valid input")
            }
        }
        return list
    }

    fun emailValidation(string: String?,fieldName: String?): List<String> {

        var list = fieldName?.let { checkEmpty(string, it) } ?: mutableListOf()

        if (list.size == 0) {
            if (!isEmailAddress(string!!))
                list.add("Email must be valid")
        }
        return list
    }

    fun isPhoneNumValidate(string: String?): List<String> {

        var list = checkEmpty(string) ?: mutableListOf()
        if (list.size == 0) {
            val NAME_PATTERN = "\\\\d{10}|(?:\\\\d{3}-){2}\\\\d{4}|\\\\(\\\\d{3}\\\\)\\\\d{3}-?\\\\d{4}"
            if (string!!.length < 3)
                list.add("Field must be greater than 3 characters.")
            if (!Pattern.compile(NAME_PATTERN).matcher(string).matches()) {
                list.add("Enter valid input")
            }


        }
        return list
    }

    fun isEmailAddress(string: String) = Pattern.compile("([A-Za-z0-9_.-]+)@([a-z0-9_.-]+[a-z])").matcher(string).matches()
    fun isValidPhoneNum(string: String) = Pattern.compile("^\\+?\\(?[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})?\n").matcher(string).matches()

    fun passwordValidation(string: String?,fieldName: String?): List<String> {

        var list = fieldName?.let { checkEmpty(string, it) } ?: mutableListOf()

        val PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-_!?;:*@#$%^&()+={}_+'|<>/.,])(?=\\S+$).{4,}$"

        if (list.size == 0) {
            if (string!!.length < 8 || string!!.length > 16)
                list.add("Password must be between 8  characters")

            if (!Pattern.compile(PASSWORD_PATTERN).matcher(string).matches()) {
                list.add("Password should have one upper case, lower case, symbols and a number")
            }
        }

        return list
    }

    fun confirmPasswordValidation(password: String?, confirmPassword: String?,fieldName: String?): List<String> {

        var list = fieldName?.let { checkEmptyField(confirmPassword, it) } ?: mutableListOf()

        if (list.size == 0) {
            if (password != null && !password.equals(confirmPassword))
                list.add("Confirm password field  does not match.")
        }
        return list
    }

    fun isPasswordChanged(oldpassword: String?, newPassword: String?): Boolean {
        return oldpassword.equals(newPassword)
    }

    fun cardValidation(text: String): List<String> {

        val list = checkEmpty(text) ?: mutableListOf()

        val CARD_PATTERN =
            "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})\$"

        if (list.size == 0) {
            if (!Pattern.compile(CARD_PATTERN).matcher(text).matches()) {
                list.add("Card Number is not valid.")
            }
        }

        return list
    }

    fun pinValidation(text: String): List<String> {
        val list = checkEmpty(text) ?: mutableListOf()

        val PIN_PATTERN = "^[0-9]{3,4}\$"

        if (list.size == 0) {
            if (!Pattern.compile(PIN_PATTERN).matcher(text).matches()) {
                list.add("CVV code is not valid.")
            }
        }
        return list
    }
}