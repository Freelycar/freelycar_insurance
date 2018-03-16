import { stringify } from 'qs';
import request from '../utils/request';

export async function getClientList(params) {
  return request(`/api/client/list?${stringify(params)}`);
}

export async function getClientDetail(params) {
  return request(`/api/client/detail?${stringify(params)}`);
}
