package com.example.quixafood.ui.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.quixafood.model.itemmodel.Itens

@ExperimentalMaterial3Api
@Composable
fun ItemCardView(itens: Itens, onFavoriteClick: () -> Unit = {}, useAnimation: Boolean = true, onDetailClick: () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = itens.imageRes),
                contentDescription = itens.name,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = itens.name)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = itens.description)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "R$ ${itens.price}")
            Row {
                Button(
                    onClick = onDetailClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                )
                {
                    Text(text = "Ver Detalhes")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onFavoriteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = if (itens.isFavorite.value) "Remover dos Favoritos" else "Adicionar aos Favoritos")
                }
            }
            AnimatedVisibility(
                visible = itens.isFavorite.value,
                enter = if(useAnimation) fadeIn() else EnterTransition.None,
                exit = if (useAnimation) fadeOut() else ExitTransition.None
            ) {
                Text(
                    text = "Adicionado aos Favoritos",
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}