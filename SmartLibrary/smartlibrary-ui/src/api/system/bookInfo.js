import request from '@/utils/request'

// 查询书籍列表
export function listBookInfo(query) {
  return request({
    url: '/bookInfo/list',
    method: 'get',
    params: query
  })
}

// 查询书籍详细
export function getBookInfo(bkId) {
  return request({
    url: '/bookInfo/get/' + bkId,
    method: 'get'
  })
}

// 新增书籍
export function addBookInfo(data) {
  return request({
    url: '/bookInfo/add',
    method: 'post',
    data: data
  })
}

// 修改书籍信息
export function updateBookInfo(data) {
  return request({
    url: '/bookInfo/update',
    method: 'put',
    data: data
  })
}

// 删除书籍记录
export function delBookInfo(bkId) {
  return request({
    url: '/bookInfo/delete/' + bkId,
    method: 'delete'
  })
}



