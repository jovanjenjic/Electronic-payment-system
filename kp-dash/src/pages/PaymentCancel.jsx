import React from 'react';
import { useAsync } from 'react-async';

import { useParams, useHistory } from 'react-router-dom';
import { Result, Button } from 'antd';

import { BTC_CANCEL_PAYMENT_URL, CANCEL_PAYPAL_PAYMENT_URL, BANK_PAYMENT_CANCEL_URL } from '../constants/url';
import { post } from '../services/api';
import { responseOk } from '../utils/responseOk';

// TODO: Add here also urls for the bank and the crypto payments
/** `Helper` method for resolving payment urls  */
const resolveCancelUrl = (paymentType = 'paypal', paymentId) => {
  let url = BANK_PAYMENT_CANCEL_URL;
  if (paymentType === 'paypal') url = CANCEL_PAYPAL_PAYMENT_URL;
  if (paymentType === 'bitcoin') return BTC_CANCEL_PAYMENT_URL;
  // add for others later

  return url.replace('{paymentId}', paymentId);
};

const cancelPayment = async (_, { paymentId, paymentType = 'paypal' }) => {
  const authToken = localStorage.getItem('access_token');

  const data = {};

  if (authToken) {
    /** `url` for the params */
    const url = resolveCancelUrl(paymentType, paymentId);

    if (paymentType === 'bitcoin') {
      data.transactionId = paymentId;
      data.isSuccess = false;
    }

    const response = await post(url, data, authToken);

    if (responseOk(response)) return await response.json();
  }

  return { error: true };
};

const PaymentCancel = () => {
  const history = useHistory();

  const { paymentId = '', paymentType = '' } = useParams();

  /** `useAsync` hook for executing payment */
  const { run: runCancelPayment, isLoading } = useAsync({
    deferFn: cancelPayment,
    paymentId,
    paymentType,
  });

  React.useEffect(() => {
    runCancelPayment();
  }, []);

  return (
    <Result
      title="Canceled Payment"
      status="info"
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

export default PaymentCancel;
