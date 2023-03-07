package org.muslimapp.feature.quran.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SectionItem(
  modifier: Modifier = Modifier,
  title: @Composable (() -> Unit)? = null,
  shape: Shape = MaterialTheme.shapes.large,
  elevation: Dp = 1.dp,
  content: @Composable (ColumnScope.() -> Unit),
) {

  Surface(
    shape = shape,
    modifier = modifier,
    tonalElevation = elevation,
  ) {

    Column {
      if (title != null) {
        Box(
          modifier = Modifier.padding(
            start = 24.dp,
            end = 8.dp,
            top = 16.dp,
            bottom = 8.dp
          )
        ) {
          CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            LocalContentColor provides MaterialTheme.colorScheme.primary,
            content = { title() }
          )
        }
      }

      content()
    }
  }
}

inline fun <T> LazyListScope.sectionItems(
  items: List<T>,
  crossinline key: (item: T) -> Any = { it.hashCode() },
  noinline divider: @Composable () -> Unit = {},
  modifier: Modifier = Modifier,
  crossinline itemContent: @Composable (LazyItemScope.(T) -> Unit),
) {
  sectionItemsIndexed(
    modifier = modifier,
    items = items,
    divider = divider,
    key = { _, item -> key(item) }
  ) { _, item -> itemContent(item) }
}

fun <T> LazyListScope.sectionItemsIndexed(
  items: List<T>,
  key: (index: Int, item: T) -> Any = { _, item -> item.hashCode() },
  divider: @Composable () -> Unit = {},
  modifier: Modifier = Modifier,
  itemContent: @Composable (LazyItemScope.(Int, T) -> Unit),
) {
  itemsIndexed(items, key = key) { index, item ->

    val shapes = MaterialTheme.shapes

    val shape = remember {
      when (index) {
        items.size - 1 -> shapes.large.copy(
          topEnd = CornerSize(0),
          topStart = CornerSize(0)
        )
        else -> RectangleShape
      }
    }

    Surface(modifier = modifier, shape = shape) {
      Column {
        if (index != 0) divider()
        itemContent(index, item)
      }
    }
  }
}

fun LazyListScope.sectionTitleItem(
  modifier: Modifier = Modifier,
  key: Any? = null,
  isEmpty: Boolean = false,
  content: @Composable BoxScope.() -> Unit,
) {
  item(key = key) {
    SectionTitle(modifier, isEmpty, content)
  }
}

@Composable
fun SectionTitle(
  modifier: Modifier = Modifier,
  isEmpty: Boolean = false,
  content: @Composable BoxScope.() -> Unit
) {

  val title = @Composable {
    val colorScheme = MaterialTheme.colorScheme
    CompositionLocalProvider(
      LocalTextStyle provides MaterialTheme.typography.labelLarge,
      LocalContentColor provides colorScheme.primary,
    ) {
      Box(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        content = content,
      )
    }
  }

  if (isEmpty) {
    Box(
      modifier = modifier
        .padding(top = 16.dp)
        .fillMaxWidth(),
    ) {
      title()
    }
  } else {
    Surface(
      modifier = modifier.fillMaxWidth(),
//            tonalElevation = 1.dp,
      shape = MaterialTheme.shapes.large.copy(
        bottomEnd = CornerSize(0),
        bottomStart = CornerSize(0)
      ),
      content = title
    )
  }
}
