package com.example.fitnessapp.presentation.screens.dashboared.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fitnessapp.R


@Composable
fun AddWaterSection(onWater:()->Unit) {
    val context= LocalContext.current
    val sharedPreferences= context.getSharedPreferences("WaterReminderPrefs", Context.MODE_PRIVATE)
    val dailyGoal= remember {
        mutableIntStateOf(sharedPreferences.getInt("daily_goal",2000))
    }
    LaunchedEffect(Unit) {
        dailyGoal.intValue=sharedPreferences.getInt("daily_goal",2000)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(21.dp))
            .clip(RoundedCornerShape(21.dp))
            .clickable {
                onWater()
            }.padding(
                all = 16.dp
            )

    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.large_bottle),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Water Goal:",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${dailyGoal.intValue}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = " ml",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = "Add Water",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(45.dp)
        )
    }
}