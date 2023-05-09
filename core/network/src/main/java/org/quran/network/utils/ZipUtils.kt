package org.quran.network.utils

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipFile

object ZipUtils {
  private const val BUFFER_SIZE = 512
  private const val MAX_FILES = 10000 // Max number of files

  @VisibleForTesting
  var MAX_UNZIPPED_SIZE = 0x1f400000 // Max size of unzipped data, 500MB

  /**
   * Unzip a file given the file, an item, and the listener
   * Does similar checks to those shown in rule 0's IDS04-J rule from:
   * [...](https://www.securecoding.cert.org/confluence/display/java)
   *
   * @param zipFile       the path to the zip file
   * @param destDirectory the directory to extract the file in
   * @param item          any data object passed back to the listener
   * @param listener      a progress listener
   * @param <T>           the type of the item passed in
   * @return a boolean representing whether we succeeded to unzip the file or not
  </T> */
  suspend fun <T> unzipFile(
    zipFile: String,
    destDirectory: String,
    item: T,
    listener: ZipListener<T>? = null,
  ): Boolean {
    return try {
      val file = File(zipFile)
      Timber.d("unzipping %s, size: %d", zipFile, file.length())
      withContext(Dispatchers.IO) {
        ZipFile(file, ZipFile.OPEN_READ).use { zip ->
          val numberOfFiles = zip.size()
          val entries = zip.entries()
          val canonicalPath = File(destDirectory).canonicalPath
          var total: Long = 0
          var processedFiles = 0
          while (entries.hasMoreElements()) {
            processedFiles++
            val entry = entries.nextElement()
            val currentEntryFile = File(destDirectory, entry.name)
            if (currentEntryFile.canonicalPath.startsWith(canonicalPath)) {
              if (entry.isDirectory) {
                if (!currentEntryFile.exists()) {
                  currentEntryFile.mkdirs()
                }
                continue
              } else if (currentEntryFile.exists()) {
                // delete files that already exist
                currentEntryFile.delete()
              }
              zip.getInputStream(entry).use { inputStream ->
                FileOutputStream(currentEntryFile).use { outputStream ->
                  var size = 0
                  val buf = ByteArray(BUFFER_SIZE)
                  while (
                    total + BUFFER_SIZE <= MAX_UNZIPPED_SIZE
                    && inputStream.read(buf).also { size = it } > 0
                  ) {
                    outputStream.write(buf, 0, size)
                    total += size.toLong()
                  }
                }
              }
              check(!(processedFiles >= MAX_FILES || total >= MAX_UNZIPPED_SIZE)) { "Invalid zip file." }
              listener?.onProcessingProgress(item, processedFiles, numberOfFiles)
            } else {
              throw IllegalStateException("Invalid zip file.")
            }
          }
        }
      }
      true
    } catch (ioe: IOException) {
      Timber.e(ioe, "Error unzipping file")
      false
    }
  }

  fun interface ZipListener<T> {
    suspend fun onProcessingProgress(obj: T, processed: Int, total: Int)
  }
}