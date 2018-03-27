import { stringify } from 'qs';
import request from '../utils/request';

export async function getPieChart(params) {
  return request(`api/order/getPieChart?${stringify(params)}`);
}

export async function getListCount(params) {
  return request(`api/order/listCount?${stringify(params)}`);
}





