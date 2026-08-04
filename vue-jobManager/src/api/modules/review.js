import request from '../../utils/request'

// 审核相关API
export const reviewApi = {
  // 审核职位
  reviewJob(data) {
    return request({
      url: '/reviewjob/',
      method: 'POST',
      data,
    })
  },

  // 获取注册用户列表
  getRegisterUserList(publisherName) {
    return request({
      url: '/registeruserlist/',
      method: 'GET',
      params: { publisherName },
    })
  },

  // 删除注册记录
  deleteRegisterJob(data) {
    return request({
      url: '/deleteregisterjobs/',
      method: 'POST',
      data,
    })
  },
}
