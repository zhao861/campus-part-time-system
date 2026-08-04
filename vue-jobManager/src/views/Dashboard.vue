<template>
  <div class="dashboard">
    <el-container>
      <!-- 头部导航栏 -->
      <el-header height="60px" class="header">
        <div class="header-content">
          <div class="logo">
            <el-icon><Briefcase /></el-icon>
            <span>职位管理系统</span>
          </div>
          <div class="user-info">
            <el-dropdown @command="handleCommand">
              <div class="user-dropdown">
                <el-avatar :size="32" :src="userAvatar">
                  {{ userInfo.name?.charAt(0) || 'U' }}
                </el-avatar>
                <span class="username">{{ userInfo.name || '用户' }}</span>
                <el-icon><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>
                    个人资料
                  </el-dropdown-item>
                  <el-dropdown-item command="changePassword">
                    <el-icon><Lock /></el-icon>
                    修改密码
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>

            <!-- 直接退出按钮（备选方案） -->
            <el-button type="danger" plain @click="handleLogout" class="logout-btn" size="small">
              <el-icon><SwitchButton /></el-icon>
              退出
            </el-button>
          </div>
        </div>
      </el-header>

      <el-container>
        <!-- 侧边栏 -->
        <el-aside width="200px">
          <el-menu :default-active="activeMenu" @select="handleMenuSelect" class="sidebar-menu">
            <!-- 所有用户都能看到兼职列表 -->
            <el-menu-item index="jobList">
              <el-icon><Menu /></el-icon>
              <span>校园兼职列表</span>
            </el-menu-item>

            <!-- 所有用户都能看到收藏兼职列表 -->
            <el-menu-item index="wishJob">
              <el-icon><Star /></el-icon>
              <span>收藏兼职列表</span>
            </el-menu-item>

            <!-- 所有用户都能看到自己的兼职报名列表 -->
            <el-menu-item index="mySignup">
              <el-icon><Document /></el-icon>
              <span>我的兼职报名列表</span>
            </el-menu-item>

            <!-- 发布者用户（permission=1）的功能 -->
            <el-menu-item index="addJob" v-if="userPermission === 1">
              <el-icon><Plus /></el-icon>
              <span>发布职位</span>
            </el-menu-item>
            <el-menu-item index="myJobs" v-if="userPermission === 1">
              <el-icon><Document /></el-icon>
              <span>我的发布</span>
            </el-menu-item>
            <el-menu-item index="signup" v-if="userPermission === 1">
              <el-icon><Setting /></el-icon>
              <span>报名管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>

        <!-- 主内容区 -->
        <el-main>
          <!-- 职位列表内容 -->
          <div v-if="activeMenu === 'jobList'">
            <el-card>
              <template #header>
                <div class="card-header">
                  <h3>校园兼职列表</h3>
                  <el-input
                    v-model="searchQuery"
                    placeholder="请输入兼职名称搜索"
                    clearable
                    style="width: 240px"
                    @clear="handleClearSearch"
                  >
                    <template #append>
                      <el-button type="primary" @click="handleSearch">
                        <el-icon><Search /></el-icon>
                        搜索
                      </el-button>
                    </template>
                  </el-input>
                </div>
              </template>
              <el-table
                :data="currentJobList"
                stripe
                style="width: 100%"
                @sort-change="handleSortChange"
              >
                <el-table-column prop="id" label="兼职ID" width="80" />
                <el-table-column prop="name" label="兼职名称" min-width="150" />
                <el-table-column prop="publisherName" label="发布者" min-width="120" />
                <el-table-column prop="salary" label="时薪" width="100" sortable="custom">
                  <template #default="scope"> {{ scope.row.salary }} 元/时 </template>
                </el-table-column>
                <el-table-column
                  prop="createTime"
                  label="创建时间"
                  min-width="180"
                  sortable="custom"
                >
                  <template #default="scope">
                    {{ new Date(scope.row.createTime).toLocaleString() }}
                  </template>
                </el-table-column>
                <el-table-column prop="updateTime" label="更新时间" min-width="180">
                  <template #default="scope">
                    {{ new Date(scope.row.updateTime).toLocaleString() }}
                  </template>
                </el-table-column>
                <!-- 操作列 -->
                <el-table-column label="操作" width="200">
                  <template #default="scope">
                    <el-button
                      type="primary"
                      :disabled="isWishlisted(scope.row.name)"
                      @click="handleWishJob(scope.row)"
                      size="small"
                      :class="{ 'el-button--success': isWishlisted(scope.row.name) }"
                    >
                      收藏
                    </el-button>
                    <el-button
                      type="info"
                      :disabled="isSignedUp(scope.row.name)"
                      @click="handleSignupJob(scope.row)"
                      size="small"
                      :class="{ 'el-button--warning': isSignedUp(scope.row.name) }"
                    >
                      报名
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <!-- 职位列表分页 -->
              <div style="margin-top: 20px; text-align: right">
                <el-pagination
                  v-model:current-page="pagination.jobList.currentPage"
                  v-model:page-size="pagination.jobList.pageSize"
                  :page-sizes="[10, 15, 20, 25]"
                  :total="pagination.jobList.total"
                  layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handlePageSizeChange('jobList')"
                  @current-change="handleCurrentPageChange('jobList')"
                />
              </div>
            </el-card>
          </div>

          <!-- 修改兼职对话框 -->
          <el-dialog v-model="modifyJobDialogVisible" title="修改兼职" width="500px">
            <el-form
              :model="modifyJobForm"
              :rules="{
                name: [
                  { required: true, message: '请输入职位名称', trigger: 'blur' },
                  { min: 1, max: 20, message: '职位名称长度为1-20个字符', trigger: 'blur' },
                ],
                salary: [
                  { required: true, message: '请输入时薪', trigger: 'blur' },
                  { type: 'number', min: 0, message: '时薪必须大于等于0', trigger: 'blur' },
                ],
              }"
              ref="modifyJobFormRef"
            >
              <el-form-item label="职位名称" prop="name">
                <el-input v-model="modifyJobForm.name" placeholder="请输入职位名称" />
              </el-form-item>
              <el-form-item label="时薪" prop="salary">
                <el-input-number
                  v-model="modifyJobForm.salary"
                  :min="0"
                  placeholder="请输入时薪"
                  :controls="false"
                  :precision="0"
                />
              </el-form-item>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="modifyJobDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmitModifyJob">确定</el-button>
              </span>
            </template>
          </el-dialog>

          <!-- 收藏职位列表内容 -->
          <div v-if="activeMenu === 'wishJob'">
            <el-card>
              <template #header>
                <div class="card-header">
                  <h3>收藏兼职列表</h3>
                </div>
              </template>
              <el-table :data="currentWishJobList" stripe style="width: 100%">
                <el-table-column prop="id" label="ID" width="80" />
                <el-table-column prop="jobName" label="兼职名称" min-width="150" />
                <el-table-column prop="userName" label="用户名" min-width="120" />
                <el-table-column prop="publisherName" label="发布者" min-width="120" />
                <el-table-column prop="salary" label="时薪" width="100">
                  <template #default="scope"> {{ scope.row.salary }} 元/时 </template>
                </el-table-column>
                <el-table-column label="操作" width="200">
                  <template #default="scope">
                    <el-button
                      type="info"
                      :disabled="isSignedUp(scope.row.jobName)"
                      @click="handleSignupJob(scope.row)"
                      size="small"
                      :class="{ 'el-button--warning': isSignedUp(scope.row.jobName) }"
                    >
                      报名
                    </el-button>
                    <el-button type="danger" @click="handleDeleteWishJob(scope.row)" size="small">
                      删除收藏
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <!-- 收藏职位列表分页 -->
              <div style="margin-top: 20px; text-align: right">
                <el-pagination
                  v-model:current-page="pagination.wishJobList.currentPage"
                  v-model:page-size="pagination.wishJobList.pageSize"
                  :page-sizes="[10, 15, 20, 25]"
                  :total="pagination.wishJobList.total"
                  layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handlePageSizeChange('wishJobList')"
                  @current-change="handleCurrentPageChange('wishJobList')"
                />
              </div>
            </el-card>
          </div>

          <!-- 我的兼职报名列表内容 -->
          <div v-if="activeMenu === 'mySignup'">
            <el-card>
              <template #header>
                <div class="card-header">
                  <h3>我的兼职报名列表</h3>
                </div>
              </template>
              <el-table :data="currentMySignupList" stripe style="width: 100%">
                <el-table-column prop="id" label="ID" width="80" />
                <el-table-column prop="userName" label="用户名" min-width="120" />
                <el-table-column prop="publisherName" label="发布者" min-width="120" />
                <el-table-column prop="jobName" label="职位名称" min-width="150" />
                <el-table-column prop="phoneNumber" label="电话号码" min-width="150" />
                <el-table-column prop="email" label="邮箱" min-width="200" />
                <el-table-column prop="createTime" label="报名时间" min-width="180">
                  <template #default="scope">
                    {{ new Date(scope.row.createTime).toLocaleString() }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="120">
                  <template #default="scope">
                    <el-button
                      type="danger"
                      @click="handleDeleteRegisterJob(scope.row)"
                      size="small"
                      :disabled="scope.row.status === 1"
                    >
                      删除报名
                    </el-button>
                    <div v-if="scope.row.status === 1" class="delete-tip">
                      无法删除已经审核的报名
                    </div>
                  </template>
                </el-table-column>
              </el-table>
              <!-- 我的兼职报名列表分页 -->
              <div style="margin-top: 20px; text-align: right">
                <el-pagination
                  v-model:current-page="pagination.mySignupList.currentPage"
                  v-model:page-size="pagination.mySignupList.pageSize"
                  :page-sizes="[10, 15, 20, 25]"
                  :total="pagination.mySignupList.total"
                  layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handlePageSizeChange('mySignupList')"
                  @current-change="handleCurrentPageChange('mySignupList')"
                />
              </div>
            </el-card>
          </div>

          <!-- 修改兼职对话框 -->
          <div v-if="activeMenu === 'addJob'">
            <el-card>
              <template #header>
                <div class="card-header">
                  <h3>发布职位</h3>
                </div>
              </template>
              <el-form
                :model="jobForm"
                :rules="{
                  name: [
                    { required: true, message: '请输入职位名称', trigger: 'blur' },
                    { min: 1, max: 20, message: '职位名称长度为1-20个字符', trigger: 'blur' },
                    // 移除只能包含数字和字母的限制，允许中文
                  ],
                  salary: [
                    { required: true, message: '请输入时薪', trigger: 'blur' },
                    { type: 'number', min: 0, message: '时薪必须大于等于0', trigger: 'blur' },
                  ],
                }"
                ref="jobFormRef"
              >
                <el-form-item label="职位名称" prop="name">
                  <el-input v-model="jobForm.name" placeholder="请输入职位名称" />
                </el-form-item>
                <el-form-item label="发布者" prop="publisherName">
                  <el-input v-model="jobForm.publisherName" disabled />
                </el-form-item>
                <el-form-item label="时薪" prop="salary">
                  <el-input-number
                    v-model="jobForm.salary"
                    :min="0"
                    placeholder="请输入时薪"
                    :controls="false"
                    :precision="0"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleAddJob">发布职位</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </div>

          <!-- 我的发布内容 -->
          <div v-if="activeMenu === 'myJobs'">
            <el-card>
              <template #header>
                <div class="card-header">
                  <h3>我的发布</h3>
                </div>
              </template>
              <el-table :data="currentMyJobs" stripe style="width: 100%">
                <el-table-column prop="id" label="职位ID" width="80" />
                <el-table-column prop="name" label="职位名称" min-width="150" />
                <el-table-column prop="publisherName" label="发布者" min-width="120" />
                <el-table-column prop="salary" label="时薪" width="100">
                  <template #default="scope"> {{ scope.row.salary }} 元/时 </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" min-width="180">
                  <template #default="scope">
                    {{ new Date(scope.row.createTime).toLocaleString() }}
                  </template>
                </el-table-column>
                <el-table-column prop="updateTime" label="更新时间" min-width="180">
                  <template #default="scope">
                    {{ new Date(scope.row.updateTime).toLocaleString() }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="scope">
                    <el-button type="primary" @click="handleModifyJob(scope.row)" size="small">
                      修改
                    </el-button>
                    <el-button type="danger" @click="handleDeleteJob(scope.row)" size="small">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <!-- 我的发布列表分页 -->
              <div style="margin-top: 20px; text-align: right">
                <el-pagination
                  v-model:current-page="pagination.myJobs.currentPage"
                  v-model:page-size="pagination.myJobs.pageSize"
                  :page-sizes="[10, 15, 20, 25]"
                  :total="pagination.myJobs.total"
                  layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handlePageSizeChange('myJobs')"
                  @current-change="handleCurrentPageChange('myJobs')"
                />
              </div>
            </el-card>
          </div>

          <!-- 报名管理内容 -->
          <div v-if="activeMenu === 'signup'">
            <el-card>
              <template #header>
                <div class="card-header">
                  <h3>报名管理</h3>
                </div>
              </template>
              <!-- 按职位名称分类展示报名信息 -->
              <div
                v-for="(group, jobName) in signupList.reduce((acc, signup) => {
                  if (!acc[signup.jobName]) {
                    acc[signup.jobName] = []
                  }
                  acc[signup.jobName].push(signup)
                  return acc
                }, {})"
                :key="jobName"
              >
                <h4 style="margin: 20px 0 10px 0">{{ jobName }}</h4>
                <el-table :data="group" stripe style="width: 100%; margin-bottom: 20px">
                  <el-table-column prop="id" label="报名ID" width="80" />
                  <el-table-column prop="userName" label="报名人" min-width="120" />
                  <el-table-column prop="phoneNumber" label="电话号码" min-width="150" />
                  <el-table-column prop="email" label="邮箱" min-width="200" />
                  <el-table-column prop="createTime" label="报名时间" min-width="180">
                    <template #default="scope">
                      {{ new Date(scope.row.createTime).toLocaleString() }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="status" label="审核状态" width="120">
                    <template #default="scope">
                      <el-tag :type="scope.row.status === 0 ? 'info' : 'success'">
                        {{ scope.row.status === 0 ? '未审核' : '审核中' }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="120">
                    <template #default="scope">
                      <el-button
                        :type="scope.row.status === 0 ? 'primary' : 'success'"
                        :disabled="scope.row.status === 1"
                        @click="handleReviewJob(scope.row)"
                        size="small"
                      >
                        {{ scope.row.status === 0 ? '审核' : '取消审核' }}
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-card>
          </div>

          <!-- 修改密码对话框 -->
          <el-dialog v-model="changePasswordDialogVisible" title="修改密码" width="500px">
            <el-form
              :model="changePasswordForm"
              :rules="{
                oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
                newPassword: [
                  { required: true, message: '请输入新密码', trigger: 'blur' },
                  { min: 5, max: 20, message: '新密码长度为5-20个字符', trigger: 'blur' },
                ],
              }"
              ref="changePasswordFormRef"
            >
              <el-form-item label="旧密码" prop="oldPassword">
                <el-input
                  v-model="changePasswordForm.oldPassword"
                  type="password"
                  placeholder="请输入旧密码"
                />
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input
                  v-model="changePasswordForm.newPassword"
                  type="password"
                  placeholder="请输入新密码"
                />
              </el-form-item>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="changePasswordDialogVisible = false">取消</el-button>
                <el-button
                  type="primary"
                  @click="handleChangePassword"
                  :loading="changePasswordLoading"
                  >确定</el-button
                >
              </span>
            </template>
          </el-dialog>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, ElDropdown, ElDropdownMenu, ElDropdownItem } from 'element-plus'
import {
  Briefcase,
  User,
  Lock,
  SwitchButton,
  ArrowDown,
  Menu,
  Plus,
  Star,
  Document,
  Setting,
} from '@element-plus/icons-vue'
import { jobApi, wishJobApi, signupApi, userApi } from '../api'

const router = useRouter()

// 用户信息
const userInfo = computed(() => {
  try {
    return JSON.parse(localStorage.getItem('userInfo') || '{}')
  } catch {
    return {}
  }
})

const userPermission = computed(() => userInfo.value.permission || 1)
const userAvatar = computed(() => {
  // 可以根据用户信息生成头像，这里使用默认
  return ''
})

// 退出登录函数
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '退出确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      // 调用后端退出接口（如果有的话）
      // logoutApi.logout()

      // 清除本地存储
      localStorage.removeItem('userInfo')

      // 清除session等其他存储
      sessionStorage.clear()

      // 显示退出成功消息
      ElMessage.success('退出成功')

      // 跳转到登录页
      router.push('/login')

      // 刷新页面确保状态完全重置
      setTimeout(() => {
        window.location.reload()
      }, 100)
    })
    .catch(() => {
      // 用户取消操作
    })
}

// 下拉菜单命令处理
const handleCommand = (command) => {
  switch (command) {
    case 'logout':
      handleLogout()
      break
    case 'profile':
      // 跳转到个人资料页面
      router.push('/profile')
      break
    case 'changePassword':
      // 打开修改密码对话框
      changePasswordDialogVisible.value = true
      break
  }
}

// 响应式变量
const activeMenu = ref('jobList')
const jobList = ref([])
const wishJobList = ref([])
const myJobs = ref([])
const signupList = ref([])
const mySignupList = ref([])
const jobFormRef = ref()
const searchQuery = ref('')

// 修改密码相关变量
const changePasswordDialogVisible = ref(false)
const changePasswordForm = reactive({
  oldPassword: '',
  newPassword: '',
})
const changePasswordFormRef = ref()
const changePasswordLoading = ref(false)

// 分页相关变量
const pagination = reactive({
  jobList: {
    currentPage: 1,
    pageSize: 15,
    total: 0,
  },
  wishJobList: {
    currentPage: 1,
    pageSize: 15,
    total: 0,
  },
  myJobs: {
    currentPage: 1,
    pageSize: 15,
    total: 0,
  },
  signupList: {
    currentPage: 1,
    pageSize: 15,
    total: 0,
  },
  mySignupList: {
    currentPage: 1,
    pageSize: 15,
    total: 0,
  },
})

// 排序相关变量
const sortConfig = reactive({
  prop: '', // 排序字段，可选值：salary, createTime
  order: '', // 排序方向，可选值：ascending, descending
})

// 计算当前页显示的数据
const currentJobList = computed(() => {
  // 复制原始数据，避免修改原始数据
  let sortedList = [...jobList.value]

  // 根据排序配置进行排序
  if (sortConfig.prop) {
    sortedList.sort((a, b) => {
      let valueA, valueB
      if (sortConfig.prop === 'createTime') {
        // 处理时间字段
        valueA = new Date(a.createTime).getTime()
        valueB = new Date(b.createTime).getTime()
      } else {
        // 处理其他字段（如salary）
        valueA = a[sortConfig.prop]
        valueB = b[sortConfig.prop]
      }

      if (valueA < valueB) {
        return sortConfig.order === 'ascending' ? -1 : 1
      }
      if (valueA > valueB) {
        return sortConfig.order === 'ascending' ? 1 : -1
      }
      return 0
    })
  }

  // 分页
  const { currentPage, pageSize } = pagination.jobList
  const start = (currentPage - 1) * pageSize
  const end = start + pageSize
  return sortedList.slice(start, end)
})

const currentWishJobList = computed(() => {
  const { currentPage, pageSize } = pagination.wishJobList
  const start = (currentPage - 1) * pageSize
  const end = start + pageSize
  return wishJobList.value.slice(start, end)
})

const currentMyJobs = computed(() => {
  const { currentPage, pageSize } = pagination.myJobs
  const start = (currentPage - 1) * pageSize
  const end = start + pageSize
  return myJobs.value.slice(start, end)
})

const currentMySignupList = computed(() => {
  const { currentPage, pageSize } = pagination.mySignupList
  const start = (currentPage - 1) * pageSize
  const end = start + pageSize
  return mySignupList.value.slice(start, end)
})

// 跟踪已收藏和已报名的职位
const wishlistedJobs = ref([])
const signedUpJobs = ref([])

// 修改兼职表单
const modifyJobDialogVisible = ref(false)
const modifyJobForm = reactive({
  name: '',
  salary: 0,
})
const originalJob = reactive({
  name: '',
  publisherName: '',
})
const modifyJobFormRef = ref()

const jobForm = reactive({
  name: '',
  publisherName: userInfo.value.name || '',
  salary: 0,
  createTime: new Date().toISOString(),
})

// 检查职位是否已收藏
const isWishlisted = (jobName) => {
  return wishlistedJobs.value.some((job) => job.name === jobName)
}

// 检查职位是否已报名
const isSignedUp = (jobName) => {
  return signedUpJobs.value.some((job) => job.name === jobName)
}

// 收藏兼职功能
const handleWishJob = async (job) => {
  try {
    const requestData = {
      jobName: job.name,
      publisherName: job.publisherName,
      salary: job.salary,
    }
    const res = await wishJobApi.addWishJob(requestData)
    if (res.code === 200) {
      // 弹窗显示返回的data属性
      ElMessage.success(res.data)
      // 添加到已收藏列表，用于按钮状态管理
      wishlistedJobs.value.push(job)
    } else {
      // 弹窗显示返回的data属性
      ElMessage.error(res.data)
    }
  } catch (error) {
    console.error('收藏失败:', error)
    ElMessage.error('收藏失败')
  }
}

// 报名兼职功能
const handleSignupJob = async (job) => {
  try {
    // 兼容职位列表（name字段）和收藏职位列表（jobName字段）
    const jobName = job.name || job.jobName
    const requestData = {
      name: jobName,
      publisherName: job.publisherName,
      salary: job.salary,
    }
    const res = await signupApi.signupJob(requestData)
    if (res.code === 200) {
      // 弹窗显示返回的data属性
      ElMessage.success(res.data)
      // 添加到已报名列表，用于按钮状态管理
      signedUpJobs.value.push({ name: jobName, ...job })
    } else {
      // 弹窗显示返回的data属性
      ElMessage.error(res.data)
    }
  } catch (error) {
    console.error('报名失败:', error)
    ElMessage.error('报名失败')
  }
}

// 菜单选择处理函数
const handleMenuSelect = (index) => {
  activeMenu.value = index
  // 根据选中的菜单加载对应数据
  if (index === 'jobList') {
    loadJobList()
  } else if (index === 'wishJob') {
    loadWishJobList()
  } else if (index === 'myJobs') {
    loadMyJobs()
  } else if (index === 'signup') {
    loadSignupList()
  } else if (index === 'mySignup') {
    loadMySignupList()
  } else if (index === 'profile') {
    // 跳转到个人资料页面
    router.push('/profile')
  }
}

// 加载职位列表数据
const loadJobList = async () => {
  try {
    const res = await jobApi.getJobList()
    if (res.code === 200) {
      jobList.value = res.data
      // 更新分页信息
      pagination.jobList.total = res.data.length
      pagination.jobList.currentPage = 1
    } else {
      ElMessage.error(res.data || '获取职位列表失败')
    }
  } catch (error) {
    console.error('获取职位列表失败:', error)
    ElMessage.error('获取职位列表失败')
  }
}

// 搜索职位功能
const handleSearch = async () => {
  try {
    // 如果搜索框为空，调用原始的loadJobList
    if (!searchQuery.value.trim()) {
      loadJobList()
      return
    }

    const requestData = {
      name: searchQuery.value,
    }
    const res = await jobApi.queryJob(requestData)
    if (res.code === 200) {
      jobList.value = res.data
      // 更新分页信息
      pagination.jobList.total = res.data.length
      pagination.jobList.currentPage = 1
    } else {
      ElMessage.error(res.data || '搜索职位失败')
    }
  } catch (error) {
    console.error('搜索职位失败:', error)
    ElMessage.error('搜索职位失败')
  }
}

// 清空搜索
const handleClearSearch = () => {
  searchQuery.value = ''
  loadJobList()
}

// 加载心愿职位列表数据
const loadWishJobList = async () => {
  try {
    const res = await wishJobApi.getWishJobList()
    if (res.code === 200) {
      wishJobList.value = res.data
      // 更新已收藏列表，用于按钮状态管理
      wishlistedJobs.value = res.data
      // 更新分页信息
      pagination.wishJobList.total = res.data.length
      pagination.wishJobList.currentPage = 1
    } else {
      ElMessage.error(res.data || '获取心愿职位列表失败')
    }
  } catch (error) {
    console.error('获取心愿职位列表失败:', error)
    ElMessage.error('获取心愿职位列表失败')
  }
}

// 加载我的发布职位列表
const loadMyJobs = async () => {
  try {
    // 调用获取我的发布职位的API
    const res = await jobApi.getMyJobs()
    if (res.code === 200) {
      myJobs.value = res.data
      // 更新分页信息
      pagination.myJobs.total = res.data.length
      pagination.myJobs.currentPage = 1
    } else {
      ElMessage.error(res.data || '获取我的发布失败')
    }
  } catch (error) {
    console.error('获取我的发布失败:', error)
    ElMessage.error('获取我的发布失败')
  }
}

// 加载报名列表数据
const loadSignupList = async () => {
  try {
    const res = await signupApi.getSignupList()
    if (res.code === 200) {
      signupList.value = res.data
      // 更新分页信息
      pagination.signupList.total = res.data.length
      pagination.signupList.currentPage = 1
    } else {
      ElMessage.error(res.data || '获取报名列表失败')
    }
  } catch (error) {
    console.error('获取报名列表失败:', error)
    ElMessage.error('获取报名列表失败')
  }
}

// 加载我的兼职报名列表数据
const loadMySignupList = async () => {
  try {
    const res = await signupApi.getSignUpJobView()
    if (res.code === 200) {
      mySignupList.value = res.data
      // 更新分页信息
      pagination.mySignupList.total = res.data.length
      pagination.mySignupList.currentPage = 1
    } else {
      ElMessage.error(res.data || '获取我的兼职报名列表失败')
    }
  } catch (error) {
    console.error('获取我的兼职报名列表失败:', error)
    ElMessage.error('获取我的兼职报名列表失败')
  }
}

// 发布职位功能
const handleAddJob = async () => {
  try {
    if (!jobFormRef.value) return
    await jobFormRef.value.validate()

    const requestData = {
      name: jobForm.name,
      salary: jobForm.salary,
      createTime: new Date().toISOString(),
      updateTime: new Date().toISOString(),
    }

    const res = await jobApi.addJob(requestData)
    if (res.code === 200) {
      ElMessage.success(res.data || '发布成功')
      // 重置表单
      jobForm.name = ''
      jobForm.salary = 0
      // 刷新职位列表或我的发布列表
      if (activeMenu.value === 'jobList') {
        loadJobList()
      } else if (activeMenu.value === 'myJobs') {
        loadMyJobs()
      }
    } else {
      ElMessage.error(res.data || '发布失败')
    }
  } catch (error) {
    ElMessage.error('发布失败')
  }
}

// 审核报名功能
const handleReviewJob = async (signup) => {
  try {
    // 点击审核按钮时，使用当前兼职的实际状态
    const requestData = {
      userName: signup.userName,
      publisherName: signup.publisherName,
      jobName: signup.jobName,
      status: signup.status, // 使用当前兼职的实际状态
    }
    const res = await signupApi.reviewJob(requestData)
    if (res.code === 200) {
      // 弹窗显示返回的data属性
      ElMessage.success(res.data)
      // 刷新报名列表，使数据得到更新
      loadSignupList()
    } else {
      // 弹窗显示返回的data属性
      ElMessage.error(res.data)
    }
  } catch (error) {
    console.error('审核失败:', error)
    ElMessage.error('审核失败')
  }
}

// 删除兼职功能
const handleDeleteJob = async (job) => {
  try {
    const requestData = {
      name: job.name,
      publisherName: job.publisherName,
      salary: job.salary,
    }
    const res = await jobApi.deleteJob(requestData)
    if (res.code === 200) {
      ElMessage.success(res.data || '删除成功')
      // 刷新我的发布列表
      loadMyJobs()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败')
  }
}

// 删除收藏职位功能
const handleDeleteWishJob = async (job) => {
  try {
    const requestData = {
      jobName: job.jobName,
      publisherName: job.publisherName,
    }
    const res = await wishJobApi.deleteWishJob(requestData)
    if (res.code === 200) {
      // 弹窗显示返回的data属性
      ElMessage.success(res.data)
      // 刷新收藏职位列表
      loadWishJobList()
    } else {
      // 弹窗显示返回的data属性
      ElMessage.error(res.data)
    }
  } catch (error) {
    console.error('删除收藏失败:', error)
    ElMessage.error('删除收藏失败')
  }
}

// 删除兼职报名功能
const handleDeleteRegisterJob = async (signup) => {
  try {
    const requestData = {
      userName: signup.userName,
      publisherName: signup.publisherName,
      jobName: signup.jobName,
    }
    const res = await signupApi.deleteRegisterJob(requestData)
    if (res.code === 200) {
      // 弹窗显示返回的data属性
      ElMessage.success(res.data)
      // 刷新我的兼职报名列表
      loadMySignupList()
    } else {
      // 弹窗显示返回的data属性
      ElMessage.error(res.data)
    }
  } catch (error) {
    console.error('删除报名失败:', error)
    ElMessage.error('删除报名失败')
  }
}

// 打开修改兼职对话框
const handleModifyJob = (job) => {
  // 保存原始职位信息
  originalJob.name = job.name
  originalJob.publisherName = job.publisherName
  // 初始化修改表单
  modifyJobForm.name = job.name
  modifyJobForm.salary = job.salary
  // 打开对话框
  modifyJobDialogVisible.value = true
}

// 提交修改兼职
const handleSubmitModifyJob = async () => {
  try {
    if (!modifyJobFormRef.value) return
    await modifyJobFormRef.value.validate()

    const requestData = {
      jobName: originalJob.name,
      publisherName: originalJob.publisherName,
      job: {
        name: modifyJobForm.name,
        publisherName: originalJob.publisherName,
        salary: modifyJobForm.salary,
      },
    }

    const res = await jobApi.modifyJob(requestData)
    if (res.code === 200) {
      ElMessage.success(res.data || '修改成功')
      // 关闭对话框
      modifyJobDialogVisible.value = false
      // 刷新我的发布列表
      loadMyJobs()
    } else {
      ElMessage.error(res.data || '修改失败')
    }
  } catch (error) {
    ElMessage.error('修改失败')
  }
}

// 提交修改密码
const handleChangePassword = async () => {
  try {
    if (!changePasswordFormRef.value) return
    await changePasswordFormRef.value.validate()

    changePasswordLoading.value = true
    const { oldPassword, newPassword } = changePasswordForm

    const res = await userApi.modifyPassword(oldPassword, newPassword)
    if (res.code === 200) {
      ElMessage.success(res.data || '修改密码成功')
      // 关闭对话框并重置表单
      changePasswordDialogVisible.value = false
      changePasswordForm.oldPassword = ''
      changePasswordForm.newPassword = ''
    } else {
      ElMessage.error(res.data || '修改密码失败')
    }
  } catch (error) {
    console.error('修改密码失败:', error)
    ElMessage.error('修改密码失败')
  } finally {
    changePasswordLoading.value = false
  }
}

// 分页大小变化处理函数
const handlePageSizeChange = (listType) => {
  return (newSize) => {
    pagination[listType].pageSize = newSize
    pagination[listType].currentPage = 1
  }
}

// 当前页变化处理函数
const handleCurrentPageChange = (listType) => {
  return (newPage) => {
    pagination[listType].currentPage = newPage
  }
}

// 排序变化处理函数
const handleSortChange = (sortObj) => {
  sortConfig.prop = sortObj.prop
  sortConfig.order = sortObj.order
  // 重置页码到第一页
  pagination.jobList.currentPage = 1
}

onMounted(() => {
  // 检查是否已登录，更健壮的判断方式
  const storedUserInfo = localStorage.getItem('userInfo')
  let parsedUserInfo = {}
  let isLoggedIn = false

  try {
    if (storedUserInfo) {
      parsedUserInfo = JSON.parse(storedUserInfo)
      // 只要localStorage中有userInfo且能解析成功，就认为已登录
      isLoggedIn = true
    }
  } catch {
    // 解析失败，清除无效的localStorage数据
    localStorage.removeItem('userInfo')
    isLoggedIn = false
  }

  if (!isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  // 初始化加载职位列表
  loadJobList()
})
</script>

<style scoped>
/* Dashboard 整体样式 */
.dashboard {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

/* 头部样式优化 */
.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  padding: 0 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: 60px;
  z-index: 100;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: 600;
  gap: 10px;
}

.logo .el-icon {
  font-size: 26px;
  transition: transform 0.3s ease;
}

.logo .el-icon:hover {
  transform: scale(1.1);
}

/* 用户信息区域 */
.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 20px;
  transition: all 0.3s ease;
  background-color: rgba(255, 255, 255, 0.1);
}

.user-dropdown:hover {
  background-color: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
}

.user-dropdown .el-avatar {
  margin-right: 8px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.user-dropdown:hover .el-avatar {
  border-color: rgba(255, 255, 255, 0.6);
}

.username {
  margin-right: 6px;
  font-size: 14px;
  font-weight: 500;
}

/* 退出按钮优化 */
.logout-btn {
  border-radius: 20px;
  padding: 6px 16px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(245, 108, 108, 0.3);
}

/* 侧边栏样式优化 */
.sidebar-menu {
  height: calc(100vh - 60px);
  border-right: none;
  background-color: white;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

/* 优化菜单项样式 */
:deep(.el-menu-item) {
  padding-left: 24px !important;
  height: 50px;
  line-height: 50px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  border-radius: 8px;
  margin: 4px 12px;
}

:deep(.el-menu-item:hover) {
  background-color: #ecf5ff !important;
  color: #409eff !important;
}

:deep(.el-menu-item.is-active) {
  background-color: #ecf5ff !important;
  color: #409eff !important;
  border-right: 4px solid #409eff;
}

:deep(.el-menu-item .el-icon) {
  font-size: 18px;
  margin-right: 10px;
}

/* 主内容区域 */
.el-main {
  padding: 24px;
  background-color: #f5f7fa;
  overflow-y: auto;
  flex: 1;
}

/* 卡片样式优化 */
:deep(.el-card) {
  border-radius: 12px;
  margin-bottom: 24px;
}

:deep(.el-card__header) {
  border-bottom: 1px solid #ebeef5;
  background-color: #fafafa;
  border-radius: 12px 12px 0 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

/* 表格样式优化 */
:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

:deep(.el-table__header-wrapper .el-table__header) {
  background-color: #fafafa;
}

:deep(.el-table__header-wrapper th) {
  background-color: #fafafa;
  font-weight: 600;
  color: #303133;
}

:deep(.el-table__body-wrapper tr:hover > td) {
  background-color: #f5f7fa !important;
}

:deep(.el-table__body-wrapper tr) {
  transition: all 0.3s ease;
}

:deep(.el-table__body-wrapper tr:hover) {
  transform: translateY(-1px);
}

/* 按钮样式优化 */
:deep(.el-button) {
  border-radius: 6px;
  padding: 6px 16px;
  font-weight: 500;
  transition: all 0.3s ease;
}

:deep(.el-button--primary) {
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

:deep(.el-button--primary:hover) {
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
  transform: translateY(-1px);
}

:deep(.el-button--danger) {
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
}

:deep(.el-button--danger:hover) {
  box-shadow: 0 4px 12px rgba(245, 108, 108, 0.4);
  transform: translateY(-1px);
}

/* 分页样式优化 */
:deep(.el-pagination) {
  margin-top: 24px;
  text-align: right;
  padding: 16px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

/* 标签样式优化 */
:deep(.el-tag) {
  border-radius: 12px;
  padding: 2px 10px;
  font-size: 12px;
  font-weight: 500;
}

/* 删除提示样式 */
.delete-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  text-align: center;
  line-height: 1;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 10px;
    padding: 10px 0;
  }

  .username {
    display: none;
  }

  .el-aside {
    width: 60px !important;
  }

  .sidebar-menu span {
    display: none;
  }

  .el-main {
    padding: 16px;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}

/* 滚动条美化 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a1a1a1;
}
</style>
