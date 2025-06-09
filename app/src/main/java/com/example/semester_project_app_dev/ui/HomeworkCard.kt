import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.data.HomeworkItem


@Composable
fun HomeworkCard(
    item: HomeworkItem,
    onToggleDone: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .heightIn(min = 130.dp)
    ) {
        // Background image based on done status
        val backgroundRes = if (item.isDone) R.drawable.hw2 else R.drawable.hw1

        Image(
            painter = painterResource(backgroundRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, top = 24.dp, end = 80.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = item.details,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "DUE ${item.dueDay.uppercase()}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Toggle Done button (checkbox)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 24.dp)
                .size(32.dp)
                .clickable { onToggleDone() }
        )
    }
}
