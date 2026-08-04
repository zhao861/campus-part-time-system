<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <RouterView />
</template>

<style>
/* 全局样式优化 */
* {
  box-sizing: border-box;
}

body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: #f5f7fa;
  color: #303133;
  line-height: 1.6;
}

/* 优化 Element Plus 主题 */
:root {
  --el-color-primary: #409eff;
  --el-color-success: #67c23a;
  --el-color-warning: #e6a23c;
  --el-color-danger: #f56c6c;
  --el-color-info: #909399;
  --el-bg-color: #f5f7fa;
}

/* 优化卡片样式 */
.el-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.el-card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

/* 优化按钮样式 */
.el-button {
  border-radius: 4px;
  transition: all 0.3s ease;
}

/* 优化表格样式 */
.el-table {
  border-radius: 8px;
  overflow: hidden;
}

.el-table th {
  background-color: #fafafa;
  font-weight: 600;
}

/* 优化分页样式 */
.el-pagination {
  margin-top: 20px;
  text-align: right;
}

/* 优化表单样式 */
.el-form-item__label {
  font-weight: 500;
}

/* 优化消息提示位置 */
.el-message {
  top: 60px;
}

/* 优化过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
