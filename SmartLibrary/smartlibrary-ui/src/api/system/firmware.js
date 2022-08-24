import request from '@/utils/request'



/**
 * 文件上传
 * @param data {Object} formData对象 "file"
 */
// 上传文件
export function uploadImgFile(data) {
  return request({
    url: '/firmware/file' ,
    method: 'post',
    data: data
  })
}

//


// 升级更新固件
export function uploadFirmware(data) {
  return request({
    url: '/firmware/uploadFirmware' ,
    method: 'post',
    data: data
  })
}


// 查询固件列表
export function listFirmware(query) {
  return request({
    url: '/firmware/list',
    method: 'get',
    params: query
  })
}

// 查询固件详细信息
export function getFirmware(id) {
  return request({
    url: '/firmware/get/' + id,
    method: 'get'
  })
}

// 新增固件信息
export function addFirmware(data) {
  return request({
    url: '/firmware/add',
    method: 'post',
    data: data
  })
}

// 修改固件信息
export function updateFirmware(data) {
  return request({
    url: '/firmware/update',
    method: 'put',
    data: data
  })
}

// 删除固件记录
export function delFirmware(ids) {
  return request({
    url: '/firmware/delete/' + ids,
    method: 'delete'
  })
}

