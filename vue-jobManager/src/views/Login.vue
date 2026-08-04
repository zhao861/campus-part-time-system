<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>校园兼职管理系统</h2>
      <el-form :model="form" :rules="rules" ref="loginForm">
        <el-form-item prop="name">
          <el-input v-model="form.name" placeholder="请输入用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%"> 登录 </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="$router.push('/register')"> 没有账号？去注册 </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { userApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loginForm = ref()

const form = reactive({
  name: '',
  password: '',
})

const rules = {
  name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const handleLogin = async () => {
  try {
    await loginForm.value.validate()
    const response = await userApi.login(form)
    if (response.code === 200) {
      ElMessage.success('登录成功')
      // 保存用户信息到本地存储或状态管理
      localStorage.setItem('userInfo', JSON.stringify(response.data))
      router.push('/dashboard')
    } else {
      ElMessage.error(response.message || '登录失败')
    }
  } catch (error) {
    ElMessage.error('登录失败')
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 420px;
  padding: 40px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

/* 优化表单样式 */
:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-input__wrapper) {
  border-radius: 6px;
}

:deep(.el-button) {
  height: 40px;
  font-size: 16px;
  font-weight: 500;
}

/* 优化链接样式 */
:deep(.el-button--text) {
  color: #409eff;
  font-size: 14px;
}

:deep(.el-button--text:hover) {
  color: #66b1ff;
}
</style>
