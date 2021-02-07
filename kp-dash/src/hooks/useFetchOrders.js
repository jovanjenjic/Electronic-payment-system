import { useAsync } from 'react-async';

import { responseOk } from '../utils/responseOk';
import { get } from '../services/api';

import { ORDERS_URL } from '../constants/url';

const fetchOrders = async ({ authToken }) => {
  try {
    const response = await get(ORDERS_URL, authToken);
    if (responseOk(response)) return await response.json();
  } catch (error) {
    console.error(error);
  }
  return [];
};

const usefetchOrders = () => {
  const authToken = localStorage.getItem('access_token');

  const ordersState = useAsync({
    promiseFn: fetchOrders,
    authToken,
  });

  return ordersState;
};

export default usefetchOrders;
