import React from 'react';

import { useParams, useLocation } from 'react-router-dom';

const PaymentSuccess = () => {

  const location = useLocation();

  const { paymentId = '', paymentType = '' } = useParams();

  const url = new URLSearchParams(location.search);

  console.log('uuu =>', url.get('lola'));

  console.log('lll => ', location);
  console.log('params => ', { paymentId, paymentType });

  return <div>Success</div>;
};

export default PaymentSuccess;
