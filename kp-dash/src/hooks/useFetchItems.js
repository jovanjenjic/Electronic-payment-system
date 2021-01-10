import { useAsync } from 'react-async';

import { responseOk } from '../utils/responseOk';
import { get } from '../services/api';

import { ALL_ITEMS_URL } from '../constants/url';

const fetchItems = async ({ authToken }) => {
  try {
    const response = await get(ALL_ITEMS_URL, authToken);
    if (responseOk(response)) return await response.json();
  } catch (error) {
    console.error(error);
  }
  return [];
};

const usefetchItems = () => {
  const authToken = localStorage.getItem('access_token');

  const paymentTypesState = useAsync({
    promiseFn: fetchItems,
    authToken,
  });

  return paymentTypesState;
};

export default usefetchItems;