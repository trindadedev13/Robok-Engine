package org.robok.engine.core.filetree.provider

/*
 * MIT License
 *
 * Copyright (c) 2024 Xed-Editor
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * provided to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import org.robok.engine.core.filetree.file.FileObject
import org.robok.engine.core.filetree.R
import org.robok.engine.core.filetree.interfaces.FileIconProvider
import org.robok.engine.core.filetree.model.Node

class DefaultFileIconProvider(context: Context) : FileIconProvider {
  private val file = ContextCompat.getDrawable(context, R.drawable.file)
  private val folder = ContextCompat.getDrawable(context, R.drawable.folder)
  private val chevronRight = ContextCompat.getDrawable(context, R.drawable.ic_chevron_right)
  private val expandMore = ContextCompat.getDrawable(context, R.drawable.round_expand_more_24)

  // Lazy initialization for drawables to save memory
  private val java by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_java) }
  private val html by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_html) }
  private val kotlin by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_kotlin) }
  private val python by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_python) }
  private val xml by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_xml) }
  private val js by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_js) }
  private val c by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_c) }
  private val cpp by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_cpp) }
  private val json by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_json) }
  private val css by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_css) }
  private val csharp by lazy { ContextCompat.getDrawable(context, R.drawable.ic_language_csharp) }
  private val unknown by lazy { ContextCompat.getDrawable(context, R.drawable.question_mark) }
  private val bash by lazy { ContextCompat.getDrawable(context, R.drawable.bash) }
  private val apk by lazy { ContextCompat.getDrawable(context, R.drawable.apkfile) }
  private val archive by lazy { ContextCompat.getDrawable(context, R.drawable.archive) }
  private val contract by lazy { ContextCompat.getDrawable(context, R.drawable.contract) }
  private val text by lazy { ContextCompat.getDrawable(context, R.drawable.text) }
  private val video by lazy { ContextCompat.getDrawable(context, R.drawable.video) }
  private val audio by lazy { ContextCompat.getDrawable(context, R.drawable.music) }
  private val image by lazy { ContextCompat.getDrawable(context, R.drawable.image) }
  private val react by lazy { ContextCompat.getDrawable(context, R.drawable.react) }
  private val rust by lazy { ContextCompat.getDrawable(context, R.drawable.rust) }
  private val markdown by lazy { ContextCompat.getDrawable(context, R.drawable.markdown) }
  private val php by lazy { ContextCompat.getDrawable(context, R.drawable.php) }

  override fun getIcon(node: Node<FileObject>): Drawable? {
    return if (node.value.isFile()) {
      when (node.value.getName()) {
        "contract.sol",
        "LICENSE",
        "NOTICE",
        "NOTICE.txt" -> contract
        "gradlew" -> bash

        else ->
          when (node.value.getName().substringAfterLast('.', "")) {
            "java",
            "bsh" -> java
            "html" -> html
            "kt",
            "kts" -> kotlin
            "py" -> python
            "xml" -> xml
            "js" -> js
            "c",
            "h" -> c
            "cpp",
            "hpp" -> cpp
            "json" -> json
            "css",
            "sass",
            "scss" -> css
            "cs" -> csharp
            "sh",
            "bash",
            "zsh",
            "bat" -> bash
            "apk",
            "xapk",
            "apks" -> apk
            "zip",
            "rar",
            "7z",
            "gz",
            "bz2",
            "tar",
            "xz" -> archive
            "md" -> markdown
            "txt" -> text
            "mp3",
            "wav",
            "ogg",
            "m4a",
            "flac" -> audio
            "mp4",
            "mov",
            "avi",
            "mkv" -> video
            "jpg",
            "jpeg",
            "png",
            "gif",
            "bmp" -> image
            "rs" -> rust
            "jsx",
            "tsx" -> react
            "php" -> php
            else -> file
          }
      }
    } else if (node.value.isDirectory()) {
      folder
    } else {
      unknown
    }
  }

  override fun getChevronRight(): Drawable? {
    return chevronRight
  }

  override fun getExpandMore(): Drawable? {
    return expandMore
  }
}
