package org.robok.engine.core.filetree.file

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
import android.net.Uri
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable

interface FileObject : Serializable {
  fun listFiles(): List<FileObject>

  fun isDirectory(): Boolean

  fun isFile(): Boolean

  fun getName(): String

  fun getParentFile(): FileObject?

  fun exists(): Boolean

  fun createNewFile(): Boolean

  fun mkdir(): Boolean

  fun mkdirs(): Boolean

  fun writeText(text: String)

  fun getInputStream(): InputStream

  fun getOutPutStream(append: Boolean): OutputStream

  fun getAbsolutePath(): String

  fun length(): Long

  fun delete(): Boolean

  fun toUri(): Uri

  fun getMimeType(context: Context): String?

  fun renameTo(string: String): Boolean

  fun hasChild(name: String): Boolean

  fun createChild(createFile: Boolean, name: String): FileObject?

  fun canWrite(): Boolean

  fun canRead(): Boolean
}
