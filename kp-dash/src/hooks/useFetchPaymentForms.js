import { useAsync } from 'react-async';

// TODO: Add later also for the bank type
import { BTC_FORM_URL, PAYPAL_FORM_URL } from '../constants/url';
import { get } from '../services/api';
import { responseOk } from '../utils/responseOk';

const fetchForms = async ({ authToken }) => {
  try {
    const requests = [BTC_FORM_URL, PAYPAL_FORM_URL].map((url) =>
      get(url, authToken)
    );

    const responses = await Promise.all(requests);

    if (!responses.some((response) => !responseOk(response))) {
      const [BTC, PAYPAL] = await Promise.all(
        responses.map((response) => response.json())
      );

      return {
        BTC,
        PAYPAL,
      };
    }
  } catch (error) {
    console.error(error);
  }
  return {
    BTC: {},
    PAYPAL: {},
  };
};

const useFetchPaymentForms = () => {
  const authToken = localStorage.getItem('access_token');

  const formsState = useAsync({
    promiseFn: fetchForms,
    authToken,
  });

  return formsState;
};

export default useFetchPaymentForms;
