import request from '@/utils/request'

// 查询报警列表
export function listAlarmInfo(query) {
  return request({
    url: '/alarmInfo/list',
    method: 'get',
    params: query
  })
}

// 查询报警详细
export function getAlarmInfo(alarmId) {
  return request({
    url: '/alarmInfo/get/' + alarmId,
    method: 'get'
  })
}

// 新增报警
export function addAlarmInfo(data) {
  return request({
    url: '/alarmInfo/add',
    method: 'post',
    data: data
  })
}

// 修改报警
export function updateAlarmInfo(data) {
  return request({
    url: '/alarmInfo/update',
    method: 'put',
    data: data
  })
}

// 删除报警记录
export function delBook(alarmIds) {
  return request({
    url: '/alarmInfo/delete/' + alarmIds,
    method: 'delete'
  })
}
