import request from '@/utils/request'

// 查询读者列表
export function listLoginRecord(query) {
  return request({
    url: '/loginRecord/list',
    method: 'get',
    params: query
  })
}

// 查询读者详细
export function getLoginRecord(loginId) {
  return request({
    url: '/loginRecord/get/' + loginId,
    method: 'get'
  })
}

// 新增读者
export function addLoginRecord(data) {
  return request({
    url: '/loginRecord/add',
    method: 'post',
    data: data
  })
}

// 修改读者信息
export function updateLoginRecord(data) {
  return request({
    url: '/loginRecord/update',
    method: 'put',
    data: data
  })
}

// 删除读者记录
export function delLoginRecord(loginIds) {
  return request({
    url: '/loginRecord/delete/' + loginIds,
    method: 'delete'
  })
}
