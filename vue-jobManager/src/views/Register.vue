<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2>校园兼职管理系统</h2>
      <el-form :model="form" :rules="rules" ref="registerForm">
        <el-form-item prop="name">
          <el-input v-model="form.name" placeholder="用户名" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item prop="phoneNumber">
          <el-input v-model="form.phoneNumber" placeholder="手机号" />
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" placeholder="邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" style="width: 100%"> 注册 </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="$router.push('/login')"> 已有账号？去登录 </el-button>
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
const registerForm = ref()

const form = reactive({
  name: '',
  password: '',
  phoneNumber: '',
  email: '',
  createTime: new Date().toISOString(),
  permission: 1, // 普通用户权限
})

const rules = {
  name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  phoneNumber: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
  ],
}

const handleRegister = async () => {
  try {
    await registerForm.value.validate()
    const response = await userApi.register(form)
    if (response.code === 200) {
      ElMessage.success(response.data || '注册成功')
      router.push('/login')
    } else {
      ElMessage.error(response.data || '注册失败')
    }
  } catch (error) {
    ElMessage.error('注册失败')
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-card {
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
