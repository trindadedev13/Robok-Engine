package org.robok.engine.core.filetree.adapters

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

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.robok.engine.core.filetree.file.FileObject
import org.robok.engine.core.filetree.R
import org.robok.engine.core.filetree.interfaces.FileClickListener
import org.robok.engine.core.filetree.interfaces.FileIconProvider
import org.robok.engine.core.filetree.interfaces.FileLongClickListener
import org.robok.engine.core.filetree.model.Node
import org.robok.engine.core.filetree.model.TreeViewModel
import org.robok.engine.core.filetree.util.Sorter
import org.robok.engine.core.filetree.widget.FileTree

class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
  val expandView: ImageView = v.findViewById(R.id.expand)
  val fileView: ImageView = v.findViewById(R.id.file_view)
  val textView: TextView = v.findViewById(R.id.text_view)
}

class NodeDiffCallback : DiffUtil.ItemCallback<Node<FileObject>>() {
  override fun areItemsTheSame(oldItem: Node<FileObject>, newItem: Node<FileObject>): Boolean {
    return oldItem.value.getAbsolutePath() == newItem.value.getAbsolutePath()
  }

  override fun areContentsTheSame(oldItem: Node<FileObject>, newItem: Node<FileObject>): Boolean {
    return areItemsTheSame(oldItem, newItem)
  }
}

class FileTreeAdapter(private val context: Context, val fileTree: FileTree) :
  ListAdapter<Node<FileObject>, ViewHolder>(NodeDiffCallback()) {

  var onClickListener: FileClickListener? = null
  var onLongClickListener: FileLongClickListener? = null
  var iconProvider: FileIconProvider? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view: View =
      LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false)
    val holder = ViewHolder(view)

    val clickListener =
      View.OnClickListener {
        val adapterPosition = holder.adapterPosition
        if (adapterPosition != RecyclerView.NO_POSITION) {
          val clickedNode = getItem(adapterPosition)

          if (clickedNode.value.isDirectory()) {
            if (!clickedNode.isExpand) {
              expandNode(clickedNode)
            } else {
              collapseNode(clickedNode)
            }
            notifyItemChanged(adapterPosition)
          }

          onClickListener?.onClick(clickedNode)
        }
      }

    holder.itemView.setOnClickListener(clickListener)

    holder.itemView.setOnLongClickListener {
      val adapterPosition = holder.adapterPosition
      if (adapterPosition != RecyclerView.NO_POSITION) {
        val clickedNode = getItem(adapterPosition)
        onLongClickListener?.onLongClick(clickedNode)
      }
      true
    }

    holder.expandView.setOnClickListener(clickListener)
    holder.fileView.setPadding(0, 0, 0, 0)
    return holder
  }

  // parent file
  fun newFile(file: FileObject) {

    val tempData = currentList.toMutableList()

    var xnode: Node<FileObject>? = null
    for (node in tempData) {
      if (node.value == file) {
        xnode = node
        break
      }
    }

    val cache = Sorter.sort(file)

    val children1 = TreeViewModel.getChildren(xnode!!)

    tempData.removeAll(children1.toSet())
    TreeViewModel.remove(xnode, xnode.child)
    xnode.isExpand = false

    val index = tempData.indexOf(xnode)

    tempData.addAll(index + 1, cache)
    TreeViewModel.add(xnode, cache)
    xnode.isExpand = true

    submitList(tempData)
  }

  fun removeFile(file: FileObject) {
    val tempData = currentList.toMutableList()
    var nodetoremove: Node<FileObject>? = null

    for (node in tempData) {
      if (node.value == file) {
        nodetoremove = node
        break
      }
    }

    if (nodetoremove != null) {
      if (file.isFile()) {
        val index = tempData.indexOf(nodetoremove)
        if (index != -1) {
          tempData.removeAt(index)
        }
      } else {
        val children = TreeViewModel.getChildren(nodetoremove)
        tempData.removeAll(children.toSet())
        TreeViewModel.remove(nodetoremove, nodetoremove.child)
        nodetoremove.isExpand = false

        val index = tempData.indexOf(nodetoremove)
        if (index != -1) {
          tempData.removeAt(index)
        }
      }

      submitList(tempData)
    }
  }

  fun renameFile(child: FileObject, newFile: FileObject) {
    val tempData = currentList.toMutableList()
    for (node in tempData) {
      if (node.value == child) {
        node.value = newFile
        submitList(tempData)

        val position = tempData.indexOf(node)
        if (position != -1) {
          notifyItemChanged(position)
        }
        break
      }
    }
  }

  private fun dpToPx(dpValue: Float): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val node = getItem(position)
    val isDir = node.value.isDirectory()
    val expandView = holder.expandView
    val fileView = holder.fileView

    // Reset padding and margins to avoid accumulation
    holder.itemView.setPadding(0, 0, 0, 0)
    val layoutParams = fileView.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(0, 0, 0, 0)
    fileView.layoutParams = layoutParams

    // Set padding based on node level
    holder.itemView.setPadding(node.level * dpToPx(17f), dpToPx(5f), 0, 0)
    fileView.setImageDrawable(iconProvider?.getIcon(node))

    val icChevronRight = iconProvider?.getChevronRight()

    if (isDir) {
      expandView.visibility = View.VISIBLE
      if (!node.isExpand) {
        expandView.setImageDrawable(icChevronRight)
      } else {
        expandView.setImageDrawable(iconProvider?.getExpandMore())
      }
    } else {
      layoutParams.setMargins(icChevronRight!!.intrinsicWidth + dpToPx(10f), 0, 0, 0)
      fileView.layoutParams = layoutParams
      expandView.visibility = View.GONE
    }

    holder.textView.text =
      if (node.value.getName().isBlank()) {
        "no_name"
      } else {
        node.value.getName()
      }
  }

  fun expandNode(clickedNode: Node<FileObject>) {
    val tempData = currentList.toMutableList()
    val index = tempData.indexOf(clickedNode)
    val children = Sorter.sort(clickedNode.value)
    tempData.addAll(index + 1, children)
    TreeViewModel.add(clickedNode, children)
    clickedNode.isExpand = true
    submitList(tempData)
  }

  private fun collapseNode(clickedNode: Node<FileObject>) {
    val tempData = currentList.toMutableList()
    val children = TreeViewModel.getChildren(clickedNode)
    tempData.removeAll(children.toSet())
    TreeViewModel.remove(clickedNode, clickedNode.child)
    clickedNode.isExpand = false
    submitList(tempData)
  }
}
