import { stringify } from 'qs';
import request from '../utils/request';

export async function getCurentCashBackRate(params) {
    return request(`api/cashbackrecord/getCurentCashBackRate?${stringify(params)}`);
}

// export async function saveupdateCashBackRate(params) {
//     return request(`api/cashbackrecord/saveupdateCashBackRate?${stringify(params)}`);
// }
export async function saveupdateCashBackRate(params) {
    return request('api/cashbackrecord/saveupdateCashBackRate', {
      method: 'POST',
      body: params,
    });
}
