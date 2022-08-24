import request from '@/utils/request'

// 查询设备列表
export function listDevInfo(query) {
  return request({
    url: '/devInfo/list',
    method: 'get',
    params: query
  })
}

//查询在线设备
export function listDev(query) {
  return request({
    url: '/devInfo/listDev',
    method: 'get',
    params: query
  })
}

// 查询设备详细
export function getDevInfo(devId) {
  return request({
    url: '/devInfo/get/' + devId,
    method: 'get'
  })
}

// 新增设备
export function addDevInfo(data) {
  return request({
    url: '/devInfo/add',
    method: 'post',
    data: data
  })
}

// 修改读者信息
export function updateDevInfo(data) {
  return request({
    url: '/devInfo/update',
    method: 'put',
    data: data
  })
}

// 删除设备记录
export function delDevInfo(devId) {
  return request({
    url: '/devInfo/delete/' + devId,
    method: 'delete'
  })
}
