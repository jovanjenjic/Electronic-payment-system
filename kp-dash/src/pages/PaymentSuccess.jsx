import React from 'react';
import { useAsync } from 'react-async';

import { useParams, useLocation, useHistory } from 'react-router-dom';
import { Result, Button } from 'antd';

import { EXECUTE_PAYPAL_PAYMENT_URL } from '../constants/url';
import { post } from '../services/api';
import { responseOk } from '../utils/responseOk';

/** `helper` method for resolving search params of the url */
const resolveSearchParams = (search = '', paymentType = '') => {
  const searchParams = new URLSearchParams(search);

  if (paymentType === 'paypal') {
    return {
      paymentId: searchParams.get('paymentId'),
      payerId: searchParams.get('PayerID'),
    };
  }

  return {};
};

// TODO: Add here also urls for the bank and the crypto payments
/** `Helper` method for resolving payment urls  */
const resolveSuccessUrl = (paymentType = 'paypal', paymentId) => {
  let url = '';
  if (paymentType === 'paypal') url = EXECUTE_PAYPAL_PAYMENT_URL;

  // add for others later

  return url.replace('{paymentId}', paymentId);
};

const executePayment = async (
  [data],
  { paymentId, paymentType = 'paypal' }
) => {
  const authToken = localStorage.getItem('access_token');

  if (authToken) {
    /** `url` for the params */
    const url = resolveSuccessUrl(paymentType, paymentId);

    const response = await post(url, data, authToken);

    if (responseOk(response)) return await response.json();
  }

  return { error: true };
};

const PaymentSuccess = () => {
  const location = useLocation();

  const history = useHistory();

  const { paymentId = '', paymentType = '' } = useParams();

  /** `useAsync` hook for executing payment */
  const { run: runPayment, isLoading } = useAsync({
    deferFn: executePayment,
    paymentId,
    paymentType,
  });

  React.useEffect(() => {
    runPayment(resolveSearchParams(location.search, paymentType));
  }, []);

  return (
    <Result
      title="Successful Payment"
      status="success"
      extra={
        <Button
          type="primary"
          onClick={() => history.push('/')}
          loading={isLoading}
        >
          Home
        </Button>
      }
    />
  );
};

export default PaymentSuccess;
