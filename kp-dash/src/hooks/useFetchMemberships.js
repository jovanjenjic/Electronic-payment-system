import { useAsync } from 'react-async';

import { responseOk } from '../utils/responseOk';
import { get } from '../services/api';
import { MEMBERSHIPS_URL } from '../constants/url';

const fetchMemberships = async ({ authToken }) => {
  try {
    const response = await get(MEMBERSHIPS_URL, authToken);
    if (responseOk(response)) return await response.json();
  } catch (error) {
    console.error(error);
  }
  return [];
};

const useFetchMemberships = () => {
  const authToken = localStorage.getItem('access_token');

  const paymentTypesState = useAsync({
    promiseFn: fetchMemberships,
    authToken,
  });

  return paymentTypesState;
};

export default useFetchMemberships;
