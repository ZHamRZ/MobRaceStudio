package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModernTreeView {
    private final int x, y, width, height;
    private final List<TreeNode> rootNodes;
    private int scrollOffset;
    private int selectedIndex = -1;
    private Consumer<Integer> onSelect;

    public ModernTreeView(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rootNodes = new ArrayList<>();
    }

    public TreeNode addRoot(String label) {
        TreeNode node = new TreeNode(label, null);
        rootNodes.add(node);
        return node;
    }

    public ModernTreeView setOnSelect(Consumer<Integer> callback) {
        this.onSelect = callback;
        return this;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        GuiRenderHelper.fill(ctx, x, y, width, height, GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.border(ctx, x, y, width, height, 1, GuiRenderHelper.COLOR_BORDER);

        int currentY = y + 4 - scrollOffset;
        int index = 0;

        for (TreeNode root : rootNodes) {
            currentY = renderNode(ctx, root, x + 4, currentY, 0, mouseX, mouseY, index);
            index++;
        }
    }

    private int renderNode(DrawContext ctx, TreeNode node, int nx, int ny, int depth, int mx, int my, int idx) {
        if (ny > y + height) return ny;
        int nodeHeight = 20;

        boolean hovered = GuiRenderHelper.isHovered(mx, my, nx, ny, width - 8 - depth * 12, nodeHeight);

        if (hovered) {
            GuiRenderHelper.fill(ctx, nx, ny, width - 8 - depth * 12, nodeHeight, GuiRenderHelper.COLOR_BG_LIGHT);
        }

        String prefix = node.hasChildren() ? (node.expanded ? "\u25BC " : "\u25B6 ") : "  ";
        GuiRenderHelper.drawText(ctx, prefix + node.label, nx + depth * 12, ny + 4, GuiRenderHelper.COLOR_TEXT_PRIMARY);

        ny += nodeHeight;

        if (node.expanded) {
            for (TreeNode child : node.children) {
                ny = renderNode(ctx, child, nx, ny, depth + 1, mx, my, ++idx);
            }
        }

        return ny;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int currentY = y + 4 - scrollOffset;
        int index = 0;

        for (TreeNode root : rootNodes) {
            boolean clicked = clickNode(root, x + 4, currentY, 0, mouseX, mouseY, index);
            if (clicked) return true;
            currentY = getNodeHeight(root, currentY);
            index++;
        }
        return false;
    }

    private boolean clickNode(TreeNode node, int nx, int ny, int depth, double mx, double my, int idx) {
        int nodeHeight = 20;

        if (GuiRenderHelper.isHovered(mx, my, nx, ny, width - 8 - depth * 12, nodeHeight)) {
            if (node.hasChildren()) {
                node.expanded = !node.expanded;
            }
            selectedIndex = idx;
            if (onSelect != null) {
                onSelect.accept(idx);
            }
            return true;
        }

        if (node.expanded) {
            for (TreeNode child : node.children) {
                boolean clicked = clickNode(child, nx, ny + nodeHeight, depth + 1, mx, my, ++idx);
                if (clicked) return true;
                ny += nodeHeight;
            }
        }
        return false;
    }

    private int getNodeHeight(TreeNode node, int ny) {
        ny += 20;
        if (node.expanded) {
            for (TreeNode child : node.children) {
                ny = getNodeHeight(child, ny);
            }
        }
        return ny;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        scrollOffset = Math.max(0, scrollOffset - (int) verticalAmount * 20);
        return true;
    }

    public static class TreeNode {
        public final String label;
        public final Object data;
        public final List<TreeNode> children;
        public boolean expanded;

        public TreeNode(String label, Object data) {
            this.label = label;
            this.data = data;
            this.children = new ArrayList<>();
            this.expanded = false;
        }

        public TreeNode addChild(String label) {
            TreeNode child = new TreeNode(label, null);
            children.add(child);
            return child;
        }

        public TreeNode addChild(String label, Object data) {
            TreeNode child = new TreeNode(label, data);
            children.add(child);
            return child;
        }

        public boolean hasChildren() {
            return !children.isEmpty();
        }
    }
}
