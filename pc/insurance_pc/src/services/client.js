import {stringify} from 'qs';
import request from '../utils/request';

export async function getClientList(params) {
  return request(`api/client/list?${stringify(params)}`);
}

export async function getClientDetail(params) {
  return request(`api/client/detail?${stringify(params)}`);
}

export async function getReciverByOrderId(params) {
  return request(`api/reciver/getReciverByOrderId?${stringify(params)}`);
}

export async function getInvoiceInfoByOrderId(params) {
  return request(`api/invoiceinfo/getInvoiceInfoByOrderId?${stringify(params)}`);
}

export async function affirmDistributionInfo(params) {
  return request(`api/order/affirmDistributionInfo?${stringify(params)}`);
}

export async function affirmSignForInfo(params) {
  return request(`api/order/affirmSignForInfo?${stringify(params)}`);
}

export async function getOrderSignForInfoByOrderId(params) {
  return request(`api/order/getOrderSignForInfoByOrderId?${stringify(params)}`);
}

export async function getCashBackRecordByOrderId(params) {
  return request(`api/cashbackrecord/getCashBackRecordByOrderId?${stringify(params)}`);
}

export async function affirmCashBackRecordInfo(params) {
  return request(`api/order/affirmCashBackRecordInfo?${stringify(params)}`);
}

