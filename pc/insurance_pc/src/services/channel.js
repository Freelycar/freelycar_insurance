import { stringify } from 'qs';
import request from '../utils/request';

export async function addChannel(params) {
    return request('/api/invition/save', {
      method: 'POST',
      body: params,
    });
}

export async function removeChannels(params) {
    return request(`/api/invition/remove?${stringify(params)}`);
}

export async function getChannelList(params) {
    return request(`/api/invition/list?${stringify(params)}`);
}
