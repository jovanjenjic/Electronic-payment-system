import { useAsync } from 'react-async';

import { responseOk } from '../utils/responseOk';
import { get } from '../services/api';

import { PAYMENT_TYPE_URL } from '../constants/url';

const fetchPaymentTypes = async ({ authToken }) => {
  try {
    const response = await get(PAYMENT_TYPE_URL, authToken);
    if (responseOk(response)) return await response.json();
  } catch (error) {
    console.error(error);
  }
  return [];
};

const useFetchPaymentTypes = () => {
  const authToken = localStorage.getItem('access_token');

  const paymentTypesState = useAsync({
    promiseFn: fetchPaymentTypes,
    authToken,
  });

  return paymentTypesState;
};

export default useFetchPaymentTypes;
