package com.example.fitnessapp.presentation.screens.profile_screen_package.profile_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.components.LogoutConfirmationDialog
import com.example.fitnessapp.presentation.viewModels.get_userData_viewModel.GetUserDataState
import com.example.fitnessapp.presentation.viewModels.get_userData_viewModel.GetUserDataViewModel
import com.example.fitnessapp.presentation.viewModels.themeView.ThemeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    themeViewModel: ThemeViewModel,
    onUser: () -> Unit,
    onPermissions: () -> Unit,
    onAbout: () -> Unit,
    onLogout: () -> Unit,
    onThirdParty:() -> Unit,
) {

    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    val getUserDataViewModel = hiltViewModel<GetUserDataViewModel>()
    val getUserDataState by getUserDataViewModel.getUserDataState.collectAsStateWithLifecycle()

    when (getUserDataState) {
        is GetUserDataState.Error -> {
            FailedLoadingScreen(
                errorMessage = (getUserDataState as GetUserDataState.Error).error,
                onFailed = { getUserDataViewModel.getUserData() }
            )
        }

        GetUserDataState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        GetUserDataState.None -> Unit

        is GetUserDataState.Success -> {
            val userInfo = (getUserDataState as GetUserDataState.Success).userInfo

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 2.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = userInfo.userName,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "${userInfo.gender}  •  ${userInfo.height}cm  •  ${userInfo.weight}kg",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Change Theme",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { isChecked ->
                                themeViewModel.toggleTheme(isChecked)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                                uncheckedTrackColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                }

                ProfileListItem("Edit Your Details", Icons.Default.Edit, onUser)
                ProfileListItem("Third-party data", Icons.Default.SyncAlt, onThirdParty)
                ProfileListItem("App Permissions", Icons.Default.Interests, onPermissions)
                ProfileListItem("About App", Icons.Default.Info, onAbout)
                ProfileListItem("Logout", Icons.Default.Logout, onClick = { showDialog = true })

                if (showDialog) {
                    LogoutConfirmationDialog(
                        onConfirm = { onLogout() },
                        onDismiss = { showDialog = false }
                    )
                }
            }
        }
    }
}


@Composable
fun ProfileListItem(title: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 12.dp, end = 0.dp, bottom = 12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary)
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}
