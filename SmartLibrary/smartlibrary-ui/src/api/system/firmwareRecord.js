import request from '@/utils/request'

// 查询固件升级记录列表
export function listfirmwareRecord(query) {
  return request({
    url: '/firmwareRecord/list',
    method: 'get',
    params: query
  })
}
