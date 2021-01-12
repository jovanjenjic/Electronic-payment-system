import React from 'react';
import { useAsync } from 'react-async';

import { useHistory, useParams } from 'react-router-dom';
import { Result, Button } from 'antd';

import { BANK_PAYMENT_FAILED_URL } from '../constants/url';
import { post } from '../services/api';
import { responseOk } from '../utils/responseOk';

const cancelPayment = async (_, { paymentId }) => {
  const authToken = localStorage.getItem('access_token');

  if (authToken) {
    const response = await post(BANK_PAYMENT_FAILED_URL.replace('{paymentId}', paymentId), {}, authToken);

    if (responseOk(response)) return await response.json();
  }

  return { error: true };
};

const PaymentFailed = () => {
  const history = useHistory();

  const { paymentId = '' } = useParams();

  const { run: runCancelPayment, isLoading } = useAsync({
    deferFn: cancelPayment,
    paymentId,
  });

  React.useEffect(() => {
    runCancelPayment();
  }, []);

  return (
    <Result
      title="Failed  Payment"
      status="error"
      extra={
        <Button type="primary" loading={isLoading} onClick={() => history.push('/')}>
          Home
        </Button>
      }
    />
  );
};

export default PaymentFailed;
