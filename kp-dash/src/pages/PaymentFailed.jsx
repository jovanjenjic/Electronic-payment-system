import React from 'react';

import { useHistory } from 'react-router-dom';
import { Result, Button } from 'antd';

const PaymentFailed = () => {
  const history = useHistory();

  return (
    <Result
      title="Failed  Payment"
      status="error"
      extra={
        <Button type="primary" onClick={() => history.push('/')}>
          Home
        </Button>
      }
    />
  );
};

export default PaymentFailed;
