package com.example.quixafood.ui.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.quixafood.model.itemmodel.Itens
import com.example.quixafood.model.nofications.AlarmReceiver

private fun setAlarm(context: Context, hour: Int, minute: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !canScheduleExactAlarms(context)) {
        requestExactAlarmPermission(context)
        Toast.makeText(context, "Permissão necessária para configurar alarmes exatos.", Toast.LENGTH_SHORT).show()
        return
    }
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
        != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(context, "Permissão necessária para notificações. Ative nas configurações do aplicativo.", Toast.LENGTH_SHORT).show()
        return
    }
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
    }
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    Toast.makeText(context, "Alarme configurado para ${hour}:${minute}", Toast.LENGTH_SHORT).show()
}

private fun canScheduleExactAlarms(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }
}

private fun requestExactAlarmPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        context.startActivity(intent)
    }
}

@Composable
fun TimePickerDialogHandler(
    show: Boolean,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
    context: Context
) {
    if (show) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                onTimeSelected(selectedHour, selectedMinute)
            },
            hour,
            minute,
            true
        ).apply {
            setTitle("Selecione o horário desejado")
            setButton(TimePickerDialog.BUTTON_POSITIVE, "Confirmar") { _, _ ->
                onTimeSelected(hour, minute)
            }
            setButton(TimePickerDialog.BUTTON_NEGATIVE, "Cancelar") { _, _ ->
                onDismiss()
            }
            setOnDismissListener { onDismiss() }
            show()
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun DetailsScreen(item: Itens, useAnimation: Boolean = true) {
    var showDetails by remember { mutableStateOf(false) }

    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }
    var showTimePicker by remember { mutableStateOf(false) }
    var enabledConfirmation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagem destacada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = "Imagem de ${item.name}",
                    modifier = Modifier
                        .size(400.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Botão para mostrar/ocultar detalhes
            Button(onClick = { showDetails = !showDetails }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text(text = if (showDetails) "Esconder Detalhes" else "Ver Detalhes")
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Informações Gerais com animação
            AnimatedVisibility(
                visible = showDetails,
                enter = if(useAnimation) fadeIn() else EnterTransition.None
            ) {
                Column {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Informações Gerais",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tipo: ${item.description}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Preço: ${item.price}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Características
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Características",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Rico em nutrientes, versão especial sem Cebolas")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "Toque para agendar pedido",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Button(
                        onClick = { showTimePicker = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red))
                    {
                        Text("Definir Horário")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if(enabledConfirmation) {
                        Text(
                            text = "Pedido definido para $hour:$minute",
                        )
                    } else {
                        Text(
                            text = "Horário não definido",
                        )
                    }
                    TimePickerDialogHandler(
                        show = showTimePicker,
                        onTimeSelected = { selectedHour, selectedMinute ->
                            hour = selectedHour
                            minute = selectedMinute
                            showTimePicker = false
                            enabledConfirmation = true
                        },
                        onDismiss = { showTimePicker = false },
                        context
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            setAlarm(
                                context,
                                hour,
                                minute
                            );
                            enabledConfirmation = false
                        },
                        enabled = enabledConfirmation,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(
                            text = "Confirmar Horário"
                        )
                    }
                }
            }
        }
    }
}