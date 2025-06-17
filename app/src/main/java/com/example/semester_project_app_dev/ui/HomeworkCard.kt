import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.data.HomeworkItem
import com.example.semester_project_app_dev.ui.PressEffect
import com.example.semester_project_app_dev.ui.PressableImage


@Composable
fun HomeworkCard(
    item: HomeworkItem,
    onToggleDone: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(110.dp)
    ) {
        // Background
        Image(
            painter = painterResource(R.drawable.hw_card),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (!item.isDone) 1f else 0.6f), // lighten background when undone
            contentScale = ContentScale.FillBounds
        )

        // Content Row: left = text, right = day + checkbox
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Left side: title + bullet points
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = Color.White
                )
                Spacer(Modifier.height(6.dp))

                item.details.split("\n").forEach {
                    if (it.isNotBlank()) {
                        Text(
                            text = "• $it",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }
            }

            // Right side: Day + checkbox (independent)
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(60.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Box 1: Day
                Box(
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = item.dueDay,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }

                // Box 2: X mark or click zone
                Box(
                    modifier = Modifier.padding(start = 29.dp, bottom = 7.dp)
                ) {
                    if (item.isDone) {
                        PressableImage(
                            imageRes = R.drawable.x_mark,
                            contentDescription = "Marked Done",
                            onClick = onToggleDone,
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { onToggleDone() }
                        )
                    }
                }
            }
        }
    }
}
