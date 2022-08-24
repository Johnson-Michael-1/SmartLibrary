import request from '@/utils/request'

// 查询借阅列表
export function listBorrow(query) {
  return request({
    url: '/borrow/list',
    method: 'get',
    params: query
  })
}

// 查询借阅详细
export function getBorrow(borrowId) {
  return request({
    url: '/borrow/get/' + borrowId,
    method: 'get'
  })
}

// 新增借阅
export function addBorrow(data) {
  return request({
    url: '/borrow/add',
    method: 'post',
    data: data
  })
}

// 修改借阅记录
export function updateBorrow(data) {
  return request({
    url: '/borrow/update',
    method: 'put',
    data: data
  })
}

// 删除借阅记录
export function delBorrow(borrowId) {
  return request({
    url: '/borrow/delete/' + borrowId,
    method: 'delete'
  })
}

// 续借
export function bkRenew(data) {
  return request({
    url: '/borrow/renew',
    method: 'put',
    data: data
  })
}
