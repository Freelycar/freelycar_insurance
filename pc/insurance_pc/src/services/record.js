import { stringify } from 'qs';
import request from '../utils/request';

export async function getQuoteRecordList(params) {
  return request(`api/quoterecord/list?${stringify(params)}`);
}

export async function getClientDetail(params) {
  return request(`/api/client/detail?${stringify(params)}`);
}

export async function getClientOrderByLicenseNumber(params) {
  return request(`api/order/getClientOrderByLicenseNumber?${stringify(params)}`);
}
