import request from '../../utils/request'

// 职位相关API
export const jobApi = {
  // 添加职位（AI同步初审耗时，单独放宽超时）
  addJob(data) {
    return request({
      url: '/publisher/jobs',
      method: 'POST',
      data,
      timeout: 20000,
    })
  },

  // 修改职位
  modifyJob(data) {
    return request({
      url: '/modifyjob/',
      method: 'POST',
      data,
    })
  },

  // 删除职位
  deleteJob(data) {
    return request({
      url: '/jobs/delete',
      method: 'POST',
      data,
    })
  },

  // 获取职位列表
  getJobList() {
    return request({
      url: '/joblist/',
      method: 'GET',
    })
  },

  // 获取我的发布职位列表
  getMyJobs() {
    return request({
      url: '/publishjoblist/',
      method: 'GET',
    })
  },

  // 查询职位（模糊搜索）
  queryJob(data) {
    return request({
      url: `/queryjob/?name=${encodeURIComponent(data.name)}`,
      method: 'GET',
    })
  },

  // 管理员复审：待人工审核列表
  getAdminReviewList() {
    return request({
      url: '/adminreview/list/',
      method: 'GET',
    })
  },

  // 管理员复审通过
  adminPass(data) {
    return request({
      url: '/adminreview/pass/',
      method: 'POST',
      data,
    })
  },

  // 管理员复审驳回（reason 必填）
  adminReject(data) {
    return request({
      url: '/adminreview/reject/',
      method: 'POST',
      data,
    })
  },
}
