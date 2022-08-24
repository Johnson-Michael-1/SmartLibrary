import request from '@/utils/request'

// 查询开门列表
export function listRecordHistory(query) {
  return request({
    url: '/recordHistory/list',
    method: 'get',
    params: query
  })
}

// 查询开门记录详细
export function getRecordHistory(openId) {
  return request({
    url: '/recordHistory/get/' + openId,
    method: 'get'
  })
}

// 新增开门记录
export function addRecordHistory(data) {
  return request({
    url: '/recordHistory/add',
    method: 'post',
    data: data
  })
}

// 修改开门记录
export function updateRecordHistory(data) {
  return request({
    url: '/recordHistory/update',
    method: 'put',
    data: data
  })
}

// 删除开门记录
export function delRecordHistory(openIds) {
  return request({
    url: '/recordHistory/delete/' + openIds,
    method: 'delete'
  })
}
