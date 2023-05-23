package org.quran.features.share.views

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView

class ShareCardView(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  private val content: @Composable () -> Unit,
) : AbstractComposeView(context, attrs, defStyleAttr) {

  @Composable
  override fun Content() {
    content()
  }
}