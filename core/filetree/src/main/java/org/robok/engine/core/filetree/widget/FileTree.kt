package org.robok.engine.core.filetree.widget

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
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.robok.engine.core.filetree.adapters.FileTreeAdapter
import org.robok.engine.core.filetree.interfaces.FileClickListener
import org.robok.engine.core.filetree.interfaces.FileIconProvider
import org.robok.engine.core.filetree.interfaces.FileLongClickListener
import org.robok.engine.core.filetree.FileObject
import org.robok.engine.core.filetree.model.Node
import org.robok.engine.core.filetree.provider.DefaultFileIconProvider
import org.robok.engine.core.filetree.util.Sorter
import org.robok.engine.core.filetree.R

class FileTree : RecyclerView {
    private var fileTreeAdapter: FileTreeAdapter
    private lateinit var rootFileObject: FileObject

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
    ) : super(context, attrs, defStyleAttr)

    init {
        setItemViewCacheSize(100)
        layoutManager = LinearLayoutManager(context)
        val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.animation)
        this.layoutAnimation = animation
        fileTreeAdapter = FileTreeAdapter(context, this)
    }

    fun getRootFileObject(): FileObject {
        return rootFileObject
    }

    fun setIconProvider(fileIconProvider: FileIconProvider) {
        fileTreeAdapter.iconProvider = fileIconProvider
    }

    fun setOnFileClickListener(clickListener: FileClickListener) {
        fileTreeAdapter.onClickListener = clickListener
    }

    fun setOnFileLongClickListener(longClickListener: FileLongClickListener) {
        fileTreeAdapter.onLongClickListener = longClickListener
    }

    private var init = false
    private var showRootNode: Boolean = true

    fun showRootNode(): Boolean {
        return showRootNode
    }

    fun loadFiles(file: FileObject, showRootNodeX: Boolean? = null) {
        rootFileObject = file

        showRootNodeX?.let { showRootNode = it }

        val nodes: List<Node<FileObject>> =
            if (showRootNode) {
                mutableListOf<Node<FileObject>>().apply { add(Node(file)) }
            } else {
                Sorter.sort(file)
            }

        if (init.not()) {
            if (fileTreeAdapter.iconProvider == null) {
                fileTreeAdapter.iconProvider = DefaultFileIconProvider(context)
            }
            adapter = fileTreeAdapter
            init = true
        }
        fileTreeAdapter.submitList(nodes)
        if (showRootNode) {
            fileTreeAdapter.expandNode(nodes[0])
        }
    }

    fun reloadFileTree() {
        val nodes: List<Node<FileObject>> =
            if (showRootNode) {
                mutableListOf<Node<FileObject>>().apply { add(Node(rootFileObject)) }
            } else {
                Sorter.sort(rootFileObject)
            }
        fileTreeAdapter.submitList(nodes)
    }

    fun onFileAdded(file: FileObject) {
        fileTreeAdapter.newFile(file)
    }

    fun onFileRemoved(file: FileObject) {
        fileTreeAdapter.removeFile(file)
    }

    fun onFileRenamed(file: FileObject, newFileObject: FileObject) {
        fileTreeAdapter.renameFile(file, newFileObject)
    }
}
