package org.gampiot.robok.feature.treeview.file.example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.gampiot.robok.feature.treeview.R;
import org.gampiot.robok.feature.treeview.model.TreeNode;
import org.gampiot.robok.feature.treeview.view.AndroidTreeView;

import java.io.File;

public class FileTreeViewActivity extends AppCompatActivity {

    private LinearLayout listContainer;
    private TreeNode root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_tree_view);

        listContainer = findViewById(R.id.listContainer);
        
        File directory = new File("/sdcard/Robok/.projects/A/");
        setupFileTree(directory);
    }

    private void setupFileTree(File rootDir) {
        root = TreeNode.root();
        buildFileTree(rootDir, root);

        AndroidTreeView tView = new AndroidTreeView(this, root);
        tView.setDefaultAnimation(true);

        listContainer.addView(tView.getView());
    }

    private void buildFileTree(File dir, TreeNode parent) {
        if (dir != null && dir.isDirectory()) {
            TreeNode dirNode = new TreeNode(new FileNode(dir.getName(), true)).setViewHolder(new FileTreeNodeViewHolder(this));
            parent.addChild(dirNode);

            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        buildFileTree(file, dirNode);
                    } else {
                        TreeNode fileNode = new TreeNode(new FileNode(file.getName(), false)).setViewHolder(new FileTreeNodeViewHolder(this));
                        dirNode.addChild(fileNode);
                    }
                }
            }
        }
    }

    private static class FileNode {
        String name;
        boolean isDirectory;

        FileNode(String name, boolean isDirectory) {
            this.name = name;
            this.isDirectory = isDirectory;
        }
    }

    private class FileTreeNodeViewHolder extends TreeNode.BaseNodeViewHolder<FileNode> {

        public FileTreeNodeViewHolder(FileTreeViewActivity context) {
            super(context);
        }

        @Override
        public View createNodeView(TreeNode node, FileNode value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.tree_node_item, null, false);

            TextView textView = view.findViewById(R.id.path);
            textView.setText(value.name);

            ImageView iconView = view.findViewById(R.id.icon);
            if (value.isDirectory) {
                iconView.setImageResource(R.drawable.ic_folder);
            } else {
                iconView.setImageResource(R.drawable.ic_file);
            }

            // Configurar visibilidade do ícone de expandir/colapsar
            ImageView expandCollapseIcon = view.findViewById(R.id.expandCollapse);
            if (node.isLeaf()) {
                expandCollapseIcon.setVisibility(View.GONE);
            } else {
                expandCollapseIcon.setVisibility(View.VISIBLE);
                expandCollapseIcon.setImageResource(node.isExpanded() ? R.drawable.ic_collapse : R.drawable.ic_expand);

                expandCollapseIcon.setOnClickListener(v -> {
                    if (node.isExpanded()) {
                        tView.collapseNode(node);
                    } else {
                        tView.expandNode(node);
                    }
                });
            }

            return view;
        }
    }
}
