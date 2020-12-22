import React from 'react';
import { useHistory } from 'react-router-dom';

import BankPaymentForm from '../forms/bank-payment-form';
import { post } from '../services/api';
import { responseOk } from '../utils/responseOk';

const onSubmitPaymentHandler = async (
  { number, name = '', expiry = '', cvc: cvv = '' },
  paymentUrl = ''
) => {
  // just to check later how much digits to use for this
  const pan = `${number}`.replace(/\s+/g, '').substring(0, 11);
  const cardHolder = name.trim();
  const [mm, yy] = expiry.split('/');

  try {
    const response = await post(paymentUrl, {
      pan,
      cardHolder,
      mm,
      yy,
      cvv,
    });
    if (responseOk(response)) {
      const url = await response.text();
      window.open(url);
      return { error: false };
    }
    const failUrl = await response.text();
    window.open(failUrl);
  } catch (error) {
    console.error(error);
  }
  return { error: true };
};

const BankPayment = () => {
  const history = useHistory();

  const {
    location: {
      state: { paymentUrl = '' },
    },
  } = history;


  const handleSubmit = async (fields) => {
    await onSubmitPaymentHandler(fields, paymentUrl);
  };

  return <BankPaymentForm onSubmit={handleSubmit} />;
};

export default BankPayment;
