import request from '../../utils/request'

// 报名相关API
export const signupApi = {
  // 报名职位
  signupJob(data) {
    return request({
      url: '/signupjob/',
      method: 'POST',
      data,
    })
  },

  // 获取报名列表（兼容旧接口）
  getSignUpJobView() {
    return request({
      url: '/signupjobview/',
      method: 'GET',
    })
  },

  // 获取报名列表（新接口，按需求）
  getSignupList() {
    return request({
      url: '/registeruserlist/',
      method: 'GET',
    })
  },

  // 审核报名
  reviewJob(data) {
    return request({
      url: '/reviewjob/',
      method: 'POST',
      data,
    })
  },

  // 删除报名
  deleteRegisterJob(data) {
    return request({
      url: '/deleteregisterjobs/',
      method: 'POST',
      data,
    })
  },
}
