import request from '@/utils/request'

// 查询读者列表
export function listReaderInfo(query) {
  return request({
    url: '/readerInfo/list',
    method: 'get',
    params: query
  })
}

// 查询读者详细
export function getReaderInfo(rdId) {
  return request({
    url: '/readerInfo/get/' + rdId,
    method: 'get'
  })
}

// 新增读者
export function addReaderInfo(data) {
  return request({
    url: '/readerInfo/add',
    method: 'post',
    data: data
  })
}

// 修改读者信息
export function updateReaderInfo(data) {
  return request({
    url: '/readerInfo/update',
    method: 'put',
    data: data
  })
}

// 删除读者记录
export function delReaderInfo(rdIds) {
  return request({
    url: '/readerInfo/delete/' + rdIds,
    method: 'delete'
  })
}

// 重置读者登录密码
export function resetPassword(rdId) {
  return request({
    url: '/readerInfo/reset/',
    method: 'put',
    data: rdId
  })
}
