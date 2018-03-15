import request from '../utils/request';

export async function getClientList() {
  return request('/api/client/list');
}

export async function getClientDetail() {
  return request('/api/client/detail');
}
