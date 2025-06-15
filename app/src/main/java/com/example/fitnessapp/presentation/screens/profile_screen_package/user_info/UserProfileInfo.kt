package com.example.fitnessapp.presentation.screens.profile_screen_package.user_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.viewModels.get_userData_viewModel.GetUserDataState
import com.example.fitnessapp.presentation.viewModels.get_userData_viewModel.GetUserDataViewModel

@Composable
fun UserProfileInfoScreen(
    onEditProfileClick: () -> Unit,
) {

    val getUserDataViewModel = hiltViewModel<GetUserDataViewModel>()
    val getUserDataState by getUserDataViewModel.getUserDataState.collectAsStateWithLifecycle()

    when (getUserDataState) {
        is GetUserDataState.Error -> {
            FailedLoadingScreen(
                errorMessage = (getUserDataState as GetUserDataState.Error).error,
                onFailed = { getUserDataViewModel.getUserData() })
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
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = userInfo.userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = userInfo.email,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelLarge,
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onEditProfileClick) {
                    Text(
                        "Edit Profile",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoItem(icon = Icons.Default.Boy, value = userInfo.age, label = "Age")
                        InfoItem(
                            icon = Icons.Default.FitnessCenter,
                            value = userInfo.weight,
                            label = "Weight"
                        )
                        InfoItem(
                            icon = Icons.Default.Height,
                            value = userInfo.height,
                            label = "Height"
                        )
                        InfoItem(
                            icon = if (userInfo.gender.equals(
                                    "Female",
                                    true
                                )
                            ) Icons.Default.Female else Icons.Default.Male,
                            value = userInfo.gender,
                            label = "Gender"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {

                }

                Spacer(modifier = Modifier.weight(1f))

            }
        }
    }


}

@Composable
fun InfoItem(icon: ImageVector, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
