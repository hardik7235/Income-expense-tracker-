package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FrostedBackgroundWrapper
import com.example.ui.theme.FrostedButtonGradient
import com.example.ui.theme.FrostedEmerald
import com.example.ui.theme.FrostedEmeraldLight
import com.example.ui.theme.FrostedIndigo
import com.example.ui.theme.FrostedIndigoLight
import com.example.ui.theme.FrostedIndigoPill
import com.example.ui.theme.FrostedRose
import com.example.ui.theme.FrostedRoseGlassBg
import com.example.ui.theme.FrostedRoseGlassBorder
import com.example.ui.theme.FrostedRoseLight
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.FinanceUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthScreen(
    uiState: FinanceUiState,
    onLogin: (email: String, password: String) -> Unit,
    onSignUp: (name: String, email: String, password: String, avatarIcon: String, monthlyBudget: Double, currencySymbol: String) -> Unit,
    onQuickDemoLogin: () -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSignUpMode by remember { mutableStateOf(false) }

    // Form inputs
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(if (isSignUpMode) "" else "ayanokojikiyotaka7235@gmail.com") }
    var password by remember { mutableStateOf("password123") }
    var confirmPassword by remember { mutableStateOf("password123") }
    var selectedAvatar by remember { mutableStateOf("person") }
    var monthlyBudgetText by remember { mutableStateOf("50000") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val avatarOptions = listOf(
        "person" to Icons.Default.Person,
        "rocket" to Icons.Default.RocketLaunch,
        "savings" to Icons.Default.Savings,
        "trending" to Icons.Default.TrendingUp,
        "star" to Icons.Default.Star,
        "wallet" to Icons.Default.AccountBalanceWallet
    )

    FrostedBackgroundWrapper {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Brand Logo & Name
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(FrostedIndigoLight, FrostedIndigo, Color(0xFF1E1B4B))
                        )
                    )
                    .border(2.dp, Color(0x66FFFFFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CurrencyRupee,
                    contentDescription = "Rupee Icon",
                    tint = Color.White,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "INCOME & EXPENSE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = FrostedIndigoLight,
                letterSpacing = 2.sp
            )

            Text(
                text = "Real-Time Finance Manager",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Track Indian Rupee (₹) transactions, analytics & reports",
                fontSize = 13.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mode Selector Pill (Sign In / Create Account)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(GlassSurface)
                    .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
                    .padding(4.dp)
            ) {
                // Sign In tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (!isSignUpMode) FrostedIndigoPill else Color.Transparent)
                        .border(1.dp, if (!isSignUpMode) FrostedIndigoLight.copy(alpha = 0.5f) else Color.Transparent, RoundedCornerShape(14.dp))
                        .clickable {
                            isSignUpMode = false
                            onClearError()
                        }
                        .padding(vertical = 12.dp)
                        .testTag("auth_signin_tab"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sign In",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (!isSignUpMode) TextWhite else TextMuted
                    )
                }

                // Sign Up tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSignUpMode) FrostedIndigoPill else Color.Transparent)
                        .border(1.dp, if (isSignUpMode) FrostedIndigoLight.copy(alpha = 0.5f) else Color.Transparent, RoundedCornerShape(14.dp))
                        .clickable {
                            isSignUpMode = true
                            onClearError()
                        }
                        .padding(vertical = 12.dp)
                        .testTag("auth_signup_tab"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Create Account",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isSignUpMode) TextWhite else TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Error Banner
            AnimatedVisibility(
                visible = uiState.authErrorMessage != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier
                            .background(FrostedRoseGlassBg)
                            .border(1.dp, FrostedRoseGlassBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = FrostedRoseLight,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = uiState.authErrorMessage ?: "",
                            fontSize = 13.sp,
                            color = FrostedRoseLight,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Main Auth Form Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(26.dp))
                        .padding(20.dp)
                ) {
                    // Full Name (Only on Sign Up)
                    if (isSignUpMode) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                onClearError()
                            },
                            label = { Text("Full Name", color = TextMuted) },
                            placeholder = { Text("e.g. Kiyotaka Ayanokoji", color = TextDarkMuted) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FrostedIndigoLight) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x18FFFFFF),
                                unfocusedContainerColor = Color(0x10FFFFFF),
                                focusedBorderColor = FrostedIndigoLight,
                                unfocusedBorderColor = GlassBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_name_input")
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Email Address
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            onClearError()
                        },
                        label = { Text("Email Address", color = TextMuted) },
                        placeholder = { Text("user@example.com", color = TextDarkMuted) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = FrostedIndigoLight) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0x18FFFFFF),
                            unfocusedContainerColor = Color(0x10FFFFFF),
                            focusedBorderColor = FrostedIndigoLight,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_email_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            onClearError()
                        },
                        label = { Text("Password", color = TextMuted) },
                        placeholder = { Text("••••••••", color = TextDarkMuted) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = FrostedIndigoLight) },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Password Visibility",
                                    tint = TextMuted
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0x18FFFFFF),
                            unfocusedContainerColor = Color(0x10FFFFFF),
                            focusedBorderColor = FrostedIndigoLight,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_password_input")
                    )

                    // Additional Sign Up Fields
                    if (isSignUpMode) {
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                onClearError()
                            },
                            label = { Text("Confirm Password", color = TextMuted) },
                            placeholder = { Text("••••••••", color = TextDarkMuted) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = FrostedIndigoLight) },
                            singleLine = true,
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x18FFFFFF),
                                unfocusedContainerColor = Color(0x10FFFFFF),
                                focusedBorderColor = FrostedIndigoLight,
                                unfocusedBorderColor = GlassBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_confirm_password_input")
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Avatar Picker
                        Text(
                            text = "Choose Profile Avatar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            avatarOptions.forEach { (key, icon) ->
                                val isSelected = selectedAvatar == key
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) FrostedIndigo else Color(0x15FFFFFF))
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) FrostedIndigoLight else Color(0x22FFFFFF),
                                            shape = CircleShape
                                        )
                                        .clickable { selectedAvatar = key },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = key,
                                        tint = if (isSelected) Color.White else TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Monthly Budget in INR
                        OutlinedTextField(
                            value = monthlyBudgetText,
                            onValueChange = { input ->
                                if (input.isEmpty() || input.matches(Regex("^\\d*$"))) {
                                    monthlyBudgetText = input
                                }
                            },
                            label = { Text("Monthly Budget (₹ INR)", color = TextMuted) },
                            placeholder = { Text("50000", color = TextDarkMuted) },
                            leadingIcon = { Text("₹", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FrostedEmeraldLight, modifier = Modifier.padding(start = 12.dp)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x18FFFFFF),
                                unfocusedContainerColor = Color(0x10FFFFFF),
                                focusedBorderColor = FrostedIndigoLight,
                                unfocusedBorderColor = GlassBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_budget_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Action Button (Sign In / Sign Up)
                    Button(
                        onClick = {
                            if (isSignUpMode) {
                                if (password != confirmPassword) {
                                    return@Button
                                }
                                val budget = monthlyBudgetText.toDoubleOrNull() ?: 50000.0
                                onSignUp(name, email, password, selectedAvatar, budget, "₹")
                            } else {
                                onLogin(email, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(FrostedButtonGradient)
                            .testTag("auth_submit_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        if (uiState.isAuthLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text = if (isSignUpMode) "Create Account & Start" else "Sign In to Account",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Quick Demo Login Card (1-tap instant access)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .clickable { onQuickDemoLogin() }
                    .testTag("auth_quick_demo_button"),
                shape = RoundedCornerShape(22.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .background(Color(0x1A6366F1))
                        .border(1.dp, FrostedIndigoLight.copy(alpha = 0.4f), RoundedCornerShape(22.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(FrostedIndigo, CircleShape)
                                .border(1.dp, Color(0x66FFFFFF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Instant Demo Login",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextWhite
                            )
                            Text(
                                text = "Explore as Kiyotaka (₹ INR data ready)",
                                fontSize = 12.sp,
                                color = FrostedIndigoLight
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(FrostedIndigoPill)
                            .border(1.dp, FrostedIndigoLight.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("1-Tap Access", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FrostedIndigoLight)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
