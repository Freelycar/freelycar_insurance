import { stringify } from 'qs';
import request from '../utils/request';

export async function getPieChart(params) {
  return request(`api/order/getPieChart?${stringify(params)}`);
}
