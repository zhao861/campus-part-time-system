<template>
  <div class="profile-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>个人资料</h3>
          <el-button type="primary" @click="goToDashboard" plain>
            <el-icon><Back /></el-icon>
            返回主界面
          </el-button>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户名">
          {{ userInfo.name || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="电话号码">
          {{ userInfo.phoneNumber || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="邮箱">
          {{ userInfo.email || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="权限">
          <el-tag :type="userInfo.permission === 0 ? 'info' : 'success'">
            {{
              userInfo.permission === 0
                ? '学生用户'
                : userInfo.permission === 1
                  ? '发布者用户'
                  : '管理员用户'
            }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDateTime(userInfo.createTime) || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { userApi } from '../api'
import { ElMessage, ElButton, ElIcon } from 'element-plus'
import { Back } from '@element-plus/icons-vue'

const router = useRouter()

// 返回主界面
const goToDashboard = () => {
  router.push('/dashboard')
}

// 用户信息，从后端API获取
const userInfo = ref({})
const loading = ref(false)

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  try {
    return new Date(dateTime).toLocaleString()
  } catch {
    return dateTime
  }
}

// 获取用户个人资料
const fetchUserProfile = async () => {
  loading.value = true
  try {
    const response = await userApi.getUserProfile()
    if (response.code === 200) {
      // 不展示密码和id，只保存其余信息
      const { password, id, ...rest } = response.data
      userInfo.value = rest
    } else {
      ElMessage.error(response.data || '获取个人资料失败')
    }
  } catch (error) {
    console.error('获取个人资料失败:', error)
    ElMessage.error('获取个人资料失败')
  } finally {
    loading.value = false
  }
}

// 页面加载时获取用户资料
onMounted(() => {
  fetchUserProfile()
})
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 24px auto;
  padding: 0 20px;
}

/* 卡片样式优化 */
:deep(.el-card) {
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  overflow: hidden;
}

:deep(.el-card:hover) {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

/* 卡片头部样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.card-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

/* 返回按钮样式 */
:deep(.el-button--primary) {
  border-radius: 20px;
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
  border: none;
  background-color: rgba(255, 255, 255, 0.2);
}

:deep(.el-button--primary:hover) {
  background-color: rgba(255, 255, 255, 0.3);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 255, 255, 0.3);
}

/* 描述列表样式优化 */
:deep(.el-descriptions) {
  padding: 24px;
}

:deep(.el-descriptions__body) {
  background-color: white;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
  color: #303133;
  background-color: #fafafa;
}

:deep(.el-descriptions__cell) {
  padding: 16px 12px;
}

/* 标签样式 */
:deep(.el-tag) {
  border-radius: 12px;
  padding: 4px 12px;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-container {
    padding: 0 16px;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  :deep(.el-descriptions) {
    padding: 16px;
  }

  :deep(.el-descriptions__cell) {
    padding: 12px 8px;
  }
}
</style>
