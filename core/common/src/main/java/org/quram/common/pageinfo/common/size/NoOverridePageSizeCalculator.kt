package org.quram.common.pageinfo.common.size

import org.quram.common.pageinfo.common.size.DefaultPageSizeCalculator
import org.quram.common.source.DisplaySize

class NoOverridePageSizeCalculator(displaySize: DisplaySize) :
    DefaultPageSizeCalculator(displaySize) {

  override fun setOverrideParameter(parameter: String) {
    // override parameter is irrelevant for these pages
  }

  override fun getTabletWidthParameter(): String {
    // use the same size for tablet landscape
    return getWidthParameter()
  }
}
