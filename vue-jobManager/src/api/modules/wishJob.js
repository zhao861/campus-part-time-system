import request from '../../utils/request'

// 心愿职位相关API
export const wishJobApi = {
  // 添加心愿职位
  addWishJob(data) {
    return request({
      url: '/wishjob/',
      method: 'POST',
      data,
    })
  },

  // 删除心愿职位
  deleteWishJob(data) {
    return request({
      url: '/deletewishjob/',
      method: 'POST',
      data,
    })
  },

  // 获取心愿职位列表
  getWishJobList() {
    return request({
      url: '/wishjoblist/',
      method: 'GET',
    })
  },
}
