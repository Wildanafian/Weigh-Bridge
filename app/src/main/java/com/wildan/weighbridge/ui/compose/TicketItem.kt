package com.wildan.weighbridge.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.ui.adapter.TicketListAdapter.Companion.ZERO


/*
 * Created by Wildan Nafian on 9/17/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

@Composable
fun TicketItem(data: TicketItem, click: () -> Unit) {

    val context = LocalContext.current
    val text: String
    val textColor: Int
    val backgroundColor: Int
    if (data.outboundWeight == ZERO) {
        text = context.getString(R.string.inbound)
        textColor = context.getColor(R.color.yellow)
        backgroundColor = context.getColor(R.color.light_yellow)
    } else {
        text = context.getString(R.string.outbound)
        textColor = context.getColor(R.color.primary)
        backgroundColor = context.getColor(R.color.light_green)
    }

    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { click() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TicketNumber(modifier = Modifier.weight(2f), data = data)

                Text(
                    modifier = Modifier
                        .background(
                            color = Color(backgroundColor),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(6.dp)
                        .weight(1f),
                    text = text,
                    color = Color(textColor),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider()

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_person_circle),
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = data.driverName.toString()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(context.getColor(R.color.light_green)),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(12.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "License Number"
                    )

                    Text(
                        modifier = Modifier.weight(2f),
                        text = data.licenseNumber.toString(),
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Net Weight"
                    )

                    Text(
                        modifier = Modifier.weight(2f),
                        text = data.licenseNumber.toString(),
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider()

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_calender),
                        contentDescription = "",
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = data.date.toString()
                    )
                }

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = "",
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = data.time.toString()
                    )
                }
            }
        }
    }
}

@Composable
fun TicketNumber(modifier: Modifier, data: TicketItem) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_order),
            contentDescription = "",
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = data.id
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TicketItemPreview() {
    TicketItem(
        TicketItem(
            id = "Dontavious",
            date = "12 Juni 2024",
            time = "14:00",
            licenseNumber = "AA1234DD",
            driverName = "Om Burhan",
            inboundWeight = 11.0,
            outboundWeight = 10.0,
            netWeight = 1.0
        )
    ) {

    }
}