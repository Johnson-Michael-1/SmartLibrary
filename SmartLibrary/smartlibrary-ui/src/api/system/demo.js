import request from '@/utils/request'


// 动态库、连接
export function uploadDll() {
  return request({
    url: '/demo/uploadDll',
    method: 'get'
  })
}

// 写入
export function gotoWrite(bkIsbn) {
  return request({
    url: '/demo/write/' + bkIsbn,
    method: 'get'
  })
}

//断开
export function stopConnect() {
  return request({
    url: '/demo/stop',
    method: 'get'
  })
}
