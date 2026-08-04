import request from '../../utils/request'

// 用户相关API
export const userApi = {
  // 用户注册
  register(data) {
    return request({
      url: '/register/users',
      method: 'POST',
      data,
    })
  },

  // 用户登录
  login(data) {
    return request({
      url: '/login/',
      method: 'POST',
      data,
    })
  },

  // 获取用户个人资料
  getUserProfile() {
    return request({
      url: '/userprofile/',
      method: 'GET',
    })
  },

  // 修改密码
  modifyPassword(oldPassword, newPassword) {
    return request({
      url: '/modifyuserpassword/',
      method: 'POST',
      params: {
        oldPassword,
        newPassword,
      },
    })
  },
}
