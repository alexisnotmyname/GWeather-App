
package com.alexc.ph.domain.common

import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"
private const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"

fun String.isValidEmail(): Boolean {
  return this.isNotBlank() && Regex(EMAIL_PATTERN).matches(this)
}

fun String.isValidPassword(): Boolean {
  return this.isNotBlank() &&
    this.length >= MIN_PASS_LENGTH &&
    Pattern.compile(PASS_PATTERN).matcher(this).matches()
}

fun String.passwordMatches(repeated: String): Boolean {
  return this == repeated
}

fun String.idFromParameter(): String {
  return this.substring(1, this.length - 1)
}
