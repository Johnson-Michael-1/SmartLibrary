import request from '@/utils/request'

// 查询远程开门列表
export function listRecordRemote(query) {
  return request({
    url: '/recordRemote/list',
    method: 'get',
    params: query
  })
}

// 查询开门记录详细
export function getRecordremote(postId) {
  return request({
    url: '/recordRemote/post/' + postId,
    method: 'get'
  })
}

// 新增开门记录
export function addRecordremote(data) {
  return request({
    url: '/recordRemote/post',
    method: 'post',
    data: data
  })
}

// 修改远程开门记录
export function updateRecordremote(data) {
  return request({
    url: '/recordRemote/post',
    method: 'put',
    data: data
  })
}

// 删除远程开门记录
export function delRecordremote(postId) {
  return request({
    url: '/recordRemote/post/' + postId,
    method: 'delete'
  })
}
