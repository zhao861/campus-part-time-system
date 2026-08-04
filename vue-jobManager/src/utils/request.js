import axios from 'axios'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8080', // 根据你的后端地址修改
  timeout: 10000,
  withCredentials: true, // 关键：允许跨域请求携带cookie
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 可以在这里添加token等
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data
    // 根据你的业务逻辑处理响应
    return res
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  },
)

export default service
